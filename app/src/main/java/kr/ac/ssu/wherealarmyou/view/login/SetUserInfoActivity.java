package kr.ac.ssu.wherealarmyou.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.user.dto.UpdateRequest;
import kr.ac.ssu.wherealarmyou.user.service.UserService;
import kr.ac.ssu.wherealarmyou.view.fragment.MainFrameActivity;

import java.util.Objects;

public class SetUserInfoActivity extends AppCompatActivity implements View.OnClickListener
{
    // View
    private EditText editTextName;
    private Button   buttonSetUserInfo;
    private TextView textViewLogout;
    
    //
    private long keyDownTime_KEYCODE_BACK;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_info);
        
        // Find View By ID
        TextView textViewMessage = findViewById(R.id.textViewMessage2);
        textViewMessage.setText(Objects.requireNonNull(FirebaseAuth.getInstance( ).getCurrentUser( )).getEmail( ));
        editTextName      = findViewById(R.id.editTextName);
        buttonSetUserInfo = findViewById(R.id.buttonSetUserInfo);
        textViewLogout    = findViewById(R.id.textViewLogout);
        
        // Set On Event Listener
        buttonSetUserInfo.setOnClickListener(this);
        textViewLogout.setOnClickListener(this);
    }
    
    private void setUserInfoActivity(String userName)
    {
        UserService userService = UserService.getInstance( );
        
        String email = Objects.requireNonNull(FirebaseAuth.getInstance( ).getCurrentUser( )).getEmail( );
        
        /* 요청 실패 */
        // 이름 미입력
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(SetUserInfoActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show( );
            return;
        }
        
        /* 요청 성공 */
        // 새로운 유저 이름으로 업데이트 요청
        userService.updateUserInfo(new UpdateRequest(email, userName))
                   .doOnSuccess(user -> {
                       Toast.makeText(SetUserInfoActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show( );
                       startActivity(new Intent(getApplicationContext( ), MainFrameActivity.class));
                       finish( );
                   })
                   .doOnError(throwable ->
                           Toast.makeText(SetUserInfoActivity.this, "오류", Toast.LENGTH_SHORT).show( ))
                   .subscribe( );
        
    }
    
    private void logoutActivity( )
    {
        UserService userService = UserService.getInstance( );
        
        userService.logout( )
                   .doOnSuccess(unused -> {
                       startActivity(new Intent(getApplicationContext( ), LoginActivity.class));
                       finish( );
                   })
                   .subscribe( );
    }
    
    /* ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ Event Listener Method ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
    @Override
    public void onClick(View view)
    {
        if (view == buttonSetUserInfo) {
            String userName = editTextName.getText( ).toString( ).trim( );
            setUserInfoActivity(userName);
        }
        if (view == textViewLogout) {
            logoutActivity( );
        }
    }
    /* ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ Event Listener Method ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ */
    
    /* ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ Event Handler Method ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        boolean result = true;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis( ) - keyDownTime_KEYCODE_BACK > 2000) {
                Toast.makeText(this, "뒤로가기 버튼을 누르면 앱이 종료됩니다", Toast.LENGTH_SHORT).show( );
                keyDownTime_KEYCODE_BACK = System.currentTimeMillis( );
            }
            else {
                finish( );
            }
        }
        else {
            result = super.onKeyDown(keyCode, event);
        }
        return result;
    }
    /* ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ Event Handler Method ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ */
}