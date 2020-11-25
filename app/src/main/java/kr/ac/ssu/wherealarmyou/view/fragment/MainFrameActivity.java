package kr.ac.ssu.wherealarmyou.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import kr.ac.ssu.wherealarmyou.R;

public class MainFrameActivity extends AppCompatActivity implements View.OnClickListener
{
    FragmentManager fragmentManager;
    FrameLayout     frameTop;
    FrameLayout     frameBottom;
    LinearLayout    blind;
    
    OnBackPressedListener onBackPressedListener;
    
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
        switch (view.getId( )) {
            case (R.id.blind):
                hideTopFragment(fragmentManager.findFragmentById(R.id.frameTop));
                break;
            case (R.id.overlap_buttonBack):
                Toast.makeText(this, "MAIN BACK 버튼", Toast.LENGTH_SHORT).show( );
                this.backTopFragment(fragmentManager.findFragmentById(R.id.frameTop));
                break;
            case (R.id.overlap_buttonHide):
                Toast.makeText(this, "MAIN HIDE 버튼", Toast.LENGTH_SHORT).show( );
                this.hideTopFragment(fragmentManager.findFragmentById(R.id.frameTop));
                break;
        }
    }
    
    /* 시작 */
    // Top FrameLayout을 띄우고 Fragment를 나타내기
    public void showTopFragment(Fragment fragment)
    {
        Bundle bundle = new Bundle(2);
        bundle.putBoolean("backButton", false);
        bundle.putBoolean("hideButton", true);
        fragment.setArguments(bundle);
        blind.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        fragmentTransaction.replace(R.id.frameTop, fragment).addToBackStack(null).commit( );
    }
    
    /* 추가 */
    // 기존 Fragment 위에 새로운 Fragment를 띄우기
    public void addTopFragment(Fragment fragment)
    {
        Bundle bundle = new Bundle(2);
        bundle.putBoolean("backButton", true);
        bundle.putBoolean("hideButton", false);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        fragmentTransaction.replace(R.id.frameTop, fragment).addToBackStack(null).commit( );
        
    }
    
    /* 뒤로 가기 */
    // 이전 Fragment로 돌아가기
    public void backTopFragment(Fragment fragment)
    {
        onBackPressedListener = null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        fragmentTransaction.remove(fragment).commit( );
        fragmentManager.popBackStack( );
    }
    
    /* 종료 */
    // Top FrameLayout을 없애고 모든 Fragment 종료하기
    public void hideTopFragment(Fragment fragment)
    {
        blind.setVisibility(View.GONE);
        onBackPressedListener = null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        fragmentTransaction.remove(fragment).commit( );
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    
    // Fragment에서 설정한 BackPressedListener가 존재하면 프래그먼트에서 이벤트 처리
    public void setOnBackPressedListener(OnBackPressedListener listener)
    {
        this.onBackPressedListener = listener;
    }
    
    @Override
    public void onBackPressed( )
    {
        if (onBackPressedListener != null) { onBackPressedListener.onBackPressed( ); }
        else { super.onBackPressed( ); }
    }
}
