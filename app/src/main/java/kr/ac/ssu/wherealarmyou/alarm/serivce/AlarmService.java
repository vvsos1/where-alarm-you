package kr.ac.ssu.wherealarmyou.alarm.serivce;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.AlarmRepository;
import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.alarm.DatesAlarm;
import kr.ac.ssu.wherealarmyou.alarm.DaysAlarm;
import kr.ac.ssu.wherealarmyou.alarm.Time;
import kr.ac.ssu.wherealarmyou.alarm.component.AlarmNotifyReceiver;
import kr.ac.ssu.wherealarmyou.alarm.dto.AlarmModifyRequest;
import kr.ac.ssu.wherealarmyou.alarm.dto.AlarmSaveRequest;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.group.GroupRepository;
import kr.ac.ssu.wherealarmyou.group.service.GroupService;
import kr.ac.ssu.wherealarmyou.user.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class AlarmService {
    private static final String TAG = "AlarmService";

    private static AlarmService instance;
    private final GroupService groupService;
    private UserService userService;

    private AlarmManager alarmManager;
    private Context context;

    private AlarmRepository alarmRepository;
    private GroupRepository groupRepository;


    private AlarmService(AlarmRepository alarmRepository, GroupRepository groupRepository, UserService userService, GroupService groupService, Context context) {
        this.userService = userService;
        this.groupService = groupService;
        this.alarmRepository = alarmRepository;
        this.groupRepository = groupRepository;

        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }


    public static AlarmService getInstance(Context context) {
        if (instance == null)
            instance = new AlarmService(
                    AlarmRepository.getInstance(),
                    GroupRepository.getInstance(),
                    UserService.getInstance(),
                    GroupService.getInstance(),
                    context);
        instance.setContext(context);
        return instance;
    }

    public void subscribeGroupAlarm(Set<String> groupUids) {
        Log.d(TAG, "subscribeGroupAlarm" + groupUids.toString());
        groupUids.forEach(groupUid -> FirebaseMessaging.getInstance().subscribeToTopic(groupUid).addOnSuccessListener(aVoid -> Log.d(TAG, groupUid + "구독 완료")));
    }

    public void setContext(Context context) {
        this.context = context;
    }

    // 알람을 Alarm Manager에 등록
    public Mono<Alarm> register(Alarm alarm) {
        return Mono.create(alarmMonoSink -> {

            Time time = alarm.getTime();
            ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
            Bundle bundle = new Bundle();
            bundle.putSerializable("Alarm", alarm);

            if (alarm instanceof DatesAlarm) {
                List<Date> dates = ((DatesAlarm) alarm).getDates();

                for (Date date : dates) {
                    Long rtcTime;
                    ZonedDateTime zonedDateTime = Year.of(date.getYear())
                            .atMonth(date.getMonth())
                            .atDay(date.getDay())
                            .atTime(time.getHours(), time.getMinutes()).atZone(ZoneId.of("Asia/Seoul"));
                    if (zonedDateTime.isAfter(currentTime)) {
                        rtcTime = zonedDateTime
                                .toInstant().toEpochMilli();
                        int requestCode = (alarm.getUid() + date.toString()).hashCode();
                        Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
                        if (alarm.getRepetition() == null) {
                            Log.d("Alarm", "null");
                        }
                        Log.d("Alarm", alarm.getRepetition().getRepeatCount().toString());
                        toAlarm.putExtra("Bundle", bundle)
                                .putExtra("RequestCode", requestCode)
                                .putExtra("RepeatCount", alarm.getRepetition().getRepeatCount());
                        PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                                requestCode, toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, rtcTime, toAlarmPendingIntent);
                    }
                }
            } else if (alarm instanceof DaysAlarm) {

                boolean flag1 = true;
                boolean flag2 = true;

                if (((DaysAlarm) alarm).getActivePeriod().getStart() != null) {
                    Date startDate = ((DaysAlarm) alarm).getActivePeriod().getStart();
                    ZonedDateTime startZonedDate = Year.of(startDate.getYear())
                            .atMonth(startDate.getMonth())
                            .atDay(startDate.getDay())
                            .atTime(time.getHours(), time.getMinutes())
                            .atZone(ZoneId.of("Asia/Seoul"));
                    flag1 = currentTime.isAfter(startZonedDate);
                }

                if (((DaysAlarm) alarm).getActivePeriod().getEnd() != null) {

                    Date endDate = ((DaysAlarm) alarm).getActivePeriod().getEnd();
                    ZonedDateTime endZonedDateTime = Year.of(endDate.getYear())
                            .atMonth(endDate.getMonth())
                            .atDay(endDate.getDay())
                            .atTime(time.getHours(), time.getMinutes())
                            .atZone(ZoneId.of("Asia/Seoul"));
                    flag2 = currentTime.isBefore(endZonedDateTime);
                }

                if (flag1 && flag2) {
                    if (((DaysAlarm) alarm).getDaysOfWeek().containsKey("EVERY_DAY")) {
                        Long rtcTime;
                        ZonedDateTime rtcZonedDateTime = LocalDate.now()
                                .atTime(time.getHours(), time.getMinutes())
                                .atZone(ZoneId.of("Asia/Seoul"));
                        if (!currentTime.isBefore(rtcZonedDateTime)) {
                            rtcZonedDateTime.plusDays(1);
                        }
                        rtcTime = rtcZonedDateTime.toInstant().toEpochMilli();
                        int requestCode = (alarm.getUid()).hashCode();
                        Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
                        toAlarm.putExtra("Bundle", bundle)
                                .putExtra("RequestCode", requestCode)
                                .putExtra("RepeatCount", alarm.getRepetition().getRepeatCount());
                        PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                                requestCode, toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, rtcTime, toAlarmPendingIntent);
                    } else {

                        if (LocalTime.of(time.getHours(), time.getMinutes())
                                .isAfter(LocalTime.now())) {

                            for (String dayOfWeek : ((DaysAlarm) alarm).getDaysOfWeek().keySet()) {
                                long rtcTime = LocalDate.now().with(TemporalAdjusters.nextOrSame(
                                        DayOfWeek.valueOf(dayOfWeek)))
                                        .atTime(time.getHours(), time.getMinutes())
                                        .atZone(ZoneId.of("Asia/Seoul"))
                                        .toInstant()
                                        .toEpochMilli();
                                int requestCode = (alarm.getUid() + dayOfWeek).hashCode();
                                Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
                                toAlarm.putExtra("Bundle", bundle)
                                        .putExtra("RequestCode", requestCode)
                                        .putExtra("RepeatCount", alarm.getRepetition().getRepeatCount());
                                PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                                        requestCode, toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                        rtcTime, toAlarmPendingIntent);
                            }
                        } else {
                            for (String dayOfWeek : ((DaysAlarm) alarm).getDaysOfWeek().keySet()) {
                                long rtcTime = LocalDate.now().with(TemporalAdjusters.next(
                                        DayOfWeek.valueOf(dayOfWeek)))
                                        .atTime(time.getHours(), time.getMinutes())
                                        .atZone(ZoneId.of("Asia/Seoul"))
                                        .toInstant()
                                        .toEpochMilli();
                                int requestCode = (alarm.getUid() + dayOfWeek).hashCode();
                                Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
                                toAlarm.putExtra("Bundle", bundle)
                                        .putExtra("RequestCode", requestCode)
                                        .putExtra("RepeatCount", alarm.getRepetition().getRepeatCount());
                                PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                                        requestCode, toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                        rtcTime, toAlarmPendingIntent);
                            }
                        }
                    }
                }

            }


            alarmMonoSink.success(alarm);
        });
    }

    // 알람을 Alarm Manager에서 삭제
    public Mono<Void> unregister(Alarm alarm) {
        return Mono.create(voidMonoSink -> {

            if (alarm instanceof DatesAlarm) {
                List<Date> dates = ((DatesAlarm) alarm).getDates();

                Time time = alarm.getTime();
                for (Date date : dates) {
                    Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
                    toAlarm.putExtra("Alarm", alarm);
                    PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                            (alarm.getUid() + date.toString()).hashCode(),
                            toAlarm, PendingIntent.FLAG_NO_CREATE);
                    if (toAlarmPendingIntent != null) {
                        alarmManager.cancel(toAlarmPendingIntent);
                    }
                }
            } else if (alarm instanceof DaysAlarm) {
                if (((DaysAlarm) alarm).getDaysOfWeek().containsKey("EVERY_DAY")) {
                    Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
                    toAlarm.putExtra("Alarm", alarm);
                    PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                            (alarm.getUid()).hashCode(),
                            toAlarm, PendingIntent.FLAG_NO_CREATE);
                    alarmManager.cancel(toAlarmPendingIntent);
                } else {
                    for (String dayOfWeek : ((DaysAlarm) alarm).getDaysOfWeek().keySet()) {
                        Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
                        PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                                (alarm.getUid() + dayOfWeek).hashCode(),
                                toAlarm, PendingIntent.FLAG_NO_CREATE);
                        alarmManager.cancel(toAlarmPendingIntent);
                    }
                }


            }

            voidMonoSink.success();
        });

    }


    // 알람을 Realtime Database에 저장
    public Mono<Alarm> save(AlarmSaveRequest request) {
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Alarm alarm = request.toAlarm();

        return alarmRepository.save(alarm)
                .flatMap(newAlarm -> {
                    if (newAlarm.hasGroup()) {
                        return sendGroupAlarmRegisterMessage(newAlarm).then(groupRepository.addAlarmToGroup(newAlarm));
                    } else {
                        return userService.addAlarm(newAlarm);
                    }
                })
                .thenReturn(alarm);
    }

    // TODO: modify DTO 다시 설계
    // 알람을 Realtime Database에서 수정
    public Mono<Alarm> modify(AlarmModifyRequest request) {
//        Alarm origin = request.getOrigin();
//        Alarm alarm = request.toAlarm();
//
//        // Dirty checking
//        if (alarm.getTitle() != null)
//        origin.updateTitle(alarm.getTitle());
//
//
//        return alarmRepository.update(origin)
//                .thenReturn(origin);
        return null;
    }

    // 알람을 Realtime Database에서 삭제
    public Mono<Void> delete(Alarm alarm) {
        return alarmRepository.deleteByUid(alarm.getUid());
    }
    
    public Flux<Alarm> getAlarmsByGroupUid(String groupUid) {
        return groupRepository.findGroupByUid(groupUid)
                              .map(Group::getAlarms)
                              .flatMapIterable(Map::keySet)
                              .flatMap(alarmRepository::getAlarmByUid);
    }

    public Flux<Alarm> gerAlarmsByGroupUid(String groupUid) {
        return groupRepository.findGroupByUid(groupUid)
                .map(Group::getAlarms)
                .flatMapIterable(Map::keySet)
                .flatMap(alarmRepository::getAlarmByUid);
    }

    public Flux<Alarm> getGroupAlarmPublisher(String groupUid) {
        return groupRepository.findAlarmUidsByGroupUid(groupUid)
                .flatMap(alarmRepository::getAlarmByUid);

    }

    public Mono<Alarm> changeSwitch(Alarm alarm, Boolean isSwitchOn) {
        String currentUserUid = userService.getCurrentUserUid();
        return alarmRepository.updateSwitchOn(currentUserUid, alarm.getUid(), isSwitchOn)
                .doOnNext(aVoid -> alarm.updateSwitch(isSwitchOn))
                .thenReturn(alarm);
    }

    private Mono<Integer> sendGroupAlarmRegisterMessage(Alarm alarm) {
        return Mono.fromCallable(() -> {
            String topic = alarm.getGroupUid();

            String alarmUid = alarm.getUid();

            URL url = new URL("https://where-alarm-you.run.goorm.io:80" + "/message/" + topic);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));

            writer.write(alarmUid);

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            StringBuffer sb = new StringBuffer();
            String s;
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            return Integer.parseInt(sb.toString());
        }).subscribeOn(Schedulers.elastic());
    }
}
