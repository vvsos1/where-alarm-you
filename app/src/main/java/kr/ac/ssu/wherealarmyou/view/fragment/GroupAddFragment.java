package kr.ac.ssu.wherealarmyou.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;

public class GroupAddFragment extends Fragment implements View.OnClickListener, OnBackPressedListener
{
    private FrameActivity   frameActivity;
    
    private View            view;
    private OverlappingView overlappingView;
    
    private Button buttonBack;
    
    public static GroupAddFragment getInstance( )
    {
        return new GroupAddFragment( );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        frameActivity = (FrameActivity)getActivity( );
        
        view = inflater.inflate(R.layout.frame_overlap_content, container, false);
        
        overlappingView = view.findViewById(R.id.overlap_view);
        overlappingView.setTitle("그룹 추가");
        overlappingView.setButtonBack(true);
        overlappingView.setContent(inflater.inflate(R.layout.content_group_add, null));

        buttonBack = view.findViewById(R.id.overlap_buttonBack);
        
        buttonBack.setOnClickListener(this);
        
        return view;
    }
    
    @Override
    public void onClick(View view)
    {
        if (view == buttonBack) {
            Toast.makeText(getContext( ), "BACK 버튼", Toast.LENGTH_SHORT).show( );
            frameActivity.backTopFragment(this);
        }
    }
    
    @Override
    public void onBackPressed( )
    {
        frameActivity.backTopFragment(this);
    }
    
    @Override
    public void onResume( )
    {
        super.onResume( );
        frameActivity.setOnBackPressedListener(this);
    }
}