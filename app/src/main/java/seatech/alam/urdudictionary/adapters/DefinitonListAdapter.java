package seatech.alam.urdudictionary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import seatech.alam.urdudictionary.R;

/**
 * Created by yesalam on 1/31/16.
 */
public class DefinitonListAdapter extends ArrayAdapter<String>  {

    private String[] urdu ;
    private String[] roman ;
    private Context context ;
    private int resource ;

    public DefinitonListAdapter(Context context, int resource, String[] urdu,String[] roman) {
        super(context, resource,urdu);
        this.context = context ;
        this.roman = roman;
        this.urdu = urdu ;
        this.resource = resource ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);
        TextView romantv = (TextView) rowView.findViewById(R.id.romaninwotd) ;
        TextView urdutv = (TextView) rowView.findViewById(R.id.urduinwotd) ;

        romantv.setText(roman[position]);
        urdutv.setText(urdu[position]);


        return rowView ;
    }
}
