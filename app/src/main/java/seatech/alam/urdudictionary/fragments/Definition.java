package seatech.alam.urdudictionary.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import seatech.alam.urdudictionary.MainActivity;
import seatech.alam.urdudictionary.R;
import seatech.alam.urdudictionary.adapters.DListAdapter;
import seatech.alam.urdudictionary.util.DBOpenHelper;
import seatech.alam.urdudictionary.util.DModel;

/**
 * Created by root on 27/9/15.
 */
public class Definition extends Fragment {

    final String TAG = "Definition" ;
    MainActivity activity;
    TextView word;
    TextView urdu;
    TextView roman;
    ListView definitionList ;
    String wordt;
    DBOpenHelper dbOpenHelper;
    DListAdapter listAdapter ;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity= (MainActivity) activity;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart() called ");
        wordt=activity.word;
        Cursor cursor = dbOpenHelper.getDetail(wordt);
        cursor.moveToFirst();
        word.setText(cursor.getString(1));
       // roman.setText(cursor.getString(2));
       // urdu.setText(cursor.getString(3));

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume() called ");
        wordt=activity.word;
        Cursor cursor = dbOpenHelper.getDetail(wordt);
        cursor.moveToFirst();
        word.setText(cursor.getString(1));
       // roman.setText(cursor.getString(2));
       // urdu.setText(cursor.getString(3));
        definitionList.removeAllViewsInLayout();
        String roman[] = cursor.getString(2).split("\n");
        String urdu[] = cursor.getString(3).split("\n");
        DModel[] model = new DModel[roman.length];
        for(int i=0;i<roman.length;i++){
           model[i] = new DModel(urdu[i],roman[i]);
        }
        Log.e(TAG,roman.length+" no of roman ");
        listAdapter = new DListAdapter(getContext(),R.layout.two_line_text_view,model);
        definitionList.setAdapter(listAdapter);

       /* LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i=0;i<roman.length;i++){
            TextView tvu = new TextView(activity);
            tvu.setLayoutParams(layoutParams);
            tvu.setText(urdu[i]);
            tvu.setTextColor(Color.BLACK);
            definitionList.addView(tvu);
            TextView tvr = new TextView(activity);
            tvr.setLayoutParams(layoutParams);
            tvr.setText(roman[i]);
            tvr.setTextColor(Color.BLACK);
            definitionList.addView(tvr);
        }*/
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
        definitionList = (ListView) view.findViewById(R.id.definitionList);

        Cursor cursor = dbOpenHelper.getDetail(wordt);
        cursor.moveToFirst();
        word.setText(cursor.getString(1));
        //roman.setText(cursor.getString(2));
        //urdu.setText(cursor.getString(3));

        String roman[] = cursor.getString(2).split("\n");
        String urdu[] = cursor.getString(3).split("\n");
        Log.e(TAG, roman.length + " no of roman ");

        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        DModel[] model = new DModel[roman.length];
        for(int i=0;i<roman.length;i++){
            model[i] = new DModel(urdu[i],roman[i]);
        }
        Log.e(TAG,roman.length+" no of roman ");
        listAdapter = new DListAdapter(getContext(),R.layout.two_line_text_view,model);
        definitionList.setAdapter(listAdapter);

        return view;

        // LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //suggestionLayout.removeAllViewsInLayout();
        /*while(cursor.moveToNext()){
            TextView tv = new TextView(activity);
            tv.setLayoutParams(layoutParams);
            tv.setText(cursor.getString(0));
            tv.setTextColor(Color.BLACK);
            suggestionLayout.addView(tv);
        }*/

    }
}
