package kr.ac.ssu.wherealarmyou.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.common.Icon;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.group.dto.GroupCreateRequest;
import kr.ac.ssu.wherealarmyou.view.custom_view.IconRecyclerViewAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupMakeFragment extends Fragment implements View.OnClickListener, OnBackPressedListener
{
    private Group group;
    
    private Bundle bundle;
    
    private EditText editTextGroupName;
    private EditText editTextIconText;
    private EditText editTextGroupInfo;
    private Button   buttonComplete;
    
    public GroupMakeFragment( ) { }
    
    public static GroupMakeFragment getInstance( )
    {
        return new GroupMakeFragment( );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        group  = new Group( );
        bundle = Objects.requireNonNull(getArguments( ));
        
        View frameView   = inflater.inflate(R.layout.frame_overlap_content, container, false);
        View contentView = inflater.inflate(R.layout.content_group_make, null);
        
        // Frame View Setting
        OverlappingView overlappingView = frameView.findViewById(R.id.overlap_view);
        overlappingView.setAtOnce(bundle, frameView, contentView, "그룹 생성", false, true);
        
        // test data
        List<Icon> icons = new ArrayList<>( );
        for (int i = 0; i < 14; i++) {
            icons.add(new Icon("", "GRP" + i));
        }
        
        // Content View Setting
        RecyclerView            recyclerView            = frameView.findViewById(R.id.groupMake_recyclerViewColor);
        LinearLayoutManager     linearLayoutManager     = new LinearLayoutManager(getContext( ));
        IconRecyclerViewAdapter iconRecyclerViewAdapter = new IconRecyclerViewAdapter(getContext( ), icons);
        
        iconRecyclerViewAdapter.setOnItemClickListener((itemView, icon) ->
                Toast.makeText(getContext( ), "색 선택", Toast.LENGTH_SHORT).show( ));
        
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setAdapter(iconRecyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        
        editTextGroupName = contentView.findViewById(R.id.groupMake_editTextGroupName);
        editTextIconText  = contentView.findViewById(R.id.groupMake_editTextIconText);
        editTextGroupInfo = contentView.findViewById(R.id.groupMake_editTextGroupInfo);
        buttonComplete    = contentView.findViewById(R.id.groupMake_buttonComplete);
        
        buttonComplete.setOnClickListener(this);
        
        return frameView;
    }
    
    @Override
    public void onClick(View view)
    {
        if (view == buttonComplete) {
            String groupName = editTextGroupName.getText( ).toString( ).trim( );
            String iconText  = editTextIconText.getText( ).toString( ).trim( );
            String groupInfo = editTextGroupInfo.getText( ).toString( ).trim( );
            
            GroupCreateRequest request = new GroupCreateRequest(groupName, "", iconText, groupInfo);
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
