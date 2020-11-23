package kr.ac.ssu.wherealarmyou.user;

import androidx.annotation.NonNull;
import com.google.firebase.database.*;
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
        DatabaseReference newUserRef = usersRef.push( );
        String            newUid     = newUserRef.getKey( );
        user.setUid(newUid);
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
                            User user = snapshot.getValue(User.class);
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
                                        User user = dataSnapshot.getValue(User.class);
                                        userMonoSink.success(user);
                                    });
                        }
                
                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {
                            userMonoSink.error(error.toException( ));
                        }
                    });
        });
    }
    
    public Mono<Void> delete(User user)
    {
        return deleteByUid(user.getUid( ));
    }
    
    public Mono<Void> deleteByUid(String uid)
    {
        return Mono.create(voidMonoSink -> {
            usersRef.child(uid).removeValue((error, ref) -> {
                if (error != null) { voidMonoSink.error(error.toException( )); }
                else { voidMonoSink.success( ); }
            });
            
        });
    }
}
