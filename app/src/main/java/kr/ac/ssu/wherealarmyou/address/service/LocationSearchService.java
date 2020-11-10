package kr.ac.ssu.wherealarmyou.address.service;

import java.util.List;

import kr.ac.ssu.wherealarmyou.address.Location;

public interface LocationSearchService {

    List<Location> search(String query);
}
