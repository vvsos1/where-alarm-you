package kr.ac.ssu.wherealarmyou.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jahirfiquitiva.libs.fabsmenu.FABsMenu;
import jahirfiquitiva.libs.fabsmenu.FABsMenuListener;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.common.Icon;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.view.DataManager;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.adapter.IconItemAdapter;
import kr.ac.ssu.wherealarmyou.view.fragment.alarm.AlarmAddFragment;
import kr.ac.ssu.wherealarmyou.view.fragment.group.GroupAddFragment;
import kr.ac.ssu.wherealarmyou.view.fragment.group.GroupFragment;
import kr.ac.ssu.wherealarmyou.view.fragment.location.LocationAddFragment;
import kr.ac.ssu.wherealarmyou.view.fragment.location.LocationFragment;
import kr.ac.ssu.wherealarmyou.view.login.ProfileActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BottomFragment extends Fragment implements View.OnClickListener
{
    private DataManager dataManager = DataManager.getInstance( );
    
    private MainFrameActivity mainFrameActivity;
    
    // FAB ( Floating Action Button )
    private FABsMenu fabsMenu;
    private View     fabsBlind;
    
    // test
    private Button buttonLocation;
    private Button buttonGroup;
    private Button buttonTemporary;
    
    public static BottomFragment getInstance( )
    {
        return new BottomFragment( );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        AtomicReference<List<Group>> groups = new AtomicReference<>(dataManager.getGroupData( ).getValue( ));
        
        List<Icon> groupIcons = new ArrayList<>( );
        for (Group group : groups.get( )) {
            groupIcons.add(group.getIcon( ));
        }
        
        View contentView = inflater.inflate(R.layout.content_main, container, false);
        mainFrameActivity = (MainFrameActivity)getActivity( );
        
        // FAB Setting
        fabsMenu  = contentView.findViewById(R.id.bottom_fabsMenu);
        fabsBlind = contentView.findViewById(R.id.bottom_viewFabsBlind);
        TitleFAB buttonAddAlarm    = contentView.findViewById(R.id.bottom_fabsButtonAddAlarm);
        TitleFAB buttonAddLocation = contentView.findViewById(R.id.bottom_fabsButtonAddLocation);
        TitleFAB buttonAddGroup    = contentView.findViewById(R.id.bottom_fabsButtonAddGroup);
        
        RecyclerView        recyclerViewLocation = contentView.findViewById(R.id.main_recyclerViewLocation);
        IconItemAdapter     locationAdapter      = new IconItemAdapter(getContext( ), groupIcons);
        LinearLayoutManager linearLayoutManagerL = new LinearLayoutManager(getContext( ));
        
        locationAdapter.setOnItemClickListener((view, icon) -> {
            // TODO 알람 필터링 구현
        });
        
        recyclerViewLocation.setAdapter(locationAdapter);
        recyclerViewLocation.setLayoutManager(linearLayoutManagerL);
        
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
        
        // test
        buttonLocation  = contentView.findViewById(R.id.buttonLocation);
        buttonGroup     = contentView.findViewById(R.id.buttonGroup);
        buttonTemporary = contentView.findViewById(R.id.buttonProfile);
        
        buttonLocation.setOnClickListener(this);
        buttonGroup.setOnClickListener(this);
        buttonTemporary.setOnClickListener(this);
        
        return contentView;
    }
    
    @Override
    public void onClick(View view)
    {
        switch (view.getId( )) {
            case (R.id.buttonLocation):
                MainFrameActivity.showTopFragment(LocationFragment.getInstance( ));
                break;
            case (R.id.buttonGroup):
                MainFrameActivity.showTopFragment(GroupFragment.getInstance( ));
                break;
            case (R.id.buttonProfile):
                startActivity(new Intent(mainFrameActivity.getApplicationContext( ), ProfileActivity.class));
                mainFrameActivity.finish( );
                break;
            case (R.id.bottom_fabsButtonAddAlarm):
                MainFrameActivity.showTopFragment(AlarmAddFragment.getInstance( ));
                fabsMenu.collapse( );
                break;
            case (R.id.bottom_fabsButtonAddLocation):
                MainFrameActivity.showTopFragment(LocationAddFragment.getInstance( ));
                fabsMenu.collapse( );
                break;
            case (R.id.bottom_fabsButtonAddGroup):
                MainFrameActivity.showTopFragment(GroupAddFragment.getInstance( ));
                fabsMenu.collapse( );
                break;
        }
    }
}