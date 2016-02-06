package seatech.alam.urdudictionary.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.widget.TextView;

import com.ocpsoft.pretty.time.PrettyTime;

import java.util.Date;

import seatech.alam.urdudictionary.R;
import seatech.alam.urdudictionary.util.HumanTime;

/**
 * Created by yesalam on 1/29/16.
 */
public class CursorAdapterPlus extends SimpleCursorAdapter {
    public CursorAdapterPlus(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    @Override
    public void setViewText(TextView v, String paramtext) {

        String text = paramtext ;
        if(v.getId() == R.id.row_time){
            PrettyTime p = new PrettyTime();
            v.setTag(text);
            //Log.e("Adapter", p.format(new Date(Long.parseLong(text))));
            text = p.format(new Date(Long.parseLong(text)));
        }
        v.setText(text);

    }
}
