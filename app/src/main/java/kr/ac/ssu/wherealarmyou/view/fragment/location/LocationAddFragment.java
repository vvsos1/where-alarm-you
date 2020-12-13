package kr.ac.ssu.wherealarmyou.view.fragment.location;

import android.Manifest;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.common.Icon;
import kr.ac.ssu.wherealarmyou.location.Address;
import kr.ac.ssu.wherealarmyou.location.UserLocation;
import kr.ac.ssu.wherealarmyou.location.service.AddressSearchService;
import kr.ac.ssu.wherealarmyou.location.service.CurrentLocationService;
import kr.ac.ssu.wherealarmyou.location.service.LocationService;
import kr.ac.ssu.wherealarmyou.location.service.NaverAddressSearchService;
import kr.ac.ssu.wherealarmyou.user.service.UserService;
import kr.ac.ssu.wherealarmyou.view.DataManager;
import kr.ac.ssu.wherealarmyou.view.MainFrameActivity;
import kr.ac.ssu.wherealarmyou.view.adapter.IconItemAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;
import kr.ac.ssu.wherealarmyou.view.custom_view.RecyclerViewDecoration;
import reactor.core.scheduler.Schedulers;

public class LocationAddFragment extends Fragment {
    private static final String TAG = "LocationAddFragment";
    private final DataManager dataManager = DataManager.getInstance();
    private ImageView currentLocationImageView;
    private SeekBar rangeSeekBar;
    private String iconColor;
    private RecyclerView iconRv;
    private Button iconColorBtn;
    private Button locationMakeBtn;
    private EditText iconTextEt;
    // 네이버 맵 객체
    private NaverMap naverMap;
    private AddressSearchService searchService;
    private LocationService locationService;
    private UserService userService;
    private CurrentLocationService currentLocationService;
    private EditText addressSearchEt;
    private EditText addressTitleEt;
    // RecyclerView 구현을 위해 필요한 필드들
    private RecyclerView searchResultRv;
    private List<Address> searchResultRvData = new ArrayList<>();
    private AddressAdapter adapter;
    // 현재 사용자가 선택한 장소
    private Address selectedAddress;
    private Marker marker = new Marker();
    private CircleOverlay rangeOverlay = new CircleOverlay();
    private Handler handler;

    public static LocationAddFragment getInstance() {
        return new LocationAddFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Bundle bundle = Objects.requireNonNull(getArguments());

        View frameView = inflater.inflate(R.layout.frame_overlap, container, false);
        View contentView = inflater.inflate(R.layout.content_location_add, null);

        // Frame View Setting
        OverlappingView overlappingView = frameView.findViewById(R.id.overlap_view);
        overlappingView.setAtOnce(bundle, frameView, contentView, "장소 추가", false, true);

        handler = new Handler(getActivity().getMainLooper());

        searchService = new NaverAddressSearchService(getContext());
        locationService = LocationService.getInstance();
        userService = UserService.getInstance();
        currentLocationService = CurrentLocationService.getInstance(getContext());

        iconColorBtn = contentView.findViewById(R.id.groupMake_buttonIconColor);
        locationMakeBtn = (Button) contentView.findViewById(R.id.btn_location_make);
        addressSearchEt = contentView.findViewById(R.id.et_address_search);
        addressTitleEt = contentView.findViewById(R.id.et_address_title);
        iconTextEt = (EditText) contentView.findViewById(R.id.et_icon_text);
        searchResultRv = contentView.findViewById(R.id.rv_search_result);
        iconRv = contentView.findViewById(R.id.rv_location_color);
        currentLocationImageView = contentView.findViewById(R.id.iv_current_location);
        rangeSeekBar = contentView.findViewById(R.id.sb_range);
        TextView textViewRangeValue = contentView.findViewById(R.id.textViewRangeValue);
        rangeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setRangeOverlay(progress);
                textViewRangeValue.setText(String.valueOf(progress) + "m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        initIconRecyclerView();

        initSearchRecyclerView();

        initAddressSearch();

        initNaverMap();

        getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 200);

        currentLocationImageView.setOnClickListener(v -> {
            Log.d(TAG, "현 위치 버튼 클릭");
            currentLocationService.getCurrentAddress()
                    .subscribe(address -> handler.post(() -> {
                        selectedAddress = address;
                        moveMapCamera(new LatLng(address.getLatitude(), address.getLongitude()));
                    }));
        });

        locationMakeBtn.setOnClickListener(v -> {
            Optional<Address> address = Optional.ofNullable(selectedAddress);
            Optional<String> title = Optional.ofNullable(addressTitleEt.getText().toString());
            Optional<Integer> range = Optional.ofNullable(rangeSeekBar.getProgress());
            Optional<String> colorHex = Optional.ofNullable(iconColor);
            Optional<String> colorText = Optional.ofNullable(iconTextEt.getText().toString());


            if (address.isPresent()) {
                UserLocation newLocation = UserLocation
                        .builder()
                        .icon(new Icon(colorHex.orElse("#FFFFFF"), colorText.orElse("")))
                        .address(address.get())
                        .ownerUid(userService.getCurrentUserUid())
                        .title(title.orElse(address.get().getTitle()))
                        .range(range.orElse(20))
                        .build();
                locationService.createLocation(newLocation)
                        .doOnSuccess(unused -> dataManager.addLocationLiveData(newLocation))
                        .subscribe( );
                MainFrameActivity.hideTopFragment( );
            }
        });

        return frameView;
    }

    private void initIconRecyclerView() {
        List<Icon> icons = new ArrayList<>();
        icons.add(new Icon("#ffe3ff", null));
        icons.add(new Icon("#efe3ff", null));
        icons.add(new Icon("#dfe3ff", null));
        icons.add(new Icon("#cfe3ff", null));
        icons.add(new Icon("#bfe3ff", null));
        icons.add(new Icon("#afe3ff", null));
        icons.add(new Icon("#9fe3ff", null));
        icons.add(new Icon("#8fe3ff", null));
        icons.add(new Icon("#7fe3ff", null));
        icons.add(new Icon("#6fe3ff", null));
        icons.add(new Icon("#5fe3ff", null));
        icons.add(new Icon("#4fe3ff", null));
        icons.add(new Icon("#3fe3ff", null));
        icons.add(new Icon("#2fe3ff", null));
        icons.add(new Icon("#1fe3ff", null));
        icons.add(new Icon("#0fe3ff", null));

        IconItemAdapter iconItemAdapter = new IconItemAdapter(getContext(), icons);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        iconItemAdapter.setOnItemClickListener((itemView, icon) -> {
            GradientDrawable drawable = (GradientDrawable) iconColorBtn.getBackground();
            drawable.setColor(Color.parseColor(icon.getColorHex()));
            iconColor = icon.getColorHex();
        });

        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        iconRv.setAdapter(iconItemAdapter);
        iconRv.setLayoutManager(linearLayoutManager);
    }

    private void initAddressSearch() {
        addressSearchEt.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {   // 엔터를 누르면
                String searchQuery = addressSearchEt.getText().toString();  // 검색어를 가져온다
                Log.d(TAG, "search query : " + searchQuery);
                searchService.search(searchQuery)
                        .subscribeOn(Schedulers.elastic())
                        .publishOn(Schedulers.elastic())
                        .collectList()
                        .subscribe(result -> handler.post(() -> setAddressListViewData(result)));

                return true;


            }
            return false;
        });
    }

    private void setAddressListViewData(List<Address> addresses) {
        Log.d(TAG, "setAddressListViewData: " + addresses);
        // 기존의 리스트 뷰 데이터 삭제
        searchResultRvData.clear();

        searchResultRvData.addAll(addresses);

        // 리스트뷰 데이터 변경을 Adapter에게 알림
        adapter.notifyDataSetChanged();
    }

    private void initSearchRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        RecyclerViewDecoration decoration = new RecyclerViewDecoration(5);
        searchResultRv.setLayoutManager(manager);
        adapter = new AddressAdapter(searchResultRvData);
        // ListView에 adapter 등록
        searchResultRv.setAdapter(adapter);
        searchResultRv.addItemDecoration(decoration);
    }

    private void initNaverMap() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(naverMap1 -> this.naverMap = naverMap1);
    }


    private void moveMapCamera(LatLng location) {
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(location);
        cameraUpdate.animate(CameraAnimation.None);
        naverMap.moveCamera(cameraUpdate);
        rangeOverlay.setCenter(location);
        marker.setPosition(location);
        marker.setMap(naverMap);
    }

    private void setRangeOverlay(Integer range) {
        LatLng center = new LatLng(selectedAddress.getLatitude(), selectedAddress.getLongitude());

        this.rangeOverlay.setCenter(center);
        this.rangeOverlay.setRadius(range);
        this.rangeOverlay.setColor(Color.argb(126, 0, 0, 0));

        this.rangeOverlay.setMap(naverMap);
    }

    class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
        private List<Address> mList;

        public AddressAdapter(List<Address> mList) {
            this.mList = mList;
        }

        @NonNull
        @Override
        public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_address_list, parent, false);

            AddressViewHolder viewHolder = new AddressViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
            holder.addressTitleTv.setText(mList.get(position).getTitle());
            holder.roadAddressTv.setText(mList.get(position).getRoadAddress());
        }

        @Override
        public int getItemCount() {
            return (mList == null ? 0 : mList.size());
        }

        class AddressViewHolder extends RecyclerView.ViewHolder {

            private final TextView addressTitleTv;
            private final TextView roadAddressTv;

            public AddressViewHolder(View view) {
                super(view);
                addressTitleTv = (TextView) view.findViewById(R.id.tv_address_title);
                roadAddressTv = (TextView) view.findViewById(R.id.tv_road_address);
                view.setOnClickListener(v -> {
                    Address current = mList.get(getAdapterPosition());
                    double latitude = current.getLatitude();
                    double longitude = current.getLongitude();
                    // TODO : 범위 값도 가져오기

                    LatLng location = new LatLng(latitude, longitude);
                    moveMapCamera(location);
                    LocationAddFragment.this.selectedAddress = current;
                });
            }

        }
    }
}