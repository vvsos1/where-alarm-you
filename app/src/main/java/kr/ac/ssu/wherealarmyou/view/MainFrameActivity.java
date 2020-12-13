package kr.ac.ssu.wherealarmyou.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Map;
import java.util.Objects;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.AlarmRepository;
import kr.ac.ssu.wherealarmyou.alarm.serivce.AlarmService;
import kr.ac.ssu.wherealarmyou.user.User;
import kr.ac.ssu.wherealarmyou.user.service.UserService;
import kr.ac.ssu.wherealarmyou.view.fragment.MainFragment;
import kr.ac.ssu.wherealarmyou.view.fragment.OnBackPressedListener;
import kr.ac.ssu.wherealarmyou.view.login.SetUserInfoActivity;
import reactor.core.publisher.Mono;

public class MainFrameActivity extends AppCompatActivity {
    private static final String TAG = "MainFrameActivity";

    public static FragmentManager fragmentManager;

    @SuppressLint("StaticFieldLeak")
    public static FrameLayout frameTop;

    @SuppressLint("StaticFieldLeak")
    public static FrameLayout frameBottom;

    @SuppressLint("StaticFieldLeak")
    public static LinearLayout blind;

    public static OnBackPressedListener onBackPressedListener;

    public DataManager dataManager = DataManager.getInstance();

    /* 시작 */
    // Top FrameLayout을 띄우고 Fragment를 나타내기
    public static void showTopFragment(Fragment fragment) {
        Bundle bundle = new Bundle(2);
        bundle.putBoolean("backButton", false);
        bundle.putBoolean("hideButton", true);
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .add(R.id.frameTop, fragment)
                .addToBackStack(null)
                .commit();
    }

    /* 추가 */
    // 기존 Fragment 위에 새로운 Fragment를 띄우기
    public static void addTopFragment(Fragment fragment) {
        Bundle bundle = new Bundle(2);
        bundle.putBoolean("backButton", true);
        bundle.putBoolean("hideButton", false);
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(0, R.anim.fade_out, 0, R.anim.fade_out)
                .add(R.id.frameTop, fragment)
                .addToBackStack(null)
                .commit();
    }

    /* 뒤로 가기 */
    // 이전 Fragment로 돌아가기
    public static void backTopFragment() {
        fragmentManager.popBackStack();
    }

    /* 종료 */
    // Top FrameLayout을 없애고 모든 Fragment 종료하기
    public static void hideTopFragment() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static void setOnBackPressedListener(OnBackPressedListener listener) {
        onBackPressedListener = listener;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        if (Objects.requireNonNull(FirebaseAuth.getInstance().getUid()).isEmpty()) {
            startActivity(new Intent(getApplicationContext(), SetUserInfoActivity.class));
        }

        fragmentManager = getSupportFragmentManager();

        // Find View By Id
        frameTop = findViewById(R.id.frameTop);
        frameBottom = findViewById(R.id.frameBottom);
        blind = findViewById(R.id.blind);

        // Set On Event Listener
        frameTop.setOnClickListener(null);
        blind.setOnClickListener(v -> hideTopFragment());

        // Bottom FrameLayout에 Main Fragment 실행
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameBottom, new MainFragment()).commit();

        // Blind Visibility Controller
        fragmentManager.addOnBackStackChangedListener(() -> {
            if (fragmentManager.getBackStackEntryCount() == 0) {
                Animation animation = new AlphaAnimation(1, 0);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.setDuration(500);
                blind.setAnimation(animation);
                blind.setVisibility(View.GONE);
            } else {
                if (blind.getVisibility() != View.VISIBLE) {
                    Animation animation = new AlphaAnimation(0, 1);
                    animation.setInterpolator(new DecelerateInterpolator());
                    animation.setDuration(500);
                    blind.setAnimation(animation);
                }
                blind.setVisibility(View.VISIBLE);
            }
        });
        UserService userService = UserService.getInstance();
        userService.findUser(userService.getCurrentUserUid()).map(User::getGroups).flatMapIterable(Map::keySet).subscribe(s -> {
            FirebaseMessaging.getInstance().subscribeToTopic(s).addOnSuccessListener(aVoid -> Log.d("MainFrameActivity", s + "구독 완료"));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Log.d(TAG, "onResume, intent : " + intent);
        if (intent != null && "action.GROUP_ALARM_REGISTER".equals(intent.getAction())) {
            String alarmUid = intent.getStringExtra("alarmUid");
            Log.d(TAG, "group alarm received : " + alarmUid);

            AlarmService alarmService = AlarmService.getInstance(getApplicationContext());
            UserService userService = UserService.getInstance();
            AlarmRepository alarmRepository = AlarmRepository.getInstance();


            Mono<Alarm> alarmMono = alarmRepository.getAlarmByUid(alarmUid);
            alarmMono.flatMap(alarm -> {
                return userService.addAlarm(alarm).zipWith(alarmService.register(alarm)).then();
            }).subscribe();
        }
        dataManager.updateAlarmLiveData();
        dataManager.updateGroupLiveData();
        dataManager.updateLocationLiveData();
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}
