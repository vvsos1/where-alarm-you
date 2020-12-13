package kr.ac.ssu.wherealarmyou.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.time.LocalDate;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.alarm.Period;

public class SetActivePeriodDialog extends Dialog implements View.OnClickListener {

    Period activePeriod;
    TextView startBtn;
    TextView endBtn;
    TextView clickView;
    TextView cancelBtn;
    TextView acceptBtn;
    ActivePeriodClickListener activePeriodClickListener;
    private Context context;

    public SetActivePeriodDialog(@NonNull Context context, ActivePeriodClickListener activePeriodClickListener) {
        super(context);
        this.context = context;
        this.activePeriodClickListener = activePeriodClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //todo : 일단 초기화 나중에 입력받을 것
        activePeriod = new Period();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_active_period);
        startBtn = findViewById(R.id.dialog_set_start);
        startBtn.setOnClickListener(this::onClick);
        endBtn = findViewById(R.id.dialog_set_end);
        endBtn.setOnClickListener(this::onClick);
        acceptBtn = findViewById(R.id.dialog_days_accept_button);
        acceptBtn.setOnClickListener(v -> {
            this.activePeriodClickListener.acceptPeriod(activePeriod);
            dismiss();
        });
        cancelBtn = findViewById(R.id.dialog_days_cancel_button);
        cancelBtn.setOnClickListener(v -> {
            dismiss();
        });

    }

    @Override
    public void onClick(View v) {
        LocalDate now = LocalDate.now();

        clickView = (TextView) v;

        DatePickerDialog dateDialog = new DatePickerDialog(getContext(), (DatePickerDialog.OnDateSetListener) (view, year, month, dayOfMonth) -> {


            Toast.makeText(getContext(), year + "-" + (month + 1) + "-" + dayOfMonth, Toast.LENGTH_SHORT).show();
            String string = year + "년 " + (month + 1) + "월 "  + dayOfMonth + "일 ";

            if (clickView == startBtn) {
                activePeriod = new Period(new Date(dayOfMonth, (month + 1), year), activePeriod.getEnd());
                startBtn.setText("시작 날짜 : " + string);

            } else if (clickView == endBtn) {
                activePeriod = new Period(activePeriod.getStart(), new Date(dayOfMonth, (month + 1), year));
                endBtn.setText("종료 날짜 : " + string);

            }

        }, now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());

        dateDialog.show();


    }


}
