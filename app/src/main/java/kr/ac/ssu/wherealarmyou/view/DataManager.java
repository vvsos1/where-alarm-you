package kr.ac.ssu.wherealarmyou.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.group.service.GroupService;
import kr.ac.ssu.wherealarmyou.location.Location;
import kr.ac.ssu.wherealarmyou.location.service.LocationService;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataManager
{
    public static DataManager instance;
    
    private final MutableLiveData<List<Location>> locationMutableLiveData = new MutableLiveData<>(new ArrayList<>( ));
    
    private final MutableLiveData<List<Group>> groupMutableLiveData = new MutableLiveData<>(new ArrayList<>( ));
    
    public static DataManager getInstance( )
    {
        if (instance == null) {
            instance = new DataManager( );
            instance.updateGroupLiveData( );
            instance.updateLocationLiveData( );
        }
        return instance;
    }
    
    public void addGroupLiveData(Group group)
    {
        List<Group> groups = Objects.requireNonNull(groupMutableLiveData.getValue( ));
        groups.add(group);
        groupMutableLiveData.setValue(groups);
    }
    
    public LiveData<List<Group>> getGroupLiveData( )
    {
        return groupMutableLiveData;
    }
    
    public void setGroupLiveData(List<Group> groups)
    {
        groupMutableLiveData.setValue(groups);
    }
    
    public void updateGroupLiveData( )
    {
        groupMutableLiveData.setValue(new ArrayList<>( ));
        GroupService groupService = GroupService.getInstance( );
        groupService.getJoinedGroup( )
                    .doOnNext(instance::addGroupLiveData)
                    .publishOn(Schedulers.elastic( ))
                    .subscribeOn(Schedulers.elastic( ))
                    .subscribe( );
    }
    
    public void updateLocationLiveData( )
    {
        locationMutableLiveData.setValue(new ArrayList<>( ));
        LocationService locationService = LocationService.getInstance(MainFrameActivity.frameBottom.getContext( ));
        
       /*
        locationService.getLocation( )
                       .doOnNext(instance::addGroupLiveData)
                       .publishOn(Schedulers.elastic( ))
                       .subscribeOn(Schedulers.elastic( ))
                       .subscribe( );
                    
        */
    }
    
    public LiveData<List<Location>> getLocationLiveData( )
    {
        return locationMutableLiveData;
    }
}
