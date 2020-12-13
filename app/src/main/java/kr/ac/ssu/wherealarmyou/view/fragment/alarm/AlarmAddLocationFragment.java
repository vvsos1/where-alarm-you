package kr.ac.ssu.wherealarmyou.view.fragment.alarm;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.location.Location;
import kr.ac.ssu.wherealarmyou.view.DataManager;
import kr.ac.ssu.wherealarmyou.view.adapter.LocationItemAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.RecyclerViewDecoration;
import kr.ac.ssu.wherealarmyou.view.viewmodel.AlarmAddLocationViewModel;

public class AlarmAddLocationFragment extends Fragment implements View.OnClickListener {


    private final DataManager dataManager = DataManager.getInstance();
    Button setIncludeBtn;
    Button setExcludeBtn;
    View selectInOrEx;
    View choiceLocation;
    boolean isInclude;
    private AlarmAddLocationViewModel alarmAddLocationViewModel;

    HashMap hashMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.content_alarm_add_location, container, false);
        alarmAddLocationViewModel = new ViewModelProvider(getActivity()).get(AlarmAddLocationViewModel.class);
        hashMap = new HashMap();

        List<Location> myLocations = dataManager.getLocationData().getValue();

        setIncludeBtn = contentView.findViewById(R.id.alarmAddLocation_buttonLocationOn);
        setIncludeBtn.setOnClickListener(this::onClick);
        setExcludeBtn = contentView.findViewById(R.id.alarmAddLocation_buttonLocationOff);
        setExcludeBtn.setOnClickListener(this::onClick);

        selectInOrEx = contentView.findViewById(R.id.alarmAddLocation_linearLayoutSetLocationCondition);
        choiceLocation = contentView.findViewById(R.id.alarmAddLocation_recyclerView);


        RecyclerView recyclerView = contentView.findViewById(R.id.alarmAddLocation_recyclerView);
        LocationItemAdapter locationItemAdapter = new LocationItemAdapter(getContext(), myLocations);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(30);

        locationItemAdapter.setOnItemClickListener((view, location) -> {
            choiceLocation.setVisibility(View.GONE);
            alarmAddLocationViewModel.setInfoString(location.getTitle());
            hashMap.put("uid", location.getUid());
            hashMap.put("inInclude", isInclude);
            Toast.makeText(getContext(), String.valueOf(hashMap.size()), Toast.LENGTH_SHORT).show();
            alarmAddLocationViewModel.setLiveData(hashMap);
            alarmAddLocationViewModel.complete();
        });

        recyclerView.setAdapter(locationItemAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(recyclerViewDecoration);


        return contentView;
    }

    @Override
    public void onClick(View v) {

        if (v == setIncludeBtn) {
            selectInOrEx.setVisibility(View.GONE);
            choiceLocation.setVisibility(View.VISIBLE);
            isInclude = true;

        } else if (v == setExcludeBtn) {
            selectInOrEx.setVisibility(View.GONE);
            choiceLocation.setVisibility(View.VISIBLE);
            isInclude = false;
        }

        hashMap.put("isInclude", isInclude);
//        alarmAddLocationViewModel.setLiveData(hashMap);

    }
}
