package kr.ac.ssu.wherealarmyou.view.fragment.alarm;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.alarm.LocationCondition;
import kr.ac.ssu.wherealarmyou.alarm.Period;
import kr.ac.ssu.wherealarmyou.alarm.Repetition;
import kr.ac.ssu.wherealarmyou.alarm.Time;
import kr.ac.ssu.wherealarmyou.alarm.dto.AlarmSaveRequest;
import kr.ac.ssu.wherealarmyou.alarm.serivce.AlarmService;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.view.DataManager;
import kr.ac.ssu.wherealarmyou.view.adapter.AlarmCategoryItemAdapter;
import kr.ac.ssu.wherealarmyou.view.custom_view.AlarmAddFrameItem;
import kr.ac.ssu.wherealarmyou.view.custom_view.OverlappingView;
import kr.ac.ssu.wherealarmyou.view.viewmodel.AlarmAddDaysViewModel;
import kr.ac.ssu.wherealarmyou.view.viewmodel.AlarmAddGroupViewModel;
import kr.ac.ssu.wherealarmyou.view.viewmodel.AlarmAddMemoViewModel;
import kr.ac.ssu.wherealarmyou.view.viewmodel.AlarmAddTimeViewModel;
import kr.ac.ssu.wherealarmyou.view.viewmodel.AlarmAddViewModel;
import lombok.Builder;
import reactor.core.scheduler.Schedulers;
import reactor.util.annotation.Nullable;

public class AlarmAddFragment extends Fragment implements View.OnClickListener
{
    private static final int TIME     = 0;
    private static final int WEEK     = 1;
    private static final int LOCATION = 2;
    private static final int GROUP    = 3;
    private static final int MEMO     = 4;
    private static final int DETAIL   = 5;
    
    AlarmAddViewModel<?> viewModel;
    AtomicInteger currentVisibleCategoryPosition;
    //알람의 시간
    private Time time;
    // 알람의 제목
    private String title;
    // 알람의 설명 or 내용
    private String description;
    // 알람이 활성화될 장소의 조건
    @Nullable
    private LocationCondition locationCondition;
    // 알람이 등록될 그룹
    @Nullable
    private Group group;
    // 소리
    @Builder.Default
    private Boolean sound = Boolean.TRUE;
    // 진동
    @Builder.Default
    private Boolean vibe = Boolean.TRUE;
    // 반복
    private Repetition repetition;
    // 알람이 활성화될 기간
    @Nullable
    private Period activePeriod;
    // 알람이 울릴 요일; Key: 요일
    @Nullable
    private Map<String, Boolean> daysOfWeek;
    @Nullable
    private List<Date> dates;

    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    private Alarm alarm;

    public AlarmAddFragment(Alarm alarm) {
        this.alarm = alarm;
    }

    public static AlarmAddFragment getInstance(Alarm alarm) {
        return new AlarmAddFragment(alarm);
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        title = "";
        description = "";
        time = new Time();
        repetition = new Repetition(2);


        Bundle bundle = Objects.requireNonNull(getArguments());

        View frameView = inflater.inflate(R.layout.frame_overlap, container, false);
        View contentView = inflater.inflate(R.layout.content_alarm_add, null);

        // Frame View Setting
        OverlappingView overlappingView = frameView.findViewById(R.id.overlap_view);
        if (alarm == null) {
            overlappingView.setAtOnce(bundle, frameView, contentView, "알람 추가", false, true);
        }
        else { overlappingView.setAtOnce(bundle, frameView, contentView, "알람 수정", false, true); }
        
        // Make Category List
        List<AlarmAddFrameItem> items = new ArrayList<>();
        items.add(new AlarmAddFrameItem(R.drawable.ic_time, "시간", ""));
        items.add(new AlarmAddFrameItem(R.drawable.ic_calendar, "요일 및 날짜", ""));
        items.add(new AlarmAddFrameItem(R.drawable.ic_location, "장소", ""));
        items.add(new AlarmAddFrameItem(R.drawable.ic_group, "그룹", ""));
        items.add(new AlarmAddFrameItem(R.drawable.ic_note, "내용", ""));
        items.add(new AlarmAddFrameItem(R.drawable.ic_add_box, "세부 설정", ""));
        
        // Content View Setting
        RecyclerView             recyclerView             = frameView.findViewById(R.id.alarmAdd_recyclerView);
        AlarmCategoryItemAdapter alarmCategoryItemAdapter = new AlarmCategoryItemAdapter(getContext( ), items);
        LinearLayoutManager      linearLayoutManager      = new LinearLayoutManager(getContext( ));
        DividerItemDecoration decoration = new DividerItemDecoration(Objects.requireNonNull(getContext( )),
                linearLayoutManager.getOrientation( ));
        
        // Content View Event Listener
        currentVisibleCategoryPosition = new AtomicInteger();
        alarmCategoryItemAdapter.setOnItemClickListener((view, position) -> {
                changeCategory(position);
        });
        
        recyclerView.setAdapter(alarmCategoryItemAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(decoration);
        layoutManager = recyclerView.getLayoutManager( );
        
        return frameView;
    }
    
    private void startCategoryFragment(int category)
    {
        FragmentManager     fragmentManager     = Objects.requireNonNull(getActivity( )).getSupportFragmentManager( );
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction( );
        
        if (category == TIME) {
            fragmentTransaction.replace(R.id.alarmAdd_frameLayoutTime, new AlarmAddTimeFragment( ));
        }
        else if (category == WEEK) {
            fragmentTransaction.replace(R.id.alarmAdd_frameLayoutWeek, new AlarmAddDaysFragment( ));
        }
        else if (category == LOCATION) {
//            fragmentTransaction.add(R.id.alarmAdd_frameLayoutLocation, new AlarmAddLocationFragment( ));
        }
        else if (category == GROUP) {
            fragmentTransaction.replace(R.id.alarmAdd_frameLayoutGroup, new AlarmAddGroupFragment( ));
        }
        else if (category == MEMO) {
            fragmentTransaction.replace(R.id.alarmAdd_frameLayoutMemo, new AlarmAddMemoFragment());
        }
        else if (category == DETAIL) {
            fragmentTransaction.replace(R.id.alarmAdd_frameLayoutDetail, new AlarmAddDetailFragment());
        }
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, 0, R.anim.fade_in, 0)
                           .addToBackStack(null)
                           .commit( );
        
    }
    
    // TODO ViewModel에서 setData와 setInfo 분리하기 (getViewModel 이용)
    private void setViewModel(int category)
    {
        if (category == TIME) {
            AlarmAddViewModel<Time> viewModel = new ViewModelProvider(requireActivity( )).get(AlarmAddTimeViewModel.class);
            viewModel.getLiveData( ).observe(getViewLifecycleOwner( ), time -> {
                if (time != null) {
                    this.time = time;
                }
            });
            viewModel.getInfoString( ).observe(getViewLifecycleOwner( ), string -> setInfo(string, category));
            viewModel.onComplete( ).observe(getViewLifecycleOwner( ), isComplete -> {
                if (isComplete) {
                    changeCategory(currentVisibleCategoryPosition.get( ) + 1);
                    viewModel.reset( );
                }
            });
        }
        else if (category == WEEK) {
            AlarmAddViewModel<Map> viewModel = new ViewModelProvider(requireActivity()).get(AlarmAddDaysViewModel.class);
            viewModel.getLiveData().observe(getViewLifecycleOwner(), map -> {
                if ((map.get("dates") instanceof List)) {
                    if (((List) map.get("dates")).isEmpty()) {
                        if (map.get("daysOfWeek") instanceof Map) {
                            dates = (List) map.get("dates");
                            daysOfWeek = (Map) map.get("daysOfWeek");
                            activePeriod = (Period) map.get("activePeriod");
                        }
                    } else {
                        dates = (List) map.get("dates");
                        daysOfWeek = null;
                        activePeriod = null;
                    }
                }
            });
            viewModel.getInfoString().observe(getViewLifecycleOwner(), string -> setInfo(string, category));
        }
        else if (category == GROUP) {
            AlarmAddViewModel<Group> viewModel = new ViewModelProvider(requireActivity()).get(AlarmAddGroupViewModel.class);
            viewModel.getLiveData().observe(getViewLifecycleOwner(), group -> {
                if (group != null) {
                    this.group = group;
                    setInfo(group.getName(), category);
                }
            });
            viewModel.onComplete().observe(getViewLifecycleOwner(), isComplete -> {
                if (isComplete) {
                    changeCategory(currentVisibleCategoryPosition.get() + 1);
                    viewModel.reset();
                }
            });
        } else if (category == MEMO) {
            AlarmAddViewModel<Map> viewModel = new ViewModelProvider(requireActivity()).get(AlarmAddMemoViewModel.class);
            viewModel.getLiveData().observe(getViewLifecycleOwner(), map -> {
                if (map == null)
                    return;

                if (map.get("title") != null) {
                    this.title = (String) map.get("title");
                    viewModel.setInfoString(title);
                }

                if (map.get("description") != null) {
                    this.description = (String) map.get("description");
                }
            });
            viewModel.getInfoString().observe(getViewLifecycleOwner(), s -> {
                setInfo(s, category);
            });
            viewModel.onComplete().observe(getViewLifecycleOwner(), isComplete -> {
                if (isComplete) {
                    changeCategory(currentVisibleCategoryPosition.get() + 1);
                    viewModel.reset();
                }
            });
        }
    }
    
    private AlarmAddViewModel<?> getViewModel(int category)
    {
        if (category == TIME) {
            return new ViewModelProvider(requireActivity()).get(AlarmAddTimeViewModel.class);
        } else if (category == WEEK) {
            return new ViewModelProvider(requireActivity()).get(AlarmAddDaysViewModel.class);
        } else if (category == MEMO) {
            return new ViewModelProvider(requireActivity()).get(AlarmAddMemoViewModel.class);
        }
        return null;
    }
    
    private void setInfo(String string, int position)
    {
        if (string == null)
            return;

        TextView infoText = Objects.requireNonNull(layoutManager.findViewByPosition(position))
                .findViewById(R.id.item_alarmAddCategory_textViewInfo);
        infoText.setText(string);
    }
    
    private void changeCategory(int category)
    {
        startCategoryFragment(category);
        setViewModel(category);
        changeVisibleCategoryView(currentVisibleCategoryPosition.get( ), category);
        currentVisibleCategoryPosition.set(category);
    }
    
    @Override
    public void onClick(View view) { }
    
    @Override
    public void onStop( ) {
        super.onStop();
        registerAlarm();

    }

    private void registerAlarm() {
        if (time == null || time.getHours() == null || time.getMinutes() == null) {
            Toast.makeText(getContext(), "시간 없는 알람을 만들 수 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        AlarmService alarmService = AlarmService.getInstance(getContext());
        if (daysOfWeek == null && (dates == null || dates.isEmpty())) {
            dates = new ArrayList<Date>();
            LocalDate now = LocalDate.now();
            dates.add(new Date(now.getDayOfMonth(), now.getMonthValue(), now.getYear()));
        }


        AlarmSaveRequest req = AlarmSaveRequest.builder(time).dates(dates)
                .repetition(repetition)
                .description(description)
                .title(title)
                .activePeriod(activePeriod)
                .daysOfWeek(daysOfWeek)
                .vibe(true)
                .sound(false)
                .build();


        Log.d("AlarmAddFragment", req.toString());

        alarmService.save(req)
                .doOnSuccess(alarm -> DataManager.getInstance().updateAlarmLiveData())
                .doOnError(throwable -> Log.e("AlarmAddFragment", throwable.getMessage()))
                .flatMap(alarmService::register)
                .publishOn(Schedulers.elastic())
                .subscribeOn(Schedulers.elastic())
                    .subscribe( );
    }
    
    private void changeVisibleCategoryView(int position_oldView, int position_newView)
    {
        View oldView = Objects.requireNonNull(layoutManager.findViewByPosition(position_oldView));
        View newView = Objects.requireNonNull(layoutManager.findViewByPosition(position_newView));
        
        RelativeLayout oldHead      = oldView.findViewById(R.id.item_alarmAddCategory_relativeLayoutHead);
        RelativeLayout newHead      = newView.findViewById(R.id.item_alarmAddCategory_relativeLayoutHead);
        ImageView      oldPictogram = oldView.findViewById(R.id.item_alarmAddCategory_imageViewPictogram);
        ImageView      newPictogram = newView.findViewById(R.id.item_alarmAddCategory_imageViewPictogram);
        TextView       oldNameView  = oldView.findViewById(R.id.item_alarmAddCategory_textViewName);
        TextView       newNameView  = newView.findViewById(R.id.item_alarmAddCategory_textViewName);
        TextView       oldInfoView  = oldView.findViewById(R.id.item_alarmAddCategory_textViewInfo);
        TextView       newInfoView  = newView.findViewById(R.id.item_alarmAddCategory_textViewInfo);
        
        int oldPadding = (int)getResources( ).getDimension(R.dimen.alarmAddFrag_oldHeadPadding);
        int newPadding = (int)getResources( ).getDimension(R.dimen.alarmAddFrag_newHeadPadding);
        oldHead.setPadding(0, oldPadding, 0, oldPadding);
        newHead.setPadding(0, newPadding, 0, newPadding);
        
        int oldLength = (int)getResources( ).getDimension(R.dimen.alarmAddFrag_oldPictogram);
        int newLength = (int)getResources( ).getDimension(R.dimen.alarmAddFrag_newPictogram);
        oldPictogram.requestLayout( );
        oldPictogram.getLayoutParams( ).width  = oldLength;
        oldPictogram.getLayoutParams( ).height = oldLength;
        oldPictogram.setScaleType(ImageView.ScaleType.FIT_XY);
        newPictogram.requestLayout( );
        newPictogram.getLayoutParams( ).width  = newLength;
        newPictogram.getLayoutParams( ).height = newLength;
        newPictogram.setScaleType(ImageView.ScaleType.FIT_XY);
        
        int oldNameSize = (int)getResources( ).getDimension(R.dimen.alarmAddFrag_oldCategoryName);
        int newNameSize = (int)getResources( ).getDimension(R.dimen.alarmAddFrag_newCategoryName);
        int oldInfoSize = (int)getResources( ).getDimension(R.dimen.alarmAddFrag_oldCategoryInfo);
        int newInfoSize = (int)getResources( ).getDimension(R.dimen.alarmAddFrag_newCategoryInfo);
        oldNameView.setTextSize(TypedValue.COMPLEX_UNIT_PX, oldNameSize);
        newNameView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newNameSize);
        oldInfoView.setTextSize(TypedValue.COMPLEX_UNIT_PX, oldInfoSize);
        newInfoView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newInfoSize);
        
        switch (position_oldView) {
            case TIME:
                oldView.findViewById(R.id.alarmAdd_frameLayoutTime).setVisibility(View.GONE);
                break;
            case WEEK:
                oldView.findViewById(R.id.alarmAdd_frameLayoutWeek).setVisibility(View.GONE);
                break;
            case LOCATION:
                oldView.findViewById(R.id.alarmAdd_frameLayoutLocation).setVisibility(View.GONE);
                break;
            case GROUP:
                oldView.findViewById(R.id.alarmAdd_frameLayoutGroup).setVisibility(View.GONE);
                break;
            case MEMO:
                oldView.findViewById(R.id.alarmAdd_frameLayoutMemo).setVisibility(View.GONE);
                break;
            case DETAIL:
                oldView.findViewById(R.id.alarmAdd_frameLayoutDetail).setVisibility(View.GONE);
                break;
        }
        switch (position_newView) {
            case TIME:
                newView.findViewById(R.id.alarmAdd_frameLayoutTime).setVisibility(View.VISIBLE);
                break;
            case WEEK:
                newView.findViewById(R.id.alarmAdd_frameLayoutWeek).setVisibility(View.VISIBLE);
                break;
            case LOCATION:
                newView.findViewById(R.id.alarmAdd_frameLayoutLocation).setVisibility(View.VISIBLE);
                break;
            case GROUP:
                newView.findViewById(R.id.alarmAdd_frameLayoutGroup).setVisibility(View.VISIBLE);
                break;
            case MEMO:
                newView.findViewById(R.id.alarmAdd_frameLayoutMemo).setVisibility(View.VISIBLE);
                break;
            case DETAIL:
                newView.findViewById(R.id.alarmAdd_frameLayoutDetail).setVisibility(View.VISIBLE);
                break;
        }
        
    }
}