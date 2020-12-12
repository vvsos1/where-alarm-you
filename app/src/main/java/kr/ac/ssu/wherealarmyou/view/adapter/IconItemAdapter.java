package kr.ac.ssu.wherealarmyou.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.common.Icon;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IconItemAdapter extends RecyclerView.Adapter<IconItemAdapter.IconContentViewHolder>
{
    private MutableLiveData<Boolean> iconSelected = new MutableLiveData<>( );
    
    private AtomicInteger selectedPosition = new AtomicInteger( );
    
    private Context context;
    
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
        
        iconSelected.observeForever(iconSelected -> {
            if (iconSelected) {
                if (position == selectedPosition.get( )) {
                    holder.blind.setVisibility(View.INVISIBLE);
                }
                else { holder.blind.setVisibility(View.VISIBLE); }
            }
            else { holder.blind.setVisibility(View.INVISIBLE); }
        });
        
        GradientDrawable drawable = (GradientDrawable)holder.buttonIcon.getBackground( );
        drawable.setColor(Color.parseColor(icon.getColorHex( )));
        holder.buttonIcon.setOnClickListener(view -> {
            if ((position != RecyclerView.NO_POSITION) && (listener != null)) {
                listener.onItemClick(position, icon);
            }
        });
    }
    
    public void iconSelected(Boolean iconSelected, int position)
    {
        selectedPosition.set(position);
        this.iconSelected.setValue(iconSelected);
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
        void onItemClick(int position, Icon icon);
    }
    
    public static class IconContentViewHolder extends RecyclerView.ViewHolder
    {
        View   blind;
        Button buttonIcon;
        
        public IconContentViewHolder(View itemView)
        {
            super(itemView);
            blind      = itemView.findViewById(R.id.item_icon_blind);
            buttonIcon = itemView.findViewById(R.id.item_icon_buttonIcon);
        }
    }
}