package kr.ac.ssu.wherealarmyou.view.viewmodel;

import kr.ac.ssu.wherealarmyou.location.Location;

public class AlarmAddLocationViewModel extends AlarmAddViewModel<Location>
{
    public void selectGroup(Location location)
    {
        setLiveData(location);
    }
}
