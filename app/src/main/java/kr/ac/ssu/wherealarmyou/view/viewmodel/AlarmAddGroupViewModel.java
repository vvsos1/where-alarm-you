package kr.ac.ssu.wherealarmyou.view.viewmodel;

import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.location.Location;

public class AlarmAddGroupViewModel extends AlarmAddViewModel<Group>
{
    public void selectGroup(Group group)
    {
        setLiveData(group);
    }
}
