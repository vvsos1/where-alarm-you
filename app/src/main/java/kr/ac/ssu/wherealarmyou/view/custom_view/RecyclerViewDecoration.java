package kr.ac.ssu.wherealarmyou.view.custom_view;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class RecyclerViewDecoration extends RecyclerView.ItemDecoration
{
    private final int interval;
    
    public RecyclerViewDecoration(int interval)
    {
        this.interval = interval;
    }
    
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);
        
        if (parent.getChildAdapterPosition(view) != Objects.requireNonNull(parent.getAdapter( )).getItemCount( )) {
            outRect.top    = interval;
            outRect.bottom = interval;
        }
    }
}
