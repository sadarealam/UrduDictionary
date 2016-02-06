package seatech.alam.urdudictionary.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;

import seatech.alam.urdudictionary.MainActivity;
import seatech.alam.urdudictionary.R;
import seatech.alam.urdudictionary.adapters.CursorAdapterPlus;
import seatech.alam.urdudictionary.util.Globals;

import static seatech.alam.urdudictionary.R.id.historyList;

/**
 * Created by root on 27/9/15.
 */
public class History extends Fragment implements AdapterView.OnItemClickListener {

    private CardView noHistory ;
    private ListView historyListView ;
    private MainActivity activity ;

    CursorAdapterPlus adapter ;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment,container,false);
        noHistory = (CardView) view.findViewById(R.id.nohistoryCard) ;
        historyListView = (ListView) view.findViewById(historyList);
        historyListView.setOnItemClickListener(this);
        return  view ;
    }

    @Override
    public void onResume() {
        super.onResume();
        setHistoryData();
    }

    public void setHistoryData(){
        try {
            Cursor cursor  = activity.userData.getAllValues(Globals.TYPE_HISTORY)  ;
            if(cursor.moveToFirst()){
                adapter = new CursorAdapterPlus(getActivity(),R.layout.fab_row_layout,cursor,new String[]{"STAMP","WORD"},new int[]{R.id.row_time,R.id.row_word}) ;
                historyListView.setAdapter(adapter);
                noHistory.setVisibility(View.GONE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dataSetChanged(){
        try {
            Cursor cursor = activity.userData.getAllValues(Globals.TYPE_HISTORY) ;
            if(cursor.moveToFirst()){
                noHistory.setVisibility(View.GONE);
                adapter = new CursorAdapterPlus(getActivity(),R.layout.fab_row_layout,cursor ,new String[]{"STAMP","WORD"},new int[]{R.id.row_time,R.id.row_word}) ;
                historyListView.setAdapter(adapter);
            } else {
                noHistory.setVisibility(View.VISIBLE);
                adapter = new CursorAdapterPlus(getActivity(),R.layout.fab_row_layout,cursor ,new String[]{"STAMP","WORD"},new int[]{R.id.row_time,R.id.row_word}) ;
                historyListView.setAdapter(adapter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) historyListView.getItemAtPosition(position);
        String word = cursor.getString(3);
        activity.setGData(word);
        activity.startDefinition();
    }
}
