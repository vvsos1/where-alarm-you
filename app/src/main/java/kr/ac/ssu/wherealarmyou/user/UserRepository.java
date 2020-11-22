package kr.ac.ssu.wherealarmyou.user;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import reactor.core.publisher.Mono;


public class UserRepository {
    // Singleton
    private static UserRepository instance;

    private UserRepository(FirebaseDatabase mDatabase) {
        this.usersRef = mDatabase.getReference("users");
    }

    private final DatabaseReference usersRef;

    public static UserRepository getInstance() {
        if (instance == null)
            instance = new UserRepository(FirebaseDatabase.getInstance());
        return instance;
    }

    public Mono<User> save(User user) {
        DatabaseReference newUserRef = usersRef.push();
        String newUid = newUserRef.getKey();
        user.setUid(newUid);
        return update(user).thenReturn(user);
    }

    public Mono<User> findUserByUid(String uid) {
        return Mono.create(userMonoSink -> {
            usersRef
                    .child(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            user.setUid(uid);
                            userMonoSink.success(user);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            userMonoSink.error(error.toException());
                        }
                    });
        });
    }

    public Mono<Void> update(User user) {
        return Mono.create(voidMonoSink -> {
            usersRef.child(user.getUid()).setValue(user, (error, ref) -> {
                if (error != null)
                    voidMonoSink.error(error.toException());
                else
                    voidMonoSink.success();
            });
        });
    }

    public Mono<User> findUserByEmail(String email) {
        return null;
    }

    public Mono<Void> delete(User user) {
        return deleteByUid(user.getUid());
    }

    public Mono<Void> deleteByUid(String uid) {
        return Mono.create(voidMonoSink -> {
            usersRef.child(uid).removeValue((error, ref) -> {
                if (error != null)
                    voidMonoSink.error(error.toException());
                else
                    voidMonoSink.success();
            });

        });
    }


}
