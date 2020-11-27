package kr.ac.ssu.wherealarmyou.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.DatesAlarm;
import kr.ac.ssu.wherealarmyou.view.custom_view.AlarmAddContentViewAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.AlarmAddFrameItem;
import kr.ac.ssu.wherealarmyou.view.custom_view.AlarmAddTimeViewModel;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class AlarmAddFragment extends Fragment implements View.OnClickListener, OnBackPressedListener
{
    private static final int TIME     = 0;
    private static final int WEEK     = 1;
    private static final int LOCATION = 2;
    private static final int GROUP    = 3;
    private static final int MEMO     = 4;
    private static final int DETAIL   = 5;
    
    private Alarm alarm;
    
    private Bundle bundle;
    
    private RecyclerView.LayoutManager layoutManager;
    
    private FragmentManager fragmentManager;
    
    public AlarmAddFragment( ) {}
    
    public static AlarmAddFragment getInstance( )
    {
        return new AlarmAddFragment( );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        alarm           = new DatesAlarm( );
        bundle          = Objects.requireNonNull(getArguments( ));
        fragmentManager = Objects.requireNonNull(getActivity( )).getSupportFragmentManager( );
        
        View frameView   = inflater.inflate(R.layout.frame_overlap_content, container, false);
        View contentView = inflater.inflate(R.layout.content_alarm_add, null);
        
        // Frame View Setting
        OverlappingView overlappingView = frameView.findViewById(R.id.overlap_view);
        overlappingView.setAtOnce(bundle, frameView, contentView, "알람 추가", false, true);
        
        // test data
        List<AlarmAddFrameItem> items = new ArrayList<>( );
        items.add(new AlarmAddFrameItem(R.drawable.ic_time, "시간", ""));
        items.add(new AlarmAddFrameItem(R.drawable.ic_location, "장소", ""));
        items.add(new AlarmAddFrameItem(R.drawable.ic_group_add, "그룹", ""));
        
        // Content View Setting
        RecyclerView               recyclerView               = frameView.findViewById(R.id.recyclerView);
        AlarmAddContentViewAdapter alarmAddContentViewAdapter = new AlarmAddContentViewAdapter(getContext( ), items);
        LinearLayoutManager        linearLayoutManager        = new LinearLayoutManager(getContext( ));
        DividerItemDecoration decoration = new DividerItemDecoration(Objects.requireNonNull(getContext( )),
                linearLayoutManager.getOrientation( ));
        layoutManager = recyclerView.getLayoutManager( );
        
        // Content View Event Listener
        AtomicInteger currentVisibleCategoryPosition = new AtomicInteger( );
        alarmAddContentViewAdapter.setOnItemClickListener((view, position) -> {
            startCategoryFragment(position);
            setViewModel(position);
            changeVisibleCategoryView(currentVisibleCategoryPosition.get( ), position);
            currentVisibleCategoryPosition.set(position);
        });
        
        recyclerView.setAdapter(alarmAddContentViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(decoration);
        
        return frameView;
    }
    
    private void startCategoryFragment(int category)
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        
        Fragment fragment = null;
        if (category == TIME) { fragment = AlarmAddTimeFragment.getInstance( ); }
//        if (category == WEEK)     { fragment = AlarmAddWeekFragment.getInstance( ); }
//        if (category == LOCATION) { fragment = AlarmAddLocationFragment.getInstance( ); }
//        if (category == GROUP)    { fragment = AlarmAddGroupFragment.getInstance( ); }
//        if (category == MEMO)     { fragment = AlarmAddMemoFragment.getInstance( ); }
//        if (category == DETAIL)   { fragment = AlarmAddDetailFragment.getInstance( ); }
        
        if (fragment != null) {
            fragmentTransaction//.setCustomAnimations(R.anim.fade_in, R.anim.test_anim, R.anim.fade_in, R.anim.test_anim)
                               .replace(R.id.contentAlarmAddFrameItem, fragment)
                               .addToBackStack(null)
                               .commit( );
        }
    }
    
    // TODO ViewModel에서 setData와 setInfo 분리하기 (getViewModel 이용)
    private void setViewModel(int category)
    {
        if (category == TIME) {
            AlarmAddTimeViewModel alarmAddTimeViewModel =
                    new ViewModelProvider(requireActivity( )).get(AlarmAddTimeViewModel.class);
            alarmAddTimeViewModel.getTimeData( ).observe(getViewLifecycleOwner( ), time -> {
                Log.d("setTimeDataInFrag1", "time" + time.getHours( ) + time.getMinutes( ));
                alarm.setTime(time);
                Log.d("setTimeDataInFrag2", "time" + time.getHours( ) + time.getMinutes( ));
            });
            alarmAddTimeViewModel.getInfoString( )
                                 .observe(getViewLifecycleOwner( ), string -> setInfo(string, category));
        }
    }
    
    private ViewModel getViewModel(int category)
    {
        if (category == TIME) { return new ViewModelProvider(requireActivity( )).get(AlarmAddTimeViewModel.class); }
        return null;
    }
    
    private void setInfo(String string, int position)
    {
        TextView infoText = Objects.requireNonNull(layoutManager.findViewByPosition(position))
                                   .findViewById(R.id.infoAlarmAddFrameItem);
        infoText.setText(string);
    }
    
    private void changeVisibleCategoryView(int position_oldView, int position_newView)
    {
        Objects.requireNonNull(layoutManager.findViewByPosition(position_oldView))
               .findViewById(R.id.contentAlarmAddFrameItem)
               .setVisibility(View.GONE);
        Objects.requireNonNull(layoutManager.findViewByPosition(position_newView))
               .findViewById(R.id.contentAlarmAddFrameItem)
               .setVisibility(View.VISIBLE);
    }
    
    @Override
    public void onClick(View view) { }
    
    @Override
    public void onBackPressed( )
    {
        if (bundle.getBoolean("backButton")) { MainFrameActivity.backTopFragment(this); }
        else if (bundle.getBoolean("hideButton")) MainFrameActivity.hideTopFragment(this);
    }
    
    @Override
    public void onResume( )
    {
        super.onResume( );
        MainFrameActivity.setOnBackPressedListener(this);
    }
    
    @Override
    public void onStop( )
    {
        super.onStop( );
        Toast.makeText(getContext( ), alarm.getTime( ).getHours( ) + "시" + alarm.getTime( ).getMinutes( ) + "분",
                Toast.LENGTH_SHORT).show( );
    }
}