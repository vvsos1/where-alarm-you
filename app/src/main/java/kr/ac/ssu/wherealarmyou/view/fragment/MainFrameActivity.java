package kr.ac.ssu.wherealarmyou.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import kr.ac.ssu.wherealarmyou.R;

public class MainFrameActivity extends AppCompatActivity implements View.OnClickListener
{
    public static FragmentManager fragmentManager;
    
    @SuppressLint("StaticFieldLeak")
    public static FrameLayout  frameTop;
    
    @SuppressLint("StaticFieldLeak")
    public static FrameLayout  frameBottom;
    
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout blind;
    
    public static OnBackPressedListener onBackPressedListener;
    
    /* 시작 */
    // Top FrameLayout을 띄우고 Fragment를 나타내기
    public static void showTopFragment(Fragment fragment)
    {
        blind.setVisibility(View.VISIBLE);
        
        Bundle bundle = new Bundle(2);
        bundle.putBoolean("backButton", false);
        bundle.putBoolean("hideButton", true);
        fragment.setArguments(bundle);
        
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        fragmentTransaction//.setCustomAnimations(R.anim.anim_in, R.anim.test_anim, R.anim.anim_in, R.anim.test_anim)
                           .replace(R.id.frameTop, fragment)
                           .addToBackStack(null)
                           .commit( );
    }
    
    /* 추가 */
    // 기존 Fragment 위에 새로운 Fragment를 띄우기
    public static void addTopFragment(Fragment fragment)
    {
        Bundle bundle = new Bundle(2);
        bundle.putBoolean("backButton", true);
        bundle.putBoolean("hideButton", false);
        fragment.setArguments(bundle);
        
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        fragmentTransaction//.setCustomAnimations(R.anim.fade_in, R.anim.test_anim, R.anim.fade_in, R.anim.test_anim)
                           .replace(R.id.frameTop, fragment)
                           .addToBackStack(null)
                           .commit( );
        
    }
    
    /* 뒤로 가기 */
    // 이전 Fragment로 돌아가기
    public static void backTopFragment(Fragment fragment)
    {
        onBackPressedListener = null;
        
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        fragmentTransaction.remove(fragment).commit( );
        fragmentManager.popBackStack( );
    }
    
    /* 종료 */
    // Top FrameLayout을 없애고 모든 Fragment 종료하기
    public static void hideTopFragment(Fragment fragment)
    {
        blind.setVisibility(View.GONE);
        onBackPressedListener = null;
        
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        fragmentTransaction.remove(fragment).commit( );
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    
    // Fragment에서 설정한 BackPressedListener가 존재하면 프래그먼트에서 이벤트 처리
    public static void setOnBackPressedListener(OnBackPressedListener listener)
    {
        onBackPressedListener = listener;
    }
    
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);
        
        fragmentManager = getSupportFragmentManager( );
        
        // Find View By Id
        frameTop    = findViewById(R.id.frameTop);
        frameBottom = findViewById(R.id.frameBottom);
        blind       = findViewById(R.id.blind);
        
        // Set On Event Listener
        frameTop.setOnClickListener(this);
        blind.setOnClickListener(this);
        
        // Bottom FrameLayout에 Main Fragment 실행
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        fragmentTransaction.add(R.id.frameBottom, MainFragment.getInstance( )).commit( );
    }
    
    @Override
    public void onClick(View view)
    {
        if (view == blind) {
            hideTopFragment(fragmentManager.findFragmentById(R.id.frameTop));
        }
    }
    
    @Override
    public void onBackPressed( )
    {
        if (onBackPressedListener != null) { onBackPressedListener.onBackPressed( ); }
        else { super.onBackPressed( ); }
    }
}
