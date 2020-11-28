package kr.ac.ssu.wherealarmyou.alarm.dto;

import java.util.List;
import java.util.Map;

import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.alarm.DatesAlarm;
import kr.ac.ssu.wherealarmyou.alarm.DaysAlarm;
import kr.ac.ssu.wherealarmyou.alarm.LocationCondition;
import kr.ac.ssu.wherealarmyou.alarm.Period;
import kr.ac.ssu.wherealarmyou.alarm.Repetition;
import kr.ac.ssu.wherealarmyou.alarm.Time;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import reactor.util.annotation.Nullable;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(builderMethodName = "privateBuilder")
public class AlarmSaveRequest {


    // 알람의 제목
    String title;

    // 알람의 설명 or 내용
    String description;

    // 알람이 울릴 시간
    Time time;

    @Nullable
    // 알람이 활성화될 장소의 조건
            LocationCondition locationCondition;

    @Nullable
    // 알람이 등록될 그룹
            String group;

    @Builder.Default
    // 소리
            Boolean sound = Boolean.TRUE;

    @Builder.Default
    // 진동
            Boolean vibe = Boolean.TRUE;

    // 반복
    Repetition repetition;

    @Nullable
    // 알람이 활성화될 기간
            Period activePeriod;

    @Nullable
    // 알람이 울릴 요일; Key: 요일
            Map<String, Boolean> daysOfWeek;

    @Nullable
    List<Date> dates;

    public static AlarmSaveRequestBuilder builder(Time time) {

        return privateBuilder().time(time)
                .sound(Boolean.TRUE).vibe(Boolean.TRUE)
                .repetition(new Repetition(1))
                .title("").description("");
    }

    // uid 값이 없는 알람 객체를 만들어 리턴
    public Alarm toAlarm() {
        if (this.daysOfWeek == null) {
            return DatesAlarm.builder()
                    .time(time)
                    .repetition(repetition)
                    .vibe(vibe)
                    .sound(sound)
                    .description(description)
                    .title(title)
                    .group(group)
                    .locationCondition(locationCondition)
                    .dates(dates)
                    .build();
        } else {
            return DaysAlarm.builder()
                    .time(time)
                    .repetition(repetition)
                    .vibe(vibe)
                    .sound(sound)
                    .description(description)
                    .title(title)
                    .group(group)
                    .locationCondition(locationCondition)
                    .activePeriod(activePeriod)
                    .daysOfWeek(daysOfWeek)
                    .build();
        }
    }
}
