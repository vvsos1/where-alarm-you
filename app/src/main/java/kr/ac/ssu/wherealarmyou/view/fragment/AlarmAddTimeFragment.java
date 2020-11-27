package kr.ac.ssu.wherealarmyou.view.fragment;

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
    
    private LinearLayout layoutSetAMPM;
    private LinearLayout layoutSetHours;
    private LinearLayout layoutSetMinutes;
    
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
        
        layoutSetAMPM    = contentView.findViewById(R.id.layoutSetAMPM);
        layoutSetHours   = contentView.findViewById(R.id.layoutSetHours);
        layoutSetMinutes = contentView.findViewById(R.id.layoutSetMinutes);
        
        editTextHours   = contentView.findViewById(R.id.editTextHours);
        editTextMinutes = contentView.findViewById(R.id.editTextMinutes);
        
        buttonAm = contentView.findViewById(R.id.buttonAM);
        buttonPm = contentView.findViewById(R.id.buttonPM);
        
        buttonAm.setOnClickListener(this);
        buttonPm.setOnClickListener(this);
        
        editTextHours.setOnClickListener(this);
        editTextMinutes.setOnClickListener(this);
        return contentView;
    }
    
    @Override
    public void onClick(View view)
    {
        if (layoutSetAMPM.getVisibility( ) == View.VISIBLE) {
            if (view == buttonAm) {
                alarmAddTimeViewModel.selectAmPm("AM");
            }
            if (view == buttonPm) {
                alarmAddTimeViewModel.selectAmPm("PM");
            }
            layoutSetAMPM.setVisibility(View.GONE);
            layoutSetHours.setVisibility(View.VISIBLE);
        }
        else if (layoutSetHours.getVisibility( ) == View.VISIBLE) {
            int hours = Integer.parseInt("" + editTextHours.getText( ));
            alarmAddTimeViewModel.selectHours(hours);
            layoutSetHours.setVisibility(View.GONE);
            layoutSetMinutes.setVisibility(View.VISIBLE);
        }
        else if (layoutSetMinutes.getVisibility( ) == View.VISIBLE) {
            int minute = Integer.parseInt("" + editTextMinutes.getText( ));
            alarmAddTimeViewModel.selectMinute(minute);
            layoutSetMinutes.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onStop( )
    {
        super.onStop( );
        alarmAddTimeViewModel.selectAmPm("");
    }
}
