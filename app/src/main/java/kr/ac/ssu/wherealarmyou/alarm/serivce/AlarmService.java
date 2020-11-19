package kr.ac.ssu.wherealarmyou.alarm.serivce;

import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.AlarmRepository;
import kr.ac.ssu.wherealarmyou.alarm.dto.AlarmModifyRequest;
import kr.ac.ssu.wherealarmyou.alarm.dto.AlarmSaveRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AlarmService {
    private static AlarmService instance;

    private AlarmRepository alarmRepository;

    private AlarmService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    public static AlarmService getInstance() {
        if (instance == null)
            instance = new AlarmService(AlarmRepository.getInstance());
        return instance;
    }

    // 알람을 Alarm Manager에 등록
    public Mono<Alarm> register(Alarm alarm) {
        return null;
    }

    // 알람을 Alarm Manager에서 삭제
    public Mono<Void> unregister(Alarm alarm) {
        return null;
    }

    // 알람을 Realtime Database에 저장
    public Mono<Alarm> save(AlarmSaveRequest request) {
        return null;
    }

    // 알람을 Realtime Database에서 수정
    public Mono<Alarm> modify(AlarmModifyRequest request) {
        return null;
    }

    // 알람을 Realtime Database에서 삭제
    public Mono<Void> delete(Alarm alarm) {
        return null;
    }


    public Flux<Alarm> getGroupAlarmFlux(String groupUid) {
        return null;

    }
}
