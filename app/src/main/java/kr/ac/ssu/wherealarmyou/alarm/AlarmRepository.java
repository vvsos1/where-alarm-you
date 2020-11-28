package kr.ac.ssu.wherealarmyou.alarm;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import reactor.core.publisher.Mono;

public class AlarmRepository
{
    private static AlarmRepository instance;
    
    private final DatabaseReference alarmsRef;
    
    private AlarmRepository(FirebaseDatabase mDatabase)
    {
        this.alarmsRef = mDatabase.getReference("alarms");
    }
    
    public static AlarmRepository getInstance( )
    {
        if (instance == null) { instance = new AlarmRepository(FirebaseDatabase.getInstance( )); }
        return instance;
    }
    
    public Mono<Alarm> getAlarmByUid(String alarmUid)
    {
        return Mono.create(sink ->
            alarmsRef.child(alarmUid).addListenerForSingleValueEvent(new ValueEventListener( )
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    Alarm alarm = Alarm.fromSnapShot(snapshot);
                    sink.success(alarm);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    sink.error(error.toException());
                }
            }));
    }

    public Mono<Alarm> save(Alarm alarm) {
        DatabaseReference newAlarmRef = alarmsRef.push();
        String newUid = newAlarmRef.getKey();

        alarm.setUid(newUid);

        return update(alarm)
                .thenReturn(alarm);
    }

    public Mono<Void> update(Alarm alarm) {
        return Mono.create(voidMonoSink -> {
            String alarmUid = alarm.getUid();
            alarmsRef.child(alarmUid).setValue(alarm, (error, ref) -> {
                if (error != null) {
                    voidMonoSink.error(error.toException());
                } else {
                    voidMonoSink.success();
                }
            });
        });
    }
    
    public Mono<Void> deleteByUid(String alarmUid)
    {
        return Mono.create(voidMonoSink ->
            alarmsRef.child(alarmUid).removeValue((error, ref) -> {
                if (error != null) { voidMonoSink.error(error.toException( )); }
                else { voidMonoSink.success( ); }
            }));
    }
}
