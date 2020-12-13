package kr.ac.ssu.wherealarmyou.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.AlarmRepository;
import kr.ac.ssu.wherealarmyou.alarm.serivce.AlarmService;
import kr.ac.ssu.wherealarmyou.location.LocationRepository;
import kr.ac.ssu.wherealarmyou.location.service.LocationService;
import kr.ac.ssu.wherealarmyou.user.UserRepository;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_SIEUN = 1004;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private UserRepository userRepository;
    private AlarmRepository alarmRepository;
    private LocationRepository locationRepository;
    private Button button;
    private TextView textView;
    private LocationService locationService;
    private Handler handler;
    private AlarmService alarmService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
//        mDatabase.setPersistenceEnabled(true);
        userRepository = UserRepository.getInstance();
        alarmRepository = AlarmRepository.getInstance();
        locationRepository = LocationRepository.getInstance();
        alarmService = AlarmService.getInstance(this);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);


        handler = new Handler(Looper.getMainLooper());


//        alarmService
//                .getGroupAlarmPublisher("-MNgAd3tfZVzjCGQ1dvN")
//                .subscribe(alarm1 -> handler.post(() -> {
//                    textView.setText(textView.getText() + "\n" + alarm1.toString());
//                }));
//
//
//        AlarmSaveRequest req = AlarmSaveRequest.builder(new Time(14, 30))
//                .group("-MNgAd3tfZVzjCGQ1dvN")
//                .title("그룹 알람 테스트")
//                .dates(List.of(new Date(12, 12, 2020)))
//                .description("테스트 !!")
//                .build();
//
//        alarmService.save(req).subscribe();

        FirebaseMessaging.getInstance().subscribeToTopic("test").addOnSuccessListener(aVoid -> Log.d(TAG, "test 토픽 구독 완료"));

    }

}