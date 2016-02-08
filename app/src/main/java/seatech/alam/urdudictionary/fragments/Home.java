package seatech.alam.urdudictionary.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import seatech.alam.urdudictionary.MainActivity;
import seatech.alam.urdudictionary.R;
import seatech.alam.urdudictionary.adapters.DefinitonListAdapter;
import seatech.alam.urdudictionary.util.DBOpenHelper;
import seatech.alam.urdudictionary.util.Globals;

/**
 * Created by root on 27/9/15.
 */
public class Home extends Fragment implements AdapterView.OnItemClickListener , View.OnClickListener{

    final String TAG = "Home Frag" ;
    //SearchView searchView ; //removed because its taking hell lot of time .
    ListView suggestionListView;
    TextView suggestionCardLabel ;
    ImageView suggestionCardCancel ;
    MainActivity activity;
    DBOpenHelper dbOpenHelper ;
    String word ;
    CardView suggestionCard ;
    ListView wotdlist ;
    TextView wotdtv ;

    private SimpleCursorAdapter mAdapter;

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"HOme onResume");
        if(Globals.query != null ) setSuggestion(Globals.query);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG,"Home onStart");

    }

    @Override
    public void onAttach(Activity activity) {
        Log.e(TAG,"onAttach for Home is called");
        super.onAttach(activity);
        this.activity = (MainActivity) activity ;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG,"Home onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "HOME onStop");
        Globals.query = null ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false) ;

        suggestionCard = (CardView) view.findViewById(R.id.suggestionCard);
        suggestionCard.setVisibility(View.GONE);
        suggestionCardLabel = (TextView) view.findViewById(R.id.suggestionCardLabel) ;
        suggestionListView = (ListView) view.findViewById(R.id.suggestionListView);
        suggestionListView.setOnItemClickListener(this);
        suggestionCardCancel = (ImageView) view.findViewById(R.id.suggestionCardCancel);
        suggestionCardCancel.setOnClickListener(this);

        wotdtv = (TextView) view.findViewById(R.id.wordinwotd) ;
        wotdlist = (ListView) view.findViewById(R.id.wotdList) ;

        //Removing searchview beacuase its taking hell lot of time ;
        //searchView = (SearchView) view.findViewById(R.id.searchView) ;
        //searchView.setSuggestionsAdapter(mAdapter);
        //searchView.setIconifiedByDefault(false);
        //searchView.setSubmitButtonEnabled(true);
        // Getting selected (clicked) item suggestion
       /* searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                // Your code here
                String word = mAdapter.getCursor().getString(0);
                searchView.setQuery(word, false);
                activity.setGData(word);
                activity.startDefinition();
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
                searchQuery(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                populateAdapter(s);
                return false;
            }
        });*/

        setWotd();

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

    public void setSuggestion(String query){
        Log.e(TAG, "Show suggestion for " + query);
        suggestionCard.setVisibility(View.VISIBLE);
        suggestionCardLabel.setText(" '"+query+"'");
        Cursor cursor = dbOpenHelper.getAllWord(query);
        cursor.moveToFirst();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),R.layout.simple_list_item,cursor, new String[]{"word"},new int[]{R.id.text1});
        suggestionListView.setAdapter(adapter);
        Log.e(TAG, "suggestion is provided");
    }

    public void setWotd(){
        String wotd = Globals.word_of_the_day ;
        String roman = Globals.roman_wotd ;
        String urdu = Globals.urdu_wotd ;
        String[] romanlist = roman.split("\n");
        String[] urdulist = urdu.split("\n");

        wotdtv.setText(wotd);

        DefinitonListAdapter definitonListAdapter = new DefinitonListAdapter(getActivity(),R.layout.twotvrow,urdulist,romanlist) ;
        wotdlist.setAdapter(definitonListAdapter);
    }

    /**
     * Call when a user search for a query rather than providing a word .
     * This method either check if provided query is a valid word and do the corresponding or pass the query to Home fragment for suggestion list .
     * @param query
     */
    public void searchQuery(String query){
        Log.e(TAG, "Main search Query ");
        Cursor cursor = dbOpenHelper.getDetail(query);
        if(cursor.moveToFirst()){
            String word = query ;
            activity.setGData(query);
            activity.startDefinition();
        }else {
             this.setSuggestion(query);
            }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) suggestionListView.getItemAtPosition(position);
        String word = cursor.getString(0);
        Log.e(TAG,word.toString());
        activity.setGData(word);
        activity.startDefinition();
    }

    @Override
    public void onClick(View v) {
        suggestionCard.setVisibility(View.GONE);
    }
}



