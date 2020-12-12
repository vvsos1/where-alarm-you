package kr.ac.ssu.wherealarmyou.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.location.Location;
import kr.ac.ssu.wherealarmyou.view.DataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class AlarmItemAdapter extends RecyclerView.Adapter<AlarmItemAdapter.AlarmViewHolder>
{
    private final DataManager dataManager = DataManager.getInstance( );
    
    private Context context;
    
    private List<Alarm> alarms;
    
    private OnItemClickListener onItemClickListener = null;
    
    private OnSwitchCheckedChangeListener onSwitchCheckedChangeListener = null;
    
    public AlarmItemAdapter(Context context, List<Alarm> alarms)
    {
        this.context = context;
        this.alarms  = alarms;
    }
    
    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext( )).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position)
    {
        Alarm alarm = alarms.get(position);
        
        for (Group group : Objects.requireNonNull(dataManager.getGroupData( ).getValue( ))) {
            if (Objects.equals(alarm.getGroupUid( ), group.getUid( ))) {
                holder.textViewGroup.setText(group.getName( ));
            }
        }
        
        boolean isInclude = true;
        List<Location> locations = new ArrayList<>( );
        if (alarm.getLocationCondition( ).isInclude( )) {
            Set<String> uidSet = alarm.getLocationCondition( ).getInclude( ).keySet( );
            locations = Objects.requireNonNull(dataManager.getLocationData( ).getValue( )).stream( ).filter(location ->
                    uidSet.contains(location.getUid( ))).collect(Collectors.toList( ));
        }
        else if (!alarm.getLocationCondition( ).isInclude()) {
            isInclude = false;
            Set<String> uidSet = alarm.getLocationCondition( ).getExclude( ).keySet( );
            locations = Objects.requireNonNull(dataManager.getLocationData( ).getValue( )).stream( ).filter(location ->
                    uidSet.contains(location.getUid( ))).collect(Collectors.toList( ));
        }
        for (Location location : locations) {
            String origin = (String)holder.textViewLocation.getText( );
            holder.textViewLocation.setText(origin + " " + location.getTitle( ));
            if (!isInclude) {
                String old = (String)holder.textViewLocation.getText( );
                holder.textViewLocation.setText(old + "밖에서");
            }
        }
        
        holder.textViewHours.setText(alarm.getTime( ).getHours( ).toString( ));
        holder.textViewMinutes.setText(alarm.getTime( ).getMinutes( ).toString( ));
        holder.textViewLocation.setText("미구현");
        holder.aSwitch.setChecked(alarm.getIsSwitchOn( ));
        holder.aSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                onSwitchCheckedChangeListener.onSwitchCheckedChangeListener(alarm, isChecked));
    }
    
    @Override
    public int getItemCount( )
    {
        return alarms.size( );
    }
    
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }
    
    public void setOnSwitchCheckedChangeListener(OnSwitchCheckedChangeListener listener)
    {
        this.onSwitchCheckedChangeListener = listener;
    }
    
    public interface OnItemClickListener
    {
        void onItemClick(View view, Alarm alarm);
    }
    
    public interface OnSwitchCheckedChangeListener
    {
        void onSwitchCheckedChangeListener(Alarm alarm, boolean isChecked);
    }
    
    public static class AlarmViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewTitle;
        
        TextView textViewAmPm;
        TextView textViewHours;
        TextView textViewMinutes;
        
        TextView textViewSun;
        TextView textViewMon;
        TextView textViewTue;
        TextView textViewWed;
        TextView textViewThu;
        TextView textViewFri;
        TextView textViewSat;
        
        TextView textViewLocation;
        TextView textViewGroup;
        
        Switch aSwitch;
        
        public AlarmViewHolder(View itemView)
        {
            super(itemView);
            textViewTitle    = itemView.findViewById(R.id.item_alarm_textViewTitle);
            textViewAmPm     = itemView.findViewById(R.id.item_alarm_textViewAmPm);
            textViewHours    = itemView.findViewById(R.id.item_alarm_textViewHours);
            textViewMinutes  = itemView.findViewById(R.id.item_alarm_textViewMinutes);
            textViewSun      = itemView.findViewById(R.id.item_alarm_textViewSun);
            textViewMon      = itemView.findViewById(R.id.item_alarm_textViewMon);
            textViewTue      = itemView.findViewById(R.id.item_alarm_textViewTue);
            textViewWed      = itemView.findViewById(R.id.item_alarm_textViewWed);
            textViewThu      = itemView.findViewById(R.id.item_alarm_textViewThu);
            textViewFri      = itemView.findViewById(R.id.item_alarm_textViewFri);
            textViewSat      = itemView.findViewById(R.id.item_alarm_textViewSat);
            textViewLocation = itemView.findViewById(R.id.item_alarm_textViewLocation);
            textViewGroup    = itemView.findViewById(R.id.item_alarm_textViewGroup);
            
            aSwitch = itemView.findViewById(R.id.item_alarm_switch);
        }
    }
}