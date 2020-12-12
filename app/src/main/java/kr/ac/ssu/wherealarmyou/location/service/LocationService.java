package kr.ac.ssu.wherealarmyou.location.service;


import kr.ac.ssu.wherealarmyou.location.Location;
import kr.ac.ssu.wherealarmyou.location.LocationRepository;
import reactor.core.publisher.Mono;

public class LocationService {
    private static LocationService instance;
    private LocationRepository locationRepository;


    private LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public static LocationService getInstance() {
        if (instance == null)
            instance = new LocationService(LocationRepository.getInstance());
        return instance;
    }


    public Mono<Location> findLocation(String locationUid) {
        return locationRepository.findByUid(locationUid);
    }
}
