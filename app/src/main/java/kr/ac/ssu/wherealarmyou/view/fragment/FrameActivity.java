package kr.ac.ssu.wherealarmyou.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import kr.ac.ssu.wherealarmyou.R;

public class FrameActivity extends AppCompatActivity implements View.OnClickListener
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
        
        frameTop    = findViewById(R.id.frameTop);
        frameBottom = findViewById(R.id.frameBottom);
        blind       = findViewById(R.id.blind);
        
        frameTop.setOnClickListener(this);
        blind.setOnClickListener(this);
        
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        fragmentTransaction.add(R.id.frameBottom, MainFragment.getInstance( )).commit( );
    }
    
    @Override
    public void onClick(View view)
    {
        if (view.getId( ) == R.id.blind) {
            hideTopFragment(fragmentManager.findFragmentById(R.id.frameTop));
        }
    }
    
    public void showTopFragment(Fragment fragment)
    {
        blind.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        fragmentTransaction.replace(R.id.frameTop, fragment).addToBackStack(null).commit( );
    }
    
    public void addTopFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        fragmentTransaction.replace(R.id.frameTop, fragment).addToBackStack(null).commit( );
        
    }
    
    public void backTopFragment(Fragment fragment)
    {
        onBackPressedListener = null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        fragmentTransaction.remove(fragment).commit( );
        fragmentManager.popBackStack( );
    }
    
    public void hideTopFragment(Fragment fragment)
    {
        blind.setVisibility(View.GONE);
        onBackPressedListener = null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        fragmentTransaction.remove(fragment).commit( );
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    
    public void setOnBackPressedListener(OnBackPressedListener listener) { this.onBackPressedListener = listener; }
    
    @Override
    public void onBackPressed( )
    {
        if (onBackPressedListener != null) { onBackPressedListener.onBackPressed( ); }
        else { super.onBackPressed( ); }
    }
}
