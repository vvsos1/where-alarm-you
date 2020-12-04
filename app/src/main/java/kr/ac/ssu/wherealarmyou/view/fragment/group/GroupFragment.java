package kr.ac.ssu.wherealarmyou.view.fragment.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.view.DataManager;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.adapter.GroupItemAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;
import kr.ac.ssu.wherealarmyou.view.custom_view.RecyclerViewDecoration;

import java.util.List;
import java.util.Objects;

public class GroupFragment extends Fragment
{
    private final DataManager dataManager = DataManager.getInstance( );
    
    public static GroupFragment getInstance( )
    {
        return new GroupFragment( );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle bundle = Objects.requireNonNull(getArguments( ));
        
        LiveData<List<Group>> groups = dataManager.getGroupLiveData( );
        
        // View
        View frameView   = inflater.inflate(R.layout.frame_overlap, container, false);
        View contentView = inflater.inflate(R.layout.content_group, null);
        
        // Frame View Setting
        OverlappingView overlappingView = frameView.findViewById(R.id.overlap_view);
        overlappingView.setAtOnce(bundle, frameView, contentView, "그룹", true, true);
        
        Button buttonAdd = frameView.findViewById(R.id.overlap_buttonAdd);
        buttonAdd.setOnClickListener(view -> overlappingView.onAddClick(GroupAddFragment.getInstance( )));
        
        // Content View Setting
        RecyclerView           recyclerView           = frameView.findViewById(R.id.group_recyclerView);
        GroupItemAdapter       groupItemAdapter       = new GroupItemAdapter(getContext( ), groups);
        LinearLayoutManager    linearLayoutManager    = new LinearLayoutManager(getContext( ));
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(30);
        
        groupItemAdapter.setOnGroupClickListener((itemView, group) ->
                MainFrameActivity.addTopFragment(GroupInfoFragment.getInstance(group)));
        
        recyclerView.setAdapter(groupItemAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(recyclerViewDecoration);
        
        return frameView;
    }
}