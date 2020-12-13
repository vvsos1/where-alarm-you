package kr.ac.ssu.wherealarmyou.view.fragment.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.location.Location;
import kr.ac.ssu.wherealarmyou.view.DataManager;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.adapter.LocationItemAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.RecyclerViewDecoration;
import kr.ac.ssu.wherealarmyou.view.fragment.OnBackPressedListener;
import kr.ac.ssu.wherealarmyou.view.viewmodel.AlarmAddLocationViewModel;

import java.util.List;

// TODO : AlarmAddFragment 연결
// TODO : Location 추가 버튼으로 그룹 추가 페이지 이동 (프래그먼트 전환은 MainFrameActivity 이용)
public class AlarmAddLocationFragment extends Fragment implements OnBackPressedListener
{
    private final DataManager dataManager = DataManager.getInstance( );
    
    private AlarmAddLocationViewModel alarmAddLocationViewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        List<Location> locations = dataManager.getLocationData( ).getValue( );
        
        View contentView = inflater.inflate(R.layout.content_alarm_add_location, container, false);
    
        alarmAddLocationViewModel = new ViewModelProvider(requireActivity( )).get(AlarmAddLocationViewModel.class);
        
        // Content View Setting - Group Recycler View (그룹 리스트)
        RecyclerView           recyclerView           = contentView.findViewById(R.id.alarmAddLocation_recyclerView);
        LocationItemAdapter    locationItemAdapter    = new LocationItemAdapter(getContext( ), locations);
        LinearLayoutManager    linearLayoutManager    = new LinearLayoutManager(getContext( ));
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(30);
        
        locationItemAdapter.setOnItemClickListener((view, location) -> {
            alarmAddLocationViewModel.setLiveData(location);
            alarmAddLocationViewModel.complete( );
        });
        
        recyclerView.setAdapter(locationItemAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(recyclerViewDecoration);
        
        return contentView;
    }
    
    @Override
    public void onResume( )
    {
        super.onResume( );
        MainFrameActivity.setOnBackPressedListener(this);
    }
    
    @Override
    public void onBackPressed( )
    {
        MainFrameActivity.hideTopFragment( );
    }
}
