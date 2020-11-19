package kr.ac.ssu.wherealarmyou.view.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.user.service.UserService;

public class FindPasswordActivity extends AppCompatActivity implements View.OnClickListener
{
    // View
    private EditText editTextEmail;
    private Button   buttonFindPassword;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        
        // Find View By ID
        editTextEmail      = findViewById(R.id.editTextEmail);
        buttonFindPassword = findViewById(R.id.buttonFindPassword);
        
        // Set On Event Listener
        buttonFindPassword.setOnClickListener(this);
    }
    
    private void findPasswordActivity(String email)
    {
        UserService userService = UserService.getInstance( );
        
        /* 요청 실패 */
        // 이메일 미입력
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show( );
            return;
        }
        
        /* 요청 성공 */
        // 비밀번호 초기화 URL 전송 요청
        userService.resetPassword(email)
                   .doOnSuccess(unused -> {
                       Toast.makeText(FindPasswordActivity.this, "메일을 확인해주세요", Toast.LENGTH_LONG).show( );
                       finish( );
                   })
                   .doOnError(throwable ->
                           Toast.makeText(FindPasswordActivity.this, "메일 전송을 실패했습니다", Toast.LENGTH_SHORT).show( ))
                   .subscribe( );
    }
    
    /* ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ Event Listener Method ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ */
    @Override
    public void onClick(View view)
    {
        if (view == buttonFindPassword) {
            String email = editTextEmail.getText( ).toString( ).trim( );
            findPasswordActivity(email);
        }
    }
    /* ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ Event Listener Method ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ */
}