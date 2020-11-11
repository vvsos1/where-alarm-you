package kr.ac.ssu.wherealarmyou.group;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GroupRepository {
    private final DatabaseReference groupsRef;

    public GroupRepository(FirebaseDatabase mDatabase) {
        this.groupsRef = mDatabase.getReference("groups");
    }

    public Flux<Group> findGroupByName(String groupName) {
        return Flux.create(fluxSink -> {
            groupsRef.orderByChild("name").equalTo(groupName).addListenerForSingleValueEvent(new ValueEventListener() {
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
            });
        });
    }

    public Mono<Group> findGroupByUid(String groupUid) {
        return Mono.create(monoSink -> {
            groupsRef.child(groupUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Group group = snapshot.getValue(Group.class);
                    monoSink.success(group);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    monoSink.error(error.toException());
                }
            });
        });
    }


}