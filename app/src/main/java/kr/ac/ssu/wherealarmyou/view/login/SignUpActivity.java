package kr.ac.ssu.wherealarmyou.view.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import kr.ac.ssu.wherealarmyou.R;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener
{
    private final Pattern      emailPattern = Patterns.EMAIL_ADDRESS;
    private       FirebaseAuth firebaseAuth;
    private       EditText     editTextEmail;
    private       EditText     editTextPassword;
    private       Button       buttonSignUp;
    private       TextView     textViewSignUp1;
    private       TextView     textViewSignUp2;
    private       TextView     textViewLogin;
    private       TextView     textViewWarning;
    private       String       userEmail;
    private final TextWatcher  textWatcher  = new TextWatcher( )
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        
        @SuppressLint("SetTextI18n")
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            final String email = editTextEmail.getText( ).toString( ).trim( );
            if (emailPattern.matcher(email).matches( )) {
                if (email.equals(userEmail)) {
                    textViewSignUp1.setText("추천된 이메일을 선택하셨군요!");
                    textViewSignUp2.setText("더욱 편리한 로그인이 제공됩니다");
                }
                else {
                    textViewSignUp1.setText("Tip! 회원가입에 안드로이드 계정 이메일을 사용하면");
                    textViewSignUp2.setText("앱을 더욱 편리하게 이용할 수 있어요");
                }
            }
            else {
                textViewSignUp1.setText("입력된 이메일에 문제가 발생했습니다");
                textViewSignUp2.setText("제대로 된 형식인지 다시 한번 확인해 주세요");
            }
        }
        
        @Override
        public void afterTextChanged(Editable s) { }
    };
    private       long         keyDownTime_KEYCODE_BACK;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Bundle extras = getIntent( ).getExtras( );
        
        firebaseAuth = FirebaseAuth.getInstance( );
        
        editTextEmail    = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignUp     = findViewById(R.id.buttonSignUp);
        textViewSignUp1  = findViewById(R.id.textViewSignUp1);
        textViewSignUp2  = findViewById(R.id.textViewSignUp2);
        textViewLogin    = findViewById(R.id.textViewLogin);
        textViewWarning  = findViewById(R.id.textViewWarning);
        
        userEmail = extras.getString("email");
        
        buttonSignUp.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
        if (emailPattern.matcher(userEmail).matches( )) editTextEmail.setOnTouchListener(this);
        editTextPassword.addTextChangedListener(textWatcher);
    }
    
    private void registerUser( )
    {
        final String email    = editTextEmail.getText( ).toString( ).trim( );
        final String password = editTextPassword.getText( ).toString( ).trim( );
        
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show( );
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show( );
            return;
        }
        
        if (emailPattern.matcher(email).matches( )) {
            if (password.length( ) > 6) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful( )) {
                                    Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show( );
                                    firebaseAuth.signOut( );
                                    Intent intent = new Intent(getApplicationContext( ), LoginActivity.class);
                                    intent.putExtra("email", email)
                                          .putExtra("password", password)
                                          .putExtra("textViewLogin", "로그인 버튼을 눌러주세요");
                                    startActivity(intent);
                                    finish( );
                                }
                                else {
                                    Toast.makeText(SignUpActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show( );
                                    textViewWarning.setText("이미 등록된 메일이거나 서버 오류일 수 있습니다");
                                }
                            });
            }
            else {
                Toast.makeText(SignUpActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show( );
                textViewWarning.setText("비밀번호를 6자리 이상으로 설정해주세요");
            }
        }
        else {
            Toast.makeText(SignUpActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show( );
            textViewWarning.setText("이메일 형식을 확인해주세요");
        }
    }
    
    @Override
    public void onClick(View view)
    {
        if (view == buttonSignUp) {
            registerUser( );
        }
        if (view == textViewLogin) {
            editTextEmail.setOnTouchListener(null);
            Intent intent = new Intent(getApplicationContext( ), LoginActivity.class);
            intent.putExtra("email", "");
            startActivity(intent);
            finish( );
        }
    }
    
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View view, @NonNull MotionEvent event)
    {
        if (event.getAction( ) == MotionEvent.ACTION_UP) {
            editTextEmail.setOnTouchListener(null);
            editTextEmail.setText(userEmail);
            textViewSignUp1.setText("입력된 이메일을 사용하는 건 어떠신가요?");
            textViewSignUp2.setText("앱을 더욱 편리하게 이용하실 수 있습니다");
        }
        return false;
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