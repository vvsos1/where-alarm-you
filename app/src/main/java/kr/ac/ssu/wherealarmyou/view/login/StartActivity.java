package kr.ac.ssu.wherealarmyou.view.login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.user.service.UserService;

import java.util.regex.Pattern;

public class StartActivity extends AppCompatActivity
{
    private AccountManager accountManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        accountManager = AccountManager.get(this);
        
        // 이미 로그인 된 사용자가 존재하면 건너뜀
        if (FirebaseAuth.getInstance( ).getCurrentUser( ) != null) {
            startActivity(new Intent(getApplicationContext( ), ProfileActivity.class));
            finish( );
            return;
        }
        
        // 이메일 중복 확인 요청
        UserService userService = UserService.getInstance( );
        userService.checkExistUser(getUserAndroidEmail( ))
                   .subscribe(this::changeActivity);
    }
    
    private void changeActivity(Boolean aBoolean)
    {
        Intent intent;
        if (aBoolean == Boolean.TRUE) {
            intent = new Intent(getApplicationContext( ), LoginActivity.class);
        }
        else {
            intent = new Intent(getApplicationContext( ), SignUpActivity.class);
        }
        intent.putExtra("email", getUserAndroidEmail( ));
        startActivity(intent);
        finish( );
    }
    
    private String getUserAndroidEmail( )
    {
        String    email        = null;
        Pattern   emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts     = accountManager.getAccounts( );
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches( )) {
                email = account.name;
            }
        }
        return email;
    }
}