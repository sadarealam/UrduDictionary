package seatech.alam.urdudictionary;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ocpsoft.pretty.time.PrettyTime;

import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import seatech.alam.urdudictionary.adapters.CursorAdapterPlus;
import seatech.alam.urdudictionary.adapters.ViewPagerAdapter;
import seatech.alam.urdudictionary.fragments.Definition;
import seatech.alam.urdudictionary.fragments.Favourite;
import seatech.alam.urdudictionary.fragments.History;
import seatech.alam.urdudictionary.fragments.Home;
import seatech.alam.urdudictionary.util.DBOpenHelper;
import seatech.alam.urdudictionary.util.Globals;
import seatech.alam.urdudictionary.util.UserData;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    final String TAG = "MainActivity";
    public ViewPager viewPager;
    public ViewPagerAdapter pagerAdapter;
    public DBOpenHelper dbOpenHelper;
    TabLayout tabLayout;
    SearchView searchView;
    public UserData userData ;
    private TextToSpeech textToSpeech ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        Log.e(TAG,"onCreate called");
        textToSpeech = new TextToSpeech(this ,this) ;
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        dbOpenHelper = new DBOpenHelper(this);
        userData = new UserData(this) ;
        try {
            userData = userData.open() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getWotd() ;
        //Globals.word_of_the_day = "alamo" ;
        setGData(Globals.word_of_the_day);
        //setWotd();
        // Get the intent, verify the action and get the query
        handleIntent(getIntent());


    }

    public void getWotd()  {
        try {
            Cursor cursor = userData.getAllValues(Globals.TYPE_WOTD) ;
            if(cursor.moveToFirst()){
                String stamp = cursor.getString(1) ;
                PrettyTime p = new PrettyTime();
                String age = p.format(new Date(Long.parseLong(stamp)));
                if(age.contains("day")) {
                    generateWotd();
                } else {
                    String word = cursor.getString(3) ;
                    cursor.close();
                    setWotdData(word);
                }
            } else {
                generateWotd();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate the word of the day based on some logic and then insert the value in the userdata .
     */
    public void generateWotd(){
        Random random = new Random();
        boolean ok = true ;

        while(ok){
            int ran = random.nextInt(130000) ;
            Cursor cursor = dbOpenHelper.getDetail(ran);
            if(cursor.moveToFirst()){
                String word = cursor.getString(1);
                try {
                    if(userData.isPresent(Globals.TYPE_WOTD,word).moveToFirst()) continue;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String roman = cursor.getString(2) ;
                String urdu = cursor.getString(3) ;
                if(word.equalsIgnoreCase(roman.trim())) {

                } else if(roman.equalsIgnoreCase("-")) {

                } else {
                    ok = false ;
                    userData.insertValue(Globals.TYPE_WOTD,word);
                    Globals.word_of_the_day=word ;
                    Globals.roman_wotd = roman ;
                    Globals.urdu_wotd = urdu ;
                    userData.insertValue(Globals.TYPE_HISTORY,word);
                }
            }

        }
    }

    /**
     * Called when a user search for a query rather than providing a word .
     * This method either check if provided query is a valid word and do the corresponding or pass the query to Home fragment for suggestion list .
     *
     * @param query
     */
    public void searchQuery(String query) {
        Log.e(TAG, "Main search Query ");
        Cursor cursor = dbOpenHelper.getDetail(query);
        if (cursor.moveToFirst()) {
            Log.e(TAG,"its a word");
            String word = query;
            setGData(word);
            viewPager.setCurrentItem(1, true);
        } else {
            Log.e(TAG,"its a query");
            Globals.query = query ;
            viewPager.setCurrentItem(0,true);
        }
    }


    public void startDefinition() {
        Definition definitionFragment = (Definition) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":1");
        if (definitionFragment != null) {
            definitionFragment.onResume();
        }
        viewPager.setCurrentItem(1, false);
    }

    /**
     * To find the meaning of param word and set them as global static variables . This also insert the word in History table .
     *
     * @param word To find the meaning of .
     */
    public void setGData(String word) {

        Globals.current_word = word;
        Log.e(TAG, "current word is " + Globals.current_word);
        if(!(Globals.current_word.equalsIgnoreCase(Globals.word_of_the_day)))
        userData.insertValue(Globals.TYPE_HISTORY,word);
        Cursor cursor = dbOpenHelper.getDetail(word);
        cursor.moveToFirst();
        Globals.urdu = cursor.getString(3);
        Globals.roman = cursor.getString(2);
        cursor.close();
    }

    public void setWotdData(String word){
        Cursor cursor = dbOpenHelper.getDetail(word);
        cursor.moveToFirst();
        Globals.word_of_the_day = word ;
        Globals.urdu_wotd = cursor.getString(3) ;
        Globals.roman_wotd = cursor.getString(2) ;
        cursor.close();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Log.e(TAG, "Action Search ");
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.setQuery(query, false);
            searchView.clearFocus();
            searchQuery(query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Log.e(TAG, "Intent  Action View ");
            Uri data = intent.getData();
            try {
                String wordid = data.getLastPathSegment();
                Log.e(TAG, "Action from toolbar with wid = " + wordid);
                String word = dbOpenHelper.getWord(wordid);
                searchView.setQuery(word, false);
                searchView.clearFocus();
                setGData(word);

            } catch (NullPointerException npe) {
                String word = intent.getStringExtra("word");
                Log.e(TAG, "Action from searchview with word = " + word);
                setGData(word);
            }
            viewPager.setCurrentItem(1, true);
        }
    }


    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void onFavorite(View view){
        try {
            Cursor cursor = userData.isPresent(Globals.TYPE_FAB,Globals.current_word) ;
            if(cursor.moveToFirst()){
                Toast.makeText(this,"Already Your Favorite ",Toast.LENGTH_LONG).show();
            } else {
                userData.insertValue(Globals.TYPE_FAB,Globals.current_word);
                Favourite favouriteFragment = (Favourite) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":2");
                if (favouriteFragment != null) {
                    favouriteFragment.setFavouritData();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void speakOut(View view){
        textToSpeech.speak(Globals.current_word,0,null) ;
    }

    /**
     * delete the row data from the database and update the History and Favorite fragments to show the recent changes in data .
     * @param view
     */
    public void deleteData(View view){
        Log.e(TAG, "deleteData() pressed");
        long l = Long.parseLong((String)((TextView)((ViewGroup)view.getParent().getParent()).findViewById(R.id.row_time)).getTag());
        userData.deleteValues(l) ;
        Favourite favouriteFragment = (Favourite) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":2");
        if (favouriteFragment != null) {
            favouriteFragment.dataSetChanged();
        }
        History historyFragment = (History) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":3");
        if (historyFragment != null) {
            historyFragment.dataSetChanged();
        }

    }

    public void wotdClick(View view){
        Globals.current_word = Globals.word_of_the_day ;
        Globals.urdu = Globals.urdu_wotd ;
        Globals.roman = Globals.roman_wotd ;
        startDefinition();
    }

    @Override
    public void onInit(int status) {
        if(status == 0){
            status = this.textToSpeech.setLanguage(Locale.US);
            if ((status == -1) || (status == -2)) {
                Log.e("Urdu Dictionary","Language not Available");
            }
            return;
        }
        Toast.makeText(this, "Text to speech is not available in this device", Toast.LENGTH_SHORT).show();
        Log.e("UrduDictionary", "Could not initialize TextToSpeech.");
    }
}
