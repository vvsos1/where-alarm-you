package kr.ac.ssu.wherealarmyou.view.fragment.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;

import java.util.Objects;

public class LocationAddFragment extends Fragment implements View.OnClickListener
{
    public static LocationAddFragment getInstance( )
    {
        return new LocationAddFragment( );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle bundle = Objects.requireNonNull(getArguments( ));
        
        View frameView   = inflater.inflate(R.layout.frame_overlap, container, false);
        View contentView = inflater.inflate(R.layout.content_location_add, null);
        
        // Frame View Setting
        OverlappingView overlappingView = frameView.findViewById(R.id.overlap_view);
        overlappingView.setAtOnce(bundle, frameView, contentView, "장소 추가", false, true);
        
        return frameView;
    }
    
    @Override
    public void onClick(View view) { }
}