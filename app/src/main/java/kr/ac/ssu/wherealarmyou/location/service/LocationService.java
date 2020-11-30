package kr.ac.ssu.wherealarmyou.location.service;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.CancellationException;

import kr.ac.ssu.wherealarmyou.location.Address;
import reactor.core.publisher.Mono;

public class LocationService {
    private static LocationService instance;
    private FusedLocationProviderClient fusedLocationClient;
    private Context context;

    public LocationService(Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public static LocationService getInstance(Context context) {
        if (instance == null)
            instance = new LocationService(context);
        return instance;
    }

    // 위치 권한을 체크 후 결과를 반환
    private boolean checkLocationPermission() {
        return (ActivityCompat
                .checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat
                .checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

    // 현재 사용자의 위치를 반환
    public Mono<Address> getCurrentAddress() {
        if (checkLocationPermission()) {
            return Mono.create(voidMonoSink -> {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(
                                location -> voidMonoSink.success(
                                        new Address(location.getLongitude(), location.getLatitude())))
                        .addOnFailureListener(
                                voidMonoSink::error)
                        .addOnCanceledListener(
                                () -> voidMonoSink.error(
                                        new CancellationException("현재 정보를 가져오는 행위가 취소되었습니다")));
            });
        } else {
            return Mono.error(new RuntimeException("위치 권한이 없습니다"));
        }
    }
}
