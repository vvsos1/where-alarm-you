package kr.ac.ssu.wherealarmyou.location;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import reactor.core.publisher.Mono;

public class LocationRepository
{
    // Singleton
    private static LocationRepository instance;
    
    private final DatabaseReference locationsRef;
    
    private LocationRepository(FirebaseDatabase mDatabase)
    {
        locationsRef = mDatabase.getReference("locations");
    }
    
    public static synchronized LocationRepository getInstance( )
    {
        if (instance == null) { instance = new LocationRepository(FirebaseDatabase.getInstance( )); }
        return instance;
    }
    
    public Mono<Location> save(Location location)
    {
        DatabaseReference newLocationRef = locationsRef.push( );
        
        location.setUid(newLocationRef.getKey( ));
        
        return update(location).then(Mono.just(location));
    }

    public Mono<Void> update(Location location) {
        return Mono.create(voidMonoSink -> {
            locationsRef.child(location.getUid())
                    .setValue(location, (error, ref) -> {
                        if (error != null) {
                            voidMonoSink.error(error.toException());
                        } else {
                            voidMonoSink.success();
                        }
                    });
        });
    }

    public Mono<Location> findByUid(String uid) {
        return Mono.create(locationMonoSink -> {
            locationsRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    locationMonoSink.success(snapshot.getValue(Location.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    locationMonoSink.error(error.toException());
                }
            });
        });
    }
}
