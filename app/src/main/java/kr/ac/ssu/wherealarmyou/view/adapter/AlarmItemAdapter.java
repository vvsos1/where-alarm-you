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

import java.util.List;

public class AlarmItemAdapter extends RecyclerView.Adapter<AlarmItemAdapter.AlarmViewHolder>
{
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
        
        holder.textViewHours.setText(alarm.getTime( ).getHours( ).toString( ));
        holder.textViewMinutes.setText(alarm.getTime( ).getMinutes( ).toString( ));
        holder.textViewLocation.setText("미구현");
        holder.textViewGroup.setText(alarm.getGroupUid( ));
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