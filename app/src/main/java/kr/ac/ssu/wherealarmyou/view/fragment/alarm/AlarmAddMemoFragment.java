package kr.ac.ssu.wherealarmyou.view.fragment.alarm;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;
import java.util.Map;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.fragment.OnBackPressedListener;
import kr.ac.ssu.wherealarmyou.view.viewmodel.AlarmAddMemoViewModel;

public class AlarmAddMemoFragment extends Fragment implements OnBackPressedListener, View.OnKeyListener {


    EditText title;
    EditText description;
    Button saveBtn;
    Map<String, String> live;
    AlarmAddMemoViewModel alarmAddMemoViewModel;

    public static AlarmAddMemoFragment getInstance() {
        return new AlarmAddMemoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.content_alarm_add_memo, container, false);
        alarmAddMemoViewModel = new ViewModelProvider(requireActivity()).get(AlarmAddMemoViewModel.class);
        live = new HashMap<>();

        title = contentView.findViewById(R.id.edit_text_title);
        title.setText("");
        title.setOnKeyListener(this::onKey);
        description = contentView.findViewById(R.id.edit_text_description);
        description.setText("");
        description.setOnKeyListener(this::onKey);

        return contentView;
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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            if (v == title) {
                live.put("title", title.getText().toString());
                alarmAddMemoViewModel.setLiveData(new HashMap(live));
            } else if (v == description) {
                live.put("description", description.getText().toString());
                alarmAddMemoViewModel.setLiveData(new HashMap(live));
                alarmAddMemoViewModel.onComplete();
            }
            return true;
        }
        return false;
    }
}
