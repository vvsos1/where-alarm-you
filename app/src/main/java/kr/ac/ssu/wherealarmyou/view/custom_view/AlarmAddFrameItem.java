package kr.ac.ssu.wherealarmyou.view.custom_view;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AlarmAddFrameItem
{
    int    pictogram_resID;
    String categoryName;
    String categoryInfo;
    
    public AlarmAddFrameItem(int pictogram_resID, String categoryName, String categoryInfo)
    {
        this.pictogram_resID = pictogram_resID;
        this.categoryName    = categoryName;
        this.categoryInfo    = categoryInfo;
    }
}
