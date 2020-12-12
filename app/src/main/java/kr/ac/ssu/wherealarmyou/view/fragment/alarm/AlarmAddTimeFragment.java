package kr.ac.ssu.wherealarmyou.view.fragment.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.fragment.OnBackPressedListener;
import kr.ac.ssu.wherealarmyou.view.viewmodel.AlarmAddTimeViewModel;

public class AlarmAddTimeFragment extends Fragment implements View.OnClickListener, OnBackPressedListener {
    private AlarmAddTimeViewModel alarmAddTimeViewModel;

    private int stage;

    private int hours;
    private int minutes;

    private LinearLayout linearLayoutSetAMPM;
    private LinearLayout linearLayoutSetTime;

    private Button buttonAm;
    private Button buttonPm;
    private TextView timeTextView;
    private TextView hourOrMinute;

    private List<Button> buttonNumber;

    private Button buttonArrowLeft;
    private Button buttonArrowRight;

    public static AlarmAddTimeFragment getInstance() {
        return new AlarmAddTimeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        stage = 0;
        hours = -1;
        minutes = -1;
        View contentView = inflater.inflate(R.layout.content_alarm_add_time, container, false);

        alarmAddTimeViewModel = new ViewModelProvider(requireActivity()).get(AlarmAddTimeViewModel.class);

        linearLayoutSetAMPM = contentView.findViewById(R.id.alarmAddTime_linearLayoutSetAMPM);
        linearLayoutSetTime = contentView.findViewById(R.id.alarmAddTime_linearLayoutSetTimes);

        buttonAm = contentView.findViewById(R.id.alarmAddTime_buttonAM);
        buttonPm = contentView.findViewById(R.id.alarmAddTime_buttonPM);

        timeTextView = contentView.findViewById(R.id.text_view_alarm_add_time);
        hourOrMinute = contentView.findViewById(R.id.hour_or_minute_alarm_add_time);


        buttonNumber = new ArrayList<>(10);
        buttonNumber.add(contentView.findViewById(R.id.button_number_0));
        buttonNumber.add(contentView.findViewById(R.id.button_number_1));
        buttonNumber.add(contentView.findViewById(R.id.button_number_2));
        buttonNumber.add(contentView.findViewById(R.id.button_number_3));
        buttonNumber.add(contentView.findViewById(R.id.button_number_4));
        buttonNumber.add(contentView.findViewById(R.id.button_number_5));
        buttonNumber.add(contentView.findViewById(R.id.button_number_6));
        buttonNumber.add(contentView.findViewById(R.id.button_number_7));
        buttonNumber.add(contentView.findViewById(R.id.button_number_8));
        buttonNumber.add(contentView.findViewById(R.id.button_number_9));

        for (Button button : buttonNumber) {
            button.setOnClickListener(this::onClick);
        }

        buttonArrowLeft = contentView.findViewById(R.id.button_arrow_left);
        buttonArrowRight = contentView.findViewById(R.id.button_arrow_right);

        buttonArrowLeft.setOnClickListener(this::onClick);
        buttonArrowRight.setOnClickListener(this::onClick);

        buttonAm.setOnClickListener(this::onClick);
        buttonPm.setOnClickListener(this::onClick);
        return contentView;
    }
    
    @Override
    public void onClick(View view) {
        if (buttonNumber.contains(view)) {
            timeTextView.setText(
                    timeTextView.getText() + Integer.toString(buttonNumber.indexOf(view)));
        } else if (stage == 0) {
            if (view == buttonAm) {
                alarmAddTimeViewModel.selectAmPm(false);
            }
            if (view == buttonPm) {
                alarmAddTimeViewModel.selectAmPm(true);
            }
            hours = -1;
            minutes = -1;
            stage++;
            linearLayoutSetAMPM.setVisibility(View.GONE);
            linearLayoutSetTime.setVisibility(View.VISIBLE);
        } else if (stage == 1) {



            if (view == buttonArrowLeft) {
                stage--;
                linearLayoutSetAMPM.setVisibility(View.VISIBLE);
                linearLayoutSetTime.setVisibility(View.GONE);
                timeTextView.setText("");
            } else if (view == buttonArrowRight) {

                if ((timeTextView.getText().equals("")))
                    return;

                int textToInt = Integer.parseInt(timeTextView.getText().toString());

                if (textToInt < 0 || textToInt >= 12) {
                    Toast.makeText(getContext(), "입력을 시간 값으로 바꿀 수 없습니다", Toast.LENGTH_SHORT).show();
                    timeTextView.setText("");
                } else {
                    stage++;
                    hourOrMinute.setText("분");
                    hours = textToInt;


                    alarmAddTimeViewModel.selectHours(hours);

                    timeTextView.setText("");

                    if (minutes >= 0 && minutes < 60) {
                        timeTextView.setText(Integer.toString(minutes));
                    }

                }
            }
        } else if (stage == 2) {

            if (view == buttonArrowLeft) {
                stage--;
                hourOrMinute.setText("시");


                timeTextView.setText("");

                if (hours >= 0 && hours < 24) {
                    timeTextView.setText(Integer.toString(hours));
                    alarmAddTimeViewModel.selectAmPm(alarmAddTimeViewModel.getBoolean());
                }

            } else if (view == buttonArrowRight) {
                if ((timeTextView.getText().equals("")))
                    return;

                int textToInt = Integer.parseInt(timeTextView.getText().toString());

                if (textToInt < 0 || textToInt >= 60) {
                    Toast.makeText(getContext(), "입력을 시간 값으로 바꿀 수 없습니다", Toast.LENGTH_SHORT).show();
                    timeTextView.setText("");
                } else {
                    stage++;
                    minutes = textToInt;
                    alarmAddTimeViewModel.selectMinute(minutes);
                    linearLayoutSetTime.setVisibility(view.GONE);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MainFrameActivity.setOnBackPressedListener(this);
    }

    @Override
    public void onBackPressed() {
        MainFrameActivity.hideTopFragment();
    }

    @Override
    public void onStop() {
        super.onStop();
        alarmAddTimeViewModel.resetLiveData();
    }
}
