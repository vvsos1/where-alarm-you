package kr.ac.ssu.wherealarmyou.alarm.dto;

import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.LocationCondition;
import kr.ac.ssu.wherealarmyou.alarm.Repetition;
import kr.ac.ssu.wherealarmyou.alarm.Time;

public class AlarmSaveRequest {


    // 알람의 제목
    String title;

    // 알람의 설명 or 내용
    String description;

    // 알람이 울릴 시간
    Time time;

    // 알람이 활성화될 장소의 조건
    LocationCondition locationCondition;

    // 알람이 등록될 그룹
    String group;

    // 소리
    Boolean sound;

    // 진동
    Boolean vibe;

    // 반복
    Repetition repetition;

    // uid 값이 없는 알람 객체를 만들어 리턴
    public Alarm toAlarm() {
        return null;
    }

}
