package kr.ac.ssu.wherealarmyou.view.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.alarm.Period;

public class AlarmAddDaysViewModel extends AlarmAddViewModel<Integer> {


    private List<Date> dates;

    private Period activePeriod;

    private Map<String, Boolean> daysOfWeek;


    public void setNecessary(List<Date> dates, Period activePeriod, Map<String, Boolean> daysOfWeek) {
        this.dates = dates;
        this.activePeriod = activePeriod;
        this.daysOfWeek = daysOfWeek;
        if (this.dates == null) {
            this.dates = new ArrayList<>();
        }

        if (this.daysOfWeek == null) {
            this.daysOfWeek = new HashMap<>();
        }
    }

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
    public void setLiveData(Integer liveData) {
        super.setLiveData(liveData);
    }
}
