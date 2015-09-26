package seatech.alam.urdudictionary;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import seatech.alam.urdudictionary.util.DBOpenHelper;

/**
 * Created by root on 26/9/15.
 */
public class DBContentProvider extends ContentProvider {

    public static final String AUTHORITY ="seatech.alam.urdudictionary.DBContentProvider" ;
    public static final Uri CONTENT_URI = Uri.parse("content://"+ AUTHORITY +"/data");

    DBOpenHelper dbOpenHelper = null ;

    private static final int SUGGESTIONS_WORD  = 1 ;
    private static final int SEARCH_QUERY = 2 ;
    private static final int GET_DETAIL = 3 ;

    UriMatcher mUriMatcher = builUriMatcher();

    private UriMatcher builUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Suggestion items of Search Dialog is provided by this uri
        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY,SUGGESTIONS_WORD);

        // This URI is invoked, when user presses "Go" in the Keyboard of Search Dialog
        // Listview items of SearchableActivity is provided by this uri
        // See android:searchSuggestIntentData="content://in.wptrafficanalyzer.searchdialogdemo.provider/countries" of searchable.xml
        uriMatcher.addURI(AUTHORITY, "data", SEARCH_QUERY);

        // This URI is invoked, when user selects a suggestion from search dialog or an item from the listview
        // Country details for CountryActivity is provided by this uri
        // See, SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID in CountryDB.java
        uriMatcher.addURI(AUTHORITY, "data/#", GET_DETAIL);

        return uriMatcher;

    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new DBOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor result = null ;
        switch (mUriMatcher.match(uri)){

            case SUGGESTIONS_WORD :
                result = dbOpenHelper.getWord(selectionArgs);
                break;
            case SEARCH_QUERY :
                result = dbOpenHelper.getWord(selectionArgs);
                break;
            case GET_DETAIL:
                result = dbOpenHelper.getDetail(Integer.parseInt(selectionArgs[0]));
                break;

        }
        return result;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}
