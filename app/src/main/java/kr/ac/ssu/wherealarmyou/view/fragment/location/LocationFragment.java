package kr.ac.ssu.wherealarmyou.view.fragment.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.location.Location;
import kr.ac.ssu.wherealarmyou.view.DataManager;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.adapter.LocationItemAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;
import kr.ac.ssu.wherealarmyou.view.custom_view.RecyclerViewDecoration;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class LocationFragment extends Fragment implements View.OnClickListener
{
    private final DataManager dataManager = DataManager.getInstance( );
    
    public static LocationFragment getInstance( )
    {
        return new LocationFragment( );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle bundle = Objects.requireNonNull(getArguments( ));
        
        AtomicReference<List<Location>> locations = new AtomicReference<>(
                dataManager.getLocationLiveData( ).getValue( ));
        
        View frameView   = inflater.inflate(R.layout.frame_overlap, container, false);
        View contentView = inflater.inflate(R.layout.content_location, null);
        
        // Frame View Setting
        OverlappingView overlappingView = frameView.findViewById(R.id.overlap_view);
        overlappingView.setAtOnce(bundle, frameView, contentView, "장소", true, true);
        
        Button buttonAdd = frameView.findViewById(R.id.overlap_buttonAdd);
        buttonAdd.setOnClickListener(view -> overlappingView.onAddClick(LocationAddFragment.getInstance( )));
        
        RecyclerView           recyclerView           = contentView.findViewById(R.id.location_recyclerView);
        LocationItemAdapter    locationItemAdapter    = new LocationItemAdapter(getContext( ), locations.get( ));
        LinearLayoutManager    linearLayoutManager    = new LinearLayoutManager(getContext( ));
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(30);
        
        locationItemAdapter.setOnItemClickListener((view, location) ->
                MainFrameActivity.addTopFragment(new LocationInfoFragment(location)));
        
        recyclerView.setAdapter(locationItemAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(recyclerViewDecoration);
        
        dataManager.getLocationLiveData().observe(getViewLifecycleOwner(), locations_ -> {
            LocationItemAdapter newLocationItemAdapter = new LocationItemAdapter(getContext(), locations_);
            newLocationItemAdapter.setOnItemClickListener((view, location) ->
                    MainFrameActivity.addTopFragment(new LocationInfoFragment(location)));
            recyclerView.setAdapter(newLocationItemAdapter);
        });
        
        return frameView;
    }
    
    @Override
    public void onClick(View view) { }
}