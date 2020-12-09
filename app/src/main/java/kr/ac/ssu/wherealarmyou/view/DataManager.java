package kr.ac.ssu.wherealarmyou.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.AlarmRepository;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.group.service.GroupService;
import kr.ac.ssu.wherealarmyou.location.Location;
import kr.ac.ssu.wherealarmyou.user.User;
import kr.ac.ssu.wherealarmyou.user.UserRepository;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataManager
{
    public static DataManager instance;
    
    private final MutableLiveData<List<Alarm>>    alarmMutableLiveData    = new MutableLiveData<>(new ArrayList<>( ));
    private final MutableLiveData<List<Location>> locationMutableLiveData = new MutableLiveData<>(new ArrayList<>( ));
    private final MutableLiveData<List<Group>>    groupMutableLiveData    = new MutableLiveData<>(new ArrayList<>( ));
    
    public static DataManager getInstance( )
    {
        if (instance == null) {
            instance = new DataManager( );
            instance.updateAlarmLiveData( );
            instance.updateGroupLiveData( );
            instance.updateLocationLiveData( );
        }
        return instance;
    }
    
    public void updateAlarmLiveData( )
    {
        alarmMutableLiveData.setValue(new ArrayList<>( ));
        
        // TODO : context 없이 alarmService 호출
//        AlarmService alarmService = AlarmService.getInstance( );
        
        // TODO : 테스트용 코드 (alarmService 생성할때 context 전달 불가능)
        AlarmRepository alarmRepository = AlarmRepository.getInstance( );
        String          currentUserUid  = FirebaseAuth.getInstance( ).getUid( );
        
        UserRepository userRepository = UserRepository.getInstance( );
        userRepository.findUserByUid(currentUserUid)
                      .map(User::getAlarms)
                      .flatMapIterable(Map::keySet)
                      .flatMap(alarmRepository::getAlarmByUid)
                      .doOnNext(alarm -> instance.addAlarmLiveData(alarm))
                      .publishOn(Schedulers.elastic( ))
                      .subscribeOn(Schedulers.elastic( ))
                      .subscribe( );
    }
    
    public void addAlarmLiveData(Alarm alarm)
    {
        List<Alarm> alarms = Objects.requireNonNull(alarmMutableLiveData.getValue( ));
        alarms.add(alarm);
        alarmMutableLiveData.setValue(alarms);
    }
    
    public LiveData<List<Alarm>> getAlarmData( )
    {
        return alarmMutableLiveData;
    }
    
    public void addGroupLiveData(Group group)
    {
        List<Group> groups = Objects.requireNonNull(groupMutableLiveData.getValue( ));
        groups.add(group);
        groupMutableLiveData.setValue(groups);
    }
    
    public LiveData<List<Group>> getGroupData( )
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
        // LocationService locationService = LocationService.getInstance(null);
        
        /*
        locationService.getLocation( )
                       .doOnNext(instance::addGroupLiveData)
                       .publishOn(Schedulers.elastic( ))
                       .subscribeOn(Schedulers.elastic( ))
                       .subscribe( );
        */
    }
    
    public LiveData<List<Location>> getLocationData( )
    {
        return locationMutableLiveData;
    }
}
