package kr.ac.ssu.wherealarmyou.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.user.UserRepository;
import kr.ac.ssu.wherealarmyou.user.dto.DeleteRequest;
import kr.ac.ssu.wherealarmyou.user.service.UserService;
import kr.ac.ssu.wherealarmyou.view.login.ProfileActivity;

public class MainFragment extends Fragment implements View.OnClickListener
{
    private FrameActivity frameActivity;
    
    private View view;
    
    private Button buttonLocation;
    private Button buttonGroup;
    
    // test
    private Button   buttonTemporary;
    private TextView text_1;
    private TextView text_2;
    private TextView text_3;
    private TextView text_4;
    private TextView text_5;
    private TextView text_6;
    
    public static MainFragment getInstance( )
    {
        return new MainFragment( );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view          = inflater.inflate(R.layout.content_main, container, false);
        frameActivity = (FrameActivity)getActivity( );
        
        buttonLocation = view.findViewById(R.id.buttonLocation);
        buttonGroup    = view.findViewById(R.id.buttonGroup);
        
        buttonLocation.setOnClickListener(this);
        buttonGroup.setOnClickListener(this);
        
        // test
        buttonTemporary = view.findViewById(R.id.buttonProfile);
        buttonTemporary.setOnClickListener(this);
        text_1 = view.findViewById(R.id.textView1);
        text_2 = view.findViewById(R.id.textView2);
        text_3 = view.findViewById(R.id.textView3);
        text_4 = view.findViewById(R.id.textView4);
        text_5 = view.findViewById(R.id.textView5);
        text_6 = view.findViewById(R.id.textView6);
        getUserInfo( );
        
        return view;
    }
    
    @Override
    public void onClick(View view)
    {
        switch (view.getId( )) {
            case (R.id.buttonLocation):
                frameActivity.showTopFragment(LocationFragment.getInstance( ));
                break;
            case (R.id.buttonGroup):
                frameActivity.showTopFragment(GroupFragment.getInstance( ));
                break;
            case (R.id.buttonProfile):
                startActivity(new Intent(frameActivity.getApplicationContext( ), ProfileActivity.class));
                frameActivity.finish( );
        }
    }
    
    private void getUserInfo( )
    {
        FirebaseUser   firebaseUser   = FirebaseAuth.getInstance( ).getCurrentUser( );
        UserRepository userRepository = UserRepository.getInstance( );
        UserService    userService    = UserService.getInstance( );
        
        if (firebaseUser != null) {
            text_1.setText("FirebaseUser Email : " + firebaseUser.getEmail( ));
            text_2.setText("FirebaseUser UID : " + firebaseUser.getUid( ));
            text_3.setText("FirebaseUser Name : " + firebaseUser.getDisplayName( ));
            userRepository.findUserByEmail(firebaseUser.getEmail( ))
                          .doOnSuccess(user -> {
                              text_4.setText("DB User Email : " + user.getEmail( ));
                              text_5.setText("DB User UID :" + user.getUid( ));
                              text_6.setText("DB User Name : " + user.getName( ));
                          })
                          .doOnError(throwable -> text_4.setText("User를 찾지 못했습니다."))
                          .subscribe( );
            
        }
        else {
            text_1.setText("FirebaseUser를 찾지 못했습니다.");
        }
    }
}