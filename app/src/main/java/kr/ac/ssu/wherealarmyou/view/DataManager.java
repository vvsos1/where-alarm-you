package kr.ac.ssu.wherealarmyou.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.AlarmRepository;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.group.service.GroupService;
import kr.ac.ssu.wherealarmyou.location.Location;
import kr.ac.ssu.wherealarmyou.location.LocationRepository;
import kr.ac.ssu.wherealarmyou.user.User;
import kr.ac.ssu.wherealarmyou.user.UserRepository;
import kr.ac.ssu.wherealarmyou.user.service.UserService;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataManager
{
    public static DataManager instance;
    
    private final MutableLiveData<Boolean> observable = new MutableLiveData<>( );
    
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
            instance.observable.setValue(Boolean.TRUE);
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
                      .doOnComplete(this::resumeObserve)
                      .publishOn(Schedulers.elastic( ))
                      .subscribeOn(Schedulers.elastic( ))
                      .subscribe( );
    }
    
    public void updateGroupLiveData( )
    {
        groupMutableLiveData.setValue(new ArrayList<>( ));
        GroupService groupService = GroupService.getInstance( );
        groupService.getJoinedGroup( )
                    .doOnNext(instance::addGroupLiveData)
                    .doOnComplete(this::pauseObserve)
                    .publishOn(Schedulers.elastic( ))
                    .subscribeOn(Schedulers.elastic( ))
                    .subscribe( );
    }
    
    public void updateLocationLiveData( )
    {
        locationMutableLiveData.setValue(new ArrayList<>( ));
        
        // TODO : context 없이 LocationService 인스턴스 받기
        // LocationService locationService = LocationService.getInstance(null);
        
        // TODO : 삭제 (테스트용 코드)
        String             currentUserUid     = UserService.getInstance( ).getCurrentUserUid( );
        UserRepository     userRepository     = UserRepository.getInstance( );
        LocationRepository locationRepository = LocationRepository.getInstance( );
        
        userRepository.findUserByUid(currentUserUid)
                      .map(User::getUserLocations)
                      .flatMapIterable(Map::keySet)
                      .flatMap(locationRepository::findByUid)
                      .doOnNext(location -> instance.addLocationLiveData(location))
                      .doOnComplete(this::resumeObserve)
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
    
    public void addGroupLiveData(Group group)
    {
        List<Group> groups = Objects.requireNonNull(groupMutableLiveData.getValue( ));
        groups.add(group);
        groupMutableLiveData.setValue(groups);
    }
    
    public void addLocationLiveData(Location location)
    {
        List<Location> locations = Objects.requireNonNull(getLocationData( ).getValue( ));
        locations.add(location);
        locationMutableLiveData.setValue(locations);
    }
    
    public LiveData<List<Alarm>> getAlarmData( )
    {
        return alarmMutableLiveData;
    }
    
    public LiveData<List<Group>> getGroupData( )
    {
        return groupMutableLiveData;
    }
    
    public LiveData<List<Location>> getLocationData( )
    {
        return locationMutableLiveData;
    }
    
    public LiveData<Boolean> isObservable( )
    {
        return observable;
    }
    
    public void resumeObserve( )
    {
        observable.setValue(Boolean.TRUE);
    }
    
    public void pauseObserve( )
    {
        observable.setValue(Boolean.FALSE);
    }
    
    public void setGroupLiveData(List<Group> groups)
    {
        groupMutableLiveData.setValue(groups);
    }
}