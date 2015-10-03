package seatech.alam.urdudictionary.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import seatech.alam.urdudictionary.MainActivity;
import seatech.alam.urdudictionary.R;
import seatech.alam.urdudictionary.util.DBOpenHelper;

/**
 * Created by root on 27/9/15.
 */
public class Definition extends Fragment {

    MainActivity activity;
    TextView word;
    TextView urdu;
    TextView roman;
    String wordt;
    DBOpenHelper dbOpenHelper;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity= (MainActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbOpenHelper = new DBOpenHelper(activity);
        wordt=activity.word;
        Log.e("Definition", wordt);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.definiton_fragment,container,false);
        word = (TextView) view.findViewById(R.id.defineword);
        urdu= (TextView) view.findViewById(R.id.defineurdu);
        roman= (TextView) view.findViewById(R.id.defineroman);

        Cursor cursor = dbOpenHelper.getDetail(wordt);
        cursor.moveToFirst();
        word.setText(cursor.getString(1));
        roman.setText(cursor.getString(2));
        urdu.setText(cursor.getString(3));

        return view;

    }
}
