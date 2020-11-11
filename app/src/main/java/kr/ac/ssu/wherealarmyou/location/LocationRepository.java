package kr.ac.ssu.wherealarmyou.location;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import reactor.core.publisher.Mono;

public class LocationRepository {
    private final DatabaseReference locationsRef;


    public LocationRepository(FirebaseDatabase mDatabase) {
        locationsRef = mDatabase.getReference("locations");
    }

    public Mono<Location> save(Location location) {
        return Mono.create(voidMonoSink -> {
            DatabaseReference newLocationRef = locationsRef.push();
            location.setLocationUid(newLocationRef.getKey());
            newLocationRef.setValue(location, (error, ref) -> {
                if (error != null)
                    voidMonoSink.error(error.toException());
                else
                    voidMonoSink.success(location);
            });
        });
    }

    public Mono<Location> save2(Location location) {
        DatabaseReference newLocationRef = locationsRef.push();

        location.setLocationUid(newLocationRef.getKey());

        return update(location)
                .then(Mono.just(location));
    }


    public Mono<Void> update(Location location) {
        return Mono.create(voidMonoSink -> {
            locationsRef.child(location.getLocationUid())
                    .setValue(location, (error, ref) -> {
                        if (error != null)
                            voidMonoSink.error(error.toException());
                        else
                            voidMonoSink.success();
                    });
        });
    }

}
