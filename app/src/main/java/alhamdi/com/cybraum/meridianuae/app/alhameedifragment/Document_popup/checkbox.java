package alhamdi.com.cybraum.meridianuae.app.alhameedifragment.Document_popup;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import alhamdi.com.cybraum.meridianuae.app.alhameedifragment.R;

/**
 * Created by anvin on 3/3/2017.
 */

public class checkbox extends CheckBox {



    public checkbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setButtonDrawable(new StateListDrawable());
    }
    @Override
    public void setChecked(boolean t){
        if(t)
        {
            this.setBackgroundResource(R.drawable.select);
        }
        else
        {
            this.setBackgroundResource(R.drawable.deselect);
        }
        super.setChecked(t);
    }
}