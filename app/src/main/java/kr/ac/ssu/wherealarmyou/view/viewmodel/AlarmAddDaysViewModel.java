package kr.ac.ssu.wherealarmyou.view.viewmodel;

import java.util.List;
import java.util.Map;

import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.alarm.Period;

public class AlarmAddDaysViewModel extends AlarmAddViewModel<Map> {


    private List<Date> dates;

    private Period activePeriod;

    private Map<String, Boolean> daysOfWeek;




    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    public void setDaysOfWeek(Map<String, Boolean> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public void setActivePeriod(Period activePeriod) {
        this.activePeriod = activePeriod;
    }

    @Override
    public void setLiveData(Map liveData) {
        super.setLiveData(liveData);
    }

    public Period getActivePeriod() {
        return activePeriod;
    }

    public List<Date> getDates() {
        return dates;
    }

    public Map<String, Boolean> getDaysOfWeek() {
        return daysOfWeek;
    }

}
