package kr.ac.ssu.wherealarmyou.view.fragment.alarm;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.fragment.OnBackPressedListener;
import kr.ac.ssu.wherealarmyou.view.viewmodel.AlarmAddDetailViewModel;

public class AlarmAddDetailFragment extends Fragment implements OnBackPressedListener
{
    private AlarmAddDetailViewModel alarmAddDetailViewModel;
    
    public static AlarmAddDetailFragment getInstance( )
    {
        return new AlarmAddDetailFragment( );
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View contentView = inflater.inflate(R.layout.content_alarm_add_detail, container, false);
        
        alarmAddDetailViewModel = new ViewModelProvider(requireActivity( )).get(AlarmAddDetailViewModel.class);
        
        EditText editTextCount    = contentView.findViewById(R.id.alarmAddDetail_editTextCount);
        EditText editTextInterval = contentView.findViewById(R.id.alarmAddDetail_editTextInterval);
        
        // 알람 소리 스위치
        Switch switchSound = contentView.findViewById(R.id.alarmDetail_switchSound);
        switchSound.setChecked(false);
        switchSound.setOnCheckedChangeListener((buttonView, isOn) -> {
            if (isOn) {
                alarmAddDetailViewModel.selectSoundSwitch(AlarmAddDetailViewModel.Switch.On);
            }
            else {
                alarmAddDetailViewModel.selectSoundSwitch(AlarmAddDetailViewModel.Switch.Off);
            }
        });
        
        // 알람 진동 스위치
        Switch switchVibration = contentView.findViewById(R.id.alarmAddDetail_switchVibration);
        switchVibration.setChecked(false);
        switchVibration.setOnCheckedChangeListener((buttonView, isOn) -> {
            if (isOn) {
                alarmAddDetailViewModel.selectVibrationSwitch(AlarmAddDetailViewModel.Switch.On);
            }
            else {
                alarmAddDetailViewModel.selectVibrationSwitch(AlarmAddDetailViewModel.Switch.Off);
            }
        });
        
        // 알람 반복 스위치
        Switch switchRepeat = contentView.findViewById(R.id.alarmAddDetail_switchRepeat);
        switchRepeat.setChecked(false);
        switchRepeat.setOnCheckedChangeListener((buttonView, isOn) -> {
            if (isOn) {
                alarmAddDetailViewModel.selectRepeatSwitch(AlarmAddDetailViewModel.Switch.On);
                editTextCount.setVisibility(View.VISIBLE);
                editTextInterval.setVisibility(View.VISIBLE);
            }
            else {
                alarmAddDetailViewModel.selectRepeatSwitch(AlarmAddDetailViewModel.Switch.Off);
                editTextCount.setVisibility(View.GONE);
                editTextInterval.setVisibility(View.GONE);
            }
        });
        
        // 알람 반복 - 횟수
        editTextCount.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction( ) == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (editTextCount.getText( ).toString( ).trim( ).isEmpty( )) { return false; }
                int count = Integer.parseInt(editTextCount.getText( ).toString( ).trim( ));
                alarmAddDetailViewModel.selectRepeatCount(count);
    
                if (editTextInterval.getText( ).toString( ).trim( ).isEmpty( )) { return false; }
                int interval = Integer.parseInt(editTextInterval.getText( ).toString( ).trim( ));
                alarmAddDetailViewModel.selectRepeatInterval(interval);
                return true;
            }
            return false;
        });
        
        // 알람 반복 - 간격
        editTextInterval.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction( ) == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (editTextCount.getText( ).toString( ).trim( ).isEmpty( )) { return false; }
                int count = Integer.parseInt(editTextCount.getText( ).toString( ).trim( ));
                alarmAddDetailViewModel.selectRepeatCount(count);
                
                if (editTextInterval.getText( ).toString( ).trim( ).isEmpty( )) { return false; }
                int interval = Integer.parseInt(editTextInterval.getText( ).toString( ).trim( ));
                alarmAddDetailViewModel.selectRepeatInterval(interval);
                return true;
            }
            return false;
        });
        
        return contentView;
    }
    
    @Override
    public void onResume( )
    {
        super.onResume( );
        MainFrameActivity.setOnBackPressedListener(this);
    }
    
    @Override
    public void onBackPressed( )
    {
        MainFrameActivity.hideTopFragment( );
    }
    
    @Override
    public void onStop( )
    {
        super.onStop( );
        alarmAddDetailViewModel.reset( );
    }
}
