package kr.ac.ssu.wherealarmyou.view.custom_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.group.Group;

import java.util.List;

public class GroupContentViewAdapter extends RecyclerView.Adapter<GroupContentViewAdapter.GroupContentViewHolder>
{
    private Context     context;
    private List<Group> groups;
    
    private OnGroupClickListener listener = null;
    
    public GroupContentViewAdapter(Context context, List<Group> groups)
    {
        this.context = context;
        this.groups  = groups;
    }
    
    @NonNull
    @Override
    public GroupContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext( )).inflate(R.layout.item_icon_and_title, parent, false);
        return new GroupContentViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(GroupContentViewHolder holder, int position)
    {
        Group group = groups.get(position);
        //holder.icon.setBackgroundColor(group.getIcon( ).getColorHex( ));
        holder.icon.setText(group.getIcon( ).getText( ));
        holder.name.setText(group.getName( ));
    }
    
    @Override
    public int getItemCount( )
    {
        return groups.size( );
    }
    
    public void setOnGroupClickListener(OnGroupClickListener listener)
    {
        this.listener = listener;
    }
    
    public interface OnGroupClickListener
    {
        void onItemClick(View view, Group group);
    }
    
    public class GroupContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        RelativeLayout group;
        Button         icon;
        TextView       name;
        
        public GroupContentViewHolder(View itemView)
        {
            super(itemView);
            group = itemView.findViewById(R.id.relativeLayoutGroup);
            icon  = itemView.findViewById(R.id.buttonIcon);
            name  = itemView.findViewById(R.id.textViewName);
            
            itemView.setOnClickListener(this);
        }
        
        public void onClick(View view)
        {
            if (view.getId( ) == R.id.relativeLayoutGroup
                || view.getId( ) == R.id.buttonIcon
                || view.getId( ) == R.id.textViewName) {
                int position = getAdapterPosition( );
                if ((position != RecyclerView.NO_POSITION) && (listener != null)) {
                    listener.onItemClick(view, groups.get(position));
                }
            }
        }
    }
}