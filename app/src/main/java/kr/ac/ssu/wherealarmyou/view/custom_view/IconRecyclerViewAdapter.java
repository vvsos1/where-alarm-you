package kr.ac.ssu.wherealarmyou.view.custom_view;

import android.content.Context;
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

public class IconRecyclerViewAdapter extends RecyclerView.Adapter<IconRecyclerViewAdapter.GroupContentViewHolder>
{
    private Context    context;
    private List<Icon> icons;
    
    private OnItemClickListener listener = null;
    
    public IconRecyclerViewAdapter(Context context, List<Icon> icons)
    {
        this.context = context;
        this.icons   = icons;
    }
    
    @NonNull
    @Override
    public GroupContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext( )).inflate(R.layout.item_icon, parent, false);
        return new GroupContentViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull GroupContentViewHolder holder, int position)
    {
        Icon icon = icons.get(position);
        //holder.icon.setBackgroundColor(group.getIcon( ).getColorHex( ));
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
    
    public class GroupContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        Button icon;
        
        public GroupContentViewHolder(View itemView)
        {
            super(itemView);
            icon = itemView.findViewById(R.id.item_icon_icon);
            icon.setOnClickListener(this);
        }
        
        public void onClick(View view)
        {
            if (view.getId( ) == R.id.item_icon_icon) {
                int position = getAdapterPosition( );
                if ((position != RecyclerView.NO_POSITION) && (listener != null)) {
                    listener.onItemClick(view, icons.get(position));
                }
            }
        }
    }
}