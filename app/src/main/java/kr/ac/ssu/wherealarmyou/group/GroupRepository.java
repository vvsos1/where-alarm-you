package kr.ac.ssu.wherealarmyou.group;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GroupRepository
{
    // Singleton
    private static GroupRepository instance;
    
    private final DatabaseReference groupsRef;
    
    private GroupRepository(FirebaseDatabase mDatabase)
    {
        this.groupsRef = mDatabase.getReference("groups");
    }
    
    public static GroupRepository getInstance( )
    {
        if (instance == null) { instance = new GroupRepository(FirebaseDatabase.getInstance( )); }
        return instance;
    }
    
    public Mono<Group> save(Group group)
    {
        DatabaseReference newGroupRef = groupsRef.push( );
        group.setUid(newGroupRef.getKey( ));
        return update(group).then(Mono.just(group));
    }

    public Mono<Group> update(Group group) {
        return Mono.create(voidMonoSink -> {
            groupsRef.child(group.getUid()).setValue(group, (error, ref) -> {
                if (error != null) {
                    voidMonoSink.error(error.toException());
                } else {
                    voidMonoSink.success(group);
                }
            });
        });
    }

    public Mono<Void> deleteByUid(String groupUid) {
        return Mono.create(voidMonoSink -> {
            groupsRef.child(groupUid).removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error != null)
                        voidMonoSink.error(error.toException());
                    else
                        voidMonoSink.success();
                }
            });
        });
    }

    public Mono<Void> delete(Group group) {
        return deleteByUid(group.getUid());
    }

    public Flux<Group> findGroupsByName(String groupName) {
        return Flux.create(fluxSink ->
                groupsRef.orderByChild("name")
                        .equalTo(groupName)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getChildren().forEach(dataSnapshot -> {
                                    Group group = dataSnapshot.getValue(Group.class);
                                    fluxSink.next(group);
                                 });
                                 fluxSink.complete( );
                             }
                        
                             @Override
                             public void onCancelled(@NonNull DatabaseError error)
                             {
                                 fluxSink.error(error.toException( ));
                             }
                         }));
    }
    
    public Mono<Group> findGroupByUid(String groupUid)
    {
        return Mono.create(monoSink ->
                groupsRef.child(groupUid).addListenerForSingleValueEvent(new ValueEventListener( )
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        Group group = snapshot.getValue(Group.class);
                        monoSink.success(group);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        monoSink.error(error.toException());
                    }
                }));
    }

    // group에 등록된 알람 리스너
    public Flux<String> findAlarmUidsByGroupUid(String groupUid) {
        return Flux.create(fluxSink ->
                groupsRef.child(groupUid).child("alarms").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        String alarmUid = snapshot.getKey();
                        fluxSink.next(alarmUid);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        fluxSink.error(error.toException());
                    }
                })
        );
    }

    public Mono<Void> addAlarmToGroup(Alarm newAlarm) {
        String groupUid = newAlarm.getGroupUid();
        return Mono.create(voidMonoSink -> {
            groupsRef.child(groupUid).child("alarms").child(newAlarm.getUid()).setValue(Boolean.TRUE, (error, ref) -> {
                if (error != null)
                    voidMonoSink.error(error.toException());
                else
                    voidMonoSink.success();
            });
        });
    }
}