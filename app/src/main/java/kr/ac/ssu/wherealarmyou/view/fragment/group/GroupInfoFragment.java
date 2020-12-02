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
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.group.service.GroupService;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.adapter.MemberRecyclerAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;
import kr.ac.ssu.wherealarmyou.view.custom_view.RecyclerViewDecoration;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupInfoFragment extends Fragment implements View.OnClickListener
{
    private final Group group;
    
    // Content View Item
    private TextView textViewGroupExit;
    
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
        textViewGroupExit = contentView.findViewById(R.id.groupInfo_textViewGroupExit);
        
        GradientDrawable drawable = (GradientDrawable)buttonIcon.getBackground( );
        drawable.setColor(Color.parseColor(group.getIcon( ).getColorHex( )));
        buttonIcon.setText(group.getIcon( ).getText( ));
        textViewName.setText(group.getName( ));
        textViewAdmin.setText("(미구현)");
        textViewIntroduction.setText(group.getDescription( ));
        
        List<String> members = Lists.newArrayList(group.getMembers( ).keySet( ));
        List<String> admin   = new ArrayList<>( );
        List<String> normal  = new ArrayList<>( );
        for (String member : members) {
            String grade = Objects.requireNonNull(group.getMembers( ).get(member));
            if (grade.equals("ADMIN")) {
                admin.add(member);
            }
            else if (grade.equals("USER")) {
                normal.add(member);
            }
        }
        
        RecyclerView           recyclerViewAdmin      = contentView.findViewById(R.id.groupInfo_recyclerViewAdmin);
        RecyclerView           recyclerViewNormal     = contentView.findViewById(R.id.groupInfo_recyclerViewNormal);
        MemberRecyclerAdapter  adminRecyclerAdapter   = new MemberRecyclerAdapter(getContext( ), admin);
        MemberRecyclerAdapter  normalRecyclerAdapter  = new MemberRecyclerAdapter(getContext( ), normal);
        LinearLayoutManager    linearLayoutManagerA   = new LinearLayoutManager(getContext( ));
        LinearLayoutManager    linearLayoutManagerN   = new LinearLayoutManager(getContext( ));
        RecyclerViewDecoration recyclerViewDecoration = new RecyclerViewDecoration(3);
        if (Objects.equals(group.getMembers( ).get(FirebaseAuth.getInstance( ).getUid( )), "ADMIN")) {
            if (group.getWaitingUsers( ) != null) {
                List<String> waiter = Lists.newArrayList(group.getWaitingUsers( ).keySet( ));
                
                LinearLayout linearLayoutWaiter = contentView.findViewById(R.id.groupInfo_linearLayoutWaiter);
                linearLayoutWaiter.setVisibility(View.VISIBLE);
                
                RecyclerView recyclerViewWaiter = contentView.findViewById(R.id.groupInfo_recyclerViewWaiter);
                
                MemberRecyclerAdapter waiterRecyclerAdapter = new MemberRecyclerAdapter(getContext( ), waiter);
                LinearLayoutManager   linearLayoutManagerW  = new LinearLayoutManager(getContext( ));
                
                recyclerViewWaiter.setAdapter(waiterRecyclerAdapter);
                recyclerViewWaiter.setLayoutManager(linearLayoutManagerW);
                recyclerViewWaiter.addItemDecoration(recyclerViewDecoration);
            }
        }
        
        textViewGroupExit.setOnClickListener(this);
        
        recyclerViewAdmin.setAdapter(adminRecyclerAdapter);
        recyclerViewAdmin.setLayoutManager(linearLayoutManagerA);
        recyclerViewAdmin.addItemDecoration(recyclerViewDecoration);
        recyclerViewNormal.setAdapter(normalRecyclerAdapter);
        recyclerViewNormal.setLayoutManager(linearLayoutManagerN);
        recyclerViewNormal.addItemDecoration(recyclerViewDecoration);
        return frameView;
    }
    
    private void groupExit( )
    {
        GroupService groupService = GroupService.getInstance( );
        
        groupService.requestLeaveGroup(group.getUid( ))
                    .doOnSuccess(unused -> Toast.makeText(getContext( ), "탈퇴가 완료되었습니다", Toast.LENGTH_SHORT).show( ))
                    .publishOn(Schedulers.elastic( ))
                    .subscribeOn(Schedulers.elastic( ))
                    .subscribe( );
        MainFrameActivity.hideTopFragment( );
        MainFrameActivity.showTopFragment(GroupFragment.getInstance( ));
    }
    
    @Override
    public void onClick(View view)
    {
        if (view == textViewGroupExit) {
            AlertDialog.Builder alertDialogDeleteUser = new AlertDialog.Builder(Objects.requireNonNull(getContext( )));
            alertDialogDeleteUser.setMessage("그룹 탈퇴를 원하시나요?")
                                 .setCancelable(false)
                                 .setPositiveButton("예", (dialogInterface, i) -> groupExit( ))
                                 .setNegativeButton("아니요", (dialogInterface, i) ->
                                         Toast.makeText(getContext( ), "탈퇴가 취소되었습니다", Toast.LENGTH_LONG).show( ))
                                 .show( );
        }
    }
}
