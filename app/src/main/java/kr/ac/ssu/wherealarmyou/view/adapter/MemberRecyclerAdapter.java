package kr.ac.ssu.wherealarmyou.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.user.User;
import kr.ac.ssu.wherealarmyou.user.service.UserService;

public class MemberRecyclerAdapter extends RecyclerView.Adapter<MemberRecyclerAdapter.MemberViewHolder>
{
    public MutableLiveData<Boolean> showMemberManageButton = new MutableLiveData<>( );
    
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
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        String memberUid = members.get(position);

        //TODO: 유저객체 생성자에 맞게 변경
        AtomicReference<User> user = new AtomicReference<>(null);

        UserService userService = UserService.getInstance();
        userService.findUser(memberUid)
                .doOnSuccess(user_ -> {
                    user.set(user_);
                    holder.textViewName.setText(user.get().getName());
                })
                .subscribe();

        LiveData<Boolean> booleanLiveData = showMemberManageButton;
        booleanLiveData.observeForever(aBoolean -> {
            if (showMemberManageButton.getValue( ) == Boolean.TRUE) {
                holder.buttonCheck.setVisibility(View.VISIBLE);
                holder.buttonCancel.setVisibility(View.VISIBLE);
            }
        });
        
        holder.buttonCheck.setOnClickListener(view -> listener.onItemClick(view, memberUid));
        holder.buttonCancel.setOnClickListener(view -> Toast.makeText(context, "취소(미구현)", Toast.LENGTH_SHORT).show( ));
    }
    
    public void bind(Boolean aBoolean)
    {
        showMemberManageButton.setValue(aBoolean);
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
        void onItemClick(View view, String userUid);
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