package kr.ac.ssu.wherealarmyou.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.*;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.location.Address;
import kr.ac.ssu.wherealarmyou.location.Location;
import kr.ac.ssu.wherealarmyou.location.service.AddressSearchService;
import kr.ac.ssu.wherealarmyou.location.service.LocationAddService;
import kr.ac.ssu.wherealarmyou.location.service.NaverAddressSearchService;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocationAddActivity extends AppCompatActivity implements OnMapReadyCallback
{
    // 네이버 맵 객체
    private NaverMap naverMap;
    
    private AddressSearchService searchService;
    private LocationAddService   addService;
    
    private EditText etAddressSearch;
    
    // ListView 구현을 위해 필요한 필드들
    private ListView                  addressListView;
    private List<Map<String, String>> addressListViewData = new ArrayList<>( );
    private SimpleAdapter             adapter;
    private FloatingActionButton      fab;
    
    // 현재 사용자가 선택한 장소
    private Address selectedAddress;
    
    private Handler handler = new Handler(Looper.getMainLooper( ));
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_add);

//        UserService.getInstance().register(new RegisterRequest("vvsos1@hotmail.co.kr","111111","박명규")).subscribe();
        FirebaseAuth.getInstance( ).signInWithEmailAndPassword("vvsos1@hotmail.co.kr", "111111");
        searchService = new NaverAddressSearchService(getApplicationContext( ));
        addService    = LocationAddService.getInstance( );
        
        etAddressSearch = findViewById(R.id.etAddressSearch);
        addressListView = findViewById(R.id.addressListView);
        
        initListView( );
        
        initAddressSearch( );
        
        initNaverMap( );
        
        initFloatingButton( );
    }
    
    private void initFloatingButton( )
    {
        fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(v -> {
            if (selectedAddress != null) {
                // TODO: 코드 완성
                Location newLocation = null;
                addService.addLocation(newLocation).subscribe( );
            }
        });
        
    }
    
    private void initAddressSearch( )
    {
        etAddressSearch.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction( ) == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {   // 엔터를 누르면
                String searchQuery = etAddressSearch.getText( ).toString( );  // 검색어를 가져온다
                
                searchService.search(searchQuery)
                             .subscribeOn(Schedulers.single( ))
                             .subscribeOn(Schedulers.elastic( ))
//                        .publishOn(Schedulers.elastic())
                             .collectList( )
                             .subscribe(result -> handler.post(( ) -> setAddressListViewData(result)));
                
                return true;
                
                
            }
            return false;
        });
    }
    
    private void initListView( )
    {
        // ListView에 보여줄 item_alarm_add_frame 레이아웃 설정
        String[] from = { "title", "roadAddress" };
        int[]    to   = { R.id.addressTitle, R.id.tvRoadAddress };
        adapter = new SimpleAdapter(getApplicationContext( ), addressListViewData, R.layout.address_list_item, from,
                to);
        // ListView에 adapter 등록
        addressListView.setAdapter(adapter);
        
        addressListView.setOnItemClickListener((parent, view, position, id) -> {
            Map<String, String> current = addressListViewData.get(position);
            
            double latitude     = Double.parseDouble(current.get("latitude"));
            double longitude    = Double.parseDouble(current.get("longitude"));
            String title        = current.get("title");
            String roadAddress  = current.get("roadAddress");
            String jibunAddress = current.get("jibunAddress");
            // TODO : 범위 값도 가져오기
            
            selectedAddress = new Address(title, roadAddress, jibunAddress, longitude, latitude);
            
            LatLng location = new LatLng(latitude, longitude);
            moveMapCamera(location);
        });
    }
    
    private void initNaverMap( )
    {
        FragmentManager fm          = getSupportFragmentManager( );
        MapFragment     mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance( );
            fm.beginTransaction( ).add(R.id.map, mapFragment).commit( );
        }
        
        mapFragment.getMapAsync(this);
    }
    
    private void moveMapCamera(LatLng location)
    {
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(location);
        cameraUpdate.animate(CameraAnimation.None);
        naverMap.moveCamera(cameraUpdate);
    }
    
    private void setAddressListViewData(List<Address> addresses)
    {
        // 기존의 리스트 뷰 데이터 삭제
        addressListViewData.clear( );
        
        for (Address address : addresses) {
            Map<String, String> item = Map.of(
                    "title", address.getTitle( ),
                    "roadAddress", address.getRoadAddress( ),
                    "jibunAddress", address.getJibunAddress( ),
                    "latitude", String.valueOf(address.getLatitude( )),
                    "longitude", String.valueOf(address.getLongitude( ))
            );
            
            // 데이터 추가
            addressListViewData.add(item);
        }
        
        // 리스트뷰 데이터 변경을 Adapter에게 알림
        adapter.notifyDataSetChanged( );
    }
    
    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap)
    {
        this.naverMap = naverMap;
    }
}