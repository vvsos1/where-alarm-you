package kr.ac.ssu.wherealarmyou.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.group.Group;
import kr.ac.ssu.wherealarmyou.location.Location;

import java.util.List;

public class LocationItemAdapter extends RecyclerView.Adapter<LocationItemAdapter.LocationContentViewHolder>
{
    private Context context;
    
    private List<Location> locations;
    
    private OnItemClickListener listener = null;
    
    public LocationItemAdapter(Context context, List<Location> locations)
    {
        this.context   = context;
        this.locations = locations;
    }
    
    @NonNull
    @Override
    public LocationContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext( )).inflate(R.layout.item_icon_and_title, parent, false);
        return new LocationContentViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(LocationContentViewHolder holder, int position)
    {
        Location location = locations.get(position);
        
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in_fast);
        holder.layout.setAnimation(animation);
        
        GradientDrawable drawable = (GradientDrawable)holder.icon.getBackground( );
        drawable.setColor(Color.parseColor(location.getIcon( ).getColorHex( )));
        
        holder.icon.setText(location.getIcon( ).getText( ));
//        holder.name.setText(location.getName( ));
    }
    
    @Override
    public int getItemCount( )
    {
        return locations.size( );
    }
    
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }
    
    public interface OnItemClickListener
    {
        void onItemClick(View view, Location location);
    }
    
    public class LocationContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        RelativeLayout layout;
        Button         icon;
        TextView       name;
        
        public LocationContentViewHolder(View itemView)
        {
            super(itemView);
            layout = itemView.findViewById(R.id.item_icon_and_title_relativeLayoutParent);
            icon   = itemView.findViewById(R.id.item_icon_and_title_buttonIcon);
            name   = itemView.findViewById(R.id.item_icon_and_title_textViewTitle);
            
            icon.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }
        
        public void onClick(View view)
        {
            if (view.getId( ) == R.id.item_icon_and_title_relativeLayoutParent
                || view.getId( ) == R.id.item_icon_and_title_buttonIcon
                || view.getId( ) == R.id.item_icon_and_title_textViewTitle) {
                int position = getAdapterPosition( );
                if ((position != RecyclerView.NO_POSITION) && (listener != null)) {
                    listener.onItemClick(view, locations.get(position));
                }
            }
        }
    }
}