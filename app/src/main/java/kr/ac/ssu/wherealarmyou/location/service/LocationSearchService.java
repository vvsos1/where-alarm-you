package kr.ac.ssu.wherealarmyou.location.service;

import kr.ac.ssu.wherealarmyou.location.Location;
import reactor.core.publisher.Flux;

public interface LocationSearchService {

    Flux<Location> search(String query);
}
