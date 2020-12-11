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

import java.util.ArrayList;
import java.util.List;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.alarm.Period;


public class AlarmAddDaysFragment extends Fragment implements View.OnClickListener {


    private static AlarmAddDaysFragment instance;
    final int[] daysPrime = {2, 3, 5, 7, 11, 13, 17};
    View contentView;
    int daysOfWeek;
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

    public static Fragment getInstance() {
        return new AlarmAddDaysFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.content_alarm_add_days, container, false);
        selectedMark = ContextCompat.getDrawable(getContext(), R.drawable.round_corners_button);
        transparentBackground = ContextCompat.getDrawable(getContext(), R.drawable.background_transparent);

        //todo : 알람 애드 프래그먼트에서 정보를 받아 설정할 예정
        daysSum = 1;
        isSelected = new boolean[]{false, false, false, false, false, false, false};

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
            view.setOnClickListener(v -> {
                int buttonIndex = buttonLikes.indexOf(v);

                if (isSelected[buttonIndex]) {
                    buttons.get(buttonIndex).setBackground(transparentBackground);
                    isSelected[buttonIndex] = false;
                } else {
                    buttons.get(buttonIndex).setBackground(selectedMark);
                    isSelected[buttonIndex] = true;
                }
            });
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


    @Override
    public void onClick(View v) {
        int buttonIndex = buttonLikes.indexOf(v);

        if (isSelected[buttonIndex]) {
            buttons.get(buttonIndex).setBackground(transparentBackground);
            isSelected[buttonIndex] = false;
        } else {
            buttons.get(buttonIndex).setBackground(selectedMark);
            isSelected[buttonIndex] = true;
        }

    }
}
