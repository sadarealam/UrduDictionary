package seatech.alam.urdudictionary.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import seatech.alam.urdudictionary.MainActivity;
import seatech.alam.urdudictionary.R;
import seatech.alam.urdudictionary.util.DBOpenHelper;

/**
 * Created by root on 27/9/15.
 */
public class Home extends Fragment {

    final String TAG = "Home Frag" ;
    SearchView searchView ;
    LinearLayout suggestionLayout ;
    MainActivity activity;
    DBOpenHelper dbOpenHelper ;
    String word ;
    CardView suggestionCard ;

    private SimpleCursorAdapter mAdapter;


    @Override
    public void onAttach(Activity activity) {
        Log.e(TAG,"onAttach for Home is called");
        super.onAttach(activity);
        this.activity = (MainActivity) activity ;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false) ;
        searchView = (SearchView) view.findViewById(R.id.searchView) ;
        suggestionCard = (CardView) view.findViewById(R.id.suggestionCard);
        suggestionCard.setVisibility(View.GONE);
        suggestionLayout = (LinearLayout) view.findViewById(R.id.suggestionCardLayout);
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        // Getting selected (clicked) item suggestion
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                // Your code here
                word = mAdapter.getCursor().getString(0);
                searchView.setQuery(word, false);
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

                searchView.clearFocus();
                activity.searchQuery(s);
                return true;
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

    private void populateAdapter(String query) {
        Cursor c = dbOpenHelper.getAllWord(query);
        mAdapter.changeCursor(c);
    }

    private void getDifinition(){
        Intent intent = new Intent(activity,MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra("word",word );
        activity.startActivity(intent);
    }

    public void setSuggestion(String query){
        Log.e(TAG, "Show suggestion for " + query);
        suggestionCard.setVisibility(View.VISIBLE);
        suggestionLayout.removeAllViews();
        Cursor cursor = dbOpenHelper.getAllWord(query);
        cursor.moveToFirst();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        while(cursor.moveToNext()){
            TextView tv = new TextView(activity);
            tv.setLayoutParams(layoutParams);
            tv.setText(cursor.getString(0));
            tv.setTextColor(Color.BLACK);
            suggestionLayout.addView(tv);
        }
    }


}
