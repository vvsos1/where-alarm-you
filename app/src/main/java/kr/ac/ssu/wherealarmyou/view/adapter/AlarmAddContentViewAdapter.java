package kr.ac.ssu.wherealarmyou.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.view.custom_view.AlarmAddFrameItem;

import java.util.List;

public class AlarmAddContentViewAdapter extends RecyclerView.Adapter<AlarmAddContentViewAdapter.AlarmAddContentViewHolder>
{
    private Context context;
    
    private List<AlarmAddFrameItem> frameItems;
    
    private OnItemClickListener onItemClickListener = null;
    
    public AlarmAddContentViewAdapter(Context context, List<AlarmAddFrameItem> frameItems)
    {
        this.context    = context;
        this.frameItems = frameItems;
    }
    
    @NonNull
    @Override
    public AlarmAddContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext( )).inflate(R.layout.item_alarm_add_frame, parent, false);
        //view.findViewById(R.id.headAlarmAddFrameItem)
        return new AlarmAddContentViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(AlarmAddContentViewHolder holder, int position)
    {
        AlarmAddFrameItem alarmAddItem = frameItems.get(position);
        
        holder.pictogram.setBackgroundResource(alarmAddItem.getPictogram_resID( ));
        holder.categoryName.setText(alarmAddItem.getCategoryName( ));
        holder.categoryInfo.setText(alarmAddItem.getCategoryInfo( ));
        holder.contentHead.setOnClickListener(view -> onItemClickListener.onItemClick(view, position));
    }
    
    @Override
    public int getItemCount( )
    {
        return frameItems.size( );
    }
    
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }
    
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }
    
    public static class AlarmAddContentViewHolder extends RecyclerView.ViewHolder
    {
        ImageView      pictogram;
        TextView       categoryName;
        TextView       categoryInfo;
        RelativeLayout contentHead;
        FrameLayout    contentFrame;
        
        public AlarmAddContentViewHolder(View itemView)
        {
            super(itemView);
            pictogram    = itemView.findViewById(R.id.item_alarmAddCategory_imageViewPictogram);
            categoryName = itemView.findViewById(R.id.item_alarmAddCategory_textViewName);
            categoryInfo = itemView.findViewById(R.id.item_alarmAddCategory_textViewInfo);
            contentHead  = itemView.findViewById(R.id.item_alarmAddCategory_relativeLayoutHead);
            contentFrame = itemView.findViewById(R.id.item_alarmAddCategory_frameLayoutContent);
        }
    }
}