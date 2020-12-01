package kr.ac.ssu.wherealarmyou.view.fragment.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.common.Icon;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.custom_view.GroupRecyclerViewAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;
import kr.ac.ssu.wherealarmyou.view.custom_view.RecyclerViewDecoration;
import kr.ac.ssu.wherealarmyou.view.fragment.OnBackPressedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupAddFragment extends Fragment implements View.OnClickListener, OnBackPressedListener
{
    private Bundle bundle;
    
    private OverlappingView overlappingView;
    
    // Content View
    private TextView makeGroup;
    private EditText findGroup;
    
    public static GroupAddFragment getInstance( )
    {
        return new GroupAddFragment( );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        bundle = Objects.requireNonNull(getArguments( ));
        
        View frameView   = inflater.inflate(R.layout.frame_overlap, container, false);
        View contentView = inflater.inflate(R.layout.content_group_add, null);
        
        // Frame View Setting
        overlappingView = frameView.findViewById(R.id.overlap_view);
        overlappingView.setAtOnce(bundle, frameView, contentView, "그룹 추가", false, true);
    
        // test data
        List<Group> groups = new ArrayList<>( );
        groups.add(new Group("Group" + 10, new Icon("#AFE3FF", "GRP" + 10)));
        groups.add(new Group("Group" + 11, new Icon("#BFE3FF", "GRP" + 11)));
        groups.add(new Group("Group" + 12, new Icon("#CFE3FF", "GRP" + 12)));
        groups.add(new Group("Group" + 13, new Icon("#DFE3FF", "GRP" + 13)));
        groups.add(new Group("Group" + 14, new Icon("#EFE3FF", "GRP" + 14)));
        groups.add(new Group("Group" + 15, new Icon("#FFE3FF", "GRP" + 15)));
        for (int i = 0; i < 10; i++) {
            groups.add(new Group("Group" + i, new Icon("#" + i + "FE3FF", "GRP" + i)));
        }
        
        // Content View Setting
        RecyclerView            recyclerView            = frameView.findViewById(R.id.groupAdd_recyclerView);
        LinearLayoutManager      linearLayoutManager      = new LinearLayoutManager(getContext( ));
        GroupRecyclerViewAdapter groupRecyclerViewAdapter = new GroupRecyclerViewAdapter(getContext( ), groups);
        RecyclerViewDecoration   recyclerViewDecoration   = new RecyclerViewDecoration(30);
        
        makeGroup = contentView.findViewById(R.id.groupAdd_textViewMakeGroup);
        findGroup = contentView.findViewById(R.id.groupAdd_editTextFindGroup);
        
        makeGroup.setOnClickListener(this);
        groupRecyclerViewAdapter.setOnGroupClickListener((itemView, group) ->
                MainFrameActivity.addTopFragment(GroupJoinFragment.getInstance(group)));
        
        recyclerView.setAdapter(groupRecyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(recyclerViewDecoration);
        
        return frameView;
    }
    
    @Override
    public void onClick(View view)
    {
        if (view == makeGroup) {
            MainFrameActivity.addTopFragment(GroupMakeFragment.getInstance( ));
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