package kr.ac.ssu.wherealarmyou.view.viewmodel;

import java.util.Objects;

import kr.ac.ssu.wherealarmyou.alarm.Time;

public class AlarmAddTimeViewModel extends AlarmAddViewModel<Time> {

    public void selectAmPm(boolean isPM) {

        if (isPM) {
            setLiveData(new Time(12, null));
            setInfoString("오후");
        } else {
            setLiveData(new Time(0, null));
            setInfoString("오전");
        }
    }
    
    public void selectHours(int hours_)
    {
        Time time = Objects.requireNonNull(getLiveData().getValue());
        int hours = time.getHours() + hours_;
        setLiveData(new Time(hours, null));

        setInfoString(getInfoString().getValue() + "  " + hours_);
    }
    
    public void selectMinute(int minute) {
        Time time = Objects.requireNonNull(getLiveData().getValue());
        int hours = time.getHours();
        setLiveData(new Time(hours, minute));

        setInfoString(getInfoString().getValue() + "  " + minute);
        complete();
    }
}
