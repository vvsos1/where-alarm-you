package kr.ac.ssu.wherealarmyou.view.fragment.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.view.custom_view.AlarmAddTimeViewModel;

public class AlarmAddTimeFragment extends Fragment implements View.OnClickListener
{
    private AlarmAddTimeViewModel alarmAddTimeViewModel;
    
    private LinearLayout linearLayoutSetAMPM;
    private LinearLayout linearLayoutSetHours;
    private LinearLayout linearLayoutSetMinutes;
    
    private Button buttonAm;
    private Button buttonPm;
    
    // test
    private EditText editTextHours;
    private EditText editTextMinutes;
    
    public static AlarmAddTimeFragment getInstance( )
    {
        return new AlarmAddTimeFragment( );
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View contentView = inflater.inflate(R.layout.content_alarm_add_time, container, false);
        
        alarmAddTimeViewModel = new ViewModelProvider(requireActivity( )).get(AlarmAddTimeViewModel.class);
        
        linearLayoutSetAMPM    = contentView.findViewById(R.id.alarmAddTime_linearLayoutSetAMPM);
        linearLayoutSetHours   = contentView.findViewById(R.id.alarmAddTime_linearLayoutSetHours);
        linearLayoutSetMinutes = contentView.findViewById(R.id.alarmAddTime_linearLayoutSetMinutes);
        
        editTextHours   = contentView.findViewById(R.id.alarmAddTime_editTextHours);
        editTextMinutes = contentView.findViewById(R.id.alarmAddTime_editTextMinutes);
        
        buttonAm = contentView.findViewById(R.id.alarmAddTime_buttonAM);
        buttonPm = contentView.findViewById(R.id.alarmAddTime_buttonPM);
        
        buttonAm.setOnClickListener(this);
        buttonPm.setOnClickListener(this);
        
        editTextHours.setOnClickListener(this);
        editTextMinutes.setOnClickListener(this);
        return contentView;
    }
    
    @Override
    public void onClick(View view)
    {
        if (linearLayoutSetAMPM.getVisibility( ) == View.VISIBLE) {
            if (view == buttonAm) {
                alarmAddTimeViewModel.selectAmPm("AM");
            }
            if (view == buttonPm) {
                alarmAddTimeViewModel.selectAmPm("PM");
            }
            linearLayoutSetAMPM.setVisibility(View.GONE);
            linearLayoutSetHours.setVisibility(View.VISIBLE);
        }
        else if (linearLayoutSetHours.getVisibility( ) == View.VISIBLE) {
            int hours = Integer.parseInt("" + editTextHours.getText( ));
            alarmAddTimeViewModel.selectHours(hours);
            linearLayoutSetHours.setVisibility(View.GONE);
            linearLayoutSetMinutes.setVisibility(View.VISIBLE);
        }
        else if (linearLayoutSetMinutes.getVisibility( ) == View.VISIBLE) {
            int minute = Integer.parseInt("" + editTextMinutes.getText( ));
            alarmAddTimeViewModel.selectMinute(minute);
            linearLayoutSetMinutes.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onStop( )
    {
        super.onStop( );
        alarmAddTimeViewModel.resetLiveData( );
    }
}
