package kr.ac.ssu.wherealarmyou.location.service;

import java.util.List;

import kr.ac.ssu.wherealarmyou.location.Location;

public interface LocationSearchService {

    List<Location> search(String query);
}
