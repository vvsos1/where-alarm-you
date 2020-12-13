package kr.ac.ssu.wherealarmyou.view.fragment.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.fragment.OnBackPressedListener;
import kr.ac.ssu.wherealarmyou.view.viewmodel.AlarmAddDetailViewModel;

public class AlarmAddDetailFragment extends Fragment implements OnBackPressedListener {


    private AlarmAddDetailViewModel alarmAddDetailViewModel;

    public static AlarmAddDetailFragment getInstance() {
        return new AlarmAddDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.content_alarm_add_detail, container, false);
        alarmAddDetailViewModel = new ViewModelProvider(requireActivity()).get(AlarmAddDetailViewModel.class);


        return contentView;
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
        alarmAddDetailViewModel.reset();
    }
}
