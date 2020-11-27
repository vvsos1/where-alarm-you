package kr.ac.ssu.wherealarmyou.view.custom_view;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import kr.ac.ssu.wherealarmyou.alarm.Time;

import java.util.Objects;

public class AlarmAddTimeViewModel extends ViewModel
{
    private final MutableLiveData<Time> timeData = new MutableLiveData<>( );
    
    private final MutableLiveData<String> infoString = new MutableLiveData<>( );
    
    public void selectAmPm(String value)
    {
        if (value.equals("AM")) {
            timeData.setValue(new Time(0, 0));
        }
        if (value.equals("PM")) {
            timeData.setValue(new Time(12, 0));
        }
        infoString.setValue(value);
    }
    
    public void selectHours(int hours_)
    {
        Time time  = Objects.requireNonNull(timeData.getValue( ));
        int  hours = time.getHours( ) + hours_;
        timeData.setValue(new Time(hours, 0));
        
        infoString.setValue(infoString.getValue( ) + "  " + hours_);
    }
    
    public void selectMinute(int minute_)
    {
        Time time  = Objects.requireNonNull(timeData.getValue( ));
        int  hours = time.getHours( );
        timeData.setValue(new Time(hours, minute_));
        
        infoString.setValue(infoString.getValue( ) + "  " + minute_);
    }
    
    public LiveData<Time> getTimeData( )
    {
        return timeData;
    }
    
    public LiveData<String> getInfoString( )
    {
        return infoString;
    }
}
