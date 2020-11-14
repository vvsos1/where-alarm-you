package kr.ac.ssu.wherealarmyou.view.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import kr.ac.ssu.wherealarmyou.R;

public class FindActivity extends AppCompatActivity implements View.OnClickListener
{
    private FirebaseAuth firebaseAuth;
    
    private EditText editTextEmail;
    private Button   buttonFindPassword;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        
        firebaseAuth = FirebaseAuth.getInstance( );
        
        editTextEmail      = findViewById(R.id.editTextEmail);
        buttonFindPassword = findViewById(R.id.buttonFindPassword);
        
        buttonFindPassword.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view)
    {
        if (view == buttonFindPassword) {
            String email = editTextEmail.getText( ).toString( ).trim( );
            
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show( );
                return;
            }
            
            firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful( )) {
                                Toast.makeText(FindActivity.this, "메일을 확인해주세요", Toast.LENGTH_LONG).show( );
                                finish( );
                            }
                            else {
                                Toast.makeText(FindActivity.this, "메일 전송을 실패했습니다", Toast.LENGTH_SHORT).show( );
                            }
                        });
        }
    }
}