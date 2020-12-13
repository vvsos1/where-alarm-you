package kr.ac.ssu.wherealarmyou.view.fragment.group;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.serivce.AlarmService;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.group.service.GroupService;
import kr.ac.ssu.wherealarmyou.view.DataManager;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.adapter.AlarmItemAdapter;
import kr.ac.ssu.wherealarmyou.view.adapter.MemberItemAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;
import kr.ac.ssu.wherealarmyou.view.custom_view.RecyclerViewDecoration;
import kr.ac.ssu.wherealarmyou.view.fragment.alarm.AlarmAddFragment;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupInfoFragment extends Fragment implements View.OnClickListener
{
    private final DataManager dataManager = DataManager.getInstance( );
    
    private final Group group;
    
    private final List<Alarm> groupAlarms = new ArrayList<>( );
    
    private AlarmItemAdapter alarmItemAdapter;
    
    public GroupInfoFragment(Group group)
    {
        this.group = group;
    }
    
    public static GroupInfoFragment getInstance(Group group)
    {
        return new GroupInfoFragment(group);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle bundle = Objects.requireNonNull(getArguments( ));
        
        getGroupAlarm(group.getUid( ));
        
        View frameView   = inflater.inflate(R.layout.frame_overlap, container, false);
        View contentView = inflater.inflate(R.layout.content_group_info, null);
        
        // Frame View Setting
        OverlappingView overlappingView = frameView.findViewById(R.id.overlap_view);
        overlappingView.setAtOnce(bundle, frameView, contentView, "그룹 정보", false, true);
        
        // Content View Setting
        Button   buttonIcon           = contentView.findViewById(R.id.groupInfo_buttonIcon);
        TextView textViewName         = contentView.findViewById(R.id.groupInfo_textViewName);
        TextView textViewAdmin        = contentView.findViewById(R.id.groupInfo_textViewAdmin);
        TextView textViewIntroduction = contentView.findViewById(R.id.groupInfo_textViewIntroduction);
        TextView textViewGroupExit    = contentView.findViewById(R.id.groupInfo_textViewGroupExit);
        
        GradientDrawable drawable = (GradientDrawable)buttonIcon.getBackground( );
        drawable.setColor(Color.parseColor(group.getIcon( ).getColorHex( )));
        buttonIcon.setText(group.getIcon( ).getText( ));
        textViewName.setText(group.getName( ));
        textViewAdmin.setText(group.getAdminName( ));
        textViewIntroduction.setText(group.getDescription( ));
        
        textViewGroupExit.setOnClickListener(this);
        
        List<String> members    = Lists.newArrayList(group.getMembers( ).keySet( ));
        List<String> adminUid   = new ArrayList<>( );
        List<String> normalsUid = new ArrayList<>( );
        for (String member : members) {
            String grade = Objects.requireNonNull(group.getMembers( ).get(member));
            if (grade.equals("ADMIN")) {
                adminUid.add(member);
            }
            else if (grade.equals("USER")) {
                normalsUid.add(member);
            }
        }
        
        // Content View Setting - Recycler View ADMIN & MEMBER
        RecyclerView        recyclerViewAlarm   = contentView.findViewById(R.id.groupInfo_recyclerViewAlarm);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext( ));
        alarmItemAdapter = new AlarmItemAdapter(getContext( ), groupAlarms);
        
        recyclerViewAlarm.setAdapter(alarmItemAdapter);
        recyclerViewAlarm.setLayoutManager(linearLayoutManager);
        
        RecyclerView           recyclerViewAdmin      = contentView.findViewById(R.id.groupInfo_recyclerViewAdmin);
        RecyclerView           recyclerViewNormal     = contentView.findViewById(R.id.groupInfo_recyclerViewNormal);
        MemberItemAdapter      adminRecyclerAdapter   = new MemberItemAdapter(getContext( ), adminUid);
        MemberItemAdapter      normalRecyclerAdapter  = new MemberItemAdapter(getContext( ), normalsUid);
        LinearLayoutManager    linearLayoutManagerA   = new LinearLayoutManager(getContext( ));
        LinearLayoutManager    linearLayoutManagerN   = new LinearLayoutManager(getContext( ));
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(3);
        
        recyclerViewAdmin.setAdapter(adminRecyclerAdapter);
        recyclerViewAdmin.setLayoutManager(linearLayoutManagerA);
        recyclerViewAdmin.addItemDecoration(recyclerViewDecoration);
        recyclerViewNormal.setAdapter(normalRecyclerAdapter);
        recyclerViewNormal.setLayoutManager(linearLayoutManagerN);
        recyclerViewNormal.addItemDecoration(recyclerViewDecoration);
        
        // Content View Setting - ADMIN SETTING
        if (Objects.equals(group.getMembers( ).get(FirebaseAuth.getInstance( ).getUid( )), "ADMIN")) {
            LinearLayout linearLayoutAdmin    = contentView.findViewById(R.id.groupInfo_linearLayoutAdmin);
            TextView     textViewAlarmAdd     = contentView.findViewById(R.id.groupInfo_textViewAddAlarm);
            TextView     textViewGroupDelete  = contentView.findViewById(R.id.groupInfo_textViewGroupDelete);
            TextView     textViewGroupSetting = contentView.findViewById(R.id.groupInfo_textViewGroupSetting);
            TextView     textViewManageMember = contentView.findViewById(R.id.groupInfo_textViewManageMember);
            textViewAlarmAdd.setVisibility(View.VISIBLE);
            linearLayoutAdmin.setVisibility(View.VISIBLE);
            textViewManageMember.setVisibility(View.VISIBLE);
            textViewGroupExit.setVisibility(View.INVISIBLE);
            
            textViewAlarmAdd.setOnClickListener(this);
            textViewGroupDelete.setOnClickListener(this);
            textViewGroupSetting.setOnClickListener(view ->
                    MainFrameActivity.showTopFragment(new GroupMakeFragment(group, GroupMakeFragment.Mode.GROUP_EDIT)));
            textViewManageMember.setOnClickListener(view -> normalRecyclerAdapter.bind(Boolean.TRUE));
            
            if (group.getWaitingUsers( ) != null) {
                List<String> waitersUid = Lists.newArrayList(group.getWaitingUsers( ).keySet( ));
                
                LinearLayout linearLayoutWaiter = contentView.findViewById(R.id.groupInfo_linearLayoutWaiter);
                linearLayoutWaiter.setVisibility(View.VISIBLE);
                
                RecyclerView        recyclerViewWaiter    = contentView.findViewById(R.id.groupInfo_recyclerViewWaiter);
                MemberItemAdapter   waiterRecyclerAdapter = new MemberItemAdapter(getContext( ), waitersUid);
                LinearLayoutManager linearLayoutManagerW  = new LinearLayoutManager(getContext( ));
                
                waiterRecyclerAdapter.setOnItemClickListener((view, userUid) -> {
                    GroupService groupService = GroupService.getInstance( );
                    if (view.getId( ) == R.id.item_name_and_button_buttonCheck) {
                        groupService.acceptWaitingUser(group.getUid( ), userUid)
                                    .doOnSuccess(unused -> {
                                        int index = -1;
                                        for (String waiter : waitersUid) {
                                            if (userUid.equals(waiter)) {
                                                index = waitersUid.indexOf(waiter);
                                                waitersUid.remove(index);
                                            }
                                        }
                                        normalsUid.add(userUid);
                                        waiterRecyclerAdapter.notifyItemRemoved(index);
                                        normalRecyclerAdapter.notifyItemInserted(normalRecyclerAdapter.getItemCount());
                                    })
                                    .publishOn(Schedulers.elastic( ))
                                    .subscribeOn(Schedulers.elastic( ))
                                    .subscribe( );
                    }
                    if (view.getId( ) == R.id.item_name_and_button_buttonCancel) {
                        groupService.rejectWaitingUser(group.getUid( ), userUid)
                                    .doOnSuccess(unused -> {
                                        waiterRecyclerAdapter.notifyDataSetChanged( );
                                        normalRecyclerAdapter.notifyDataSetChanged( );
                                    })
                                    .publishOn(Schedulers.elastic( ))
                                    .subscribeOn(Schedulers.elastic( ))
                                    .subscribe( );
                    }
                });
                
                recyclerViewWaiter.setAdapter(waiterRecyclerAdapter);
                recyclerViewWaiter.setLayoutManager(linearLayoutManagerW);
                recyclerViewWaiter.addItemDecoration(recyclerViewDecoration);
                waiterRecyclerAdapter.bind(Boolean.TRUE);
            }
        }
        
        return frameView;
    }
    
    private void exitGroup(String groupUid)
    {
        GroupService groupService = GroupService.getInstance( );
        
        groupService.requestLeaveGroup(groupUid)
                    .doOnSuccess(unused -> {
                        dataManager.updateGroupLiveData( );
                        Toast.makeText(getContext( ), "그룹 탈퇴가 완료되었습니다", Toast.LENGTH_SHORT).show( );
                    })
                    .publishOn(Schedulers.elastic( ))
                    .subscribeOn(Schedulers.elastic( ))
                    .subscribe( );
        MainFrameActivity.hideTopFragment( );
        MainFrameActivity.showTopFragment(GroupFragment.getInstance( ));
    }
    
    private void deleteGroup(String groupUid)
    {
        GroupService groupService = GroupService.getInstance( );
        
        groupService.deleteGroup(groupUid)
                    .doOnSuccess(unused -> {
                        dataManager.updateGroupLiveData( );
                        Toast.makeText(getContext( ), "그룹 삭제가 완료되었습니다", Toast.LENGTH_SHORT).show( );
                    })
                    .publishOn(Schedulers.elastic( ))
                    .subscribeOn(Schedulers.elastic( ))
                    .subscribe( );
        MainFrameActivity.hideTopFragment( );
        MainFrameActivity.showTopFragment(GroupFragment.getInstance( ));
    }
    
    private void getGroupAlarm(String groupUid)
    {
        AlarmService alarmService = AlarmService.getInstance(getContext( ));
        alarmService.getAlarmsByGroupUid(groupUid)
                    .map(groupAlarms::add)
                    .doOnComplete(( ) -> alarmItemAdapter.notifyDataSetChanged( ))
                    .subscribe( );
    }
    
    @Override
    public void onClick(View view)
    {
        if (view.getId( ) == R.id.groupInfo_textViewGroupExit) {
            AlertDialog.Builder alertDialogDeleteUser = new AlertDialog.Builder(Objects.requireNonNull(getContext( )));
            alertDialogDeleteUser.setMessage("그룹 탈퇴를 원하시나요?")
                                 .setCancelable(false)
                                 .setPositiveButton("예", (dialogInterface, i) -> exitGroup(group.getUid( )))
                                 .setNegativeButton("아니요", (dialogInterface, i) ->
                                         Toast.makeText(getContext( ), "탈퇴가 취소되었습니다", Toast.LENGTH_LONG).show( ))
                                 .show( );
        }
        
        if (view.getId( ) == R.id.groupInfo_textViewGroupDelete) {
            AlertDialog.Builder alertDialogDeleteUser = new AlertDialog.Builder(Objects.requireNonNull(getContext( )));
            alertDialogDeleteUser.setMessage("그룹 삭제를 원하시나요?")
                                 .setCancelable(false)
                                 .setPositiveButton("예", (dialogInterface, i) -> deleteGroup(group.getUid( )))
                                 .setNegativeButton("아니요", (dialogInterface, i) ->
                                         Toast.makeText(getContext( ), "삭제가 취소되었습니다", Toast.LENGTH_LONG).show( ))
                                 .show( );
        }
        
        if (view.getId( ) == R.id.groupInfo_textViewAddAlarm) {
            MainFrameActivity.addTopFragment(new AlarmAddFragment(null));
        }
    }
}
