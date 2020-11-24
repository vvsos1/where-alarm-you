package kr.ac.ssu.wherealarmyou.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.user.dto.DeleteRequest;
import kr.ac.ssu.wherealarmyou.user.service.UserService;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener
{
    private FirebaseAuth firebaseAuth;
    
    private Button   buttonLogout;
    private TextView textViewDeleteUser;
    
    private String userName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        firebaseAuth = FirebaseAuth.getInstance( );
        userName     = Objects.requireNonNull(firebaseAuth.getCurrentUser( )).getDisplayName( );
        
        TextView textViewUserEmail = findViewById(R.id.textViewUserEmail);
        buttonLogout       = findViewById(R.id.buttonLogout);
        textViewDeleteUser = findViewById(R.id.textViewDeleteUser);
        
        if (firebaseAuth.getCurrentUser( ) == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish( );
        }
        if (TextUtils.isEmpty(userName)) {
            startActivity(new Intent(this, SetUserInfoActivity.class));
            finish( );
        }
        
        textViewUserEmail.setText(
                "\n" + userName + "님 반갑습니다\n\n" + firebaseAuth.getCurrentUser( ).getEmail( ) + "으로 로그인 하였습니다");
        
        buttonLogout.setOnClickListener(this);
        textViewDeleteUser.setOnClickListener(this);
    }
    
    public void deleteUserActivity(String email)
    {
        UserService userService = UserService.getInstance( );
        
        userService.deleteUser(new DeleteRequest(email, ""))
                   .doOnSuccess(unused -> {
                       Toast.makeText(ProfileActivity.this, "계정이 삭제 되었습니다", Toast.LENGTH_SHORT).show( );
                       Intent intent = new Intent(getApplicationContext( ), SignUpActivity.class);
                       intent.putExtra("email", "");
                       startActivity(intent);
                       finish( );
                   })
                   .subscribe( );
    }
    
    @Override
    public void onClick(View view)
    {
        if (view == buttonLogout) {
            firebaseAuth.signOut( );
            Intent intent = new Intent(getApplicationContext( ), LoginActivity.class);
            intent.putExtra("email", "");
            startActivity(intent);
            finish( );
        }
        
        if (view == textViewDeleteUser) {
            AlertDialog.Builder alertDialogDeleteUser = new AlertDialog.Builder(ProfileActivity.this);
            alertDialogDeleteUser.setMessage("정말 계정을 삭제 할까요?")
                                 .setCancelable(false)
                                 .setPositiveButton("확인", (dialogInterface, i) -> {
                                     FirebaseUser user = firebaseAuth.getCurrentUser( );
                                     if (user != null) {
                                         deleteUserActivity(firebaseAuth.getCurrentUser( ).getEmail( ));
                                     }
                                 })
                                 .setNegativeButton("취소", (dialogInterface, i) ->
                                         Toast.makeText(ProfileActivity.this, "탈퇴가 취소되었습니다", Toast.LENGTH_LONG).show( ))
                                 .show( );
        }
    }
}
