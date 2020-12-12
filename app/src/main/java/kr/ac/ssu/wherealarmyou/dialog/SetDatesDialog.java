package kr.ac.ssu.wherealarmyou.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.view.adapter.DatesItemAdapter;

public class SetDatesDialog extends Dialog {


    TextView addDateBtn;
    RecyclerView recyclerView;
    DatesItemAdapter adapter;
    List<Date> dates;
    LinearLayoutManager layoutManager;
    TextView cancelBtn;
    TextView acceptBtn;
    DatesClickListener datesClickListener;
    int position;

    public SetDatesDialog(@NonNull Context context, DatesClickListener datesClickListener) {
        super(context);
        this.datesClickListener = datesClickListener;
        //todo : 일단 초기화 나중에 입력받을 것
        dates = new ArrayList<>();
        setContentView(R.layout.dialog_dates);
        recyclerView = findViewById(R.id.dialog_dates_recyclerview);
        adapter = new DatesItemAdapter(dates);

        adapter.setOnItemClickListener(new DatesItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Date date, int position) {
                modify(date, position);
            }

            @Override
            public void onCancelClick(int position) {
                dates.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        cancelBtn = findViewById(R.id.dialog_dates_cancel_button);
        cancelBtn.setOnClickListener(v -> {
            dismiss();
        });
        acceptBtn = findViewById(R.id.dialog_dates_accept_button);
        acceptBtn.setOnClickListener(v -> {
            Toast.makeText(getContext(), this.dates.toString(), Toast.LENGTH_SHORT).show();
            this.datesClickListener.acceptDates(this.dates);
            dismiss();
        });

        addDateBtn = findViewById(R.id.button_dates_add);
        addDateBtn.setOnClickListener(v -> {


            LocalDate now = LocalDate.now();

            DatePickerDialog dateDialog = new DatePickerDialog(getContext(), (DatePickerDialog.OnDateSetListener) (view, year, month, dayOfMonth) -> {


                boolean flag = true;

                Date date = new Date(dayOfMonth, month + 1, year);

                for (Date tmpDate : dates) {
                    if (tmpDate.toString().equals(date.toString())) {
                        flag = false;
                        break;
                    }
                }

                if (flag) {
                    dates.add(date);
                    adapter.notifyItemInserted(adapter.getItemCount());
                } else {
                    Toast.makeText(getContext(), "선택하지 않은 날짜만 선택할 수 있습니다", Toast.LENGTH_SHORT).show();
                }


            }, now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());

            dateDialog.show();

        });


    }


    void modify(Date selectDate, int position) {


        DatePickerDialog dateDialog = new DatePickerDialog(getContext(), (DatePickerDialog.OnDateSetListener) (view, year, month, dayOfMonth) -> {


            boolean flag = true;

            Date date = new Date(dayOfMonth, month + 1, year);

            for (Date tmpDate : dates) {
                if (tmpDate.toString().equals(date.toString())) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                dates.set(position, date);
                adapter.notifyItemChanged(position);
            } else {
                Toast.makeText(getContext(), "이미 있는 날짜입니다.", Toast.LENGTH_SHORT).show();
            }


        }, selectDate.getYear(), selectDate.getMonth() - 1, selectDate.getDay());

        dateDialog.show();

    }


}
