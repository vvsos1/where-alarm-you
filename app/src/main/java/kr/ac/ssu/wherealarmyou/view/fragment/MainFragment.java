package kr.ac.ssu.wherealarmyou.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jahirfiquitiva.libs.fabsmenu.FABsMenu;
import jahirfiquitiva.libs.fabsmenu.FABsMenuListener;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.common.Icon;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.location.Location;
import kr.ac.ssu.wherealarmyou.view.DataManager;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.adapter.AlarmItemAdapter;
import kr.ac.ssu.wherealarmyou.view.adapter.IconItemAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.RecyclerViewDecoration;
import kr.ac.ssu.wherealarmyou.view.fragment.alarm.AlarmAddFragment;
import kr.ac.ssu.wherealarmyou.view.fragment.group.GroupAddFragment;
import kr.ac.ssu.wherealarmyou.view.fragment.group.GroupFragment;
import kr.ac.ssu.wherealarmyou.view.fragment.location.LocationAddFragment;
import kr.ac.ssu.wherealarmyou.view.fragment.location.LocationFragment;
import kr.ac.ssu.wherealarmyou.view.login.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements View.OnClickListener
{
    private final DataManager dataManager = DataManager.getInstance( );
    
    private MainFrameActivity mainFrameActivity;
    
    // FAB ( Floating Action Button )
    private FABsMenu fabsMenu;
    private View     fabsBlind;
    
    public static MainFragment getInstance( )
    {
        return new MainFragment( );
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        List<Alarm>    alarms    = dataManager.getAlarmData( ).getValue( );
        List<Group>    groups    = dataManager.getGroupData( ).getValue( );
        List<Location> locations = dataManager.getLocationData( ).getValue( );
        
        List<Icon> groupIcons = new ArrayList<>( );
        if (groups != null) {
            for (Group group : groups) {
                groupIcons.add(group.getIcon( ));
            }
        }
        
        List<Icon> locationIcons = new ArrayList<>( );
        if (locations != null) {
            for (Location location : locations) {
                locationIcons.add(location.getIcon( ));
            }
        }
        
        View contentView = inflater.inflate(R.layout.content_main, container, false);
        mainFrameActivity = (MainFrameActivity)getActivity( );
        
        // FAB Setting
        fabsMenu  = contentView.findViewById(R.id.main_fabsMenu);
        fabsBlind = contentView.findViewById(R.id.main_viewFabsBlind);
        TitleFAB buttonAddAlarm    = contentView.findViewById(R.id.main_fabsButtonAddAlarm);
        TitleFAB buttonAddLocation = contentView.findViewById(R.id.main_fabsButtonAddLocation);
        TitleFAB buttonAddGroup    = contentView.findViewById(R.id.main_fabsButtonAddGroup);
        
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
        
        
        // Content View Setting - Location Recycler View Setting
        RecyclerView        recyclerViewLocation = contentView.findViewById(R.id.main_recyclerViewLocation);
        IconItemAdapter     locationAdapter      = new IconItemAdapter(getContext( ), locationIcons);
        LinearLayoutManager linearLayoutManagerL = new LinearLayoutManager(getContext( ));
        
        locationAdapter.setOnItemClickListener((view, icon) -> {
            // TODO : 알람 필터링 구현
        });
        
        linearLayoutManagerL.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewLocation.setAdapter(locationAdapter);
        recyclerViewLocation.setLayoutManager(linearLayoutManagerL);
        
        
        // Content View Setting - Group Recycler View Setting
        RecyclerView        recyclerViewGroup    = contentView.findViewById(R.id.main_recyclerViewGroup);
        IconItemAdapter     groupAdapter         = new IconItemAdapter(getContext( ), groupIcons);
        LinearLayoutManager linearLayoutManagerG = new LinearLayoutManager(getContext( ));
        
        groupAdapter.setOnItemClickListener((view, icon) -> {
            // TODO : 알람 필터링 구현
        });
        
        linearLayoutManagerG.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewGroup.setAdapter(groupAdapter);
        recyclerViewGroup.setLayoutManager(linearLayoutManagerG);
        
        dataManager.getGroupData( ).observe(getViewLifecycleOwner( ), groups_ -> {
            groupIcons.clear( );
            for (Group group : groups_) {
                groupIcons.add(group.getIcon( ));
            }
            IconItemAdapter newGroupAdapter = new IconItemAdapter(getContext( ), groupIcons);
            recyclerViewGroup.setAdapter(newGroupAdapter);
        });
        
        
        // Content View Setting - Alarm Recycler View Setting
        RecyclerView           recyclerViewAlarm      = contentView.findViewById(R.id.main_recyclerViewAlarm);
        AlarmItemAdapter       alarmItemAdapter       = new AlarmItemAdapter(getContext( ), alarms);
        LinearLayoutManager    linearLayoutManagerA   = new LinearLayoutManager(getContext( ));
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(20);
        
        alarmItemAdapter.setOnItemClickListener((view, alarm) -> {
            // TODO : 알람 클릭 이벤트 구현
        });
       /*
        recyclerViewAlarm.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            RelativeLayout relativeLayoutLocation = contentView.findViewById(R.id.main_relativeLayoutLocation);
            RelativeLayout relativeLayoutGroup    = contentView.findViewById(R.id.main_relativeLayoutGroup);
            
            if (oldScrollY - scrollY > 200) {
                relativeLayoutLocation.setVisibility(View.VISIBLE);
                relativeLayoutGroup.setVisibility(View.VISIBLE);
            }
            else if (scrollY - oldScrollY > 200) {
                relativeLayoutLocation.setVisibility(View.GONE);
                relativeLayoutGroup.setVisibility(View.GONE);
            }
            else if (!recyclerViewAlarm.canScrollVertically(-1)) {
                relativeLayoutLocation.setVisibility(View.VISIBLE);
                relativeLayoutGroup.setVisibility(View.VISIBLE);
            }
            
        });
        */
        recyclerViewAlarm.setAdapter(alarmItemAdapter);
        recyclerViewAlarm.setLayoutManager(linearLayoutManagerA);
        recyclerViewAlarm.addItemDecoration(recyclerViewDecoration);
        
        dataManager.getAlarmData( ).observe(getViewLifecycleOwner( ), alarms_ -> {
            AlarmItemAdapter newAlarmItemAdapter = new AlarmItemAdapter(getContext( ), alarms_);
            recyclerViewAlarm.setAdapter(newAlarmItemAdapter);
        });
        
        
        // Content View Setting - Other Items
        TextView textViewLocationInfo = contentView.findViewById(R.id.main_textViewToLocation);
        TextView textVIewGroupInfo    = contentView.findViewById(R.id.main_textViewToGroup);
        TextView textViewUserProfile  = contentView.findViewById(R.id.main_textViewUserProfile);
        
        textViewLocationInfo.setOnClickListener(this);
        textVIewGroupInfo.setOnClickListener(this);
        textViewUserProfile.setOnClickListener(this);
        
        return contentView;
    }
    
    @Override
    public void onClick(View view)
    {
        switch (view.getId( )) {
            case (R.id.main_textViewToLocation):
                MainFrameActivity.showTopFragment(new LocationFragment( ));
                break;
            case (R.id.main_textViewToGroup):
                MainFrameActivity.showTopFragment(new GroupFragment( ));
                break;
            case (R.id.main_textViewUserProfile):
                startActivity(new Intent(mainFrameActivity.getApplicationContext( ), ProfileActivity.class));
                break;
            case (R.id.main_fabsButtonAddAlarm):
                MainFrameActivity.showTopFragment(new AlarmAddFragment(null));
                fabsMenu.collapse( );
                break;
            case (R.id.main_fabsButtonAddLocation):
                MainFrameActivity.showTopFragment(LocationAddFragment.getInstance( ));
                fabsMenu.collapse( );
                break;
            case (R.id.main_fabsButtonAddGroup):
                MainFrameActivity.showTopFragment(GroupAddFragment.getInstance( ));
                fabsMenu.collapse( );
                break;
        }
    }
}
