package seatech.alam.urdudictionary;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.TextView;

import seatech.alam.urdudictionary.adapters.ViewPagerAdapter;
import seatech.alam.urdudictionary.fragments.Home;
import seatech.alam.urdudictionary.util.DBOpenHelper;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    final String TAG = "MainActivity" ;
    public ViewPager viewPager ;
    public ViewPagerAdapter pagerAdapter;
    public static DBOpenHelper dbOpenHelper ;
    TabLayout tabLayout ;
    SearchView searchView;

    public String wotd="alamo";
    public String word= wotd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        viewPager = (ViewPager) findViewById(R.id.viewpager) ;
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs) ;
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        dbOpenHelper = new DBOpenHelper(this) ;
        // Get the intent, verify the action and get the query
        handleIntent(getIntent());

        tabLayout.setOnTabSelectedListener(this);
    }

    private void doMySearch(String query){
        //Home homeFragment = (Home) pagerAdapter.getItem(0);
        Home homeFragment = (Home) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":0");
        if(homeFragment != null) {
            homeFragment.setSuggestion(query);
        }else {
            Log.e(TAG,"Home fragments is null");
        }
    }
    private void doMyView(String query){


    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.setQuery(query,false);
            searchView.clearFocus();
            doMySearch(query);
        } else if(Intent.ACTION_VIEW.equals(intent.getAction())){
            Log.e(TAG,"Intent  Action View ");
            Uri data = intent.getData() ;
            try{
                String wordid = data.getLastPathSegment();
                Log.e(TAG, "Action from toolbar with wid = "+wordid);
                word = dbOpenHelper.getWord(wordid);
                searchView.setQuery(word,false);
                searchView.clearFocus();
                doMyView(word);
            }catch (NullPointerException npe){
                word = intent.getStringExtra("word");
                Log.e(TAG, "Action from searchview with word = "+word);
                doMyView(word);
            }
            viewPager.setCurrentItem(1, true);
        }
    }


    private void setupToolbar(){
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
    public void onTabSelected(TabLayout.Tab tab) {

        Log.e(TAG,"Tab Selected");
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
