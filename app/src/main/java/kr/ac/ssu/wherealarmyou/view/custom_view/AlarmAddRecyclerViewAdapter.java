package kr.ac.ssu.wherealarmyou.view.custom_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;

import java.util.List;

public class AlarmAddRecyclerViewAdapter extends RecyclerView.Adapter<AlarmAddRecyclerViewAdapter.ViewHolder>
{
    private Context            context;
    private List<AlarmAddItem> alarmAddItems;
    
    public AlarmAddRecyclerViewAdapter(Context context, List<AlarmAddItem> alarmAddItems)
    {
        this.context       = context;
        this.alarmAddItems = alarmAddItems;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext( )).inflate(R.layout.item_alarm_add, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        AlarmAddItem alarmAddItem = alarmAddItems.get(position);
        holder.icon.setBackgroundResource(alarmAddItem.getIcon_resID( ));
        holder.title.setText(alarmAddItem.getTitle( ));
        holder.detail.setText(alarmAddItem.getDetail( ));
        holder.content.addView(alarmAddItem.getContent( ));
    }
    
    @Override
    public int getItemCount( )
    {
        return alarmAddItems.size( );
    }
    
    private void removeItemView(int position)
    {
        alarmAddItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, alarmAddItems.size( ));
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView   icon;
        TextView    title;
        TextView    detail;
        FrameLayout content;
        
        public ViewHolder(View itemView)
        {
            super(itemView);
            icon    = itemView.findViewById(R.id.iconAlarmAdd);
            title   = itemView.findViewById(R.id.titleAlarmAdd);
            detail  = itemView.findViewById(R.id.detailAlarmAdd);
            content = itemView.findViewById(R.id.contentAlarmAdd);
            
            itemView.setOnClickListener(this);
        }
        
        public void onClick(View view)
        {
            switch (view.getId( )) {
                case (R.id.itemAlarmAdd):
                    if (content.getVisibility( ) == View.GONE) {
                        content.setVisibility(View.VISIBLE);
                        return;
                    }
                    if (content.getVisibility( ) == View.VISIBLE) {
                        content.setVisibility(View.GONE);
                    }
            }
        }
    }
}