package kr.ac.ssu.wherealarmyou.view.custom_view;

import android.view.View;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AlarmAddItem
{
    int    icon_resID;
    String title;
    String detail;
    View   content;
    
    public AlarmAddItem(int icon_resID, String title, String detail, View content)
    {
        this.icon_resID = icon_resID;
        this.title      = title;
        this.detail     = detail;
        this.content    = content;
    }
}
