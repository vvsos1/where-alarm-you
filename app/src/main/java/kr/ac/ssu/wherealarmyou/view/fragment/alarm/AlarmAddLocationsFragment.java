package kr.ac.ssu.wherealarmyou.view.fragment.alarm;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.LocationCondition;
import kr.ac.ssu.wherealarmyou.location.Location;
import kr.ac.ssu.wherealarmyou.view.DataManager;
import kr.ac.ssu.wherealarmyou.view.adapter.LocationItemAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.RecyclerViewDecoration;
import kr.ac.ssu.wherealarmyou.view.viewmodel.AlarmAddLocationsViewModel;

public class AlarmAddLocationsFragment extends Fragment implements View.OnClickListener {


    private final DataManager dataManager = DataManager.getInstance();
    Button setIncludeBtn;
    Button setExcludeBtn;
    View selectInOrEx;
    View choiceLocation;
    private LocationCondition locationCondition;
    private AlarmAddLocationsViewModel alarmAddLocationsViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.content_alarm_add_location, container, false);
        alarmAddLocationsViewModel = new ViewModelProvider(getActivity()).get(AlarmAddLocationsViewModel.class);


        List<Location> myLocations = dataManager.getLocationData().getValue();

        setExcludeBtn = contentView.findViewById(R.id.alarmAddLocation_buttonLocationOn);
        setExcludeBtn.setOnClickListener(this::onClick);
        setIncludeBtn = contentView.findViewById(R.id.alarmAddLocation_buttonLocationOff);
        setIncludeBtn.setOnClickListener(this::onClick);

        selectInOrEx = contentView.findViewById(R.id.alarmAddLocation_linearLayoutSetLocationCondition);
        choiceLocation = contentView.findViewById(R.id.alarmAddLocation_recyclerView);


        RecyclerView recyclerView = contentView.findViewById(R.id.alarmAddLocation_recyclerView);
        LocationItemAdapter locationItemAdapter = new LocationItemAdapter(getContext(), myLocations);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(30);

        locationItemAdapter.setOnItemClickListener((view, location) -> {
            locationCondition.addLocation(location.getUid());
            alarmAddLocationsViewModel.setLiveData(new LocationCondition(new HashMap<String, Boolean>(locationCondition.getMap()), locationCondition.isInclude()));
            alarmAddLocationsViewModel.setInfoString(location.getTitle());
            alarmAddLocationsViewModel.complete();
        });

        recyclerView.setAdapter(locationItemAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(recyclerViewDecoration);


        return contentView;
    }

    @Override
    public void onClick(View v) {

        if (v == setIncludeBtn) {
            locationCondition = new LocationCondition(new HashMap<String, Boolean>(), true);


        } else if (v == setExcludeBtn) {
            locationCondition = new LocationCondition(new HashMap<String, Boolean>(), false);
        }

        selectInOrEx.setVisibility(View.GONE);
        choiceLocation.setVisibility(View.VISIBLE);
    }
}
