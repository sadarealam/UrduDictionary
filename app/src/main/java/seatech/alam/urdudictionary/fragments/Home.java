package seatech.alam.urdudictionary.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import seatech.alam.urdudictionary.MainActivity;
import seatech.alam.urdudictionary.R;
import seatech.alam.urdudictionary.util.DBOpenHelper;

/**
 * Created by root on 27/9/15.
 */
public class Home extends Fragment {

    SearchView searchView;
    SearchManager searchManager;
    MainActivity activity;
    DBOpenHelper dbOpenHelper ;

    private static final String[] SUGGESTIONS = {
            "Bauru", "Sao Paulo", "Rio de Janeiro",
            "Bahia", "Mato Grosso", "Minas Gerais",
            "Tocantins", "Rio Grande do Sul"
    };
    private SimpleCursorAdapter mAdapter;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity ;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false) ;
        searchView = (SearchView) view.findViewById(R.id.searchView) ;
        searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        // Getting selected (clicked) item suggestion
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                // Your code here
                searchView.setQuery(mAdapter.getCursor().getString(0), false);
                getDifinition();
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //when user submit query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                populateAdapter(s);
                return false;
            }
        });

        return view ;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String[] from = new String[] {"word"};
        final int[] to = new int[] {android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        dbOpenHelper = new DBOpenHelper(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    // You must implements your logic to get data using OrmLite
    private void populateAdapter(String query) {
       /* final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
        for (int i=0; i<SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[] {i, SUGGESTIONS[i]});
        }*/
        Cursor c = dbOpenHelper.getWord(query);
        mAdapter.changeCursor(c);
    }

    private void getDifinition(){

        Intent intent = new Intent(activity,MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra("word","love");
        activity.startActivity(intent);
    }


}
