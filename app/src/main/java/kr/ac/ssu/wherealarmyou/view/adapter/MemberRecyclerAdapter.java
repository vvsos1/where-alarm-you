package kr.ac.ssu.wherealarmyou.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.common.Icon;
import kr.ac.ssu.wherealarmyou.user.User;
import kr.ac.ssu.wherealarmyou.user.service.UserService;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MemberRecyclerAdapter extends RecyclerView.Adapter<MemberRecyclerAdapter.MemberViewHolder>
{
    private Context context;
    
    private List<String> members;
    
    private OnItemClickListener listener = null;
    
    public MemberRecyclerAdapter(Context context, List<String> members)
    {
        this.context = context;
        this.members = members;
    }
    
    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext( )).inflate(R.layout.item_name_and_button, parent, false);
        return new MemberViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position)
    {
        String memberUid = members.get(position);
        
        AtomicReference<User> user = new AtomicReference<>(new User( ));
        
        UserService userService = UserService.getInstance( );
        userService.findUser(memberUid)
                   .doOnSuccess(user_ -> {
                       user.set(user_);
                       holder.textViewName.setText(user.get( ).getName( ));
                   })
                   .subscribe( );
    }
    
    @Override
    public int getItemCount( )
    {
        return members.size( );
    }
    
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }
    
    public interface OnItemClickListener
    {
        void onItemClick(View view, Icon icon);
    }
    
    public static class MemberViewHolder extends RecyclerView.ViewHolder
    {
        
        TextView textViewName;
        Button   buttonCheck;
        Button   buttonCancel;
        
        public MemberViewHolder(View itemView)
        {
            super(itemView);
            textViewName = itemView.findViewById(R.id.item_name_and_button_textViewName);
            buttonCheck  = itemView.findViewById(R.id.item_name_and_button_buttonCheck);
            buttonCancel = itemView.findViewById(R.id.item_name_and_button_buttonCancel);
        }
    }
}