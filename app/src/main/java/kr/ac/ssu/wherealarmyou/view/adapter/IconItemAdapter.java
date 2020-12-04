package kr.ac.ssu.wherealarmyou.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.common.Icon;

import java.util.List;

public class IconItemAdapter extends RecyclerView.Adapter<IconItemAdapter.GroupContentViewHolder>
{
    private Context    context;
    private List<Icon> icons;
    
    private OnItemClickListener listener = null;
    
    public IconItemAdapter(Context context, List<Icon> icons)
    {
        this.context = context;
        this.icons   = icons;
    }
    
    @NonNull
    @Override
    public GroupContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext( )).inflate(R.layout.item_icon_no_text, parent, false);
        return new GroupContentViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull GroupContentViewHolder holder, int position)
    {
        Icon icon = icons.get(position);
        
        GradientDrawable drawable = (GradientDrawable)holder.buttonIcon.getBackground( );
        drawable.setColor(Color.parseColor(icon.getColorHex( )));
        holder.buttonIcon.setOnClickListener(view -> {
            if ((position != RecyclerView.NO_POSITION) && (listener != null)) {
                listener.onItemClick(view, icon);
            }
        });
    }
    
    @Override
    public int getItemCount( )
    {
        return icons.size( );
    }
    
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }
    
    public interface OnItemClickListener
    {
        void onItemClick(View view, Icon icon);
    }
    
    public static class GroupContentViewHolder extends RecyclerView.ViewHolder
    {
        Button buttonIcon;
        
        public GroupContentViewHolder(View itemView)
        {
            super(itemView);
            buttonIcon = itemView.findViewById(R.id.item_icon_icon);
        }
    }
}