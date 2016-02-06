package seatech.alam.urdudictionary.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.SQLException;

import seatech.alam.urdudictionary.MainActivity;
import seatech.alam.urdudictionary.R;
import seatech.alam.urdudictionary.adapters.CursorAdapterPlus;
import seatech.alam.urdudictionary.util.Globals;

/**
 * Created by root on 27/9/15.
 */
public class Favourite extends Fragment implements AdapterView.OnItemClickListener {

    private final String TAG = "FAVOURITE" ;
    private ListView fabList ;
    private CardView noFabCard ;
    private MainActivity activity ;
    private CursorAdapterPlus adapter ;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity ;
    }

    @Override
    public void onResume() {
        super.onResume();
        setFavouritData();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourite_fragment,container,false) ;
        noFabCard = (CardView) view.findViewById(R.id.nofavouriteCard) ;
        fabList = (ListView) view.findViewById(R.id.favouriteList) ;
        fabList.setOnItemClickListener(this);

        return view ;
    }

    public void setFavouritData(){
        try {
            Cursor cursor = activity.userData.getAllValues(Globals.TYPE_FAB) ;
            if(cursor.moveToFirst()){
                noFabCard.setVisibility(View.GONE);
                adapter = new CursorAdapterPlus(getActivity(),R.layout.fab_row_layout,cursor ,new String[]{"STAMP","WORD"},new int[]{R.id.row_time,R.id.row_word}) ;
                fabList.setAdapter(adapter);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dataSetChanged(){
        try {
            Cursor cursor = activity.userData.getAllValues(Globals.TYPE_FAB) ;
            if(cursor.moveToFirst()){
                noFabCard.setVisibility(View.GONE);
                adapter = new CursorAdapterPlus(getActivity(),R.layout.fab_row_layout,cursor ,new String[]{"STAMP","WORD"},new int[]{R.id.row_time,R.id.row_word}) ;
                fabList.setAdapter(adapter);
            } else {
                noFabCard.setVisibility(View.VISIBLE);
                adapter = new CursorAdapterPlus(getActivity(),R.layout.fab_row_layout,cursor ,new String[]{"STAMP","WORD"},new int[]{R.id.row_time,R.id.row_word}) ;
                fabList.setAdapter(adapter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) fabList.getItemAtPosition(position);
        String word = cursor.getString(3);
        activity.setGData(word);
        activity.startDefinition();
    }
}
