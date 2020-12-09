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
import kr.ac.ssu.wherealarmyou.group.Group;

import java.util.List;

public class IconItemAdapter extends RecyclerView.Adapter<IconItemAdapter.IconContentViewHolder>
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
    public IconContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext( )).inflate(R.layout.item_icon, parent, false);
        return new IconContentViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull IconContentViewHolder holder, int position)
    {
        Icon icon = icons.get(position);
        
        holder.buttonIcon.setText(icon.getText( ));
        
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
    
    public static class IconContentViewHolder extends RecyclerView.ViewHolder
    {
        Button buttonIcon;
        
        public IconContentViewHolder(View itemView)
        {
            super(itemView);
            buttonIcon = itemView.findViewById(R.id.item_icon_buttonIcon);
        }
    }
}