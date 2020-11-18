package kr.ac.ssu.wherealarmyou.location.service;

import kr.ac.ssu.wherealarmyou.location.Address;
import reactor.core.publisher.Flux;

public interface AddressSearchService {

    Flux<Address> search(String query);
}
