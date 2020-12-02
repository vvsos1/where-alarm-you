package kr.ac.ssu.wherealarmyou.view.fragment.group;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;
import kr.ac.ssu.wherealarmyou.view.fragment.OnBackPressedListener;

import java.util.Objects;

public class GroupInfoFragment extends Fragment implements View.OnClickListener, OnBackPressedListener
{
    private final Group group;
    
    private Bundle bundle;
    
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
        bundle = Objects.requireNonNull(getArguments( ));
        
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
        
        
        
        return frameView;
    }
    
    @Override
    public void onClick(View view) { }
    
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
