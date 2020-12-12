package kr.ac.ssu.wherealarmyou.view.fragment.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.fragment.OnBackPressedListener;
import kr.ac.ssu.wherealarmyou.view.viewmodel.AlarmAddMemoViewModel;

public class AlarmAddMemoFragment extends Fragment implements OnBackPressedListener {


    EditText title;
    EditText description;
    Button saveBtn;
    AlarmAddMemoViewModel alarmAddMemoViewModel;

    public static AlarmAddMemoFragment getInstance() {
        return new AlarmAddMemoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.content_alarm_add_memo, container, false);
        alarmAddMemoViewModel = new ViewModelProvider(requireActivity()).get(AlarmAddMemoViewModel.class);

        title = contentView.findViewById(R.id.edit_text_title);
        title.setText("");
        description = contentView.findViewById(R.id.edit_text_description);
        description.setText("");
        saveBtn = contentView.findViewById(R.id.button_save_memo);
        saveBtn.setOnClickListener(v -> {


            alarmAddMemoViewModel.setInfoString(title.getText().toString());
            alarmAddMemoViewModel.setLiveData(description.getText().toString());
            alarmAddMemoViewModel.complete();
        });


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainFrameActivity.setOnBackPressedListener(this);
    }

    @Override
    public void onBackPressed() {
        MainFrameActivity.backTopFragment();
    }

    @Override
    public void onStop() {
        super.onStop();
        alarmAddMemoViewModel.reset();
    }
}
