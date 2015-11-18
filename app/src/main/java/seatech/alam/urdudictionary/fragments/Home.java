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
import android.widget.ListView;
import android.widget.TextView;

import seatech.alam.urdudictionary.MainActivity;
import seatech.alam.urdudictionary.R;
import seatech.alam.urdudictionary.util.DBOpenHelper;

/**
 * Created by root on 27/9/15.
 */
public class Home extends Fragment {

    final String TAG = "Home Frag" ;

    //View's reference from java code
    SearchView searchView;
    ListView suggestionList ;
    TextView suggestionLabel ;
    CardView suggestionCard ;
    CardView pwotdCard ;


    int temp =0 ;

    MainActivity activity;

    DBOpenHelper dbOpenHelper ;
    String word ;


    private SimpleCursorAdapter mAdapter;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity ;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false) ;
        //initializing the views
        searchView = (SearchView) view.findViewById(R.id.searchView) ;
        suggestionCard = (CardView) view.findViewById(R.id.suggestionCard);
        pwotdCard = (CardView) view.findViewById(R.id.pwotdcard);
        suggestionList = (ListView) view.findViewById(R.id.suggestionList);
        suggestionLabel = (TextView) view.findViewById(R.id.suggestionLabel) ;
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
                setSuggestion(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.length()<=2){
                    //Safeguard for showing suggestion when query length is too small .
                    return false ;
                }
                if(temp-s.length() <=0 ){
                    populateAdapter(s);
                }else {
                    mAdapter.changeCursor(null);
                }
                temp = s.length();
                return true;
            }
        });

        //Hiding the view that are not require initially
        suggestionCard.setVisibility(View.GONE);
        pwotdCard.setVisibility(View.GONE);
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
        dbOpenHelper = activity.dbOpenHelper ;
    }



    private  boolean populateAdapter(String query) {
        Cursor c = dbOpenHelper.getAllWord(query);
        try {
            mAdapter.changeCursor(c);
        } catch (Exception e){
            Log.e(TAG,e.getMessage());
            return false ;
        }
        return true ;
    }

    private void getDifinition(){
        Intent intent = new Intent(activity,MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra("word",word );
        activity.startActivity(intent);
    }


    public void setSuggestion(String query){

        if(dbOpenHelper == null ){
            dbOpenHelper= activity.dbOpenHelper ;
        }

        Cursor detail = dbOpenHelper.getDetail(query);
        if(detail.moveToFirst()){
            //query is a word
            Log.e(TAG,"query is a word");
            word = query ;
            getDifinition();
        }else {
            detail.close();
            Cursor cursor = dbOpenHelper.getAllWord(query);
            cursor.moveToFirst();

            final String[] from = new String[]{"word"};
            final int[] to = new int[]{android.R.id.text1};
            SimpleCursorAdapter sAdapter = new SimpleCursorAdapter(getActivity(),
                    R.layout.simple_list_item,
                    null,
                    from,
                    to);
            suggestionLabel.setText("Search Result for " + query);
            sAdapter.changeCursor(cursor);
            suggestionList.setAdapter(sAdapter);

            suggestionCard.setVisibility(View.VISIBLE);
            suggestionCard.requestFocus();
        }
    }


}
