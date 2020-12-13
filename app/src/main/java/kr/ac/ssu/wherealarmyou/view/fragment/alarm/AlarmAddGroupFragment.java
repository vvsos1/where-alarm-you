package kr.ac.ssu.wherealarmyou.view.fragment.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.view.DataManager;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.adapter.GroupItemAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.RecyclerViewDecoration;
import kr.ac.ssu.wherealarmyou.view.fragment.OnBackPressedListener;
import kr.ac.ssu.wherealarmyou.view.viewmodel.AlarmAddGroupViewModel;

// TODO : AlarmAddFragment 연결
// TODO : 그룹 추가 버튼으로 그룹 추가 페이지 이동 (프래그먼트 전환은 MainFrameActivity 이용)
public class AlarmAddGroupFragment extends Fragment implements OnBackPressedListener
{
    private final DataManager dataManager = DataManager.getInstance( );
    
    private AlarmAddGroupViewModel alarmAddGroupViewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        List<Group> myGroups = dataManager.getGroupData().getValue();
        List<Group> adminGroups = new ArrayList<>();


        for (Group group : Objects.requireNonNull(myGroups)) {
            if (Objects.equals(group.getMembers().get(FirebaseAuth.getInstance().getUid()), "ADMIN")) {
                adminGroups.add(group);
            }
        }


        View contentView = inflater.inflate(R.layout.content_alarm_add_group, container, false);

        alarmAddGroupViewModel = new ViewModelProvider(requireActivity()).get(AlarmAddGroupViewModel.class);

        // Content View Setting - Group Recycler View (그룹 리스트)
        RecyclerView recyclerView = contentView.findViewById(R.id.alarmAddGroup_recyclerView);
        GroupItemAdapter groupItemAdapter = new GroupItemAdapter(getContext(), adminGroups);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(30);
        
        groupItemAdapter.setOnItemClickListener((view, group) -> {
            alarmAddGroupViewModel.setLiveData(group);
            alarmAddGroupViewModel.complete( );
        });
        
        recyclerView.setAdapter(groupItemAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(recyclerViewDecoration);
        
        return contentView;
    }
    
    @Override
    public void onResume( )
    {
        super.onResume( );
        MainFrameActivity.setOnBackPressedListener(this);
    }
    
    @Override
    public void onBackPressed( )
    {
        MainFrameActivity.backTopFragment( );
        MainFrameActivity.backTopFragment( );
    }
    
    @Override
    public void onStop( )
    {
        super.onStop( );
        MainFrameActivity.setOnBackPressedListener(null);
    }
}
