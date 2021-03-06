package kr.ac.ssu.wherealarmyou.group;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    // user가 가입된 그룹들
    public Flux<Group> findGroupsByUserUid(String userUid) {
        return Flux.create(fluxSink ->
                groupsRef.orderByChild("members")
                        .orderByKey()
                        .equalTo(userUid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getChildren().forEach(dataSnapshot -> {
                                    Group group = dataSnapshot.getValue(Group.class);
                                    fluxSink.next(group);
                                });
                                fluxSink.complete();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                fluxSink.error(error.toException());
                            }
                        })
        );
    }
}