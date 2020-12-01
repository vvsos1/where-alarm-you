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
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;
import kr.ac.ssu.wherealarmyou.view.fragment.OnBackPressedListener;

import java.util.Objects;

public class GroupJoinFragment extends Fragment implements View.OnClickListener, OnBackPressedListener
{
    private final Group group;
    
    private Bundle bundle;
    
    private Button buttonJoin;
    
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
        bundle = Objects.requireNonNull(getArguments( ));
        
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
        buttonJoin = contentView.findViewById(R.id.groupJoin_buttonJoin);
        
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
    
    @Override
    public void onClick(View view)
    {
        if (view == buttonJoin) {
            Toast.makeText(getContext( ), "그룹 가입 요청(미구현)", Toast.LENGTH_SHORT).show( );
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
