package kr.ac.ssu.wherealarmyou.view.viewmodel;

import java.util.Map;

public class AlarmAddLocationViewModel extends AlarmAddViewModel<Map> {
    public void selectLocation(Map map) {
        setLiveData(map);
    }

    @Override
    public void setLiveData(Map liveData) {
        super.setLiveData(liveData);
    }
}
