package kr.ac.ssu.wherealarmyou.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.view.login.ProfileActivity;

public class MainFragment extends Fragment implements View.OnClickListener
{
    private FrameActivity frameActivity;
    
    private View view;
    
    private Button buttonLocation;
    private Button buttonGroup;
    
    // test
    private Button buttonTemporary;
    
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
        
        buttonTemporary = view.findViewById(R.id.buttonProfile);
        buttonTemporary.setOnClickListener(this);
        
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
}