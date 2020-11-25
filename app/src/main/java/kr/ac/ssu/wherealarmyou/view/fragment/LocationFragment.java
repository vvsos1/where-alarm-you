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

public class LocationFragment extends Fragment implements View.OnClickListener, OnBackPressedListener
{
    private FrameActivity frameActivity;
    
    private View            view;
    private OverlappingView overlappingView;
    
    private Button buttonAdd;
    private Button buttonHide;
    
    public static LocationFragment getInstance( )
    {
        return new LocationFragment( );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        frameActivity = (FrameActivity)getActivity( );
        
        view = inflater.inflate(R.layout.frame_overlap_content, container, false);
        
        overlappingView = view.findViewById(R.id.overlap_view);
        overlappingView.setTitle("장소");
        overlappingView.setButtonAdd(true);
        overlappingView.setButtonHide(true);
        overlappingView.setContent(inflater.inflate(R.layout.content_location, null));
    
        buttonAdd  = view.findViewById(R.id.overlap_buttonAdd);
        buttonHide = view.findViewById(R.id.overlap_buttonHide);
        
        buttonAdd.setOnClickListener(this);
        buttonHide.setOnClickListener(this);
        
        return view;
    }
    
    @Override
    public void onClick(View view)
    {
        if (view == buttonAdd) {
            Toast.makeText(getContext( ), "ADD 버튼", Toast.LENGTH_SHORT).show( );
            frameActivity.addTopFragment(LocationAddFragment.getInstance( ));
        }
        if (view == buttonHide) {
            Toast.makeText(getContext( ), "HIDE 버튼", Toast.LENGTH_SHORT).show( );
            frameActivity.hideTopFragment(this);
        }
    }
    
    @Override
    public void onBackPressed( )
    {
        frameActivity.hideTopFragment(this);
    }
    
    @Override
    public void onResume( )
    {
        super.onResume( );
        frameActivity.setOnBackPressedListener(this);
    }
}