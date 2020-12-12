package kr.ac.ssu.wherealarmyou.location.service;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import kr.ac.ssu.wherealarmyou.location.Address;
import reactor.core.publisher.Mono;

public class CurrentLocationService {
    private static CurrentLocationService instance;

    private FusedLocationProviderClient fusedLocationClient;
    private Context context;

    private CurrentLocationService(Context context) {
        setContext(context);
    }

    public static CurrentLocationService getInstance(Context context) {
        if (instance == null)
            instance = new CurrentLocationService(context);
        instance.setContext(context);
        return instance;
    }

    private void setContext(Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    // 위치 권한을 체크 후 결과를 반환
    private boolean checkLocationPermission() {
        return (ActivityCompat
                .checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat
                .checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }


    // 현재 사용자의 위치를 반환
    public Mono<Address> getCurrentAddress() {
        if (checkLocationPermission()) {
            return Mono.create(monoSink -> {
                LocationRequest locationRequest = LocationRequest.create();
                // Location 업데이트를 1번만 요청
                locationRequest.setNumUpdates(1);

                fusedLocationClient.requestLocationUpdates(locationRequest,
                        new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                android.location.Location lastLocation = locationResult.getLastLocation();
                                if (lastLocation != null) {
                                    Log.d("CurrentLocationService", "last location : " + lastLocation.toString());
                                    Address address = new Address(lastLocation.getLongitude(), lastLocation.getLatitude());
                                    monoSink.success(address);

                                }
                            }
                        },
                        null);

            });
        } else {
            return Mono.error(new RuntimeException("위치 권한이 없습니다"));
        }
    }
}
