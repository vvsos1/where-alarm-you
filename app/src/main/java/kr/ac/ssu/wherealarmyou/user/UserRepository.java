package kr.ac.ssu.wherealarmyou.user;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import reactor.core.publisher.Mono;

public class UserRepository
{
    // Singleton
    private static UserRepository    instance;
    private final  DatabaseReference usersRef;

    private UserRepository(FirebaseDatabase mDatabase)
    {
        this.usersRef = mDatabase.getReference("users");
    }

    public static UserRepository getInstance( )
    {
        if (instance == null) { instance = new UserRepository(FirebaseDatabase.getInstance( )); }
        return instance;
    }

    public Mono<User> save(User user)
    {
        return update(user).thenReturn(user);
    }

    public Mono<User> findUserByUid(String uid)
    {
        return Mono.create(userMonoSink -> {
            usersRef.child(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener( )
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            User user = User.fromSnapshot(snapshot);
                            user.setUid(uid);
                            userMonoSink.success(user);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {
                            userMonoSink.error(error.toException( ));
                        }
                    });
        });
    }

    public Mono<Void> update(User user)
    {
        return Mono.create(voidMonoSink -> {
            usersRef.child(user.getUid( )).setValue(user, (error, ref) -> {
                if (error != null) { voidMonoSink.error(error.toException( )); }
                else { voidMonoSink.success( ); }
            });
        });
    }

    public Mono<User> findUserByEmail(String email)
    {
        return Mono.create(userMonoSink -> {
            usersRef.orderByChild("email")
                    .equalTo(email)
                    .addListenerForSingleValueEvent(new ValueEventListener( )
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            snapshot.getChildren( )
                                    .forEach(dataSnapshot -> {
                                        User user = User.fromSnapshot(dataSnapshot);
                                        userMonoSink.success(user);
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            userMonoSink.error(error.toException());
                        }
                    });
        });
    }

    public Mono<Void> addAlarm(String userUid, Alarm alarm) {
        return Mono.create(voidMonoSink -> {
            usersRef.child(userUid)
                    .child("alarms")
                    .child(alarm.getUid())
                    .setValue(alarm, (error, ref) -> {
                        if (error != null)
                            voidMonoSink.error(error.toException());
                        else
                            voidMonoSink.success();
                    });
        });
    }

    public Mono<Void> removeAlarm(String userUid, String alarmUid) {
        return Mono.create(voidMonoSink -> {
            usersRef.child(userUid)
                    .child("alarms")
                    .child(alarmUid)
                    .removeValue((error, ref) -> {
                        if (error != null)
                            voidMonoSink.error(error.toException());
                        else
                            voidMonoSink.success();
                    });
        });
    }

    public Mono<Void> delete(User user) {
        return deleteByUid(user.getUid());
    }

    public Mono<Void> deleteByUid(String uid) {
        return Mono.create(voidMonoSink -> {
            usersRef.child(uid).removeValue((error, ref) -> {
                if (error != null) {
                    voidMonoSink.error(error.toException());
                } else {
                    voidMonoSink.success();
                }
            });

        });
    }


}
