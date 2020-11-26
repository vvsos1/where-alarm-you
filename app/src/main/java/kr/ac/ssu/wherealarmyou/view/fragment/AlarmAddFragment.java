package kr.ac.ssu.wherealarmyou.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.view.custom_view.AlarmAddItem;
import kr.ac.ssu.wherealarmyou.view.custom_view.AlarmAddRecyclerViewAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlarmAddFragment extends Fragment implements View.OnClickListener, OnBackPressedListener
{
    private RecyclerView                recyclerView;
    private LinearLayoutManager         linearLayoutManager;
    private AlarmAddRecyclerViewAdapter recyclerViewAdapter;
    private OverlappingView             overlappingView;
    private Button                      buttonBack;
    private Button                      buttonHide;
    
    public static AlarmAddFragment getInstance( )
    {
        return new AlarmAddFragment( );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle bundle = Objects.requireNonNull(getArguments( ));
        
        View view = inflater.inflate(R.layout.frame_overlap_content, container, false);
        
        overlappingView = view.findViewById(R.id.overlap_view);
        overlappingView.setTitle("알람 추가");
        overlappingView.setButtonBack(bundle.getBoolean("backButton"));
        overlappingView.setButtonHide(bundle.getBoolean("hideButton"));
        overlappingView.setContent(inflater.inflate(R.layout.content_alarm_add, null));
        
        recyclerView        = view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext( ));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                Objects.requireNonNull(getContext( )), linearLayoutManager.getOrientation( )));
        recyclerView.setLayoutManager(linearLayoutManager);
        
        List<AlarmAddItem> alarmAddItems = new ArrayList<>( );
        alarmAddItems.add(
                new AlarmAddItem(R.drawable.ic_location, "위치", "집", inflater.inflate(R.layout.content_location, null)));
        alarmAddItems.add(
                new AlarmAddItem(R.drawable.ic_group_add, "그룹", "과정방", inflater.inflate(R.layout.content_group, null)));
        
        recyclerViewAdapter = new AlarmAddRecyclerViewAdapter(getActivity( ), alarmAddItems);
        recyclerView.setAdapter(recyclerViewAdapter);
        
        buttonBack = view.findViewById(R.id.overlap_buttonBack);
        buttonHide = view.findViewById(R.id.overlap_buttonHide);
        
        buttonBack.setOnClickListener(this);
        buttonHide.setOnClickListener(this);
        
        return view;
    }
    
    @Override
    public void onClick(View view)
    {
        switch (view.getId( )) {
            case (R.id.overlap_buttonBack):
            case (R.id.overlap_buttonHide):
                overlappingView.onClick(view);
                break;
        }
    }
    
    @Override
    public void onBackPressed( )
    {
        if (buttonBack.getVisibility( ) == View.VISIBLE) { MainFrameActivity.backTopFragment(this); }
        else if (buttonHide.getVisibility( ) == View.VISIBLE) MainFrameActivity.hideTopFragment(this);
    }
    
    @Override
    public void onResume( )
    {
        super.onResume( );
        MainFrameActivity.setOnBackPressedListener(this);
    }
}