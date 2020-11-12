package kr.ac.ssu.wherealarmyou.location.service;


import com.google.firebase.auth.FirebaseAuth;

import kr.ac.ssu.wherealarmyou.location.Location;
import kr.ac.ssu.wherealarmyou.location.LocationRepository;
import kr.ac.ssu.wherealarmyou.user.UserRepository;
import reactor.core.publisher.Mono;

public class LocationAddService {
    private static LocationAddService instance;

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    private LocationAddService(LocationRepository locationRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    public static LocationAddService getInstance() {
        if (instance == null)
            instance = new LocationAddService(LocationRepository.getInstance(), UserRepository.getInstance());
        return instance;
    }

    public Mono<Void> addLocation(Location newLocation) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        return
                locationRepository.save(newLocation)
                        .flatMap(location ->
                                userRepository.findUserByUid(userUid)
                                        .doOnNext(user -> user.addLocation(location))
                                        .flatMap(userRepository::update)
                        );


    }

}
