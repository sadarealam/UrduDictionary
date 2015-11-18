package seatech.alam.urdudictionary.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import seatech.alam.urdudictionary.R;
import seatech.alam.urdudictionary.util.DModel;

/**
 * Created by yesalam on 18-11-2015.
 */
public class DListAdapter extends ArrayAdapter<DModel> {

    Context context ;
    DModel[] data;
    int resourceId ;

    public DListAdapter(Context context, int resource,DModel[] data) {
        super(context, resource);
        this.context = context ;
        this.data = data ;
        this.resourceId = resource ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // return super.getView(position, convertView, parent);
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resourceId, parent, false);
        }

        // object item based on the position



        // get the TextView and then set the text (item name) and tag (item ID) values

        TextView textViewurdu= (TextView) convertView.findViewById(R.id.defineurdu);
        TextView textViewroman = (TextView) convertView.findViewById(R.id.defineroman);

        textViewurdu.setText(data[position].getUrdu());
        textViewroman.setText(data[position].getRoman());


        return convertView;

    }

    @Override
    public int getCount() {
        return data.length ;
    }
}
