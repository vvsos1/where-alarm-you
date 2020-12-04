package kr.ac.ssu.wherealarmyou.view.fragment.group;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.group.service.GroupService;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

public class GroupJoinFragment extends Fragment implements View.OnClickListener
{
    private final Group group;
    
    public GroupJoinFragment(Group group)
    {
        this.group = group;
    }
    
    public static GroupJoinFragment getInstance(Group group)
    {
        return new GroupJoinFragment(group);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle bundle = Objects.requireNonNull(getArguments( ));
        
        View frameView   = inflater.inflate(R.layout.frame_overlap, container, false);
        View contentView = inflater.inflate(R.layout.content_group_join, null);
        
        // Frame View Setting
        OverlappingView overlappingView = frameView.findViewById(R.id.overlap_view);
        overlappingView.setAtOnce(bundle, frameView, contentView, "그룹 가입", false, true);
        
        // Content View Setting
        Button   buttonIcon    = contentView.findViewById(R.id.groupJoin_buttonIcon);
        TextView textViewName  = contentView.findViewById(R.id.groupJoin_textViewName);
        TextView textViewAdmin = contentView.findViewById(R.id.groupJoin_textViewAdmin);
        TextView textViewInfo  = contentView.findViewById(R.id.groupJoin_textViewInfo);
        Button   buttonJoin    = contentView.findViewById(R.id.groupJoin_buttonJoin);
        
        String groupAdmin = "(미구현)";
        
        GradientDrawable drawable = (GradientDrawable)buttonIcon.getBackground( );
        drawable.setColor(Color.parseColor(group.getIcon( ).getColorHex( )));
        buttonIcon.setText(group.getIcon( ).getText( ));
        textViewName.setText(group.getName( ));
        textViewAdmin.setText("관리자 : " + groupAdmin);
        textViewInfo.setText(group.getDescription( ));
        
        buttonJoin.setOnClickListener(this);
        
        return frameView;
    }
    
    private void joinGroup(String groupUid)
    {
        GroupService groupService = GroupService.getInstance( );
        
        /* 요청 실패 */
        // 중복된 가입 요청
        if (group.getMembers( ).get(FirebaseAuth.getInstance( ).getUid( )) != null) {
            Toast.makeText(getContext( ), "이미 가입된 그룹입니다.", Toast.LENGTH_SHORT).show( );
            return;
        }
        
        /* 요청 성공 */
        // 그룹 가입 요청
        groupService.requestJoinGroup(groupUid)
                    .doOnSuccess(unused -> Toast.makeText(getContext( ), "가입 요청 성공", Toast.LENGTH_SHORT).show( ))
                    .publishOn(Schedulers.elastic( ))
                    .subscribeOn(Schedulers.elastic( ))
                    .subscribe( );
        MainFrameActivity.hideTopFragment( );
        MainFrameActivity.showTopFragment(GroupFragment.getInstance( ));
    }
    
    @Override
    public void onClick(View view)
    {
        if (view.getId( ) == R.id.groupJoin_buttonJoin) {
            joinGroup(group.getUid( ));
        }
    }
}
