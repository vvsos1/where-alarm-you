package kr.ac.ssu.wherealarmyou.login;

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
import com.google.firebase.auth.UserProfileChangeRequest;
import kr.ac.ssu.wherealarmyou.R;

import java.util.Objects;

public class SetUserInfoActivity extends AppCompatActivity implements View.OnClickListener
{
    private FirebaseAuth             firebaseAuth;
    private UserProfileChangeRequest userProfileChangeRequest;
    
    private EditText editTextName;
    private Button   buttonSignUp;
    private TextView textViewLogout;
    
    private long keyDownTime_KEYCODE_BACK;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_info);
        
        firebaseAuth = FirebaseAuth.getInstance( );
        
        TextView textViewMessage = findViewById(R.id.textViewMessage2);
        editTextName   = findViewById(R.id.editTextName);
        buttonSignUp   = findViewById(R.id.buttonSignUp2);
        textViewLogout = findViewById(R.id.textViewLogout);
        
        textViewMessage.setText(Objects.requireNonNull(firebaseAuth.getCurrentUser( )).getEmail( ));
        
        buttonSignUp.setOnClickListener(this);
        textViewLogout.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view)
    {
        if (view == buttonSignUp) {
            setUserName( );
        }
        else if (view == textViewLogout) {
            firebaseAuth.signOut( );
            startActivity(new Intent(getApplicationContext( ), LoginActivity.class));
            finish( );
        }
    }
    
    private void setUserName( )
    {
        String userName = editTextName.getText( ).toString( ).trim( );
        userProfileChangeRequest = new UserProfileChangeRequest.Builder( ).setDisplayName(userName).build( );
        
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(SetUserInfoActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show( );
        }
        else {
            Objects.requireNonNull(firebaseAuth.getCurrentUser( )).updateProfile(userProfileChangeRequest)
                   .addOnCompleteListener(task -> {
                       if (task.isSuccessful( )) {
                           Toast.makeText(SetUserInfoActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show( );
                           startActivity(new Intent(getApplicationContext( ), ProfileActivity.class));
                           finish( );
                       }
                       else {
                           Toast.makeText(SetUserInfoActivity.this, "오류", Toast.LENGTH_SHORT).show( );
                       }
                   });
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