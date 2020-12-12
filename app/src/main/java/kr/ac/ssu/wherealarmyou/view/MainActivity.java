package kr.ac.ssu.wherealarmyou.view;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.AlarmRepository;
import kr.ac.ssu.wherealarmyou.common.Icon;
import kr.ac.ssu.wherealarmyou.location.Address;
import kr.ac.ssu.wherealarmyou.location.Location;
import kr.ac.ssu.wherealarmyou.location.LocationRepository;
import kr.ac.ssu.wherealarmyou.location.UserLocation;
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
        locationService = LocationService.getInstance(this);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        handler = new Handler(Looper.getMainLooper());
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 10);


        button.setOnClickListener(v -> {
            locationService.getCurrentAddress().subscribe(address -> {
                Address addr = new Address(126.958043, 37.494564);
                Location location = new UserLocation("집", addr, 30, new Icon("#FFFFFF", "hi"), "vvsos1");

                String text = "current address : " + address.getLatitude() + ", " + address.getLongitude() + "\ntarget address : " +
                        addr.getLatitude() + ", " +
                        addr.getLongitude() + "\ndistance : " +
                        addr.getDistance(address) + " \nisCover : " +
                        location.isCover(address) + "\n";

                handler.post(() -> textView.setText(text));

            });
        });


    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> grantPermissionList = Arrays.asList(permissions);

        if (requestCode == 10) {
            if (grantPermissionList.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "ACCESS_FINE_LOCATION 허가");
            }
            if (grantPermissionList.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Log.d(TAG, "ACCESS_COARSE_LOCATION 허가");
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}