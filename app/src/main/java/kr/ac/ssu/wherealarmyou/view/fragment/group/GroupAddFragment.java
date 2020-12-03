package kr.ac.ssu.wherealarmyou.view.fragment.group;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.group.service.GroupService;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.adapter.GroupRecyclerViewAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;
import kr.ac.ssu.wherealarmyou.view.custom_view.RecyclerViewDecoration;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupAddFragment extends Fragment implements View.OnClickListener
{
    List<Group> groups = new ArrayList<>( );
    
    // Content View
    private TextView textViewMakeGroup;
    private EditText editTextFindGroup;
    
    public static GroupAddFragment getInstance( )
    {
        return new GroupAddFragment( );
    }
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle bundle = Objects.requireNonNull(getArguments( ));
        
        View frameView   = inflater.inflate(R.layout.frame_overlap, container, false);
        View contentView = inflater.inflate(R.layout.content_group_add, null);
        
        // Frame View Setting
        OverlappingView overlappingView = frameView.findViewById(R.id.overlap_view);
        overlappingView.setAtOnce(bundle, frameView, contentView, "그룹 추가", false, true);
        
        // Content View Setting
        textViewMakeGroup = contentView.findViewById(R.id.groupAdd_textViewMakeGroup);
        editTextFindGroup = contentView.findViewById(R.id.groupAdd_editTextFindGroup);
        
        RecyclerView             recyclerView             = frameView.findViewById(R.id.groupAdd_recyclerView);
        GroupRecyclerViewAdapter groupRecyclerViewAdapter = new GroupRecyclerViewAdapter(getContext( ), groups);
        LinearLayoutManager      linearLayoutManager      = new LinearLayoutManager(getContext( ));
        RecyclerViewDecoration   recyclerViewDecoration   = new RecyclerViewDecoration(30);
        
        contentView.setOnClickListener(this);
        recyclerView.setOnClickListener(this);
        textViewMakeGroup.setOnClickListener(this);
        groupRecyclerViewAdapter.setOnGroupClickListener((itemView, group) -> {
            MainFrameActivity.addTopFragment(GroupJoinFragment.getInstance(group));
            InputMethodManager systemService =
                    (InputMethodManager)Objects.requireNonNull(getActivity( ))
                                               .getSystemService(Context.INPUT_METHOD_SERVICE);
            systemService.hideSoftInputFromWindow(editTextFindGroup.getWindowToken( ), 0);
        });
        
        editTextFindGroup.addTextChangedListener(new TextWatcher( )
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                findGroup(s, groupRecyclerViewAdapter);
            }
            
            @Override
            public void afterTextChanged(Editable s) { }
        });
        
        recyclerView.setAdapter(groupRecyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(recyclerViewDecoration);
        return frameView;
    }
    
    private void findGroup(CharSequence sequence, GroupRecyclerViewAdapter groupRecyclerViewAdapter)
    {
        groups.clear( );
        groupRecyclerViewAdapter.notifyDataSetChanged( );
        GroupService groupService = GroupService.getInstance( );
        groupService.findGroupsByName(sequence.toString( ))
                    .doOnNext(group -> {
                        groups.add(group);
                        groupRecyclerViewAdapter.notifyDataSetChanged( );
                    })
                    .publishOn(Schedulers.elastic( ))
                    .subscribeOn(Schedulers.elastic( ))
                    .subscribe( );
    }
    
    @Override
    public void onClick(View view)
    {
        InputMethodManager systemService =
                (InputMethodManager)Objects.requireNonNull(getActivity( ))
                                           .getSystemService(Context.INPUT_METHOD_SERVICE);
        systemService.hideSoftInputFromWindow(editTextFindGroup.getWindowToken( ), 0);
        if (view == textViewMakeGroup) {
            MainFrameActivity.addTopFragment(GroupMakeFragment.getInstance( ));
        }
    }
}