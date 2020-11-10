package kr.ac.ssu.wherealarmyou.view;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.address.Location;
import kr.ac.ssu.wherealarmyou.address.service.LocationSearchService;
import kr.ac.ssu.wherealarmyou.address.service.NaverLocationSearchService;
import kr.ac.ssu.wherealarmyou.common.ThreadUtil;

public class LocationAddActivity extends AppCompatActivity implements OnMapReadyCallback {
    // 네이버 맵 객체
    private NaverMap naverMap;

    private LocationSearchService searchService;
    private EditText etAddressSearch;

    // ListView 구현을 위해 필요한 필드들
    private ListView addressListView;
    private List<Map<String, String>> addressListViewData = new ArrayList<>();
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_add);

        searchService = new NaverLocationSearchService(getApplicationContext());

        etAddressSearch = findViewById(R.id.etAddressSearch);
        addressListView = findViewById(R.id.addressListView);

        initListView();

        initAddressSearch();

        initNaverMap();
    }

    private void initAddressSearch() {
        etAddressSearch.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {   // 엔터를 누르면
                String searchQuery = etAddressSearch.getText().toString();  // 검색어를 가져온다
                ThreadUtil.runAsync(
                        () -> searchService.search(searchQuery),
                        result -> setAddressListViewData(result)
                );
                return true;
            }
            return false;
        });
    }

    private void initListView() {
        // ListView에 보여줄 item 레이아웃 설정
        String[] from = {"title", "roadAddress"};
        int[] to = {R.id.addressTitle, R.id.tvRoadAddress};
        adapter = new SimpleAdapter(getApplicationContext(), addressListViewData, R.layout.address_list_item, from, to);
        // ListView에 adapter 등록
        addressListView.setAdapter(adapter);

        addressListView.setOnItemClickListener((parent, view, position, id) -> {
            Map<String, String> current = addressListViewData.get(position);
            double latitude = Double.parseDouble(current.get("latitude"));
            double longitude = Double.parseDouble(current.get("longitude"));

            LatLng location = new LatLng(latitude, longitude);
            moveMapCamera(location);
        });
    }

    private void initNaverMap() {
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
    }

    private void moveMapCamera(LatLng location) {
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(location);
        cameraUpdate.animate(CameraAnimation.None);
        naverMap.moveCamera(cameraUpdate);
    }

    private void setAddressListViewData(List<Location> locations) {
        // 기존의 리스트 뷰 데이터 삭제
        addressListViewData.clear();

        for (Location location : locations) {
            Map<String, String> item = Map.of(
                    "title", location.getTitle(),
                    "roadAddress", location.getRoadAddress(),
                    "latitude", String.valueOf(location.getLatitude()),
                    "longitude", String.valueOf(location.getLongitude())
            );
            // 데이터 추가
            addressListViewData.add(item);
        }

        // 리스트뷰 데이터 변경을 Adapter에게 알림
        adapter.notifyDataSetChanged();
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
    }
}