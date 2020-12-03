package kr.ac.ssu.wherealarmyou.view.model;

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
            timeData.setValue(new Time(0, null));
            infoString.setValue("오전");
        }
        if (value.equals("PM")) {
            timeData.setValue(new Time(12, null));
            infoString.setValue("오후");
        }
    }
    
    public void selectHours(int hours_)
    {
        Time time  = Objects.requireNonNull(timeData.getValue( ));
        int  hours = time.getHours( ) + hours_;
        timeData.setValue(new Time(hours, null));
        
        infoString.setValue(infoString.getValue( ) + "  " + hours_);
    }
    
    public void selectMinute(int minute)
    {
        Time time  = Objects.requireNonNull(timeData.getValue( ));
        int  hours = time.getHours( );
        timeData.setValue(new Time(hours, minute));
        
        infoString.setValue(infoString.getValue( ) + "  " + minute);
    }
    
    public void resetLiveData( )
    {
        timeData.setValue(null);
        infoString.setValue(null);
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
