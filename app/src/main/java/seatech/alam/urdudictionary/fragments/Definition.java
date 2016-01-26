package seatech.alam.urdudictionary.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import seatech.alam.urdudictionary.MainActivity;
import seatech.alam.urdudictionary.R;
import seatech.alam.urdudictionary.util.Globals;

/**
 * Created by root on 27/9/15.
 */
public class Definition extends Fragment {

    final String TAG = "Definition" ;
    MainActivity activity;
    TextView wordtv;
    TextView urdutv;
    TextView romantv;
    LinearLayout definitionHolder;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity= (MainActivity) activity;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart() called ");
        //setDefinition();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume() called ");
        definitionHolder.removeAllViews();
        setDefinition();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.definiton_fragment,container,false);
        wordtv = (TextView) view.findViewById(R.id.defineword);
        urdutv = (TextView) view.findViewById(R.id.defineurdu);
        romantv = (TextView) view.findViewById(R.id.defineroman);
        definitionHolder = (LinearLayout) view.findViewById(R.id.definitionholder);

        urdutv.setVisibility(View.GONE);
        romantv.setVisibility(View.GONE);

        setDefinition();

        return view;

    }


    private void setDefinition(){
        String word = Globals.current_word ;
        String urdu = Globals.urdu ;
        String roman = Globals.roman ;

        String[] urdulist= urdu.split("\n");
        String[] romanlist = roman.split("\n");
        for(String itme:romanlist){
            Log.e(TAG,itme);
        }

        wordtv.setText(word);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i=0;i<urdulist.length;i++){
            TextView tvu = new TextView(activity);
            tvu.setLayoutParams(layoutParams);
            tvu.setTextColor(Color.RED);
            tvu.setTextSize(20);
            tvu.setText(urdulist[i]);

            TextView tvr = new TextView(activity);
            tvr.setLayoutParams(layoutParams);
            tvr.setTextColor(Color.BLACK);
            tvr.setTextSize(20);
            tvr.setText(romanlist[i]);

            definitionHolder.addView(tvu);
            definitionHolder.addView(tvr);
        }


    }

}
