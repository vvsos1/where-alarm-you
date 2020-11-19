package kr.ac.ssu.wherealarmyou.view.login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.user.service.UserService;

import java.util.regex.Pattern;

public class StartActivity extends AppCompatActivity
{
    private AccountManager accountManager;
    private FirebaseAuth   firebaseAuth;
    
    private UserService userService;
    private Handler     handler = new Handler(Looper.getMainLooper( ));
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        accountManager = AccountManager.get(this);
        firebaseAuth   = FirebaseAuth.getInstance( );
        /*
        TextView startDecorationL = findViewById(R.id.startDecorationLeft);
        TextView startDecorationR = findViewById(R.id.startDecorationRight);
        
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext( ), R.anim.animation_rotation);
        
        startDecorationL.setAnimation(animation);
        startDecorationR.setAnimation(animation);
        */
        if (firebaseAuth.getCurrentUser( ) != null) {
            startActivity(new Intent(getApplicationContext( ), ProfileActivity.class));
            finish( );
            return;
        }
    
        userService = UserService.getInstance( );
        userService.checkExistUser(getUserEmail( ))
                   .subscribe(this::changeActivity);
    }
    
    private void changeActivity(Boolean aBoolean)
    {
        Intent intent;
        if (aBoolean == Boolean.TRUE) {
            intent = new Intent(getApplicationContext( ), SignUpActivity.class);
        }
        else {
            intent = new Intent(getApplicationContext( ), LoginActivity.class);
        }
        intent.putExtra("email", getUserEmail( ));
        startActivity(intent);
        finish( );
        /*
        firebaseAuth.signInWithEmailAndPassword(getUserEmail( ), "TEST")
                    .addOnCompleteListener(this, (task) -> {
                        if (!task.isSuccessful( )) {
                            try {
                                throw Objects.requireNonNull(task.getException( ));
                            }
                            catch (FirebaseAuthInvalidUserException ignored) {
                                Intent intent = new Intent(getApplicationContext( ), SignUpActivity.class);
                                intent.putExtra("email", getUserEmail( ));
                                startActivity(intent);
                                finish( );
                                try {
                                    Thread.sleep(200);
                                }
                                catch (InterruptedException e) {
                                    e.printStackTrace( );
                                }
                            }
                            catch (FirebaseAuthInvalidCredentialsException ignored) {
                                Intent intent = new Intent(getApplicationContext( ), LoginActivity.class);
                                intent.putExtra("email", getUserEmail( ));
                                startActivity(intent);
                                finish( );
                                try {
                                    Thread.sleep(1000);
                                }
                                catch (InterruptedException e) {
                                    e.printStackTrace( );
                                }
                            }
                            catch (Exception e) {
                                checkRegisteredUser( );
                                Log.e("StartError", e.getMessage( ));
                            }
                            checkRegisteredUser( );
                        }
                    });
            */
    }
    
    private String getUserEmail( )
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