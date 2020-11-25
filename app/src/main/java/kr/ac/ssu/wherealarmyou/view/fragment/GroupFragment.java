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

import java.util.Objects;

public class GroupFragment extends Fragment implements View.OnClickListener, OnBackPressedListener
{
    private MainFrameActivity mainFrameActivity;
    
    private View            view;
    private OverlappingView overlappingView;
    
    private Button buttonAdd;
    private Button buttonHide;
    
    public static GroupFragment getInstance( )
    {
        return new GroupFragment( );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle bundle = Objects.requireNonNull(getArguments( ));
        mainFrameActivity = (MainFrameActivity)getActivity( );
        
        view = inflater.inflate(R.layout.frame_overlap_content, container, false);
        
        overlappingView = view.findViewById(R.id.overlap_view);
        overlappingView.setTitle("그룹");
        overlappingView.setButtonAdd(true);
        overlappingView.setButtonBack(bundle.getBoolean("backButton"));
        overlappingView.setButtonHide(bundle.getBoolean("hideButton"));
        overlappingView.setContent(inflater.inflate(R.layout.content_group, null));
        
        buttonAdd  = view.findViewById(R.id.overlap_buttonAdd);
        buttonHide = view.findViewById(R.id.overlap_buttonHide);
        
        buttonAdd.setOnClickListener(this);
        buttonHide.setOnClickListener(this);
        
        return view;
    }
    
    @Override
    public void onClick(View view)
    {
        switch (view.getId( )) {
            case (R.id.overlap_buttonAdd):
                Toast.makeText(getContext( ), "ADD 버튼", Toast.LENGTH_SHORT).show( );
                mainFrameActivity.addTopFragment(GroupAddFragment.getInstance( ));
                break;
            case (R.id.overlap_buttonBack):
                Toast.makeText(getContext( ), "BACK 버튼", Toast.LENGTH_SHORT).show( );
                mainFrameActivity.onClick(view);
                break;
            case (R.id.overlap_buttonHide):
                Toast.makeText(getContext( ), "HIDE 버튼", Toast.LENGTH_SHORT).show( );
                mainFrameActivity.onClick(view);
                break;
        }
    }
    
    @Override
    public void onBackPressed( )
    {
        mainFrameActivity.hideTopFragment(this);
    }
    
    @Override
    public void onResume( )
    {
        super.onResume( );
        mainFrameActivity.setOnBackPressedListener(this);
    }
}