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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    private FirebaseAuth firebaseAuth;
    
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button   buttonLogin;
    private TextView textViewSignUp;
    private TextView textViewFindPassword;
    private TextView textViewWarning1;
    private TextView textViewWarning2;
    
    private long keyDownTime_KEYCODE_BACK;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bundle extras = getIntent( ).getExtras( );
        
        firebaseAuth = FirebaseAuth.getInstance( );
        
        TextView textViewLogin = findViewById(R.id.textViewLogin);
        editTextEmail        = findViewById(R.id.editTextEmail);
        editTextPassword     = findViewById(R.id.editTextPassword);
        buttonLogin          = findViewById(R.id.buttonLogin);
        textViewSignUp       = findViewById(R.id.textViewSignUp);
        textViewFindPassword = findViewById(R.id.textViewFindPassword);
        textViewWarning1     = findViewById(R.id.textViewWarning1);
        textViewWarning2     = findViewById(R.id.textViewWarning2);
        
        if (extras != null) {
            editTextEmail.setText(extras.getString("email"));
            editTextPassword.setText(extras.getString("password"));
            if (extras.getString("textViewLogin") != null) {
                textViewLogin.setText(extras.getString("textViewLogin"));
            }
        }
        
        buttonLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
        textViewFindPassword.setOnClickListener(this);
    }
    
    private void userLogin( )
    {
        String email    = editTextEmail.getText( ).toString( ).trim( );
        String password = editTextPassword.getText( ).toString( ).trim( );
        
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show( );
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show( );
            return;
        }
        
        firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful( )) {
                            Toast.makeText(getApplicationContext( ), "반갑습니다", Toast.LENGTH_LONG).show( );
                            finish( );
                            startActivity(new Intent(getApplicationContext( ), ProfileActivity.class));
                        }
                        else {
                            Toast.makeText(getApplicationContext( ), "로그인 실패", Toast.LENGTH_LONG).show( );
                            textViewWarning1.setText("아이디 또는 비밀번호가 일치하지 않거나");
                            textViewWarning2.setText("네트워크에 연결되어 있지 않습니다");
                            textViewWarning2.setVisibility(View.VISIBLE);
                        }
                    });
    }
    
    @Override
    public void onClick(View view)
    {
        if (view == buttonLogin) {
            userLogin( );
        }
        if (view == textViewSignUp) {
            Intent intent = new Intent(getApplicationContext( ), SignUpActivity.class);
            intent.putExtra("email", "");
            startActivity(intent);
            finish( );
        }
        if (view == textViewFindPassword) {
            startActivity(new Intent(this, FindActivity.class));
        }
    }
    
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
}
