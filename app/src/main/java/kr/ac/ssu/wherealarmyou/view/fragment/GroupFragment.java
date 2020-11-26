package kr.ac.ssu.wherealarmyou.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.common.Icon;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.view.custom_view.GroupContentViewAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;
import kr.ac.ssu.wherealarmyou.view.custom_view.RecyclerViewDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupFragment extends Fragment implements View.OnClickListener, OnBackPressedListener
{
    private Bundle bundle;
    
    private OverlappingView overlappingView;
    
    public static GroupFragment getInstance( )
    {
        return new GroupFragment( );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        bundle = Objects.requireNonNull(getArguments( ));
        
        View frameView   = inflater.inflate(R.layout.frame_overlap_content, container, false);
        View contentView = inflater.inflate(R.layout.content_group, null);
        
        // Frame View Setting
        overlappingView = frameView.findViewById(R.id.overlap_view);
        overlappingView.setAtOnce(bundle, frameView, contentView, "그룹", true, true);
        
        Button buttonAdd = frameView.findViewById(R.id.overlap_buttonAdd);
        buttonAdd.setOnClickListener(this);
        
        // test data
        List<Group> groups = new ArrayList<>( );
        for (int i = 0; i < 14; i++) {
            groups.add(new Group("Group" + i, new Icon("", "GRP" + i)));
        }
        
        // Content View Setting
        RecyclerView            recyclerView           = frameView.findViewById(R.id.recyclerViewGroup);
        LinearLayoutManager     linearLayoutManager    = new LinearLayoutManager(getContext( ));
        GroupContentViewAdapter recyclerViewAdapter    = new GroupContentViewAdapter(getContext( ), groups);
        RecyclerViewDecoration  recyclerViewDecoration = new RecyclerViewDecoration(30);
        
        recyclerViewAdapter.setOnGroupClickListener((itemView, group) ->
                MainFrameActivity.addTopFragment(GroupDetailFragment.getInstance(group)));
        
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(recyclerViewDecoration);
//        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext( )), linearLayoutManager.getOrientation( )));
        
        return frameView;
    }
    
    @Override
    public void onClick(View view)
    {
        if (view.getId( ) == R.id.overlap_buttonAdd) {
            overlappingView.onAddClick(GroupAddFragment.getInstance( ));
        }
    }
    
    
    @Override
    public void onBackPressed( )
    {
        if (bundle.getBoolean("backButton")) { MainFrameActivity.backTopFragment(this); }
        else if (bundle.getBoolean("hideButton")) { MainFrameActivity.hideTopFragment(this); }
    }
    
    @Override
    public void onResume( )
    {
        super.onResume( );
        MainFrameActivity.setOnBackPressedListener(this);
    }
}