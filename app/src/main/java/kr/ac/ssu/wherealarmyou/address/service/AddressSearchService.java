package kr.ac.ssu.wherealarmyou.address.service;

import java.util.List;

import kr.ac.ssu.wherealarmyou.address.Address;

public interface AddressSearchService {

    List<Address> search(String query);
}
