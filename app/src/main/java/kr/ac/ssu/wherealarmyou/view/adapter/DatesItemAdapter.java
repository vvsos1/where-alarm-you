package kr.ac.ssu.wherealarmyou.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.Date;

public class DatesItemAdapter extends RecyclerView.Adapter<DatesItemAdapter.ViewHolder> {


    List<Date> dates;
    private OnItemClickListener listener = null;


    public DatesItemAdapter(List<Date> dates) {
        this.dates = dates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Date date = dates.get(position);
        holder.textView.setText(date.toString());
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Date date, int position);

        void onCancelClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView textView;
        Button cancelButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_date);
            cancelButton = itemView.findViewById(R.id.item_date_cancel);
            textView.setOnClickListener(v -> {
                listener.onItemClick(dates.get(getAdapterPosition()), getAdapterPosition());
            });

            cancelButton.setOnClickListener(v -> {
                listener.onCancelClick(getAdapterPosition());
            });
        }
    }


}


