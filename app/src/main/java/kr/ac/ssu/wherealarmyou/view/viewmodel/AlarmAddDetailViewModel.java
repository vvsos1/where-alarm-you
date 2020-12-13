package kr.ac.ssu.wherealarmyou.view.viewmodel;

import android.widget.Switch;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AlarmAddDetailViewModel extends AlarmAddViewModel<Map<String, Integer>>
{
    private static final int TRUE  = 1;
    private static final int FALSE = 0;
    
    public AlarmAddDetailViewModel( )
    {
        setLiveData(new HashMap<>( ));
        selectSoundSwitch(Switch.Off);
        selectVibrationSwitch(Switch.Off);
        selectRepeatSwitch(Switch.Off);
    }
    
    public void selectSoundSwitch(Switch isSoundOn)
    {
        Map<String, Integer> map = getLiveData( ).getValue( );
        if(map == null){
            map = new HashMap<>();
        }
        if (isSoundOn.equals(Switch.On)) {
            map.put("sound", TRUE);
            setLiveData(map);
        }
        else {
            map.put("sound", FALSE);
            setLiveData(map);
        }
    }
    
    public void selectVibrationSwitch(Switch isVibrationOn)
    {
        Map<String, Integer> map = getLiveData( ).getValue( );
        if(map == null){
            map = new HashMap<>();
        }
        if (isVibrationOn.equals(Switch.On)) {
            map.put("vibration", TRUE);
            setLiveData(map);
        }
        else {
            map.put("vibration", FALSE);
            setLiveData(map);
        }
    }
    
    public void selectRepeatSwitch(Switch isRepeatOn)
    {
        Map<String, Integer> map = getLiveData( ).getValue( );
        if(map == null){
            map = new HashMap<>();
        }
        if (isRepeatOn.equals(Switch.On)) {
            
            map.put("repeat", TRUE);
            setLiveData(map);
        }
        else {
            map.put("repeat", FALSE);
            setLiveData(map);
        }
    }
    
    public void selectRepeatInterval(int interval)
    {
        Map<String, Integer> map = getLiveData( ).getValue( );
        if(map == null){
            map = new HashMap<>();
        }
        map.put("repeatInterval", interval);
        setLiveData(map);
    }
    
    public void selectRepeatCount(int count)
    {
        Map<String, Integer> map = getLiveData( ).getValue( );
        if(map == null){
            map = new HashMap<>();
        }
        map.put("repeatCount", count);
        setLiveData(map);
    }
    
    public enum Switch
    {Off, On}
}