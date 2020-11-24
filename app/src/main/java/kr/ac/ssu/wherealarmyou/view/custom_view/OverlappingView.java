package kr.ac.ssu.wherealarmyou.view.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import kr.ac.ssu.wherealarmyou.R;

public class OverlappingView extends LinearLayout
{
    private TextView    textViewTitle;
    private Button      buttonAdd;
    private Button      buttonBack;
    private Button      buttonHide;
    private FrameLayout frameLayoutContent;
    private TextView    distinguishBar;
    private int         mSelected = 0;
    
    public OverlappingView(Context context)
    {
        super(context);
        initializeView( );
    }
    
    public OverlappingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initializeView( );
        getAttrs(attrs);
    }
    
    public OverlappingView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs);
        initializeView( );
        getAttrs(attrs, defStyleAttr);
    }
    
    private void initializeView( )
    {
        LayoutInflater inflater = (LayoutInflater)getContext( ).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View           view     = inflater.inflate(R.layout.overlapping_view_src, this, false);
        addView(view);
        
        textViewTitle      = findViewById(R.id.overlap_title);
        buttonAdd          = findViewById(R.id.overlap_buttonAdd);
        buttonBack         = findViewById(R.id.overlap_buttonBack);
        buttonHide         = findViewById(R.id.overlap_buttonHide);
        frameLayoutContent = findViewById(R.id.overlap_content);
        distinguishBar     = findViewById(R.id.overlap_bar);
        
        AnimationDrawable animationDrawable = (AnimationDrawable)distinguishBar.getBackground( );
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start( );
        
    }
    
    private void getAttrs(AttributeSet attrs)
    {
        TypedArray typedArray = getContext( ).obtainStyledAttributes(attrs, R.styleable.overlappingView);
        setTypeArray(typedArray);
    }
    
    private void getAttrs(AttributeSet attrs, int defStyle)
    {
        TypedArray typedArray = getContext( ).obtainStyledAttributes(attrs, R.styleable.overlappingView, defStyle, 0);
        setTypeArray(typedArray);
    }
    
    private void setTypeArray(TypedArray typedArray)
    {
        // 제목
        String title = typedArray.getString(R.styleable.overlappingView_title);
        this.textViewTitle.setText(title);
        
        // 추가 버튼
        boolean buttonAdd = typedArray.getBoolean(R.styleable.overlappingView_button_add, false);
        if (buttonAdd) { this.buttonAdd.setVisibility(VISIBLE); }
        else { this.buttonAdd.setVisibility(GONE); }
        
        // 뒤로가기 버튼
        boolean buttonBack = typedArray.getBoolean(R.styleable.overlappingView_button_back, false);
        if (buttonBack) { this.buttonBack.setVisibility(VISIBLE); }
        else { this.buttonBack.setVisibility(GONE); }
        
        // 숨기기 버튼
        boolean buttonHide = typedArray.getBoolean(R.styleable.overlappingView_button_hide, false);
        if (buttonHide) { this.buttonHide.setVisibility(VISIBLE); }
        else { this.buttonHide.setVisibility(GONE); }
        
        typedArray.recycle( );
    }
    
    public void setTitle(String title)
    {
        this.textViewTitle.setText(title);
    }
    
    public void setButtonAdd(boolean isValid)
    {
        if (isValid) { this.buttonAdd.setVisibility(VISIBLE); }
        else { this.buttonAdd.setVisibility(GONE); }
    }
    
    public void setButtonBack(boolean isValid)
    {
        if (isValid) { this.buttonBack.setVisibility(VISIBLE); }
        else { this.buttonBack.setVisibility(GONE); }
    }
    
    public void setButtonHide(boolean isValid)
    {
        if (isValid) { this.buttonHide.setVisibility(VISIBLE); }
        else { this.buttonHide.setVisibility(GONE); }
    }
    
    public void setContent(View view)
    {
        this.frameLayoutContent.addView(view);
    }
}
