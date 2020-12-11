package kr.ac.ssu.wherealarmyou.view.viewmodel;

import kr.ac.ssu.wherealarmyou.location.Location;

public class AlarmAddGroupViewModel extends AlarmAddViewModel<Location>
{
    public void selectLocation(Location location)
    {
        setLiveData(location);
    }
}
