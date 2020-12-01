package kr.ac.ssu.wherealarmyou.view.fragment;

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
import kr.ac.ssu.wherealarmyou.view.custom_view.GroupContentViewAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;
import kr.ac.ssu.wherealarmyou.view.custom_view.RecyclerViewDecoration;

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
        
        View frameView   = inflater.inflate(R.layout.frame_overlap_content, container, false);
        View contentView = inflater.inflate(R.layout.content_group_add, null);
        
        // Frame View Setting
        overlappingView = frameView.findViewById(R.id.overlap_view);
        overlappingView.setAtOnce(bundle, frameView, contentView, "그룹 추가", false, true);
        
        // test group data
        List<Group> groups = new ArrayList<>( );
        for (int i = 0; i < 14; i++) {
            groups.add(new Group("Group" + i, new Icon("", "GRP" + i)));
        }
        
        // Content View Setting
        RecyclerView            recyclerView            = frameView.findViewById(R.id.recyclerViewGroupAdd);
        LinearLayoutManager     linearLayoutManager     = new LinearLayoutManager(getContext( ));
        GroupContentViewAdapter groupContentViewAdapter = new GroupContentViewAdapter(getContext( ), groups);
        RecyclerViewDecoration  recyclerViewDecoration  = new RecyclerViewDecoration(30);
        
        makeGroup = contentView.findViewById(R.id.textViewMakeGroup_groupAdd);
        findGroup = contentView.findViewById(R.id.editTextFindGroup_groupAdd);
        
        makeGroup.setOnClickListener(this);
        groupContentViewAdapter.setOnGroupClickListener((itemView, group) ->
                MainFrameActivity.addTopFragment(GroupJoinFragment.getInstance(group)));
        
        recyclerView.setAdapter(groupContentViewAdapter);
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