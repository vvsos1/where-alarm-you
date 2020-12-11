package kr.ac.ssu.wherealarmyou.view.fragment.alarm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.group.service.GroupService;
import kr.ac.ssu.wherealarmyou.view.adapter.GroupItemAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.RecyclerViewDecoration;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

// TODO : AlarmAddFragment 연결
// TODO : 그룹 추가 버튼으로 그룹 추가 페이지 이동 (프래그먼트 전환은 MainFrameActivity 이용)
public class AlarmAddGroupFragment extends Fragment
{
    private List<Group> groups = new ArrayList<>( );
    
    private GroupItemAdapter groupItemAdapter;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View contentView = inflater.inflate(R.layout.content_alarm_add_group, container, false);
        
        // TODO : 테스트용 임시 작성 코드
        getGroupData( );
        
        // Content View Setting - Group Recycler View (그룹 리스트)
        RecyclerView recyclerView = contentView.findViewById(R.id.alarmAddLocation_recyclerView);
        groupItemAdapter = new GroupItemAdapter(getContext( ), groups);
        LinearLayoutManager    linearLayoutManager    = new LinearLayoutManager(getContext( ));
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(30);
        
        groupItemAdapter.setOnItemClickListener((view, group) -> {
            // TODO : 그룹 클릭시 이벤트 처리 (ViewModel 이용)
        });
        
        recyclerView.setAdapter(groupItemAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(recyclerViewDecoration);
        
        return contentView;
    }
    
    // TODO : 테스트용 임시 작성 코드
    private void getGroupData( )
    {
        // test code
        GroupService groupService = GroupService.getInstance( );
        groupService.getJoinedGroup( )
                    .doOnNext(group -> {
                        groups.add(group);
                        groupItemAdapter.notifyItemInserted(groupItemAdapter.getItemCount( ));
                    })
                    .doOnError(throwable -> Log.e("GroupFragment", throwable.getLocalizedMessage( )))
                    .publishOn(Schedulers.elastic( ))
                    .subscribeOn(Schedulers.elastic( ))
                    .subscribe( );
    }
}
