package seatech.alam.urdudictionary;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.sql.SQLException;

import seatech.alam.urdudictionary.adapters.ViewPagerAdapter;
import seatech.alam.urdudictionary.fragments.Definition;
import seatech.alam.urdudictionary.fragments.Home;
import seatech.alam.urdudictionary.util.DBOpenHelper;
import seatech.alam.urdudictionary.util.Globals;
import seatech.alam.urdudictionary.util.UserData;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    public ViewPager viewPager;
    public ViewPagerAdapter pagerAdapter;
    public DBOpenHelper dbOpenHelper;
    TabLayout tabLayout;
    SearchView searchView;
    public UserData userData ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        Globals.word_of_the_day = "alamo";

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
        setGData(Globals.word_of_the_day);
        setWotd();
        // Get the intent, verify the action and get the query
        handleIntent(getIntent());


    }

    public void setWotd(){
        try {
            Cursor cursor = userData.getAllValues(Globals.TYPE_WOTD);
            if(cursor.moveToFirst()){

            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            String word = query;
            setGData(word);
            viewPager.setCurrentItem(1, true);
        } else {

            Globals.query = query ;
            viewPager.setCurrentItem(0,true);
        }
    }


    public void startDefinition() {
        Definition definitionFragment = (Definition) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":1");
        if (definitionFragment != null) {
            definitionFragment.onResume();
        }
        viewPager.setCurrentItem(1, true);
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://seatech.alam.urdudictionary/http/host/path")
        );

    }
}
