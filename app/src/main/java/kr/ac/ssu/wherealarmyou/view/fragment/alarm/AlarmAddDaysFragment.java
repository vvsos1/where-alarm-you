package kr.ac.ssu.wherealarmyou.view.fragment.alarm;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.alarm.Period;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.fragment.OnBackPressedListener;
import kr.ac.ssu.wherealarmyou.view.viewmodel.AlarmAddDaysViewModel;


public class AlarmAddDaysFragment extends Fragment implements View.OnClickListener, OnBackPressedListener {


    private static AlarmAddDaysFragment instance;
    final int[] daysPrime = {2, 3, 5, 7, 11, 13, 17};
    View contentView;
    Map<String, Boolean> daysOfWeek;
    List<View> buttonLikes;
    List<Button> buttons;
    Button setDays;
    Button setDates;
    List<Date> dates;
    Period activePeriod;
    boolean isSelected[];
    int daysSum;
    private Drawable selectedMark;
    private Drawable transparentBackground;
    String toViewModel;
    private AlarmAddDaysViewModel alarmAddDaysViewModel;

    public static Fragment getInstance() {
        return new AlarmAddDaysFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.content_alarm_add_days, container, false);
        selectedMark = ContextCompat.getDrawable(getContext(), R.drawable.round_corners_button);
        transparentBackground = ContextCompat.getDrawable(getContext(), R.drawable.background_transparent);
        alarmAddDaysViewModel = new ViewModelProvider(requireActivity()).get(AlarmAddDaysViewModel.class);

        //todo : 알람 애드 프래그먼트에서 정보를 받아 설정할 예정
        daysSum = 1;
        toViewModel = "";
        isSelected = new boolean[]{false, false, false, false, false, false, false};
        dates = new ArrayList<>();
        daysOfWeek = new HashMap<>();

        setDays = contentView.findViewById(R.id.alarmAddDay_button_set_days);
        setDays.setOnClickListener(v -> {
            dates = null;
        });
        setDates = contentView.findViewById(R.id.alarmAddDay_button_set_dates);
        setDates.setOnClickListener(v -> {
            activePeriod = null;
        });

        buttonLikes = new ArrayList<View>(7);
        buttonLikes.add(contentView.findViewById(R.id.alarmAddDay_days_click_SUN));
        buttonLikes.add(contentView.findViewById(R.id.alarmAddDay_days_click_MON));
        buttonLikes.add(contentView.findViewById(R.id.alarmAddDay_days_click_TUE));
        buttonLikes.add(contentView.findViewById(R.id.alarmAddDay_days_click_WED));
        buttonLikes.add(contentView.findViewById(R.id.alarmAddDay_days_click_THU));
        buttonLikes.add(contentView.findViewById(R.id.alarmAddDay_days_click_FRI));
        buttonLikes.add(contentView.findViewById(R.id.alarmAddDay_days_click_SAT));

        for (View view : buttonLikes) {
            view.setOnClickListener(this::onClick);
        }

        buttons = new ArrayList<Button>(7);
        buttons.add(contentView.findViewById(R.id.alarmAddDay_days_button_SUN));
        buttons.add(contentView.findViewById(R.id.alarmAddDay_days_button_MON));
        buttons.add(contentView.findViewById(R.id.alarmAddDay_days_button_TUE));
        buttons.add(contentView.findViewById(R.id.alarmAddDay_days_button_WED));
        buttons.add(contentView.findViewById(R.id.alarmAddDay_days_button_THU));
        buttons.add(contentView.findViewById(R.id.alarmAddDay_days_button_FRI));
        buttons.add(contentView.findViewById(R.id.alarmAddDay_days_button_SAT));


        return contentView;
    }


    String makeInfoString() {

        if (daysSum == 510510) {
            daysOfWeek.put("EVERY_DAY", true);
            toViewModel = "매일";
        } else {

            if (daysOfWeek.containsKey("EVERY_DAY")) {
                daysOfWeek.remove("EVERY_DAY");
            }


            if (daysSum == 1) {
                toViewModel = "";
                boolean flag = true;

                if (dates.isEmpty()) {
                    return "오늘";
                }

                for (Date date : dates) {
                    if (flag) {
                        flag = false;
                        toViewModel = date.toString();
                    } else {
                        toViewModel = toViewModel + ", " + date.toString();
                    }
                }

            } else if (daysSum < 510510) {
                boolean isFirst = true;

                if (daysSum % 3 == 0) {
                    isFirst = false;
                    toViewModel = "월";
                }


                if (daysSum % 5 == 0) {
                    if (isFirst) {
                        isFirst = false;
                        toViewModel = "화";
                    } else {
                        toViewModel = toViewModel + ", 화";
                    }
                }

                if (daysSum % 7 == 0) {
                    if (isFirst) {
                        isFirst = false;
                        toViewModel = "수";
                    } else {
                        toViewModel = toViewModel + ", 수";
                    }
                }

                if (daysSum % 11 == 0) {
                    if (isFirst) {
                        isFirst = false;
                        toViewModel = "목";
                    } else {
                        toViewModel = toViewModel + ", 목";
                    }
                }

                if (daysSum % 13 == 0) {
                    if (isFirst) {
                        isFirst = false;
                        toViewModel = "금";
                    } else {
                        toViewModel = toViewModel + ", 금";
                    }
                }

                if (daysSum % 17 == 0) {
                    if (isFirst) {
                        isFirst = false;
                        toViewModel = "토";
                    } else {
                        toViewModel = toViewModel + ", 토";
                    }
                }

                if (daysSum % 2 == 0) {
                    if (isFirst) {
                        toViewModel = "일";
                    } else {
                        toViewModel = toViewModel + ", 일";
                    }
                }


            }
        }

        return toViewModel;
    }


    @Override
    public void onClick(View v) {
        int buttonIndex = buttonLikes.indexOf(v);

        if (isSelected[buttonIndex]) {
            buttons.get(buttonIndex).setBackground(transparentBackground);
            isSelected[buttonIndex] = false;

            daysSum /= daysPrime[buttonIndex];

            if (buttonIndex == 0)
                daysOfWeek.remove(DayOfWeek.of(7).toString());
            else
                daysOfWeek.remove(DayOfWeek.of(buttonIndex).toString());


        } else {
            buttons.get(buttonIndex).setBackground(selectedMark);
            isSelected[buttonIndex] = true;

            daysSum *= daysPrime[buttonIndex];

            if (buttonIndex == 0)
                daysOfWeek.put(DayOfWeek.of(7).toString(), true);
            else
                daysOfWeek.put(DayOfWeek.of(buttonIndex).toString(), true);

        }

        alarmAddDaysViewModel.setDaysOfWeek(daysOfWeek);
        alarmAddDaysViewModel.setLiveData(daysSum);
        alarmAddDaysViewModel.setInfoString(makeInfoString());


    }

    @Override
    public void onResume() {
        super.onResume();
        MainFrameActivity.setOnBackPressedListener(this);
    }

    @Override
    public void onBackPressed() {
        MainFrameActivity.backTopFragment();
        MainFrameActivity.backTopFragment();
    }

    @Override
    public void onStop() {
        super.onStop();
        MainFrameActivity.setOnBackPressedListener(null);
    }
}
