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
import jahirfiquitiva.libs.fabsmenu.FABsMenu;
import jahirfiquitiva.libs.fabsmenu.FABsMenuListener;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.user.UserRepository;
import kr.ac.ssu.wherealarmyou.view.login.ProfileActivity;

public class MainFragment extends Fragment implements View.OnClickListener
{
    private MainFrameActivity mainFrameActivity;
    
    // FAB ( Floating Action Button )
    private FABsMenu fabsMenu;
    private View     fabsBlind;
    
    // test
    private Button   buttonLocation;
    private Button   buttonGroup;
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
        View view = inflater.inflate(R.layout.content_main, container, false);
        mainFrameActivity = (MainFrameActivity)getActivity( );
        
        // FAB Setting
        fabsMenu  = view.findViewById(R.id.fabsMenu);
        fabsBlind = view.findViewById(R.id.blind_fabs);
        TitleFAB buttonAddAlarm    = view.findViewById(R.id.addAlarmFab);
        TitleFAB buttonAddLocation = view.findViewById(R.id.addLocationFab);
        TitleFAB buttonAddGroup    = view.findViewById(R.id.addGroupFab);
        
        buttonAddAlarm.setOnClickListener(this);
        buttonAddLocation.setOnClickListener(this);
        buttonAddGroup.setOnClickListener(this);
        fabsMenu.setMenuListener(new FABsMenuListener( )
        {
            @Override
            public void onMenuExpanded(FABsMenu fabsMenu)
            {
                super.onMenuExpanded(fabsMenu);
                fabsBlind.setVisibility(View.VISIBLE);
            }
            
            @Override
            public void onMenuCollapsed(FABsMenu fabsMenu)
            {
                super.onMenuCollapsed(fabsMenu);
                fabsBlind.setVisibility(View.GONE);
            }
        });
        
        // test
        buttonLocation  = view.findViewById(R.id.buttonLocation);
        buttonGroup     = view.findViewById(R.id.buttonGroup);
        buttonTemporary = view.findViewById(R.id.buttonProfile);
        text_1          = view.findViewById(R.id.textView1);
        text_2          = view.findViewById(R.id.textView2);
        text_3          = view.findViewById(R.id.textView3);
        text_4          = view.findViewById(R.id.textView4);
        text_5          = view.findViewById(R.id.textView5);
        text_6          = view.findViewById(R.id.textView6);
        
        buttonLocation.setOnClickListener(this);
        buttonGroup.setOnClickListener(this);
        buttonTemporary.setOnClickListener(this);
        
        getUserInfo( );
        
        return view;
    }
    
    @Override
    public void onClick(View view)
    {
        switch (view.getId( )) {
            case (R.id.buttonLocation):
                MainFrameActivity.showTopFragment(LocationFragment.getInstance( ));
                break;
            case (R.id.buttonGroup):
                MainFrameActivity.showTopFragment(GroupFragment.getInstance( ));
                break;
            case (R.id.buttonProfile):
                startActivity(new Intent(mainFrameActivity.getApplicationContext( ), ProfileActivity.class));
                mainFrameActivity.finish( );
                break;
            case (R.id.addAlarmFab):
                MainFrameActivity.showTopFragment(AlarmAddFragment.getInstance( ));
                fabsMenu.collapse( );
                break;
            case (R.id.addLocationFab):
                MainFrameActivity.showTopFragment(LocationAddFragment.getInstance( ));
                fabsMenu.collapse( );
                break;
            case (R.id.addGroupFab):
                MainFrameActivity.showTopFragment(GroupAddFragment.getInstance( ));
                fabsMenu.collapse( );
                break;
        }
    }
    
    // test
    private void getUserInfo( )
    {
        FirebaseUser   firebaseUser   = FirebaseAuth.getInstance( ).getCurrentUser( );
        UserRepository userRepository = UserRepository.getInstance( );
        
        if (firebaseUser != null) {
            text_1.setText("FirebaseUser Email : " + firebaseUser.getEmail( ));
            text_2.setText("FirebaseUser UID : " + firebaseUser.getUid( ));
            text_3.setText("FirebaseUser Name : " + firebaseUser.getDisplayName( ));
            userRepository.findUserByUid(firebaseUser.getUid( ))
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