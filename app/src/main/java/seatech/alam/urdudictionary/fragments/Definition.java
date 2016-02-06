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
import android.widget.ListView;
import android.widget.TextView;

import seatech.alam.urdudictionary.MainActivity;
import seatech.alam.urdudictionary.R;
import seatech.alam.urdudictionary.adapters.DefinitonListAdapter;
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
    ListView definitionList ;
    DefinitonListAdapter listAdapter ;

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

        definitionList = (ListView) view.findViewById(R.id.definitionList);


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

        listAdapter = new DefinitonListAdapter(getActivity(),R.layout.twotvrow,urdulist,romanlist);
        definitionList.setAdapter(listAdapter);



    }

}
