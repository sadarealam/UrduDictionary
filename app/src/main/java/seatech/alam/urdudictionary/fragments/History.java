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
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;

import seatech.alam.urdudictionary.MainActivity;
import seatech.alam.urdudictionary.R;
import seatech.alam.urdudictionary.util.Globals;

import static seatech.alam.urdudictionary.R.id.historyList;

/**
 * Created by root on 27/9/15.
 */
public class History extends Fragment {

    private CardView noHistory ;
    private ListView historyListView ;
    private MainActivity activity ;
    Cursor cursor ;
    SimpleCursorAdapter adapter ;

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
        try {
            cursor = activity.userData.getAllValues(Globals.TYPE_HISTORY)  ;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(cursor.moveToFirst()){
            adapter = new SimpleCursorAdapter(getActivity(),R.layout.fab_row_layout,cursor,new String[]{"STAMP","WORD"},new int[]{R.id.row_time,R.id.row_word}) ;
            historyListView.setAdapter(adapter);
            noHistory.setVisibility(View.GONE);
        }


        return  view ;
    }
}
