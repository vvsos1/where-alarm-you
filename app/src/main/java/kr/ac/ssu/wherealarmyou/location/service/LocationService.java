package kr.ac.ssu.wherealarmyou.location.service;


import kr.ac.ssu.wherealarmyou.location.Location;
import kr.ac.ssu.wherealarmyou.location.LocationRepository;
import kr.ac.ssu.wherealarmyou.user.UserRepository;
import kr.ac.ssu.wherealarmyou.user.service.UserService;
import reactor.core.publisher.Mono;

public class LocationService {
    private static LocationService instance;
    private LocationRepository locationRepository;
    private UserRepository userRepository;
    private UserService userService;


    private LocationService(LocationRepository locationRepository, UserRepository userRepository, UserService userService) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public static LocationService getInstance() {
        if (instance == null)
            instance = new LocationService(LocationRepository.getInstance(), UserRepository.getInstance(), UserService.getInstance());
        return instance;
    }

    public Mono<Void> createLocation(Location newLocation) {
        String userUid = userService.getCurrentUserUid();

        return locationRepository.save(newLocation)
                .flatMap(location -> userRepository.findUserByUid(userUid)
                        .doOnNext(user -> user.addLocation(location))
                        .flatMap(userRepository::update));
    }

    public Mono<Location> findLocation(String locationUid) {
        return locationRepository.findByUid(locationUid);
    }
}
