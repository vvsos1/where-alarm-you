package kr.ac.ssu.wherealarmyou.view;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.AlarmRepository;
import kr.ac.ssu.wherealarmyou.user.UserRepository;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private UserRepository userRepository;
    private AlarmRepository alarmRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        userRepository = new UserRepository(mDatabase);
        alarmRepository = new AlarmRepository(mDatabase);

        alarmRepository.getAlarmByUid("alarmUid2")
                .subscribe(alarm -> Log.d("Alarm", alarm.toString() + "\n Alarm Type : " + alarm.getClass().getSimpleName()));
    }
}