package kr.ac.ssu.wherealarmyou.view;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.AlarmRepository;
import kr.ac.ssu.wherealarmyou.location.Location;
import kr.ac.ssu.wherealarmyou.location.LocationRepository;
import kr.ac.ssu.wherealarmyou.user.UserRepository;
import reactor.core.publisher.Mono;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private UserRepository userRepository;
    private AlarmRepository alarmRepository;
    private LocationRepository locationRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.setPersistenceEnabled(true);
        userRepository = new UserRepository(mDatabase);
        alarmRepository = new AlarmRepository(mDatabase);
        locationRepository = new LocationRepository(mDatabase);

        locationRepository.save(new Location("title", "road", "jibun", 127.127, 32.32))
                .doOnError(exception -> Log.e("MainActivity", exception.getMessage()))
                .then(Mono.just("location save success"))
                .doOnNext(str -> Log.d("MainActivity", str))
                .subscribe();

    }
}