package kr.ac.ssu.wherealarmyou.view.fragment.group;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.common.Icon;
import kr.ac.ssu.wherealarmyou.group.dto.GroupCreateRequest;
import kr.ac.ssu.wherealarmyou.view.custom_view.IconRecyclerViewAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.fragment.OnBackPressedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupMakeFragment extends Fragment implements View.OnClickListener, OnBackPressedListener
{
    private Bundle bundle;
    
    private LinearLayout linearLayoutName;
    private LinearLayout linearLayoutIcon;
    
    private EditText editTextGroupName;
    private EditText editTextIconText;
    private EditText editTextGroupInfo;
    private Button   buttonComplete;
    private Button   buttonIconColor;
    
    private String iconColor;
    
    public GroupMakeFragment( ) { }
    
    public static GroupMakeFragment getInstance( )
    {
        return new GroupMakeFragment( );
    }
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        bundle = Objects.requireNonNull(getArguments( ));
        
        View frameView   = inflater.inflate(R.layout.frame_overlap, container, false);
        View contentView = inflater.inflate(R.layout.content_group_make, null);
        
        // Frame View Setting
        OverlappingView overlappingView = frameView.findViewById(R.id.overlap_view);
        overlappingView.setAtOnce(bundle, frameView, contentView, "그룹 생성", false, true);
        
        // test data
        List<Icon> icons = new ArrayList<>( );
        icons.add(new Icon("#ffe3ff", null));
        icons.add(new Icon("#efe3ff", null));
        icons.add(new Icon("#dfe3ff", null));
        icons.add(new Icon("#cfe3ff", null));
        icons.add(new Icon("#bfe3ff", null));
        icons.add(new Icon("#afe3ff", null));
        icons.add(new Icon("#9fe3ff", null));
        icons.add(new Icon("#8fe3ff", null));
        icons.add(new Icon("#7fe3ff", null));
        icons.add(new Icon("#6fe3ff", null));
        icons.add(new Icon("#5fe3ff", null));
        icons.add(new Icon("#4fe3ff", null));
        icons.add(new Icon("#3fe3ff", null));
        icons.add(new Icon("#2fe3ff", null));
        icons.add(new Icon("#1fe3ff", null));
        icons.add(new Icon("#0fe3ff", null));
        
        
        // Content View Setting
        LinearLayout linearLayoutParent = contentView.findViewById(R.id.groupMake_linearLayoutParent);
        linearLayoutName  = contentView.findViewById(R.id.groupMake_linearLayoutName);
        linearLayoutIcon  = contentView.findViewById(R.id.groupMake_linearLayoutIcon);
        editTextGroupName = contentView.findViewById(R.id.groupMake_editTextGroupName);
        editTextIconText  = contentView.findViewById(R.id.groupMake_editTextIconText);
        editTextGroupInfo = contentView.findViewById(R.id.groupMake_editTextGroupInfo);
        buttonComplete    = contentView.findViewById(R.id.groupMake_buttonComplete);
        buttonIconColor   = contentView.findViewById(R.id.groupMake_buttonIconColor);
        
        RecyclerView            recyclerView            = frameView.findViewById(R.id.groupMake_recyclerViewColor);
        LinearLayoutManager     linearLayoutManager     = new LinearLayoutManager(getContext( ));
        IconRecyclerViewAdapter iconRecyclerViewAdapter = new IconRecyclerViewAdapter(getContext( ), icons);
        
        iconRecyclerViewAdapter.setOnItemClickListener((itemView, icon) -> {
            GradientDrawable drawable = (GradientDrawable)buttonIconColor.getBackground( );
            drawable.setColor(Color.parseColor(icon.getColorHex( )));
            iconColor = icon.getColorHex( );
        });
        
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setAdapter(iconRecyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        
        buttonComplete.setOnClickListener(this);
        linearLayoutParent.setOnClickListener(view -> {
            linearLayoutName.setVisibility(View.VISIBLE);
            linearLayoutIcon.setVisibility(View.VISIBLE);
            InputMethodManager systemService =
                    (InputMethodManager)Objects.requireNonNull(getActivity( ))
                                               .getSystemService(Context.INPUT_METHOD_SERVICE);
            systemService.hideSoftInputFromWindow(editTextGroupInfo.getWindowToken( ), 0);
        });
        editTextGroupInfo.setOnTouchListener((v, event) -> {
            if (event.getAction( ) == MotionEvent.ACTION_UP) {
                linearLayoutName.setVisibility(View.GONE);
                linearLayoutIcon.setVisibility(View.GONE);
            }
            return false;
        });
        
        return frameView;
    }
    
    @Override
    public void onClick(View view)
    {
        if (view == buttonComplete) {
            String groupName = editTextGroupName.getText( ).toString( ).trim( );
            String iconText  = editTextIconText.getText( ).toString( ).trim( );
            String groupInfo = editTextGroupInfo.getText( ).toString( ).trim( );
            
            GroupCreateRequest request = new GroupCreateRequest(groupName, iconColor, iconText, groupInfo);
            
            Toast.makeText(getContext( ), "컬러" + request.getIconColorHex( ), Toast.LENGTH_SHORT).show( );
            MainFrameActivity.backTopFragment(this);
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
    
    @Override
    public void onStop( )
    {
        super.onStop( );
        GradientDrawable backgroundGradient = (GradientDrawable)buttonIconColor.getBackground( );
        backgroundGradient.setColor(Color.WHITE);
    }
}
