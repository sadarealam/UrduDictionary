package seatech.alam.urdudictionary.util;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.HashMap;

/**
 * Created by root on 26/9/15.
 */
public class DBOpenHelper extends SQLiteAssetHelper {

    private final String TAG = "DBOpenHelper" ;
    private static final String DATABASE_NAME = "urdu.db" ;
    private static final int DATABASE_VERSION = 1 ;
    private static final String TABLE_NAME = "urdu" ;
    private static final String FIELD_ID = "_id" ;
    private static final String FIELD_WORD = "word" ;
    private static final String FIELD_ROMAN = "roman" ;
    private static final String FIELD_URDU = "urdu" ;
    private HashMap<String,String> mAliasMap;





    public DBOpenHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        // This HashMap is used to map table fields to Custom Suggestion fields
        mAliasMap = new HashMap<String, String>();

        // Unique id for the each Suggestions ( Mandatory )
        mAliasMap.put("_ID", FIELD_ID + " as " + "_id" );

        // Text for Suggestions ( Mandatory )
        mAliasMap.put(SearchManager.SUGGEST_COLUMN_TEXT_1, FIELD_WORD + " as " + SearchManager.SUGGEST_COLUMN_TEXT_1);

        // Icon for Suggestions ( Optional )
        //mAliasMap.put( SearchManager.SUGGEST_COLUMN_ICON_1, FIELD_FLAG + " as " + SearchManager.SUGGEST_COLUMN_ICON_1);

        // This value will be appended to the Intent data on selecting an item from Search result or Suggestions ( Optional )
        mAliasMap.put( SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, FIELD_ID + " as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID );

    }

    public Cursor getWord(String[] selectionArgs){
        String selection = FIELD_WORD + " like ? " ;
        if(selectionArgs != null){
            selectionArgs[0] = selectionArgs[0] + "%" ;
        }

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setProjectionMap(mAliasMap);
        queryBuilder.setTables(TABLE_NAME);

        Cursor result = queryBuilder.query(getReadableDatabase(),
                new String[]{"_ID",SearchManager.SUGGEST_COLUMN_TEXT_1,SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID},
                selection,
                selectionArgs,
                null,
                null,
                null);

        return result ;
    }

    public Cursor getAllWord(String query){
        String sql = "select "+FIELD_WORD+", _id from "+TABLE_NAME+" where "+FIELD_WORD+" like '"+query+"%'";
        Cursor result = getReadableDatabase().rawQuery(sql, null);
        return result ;
    }

    public Cursor getDetail(int id){
        String sql = "select * from urdu where _id = "+id ;
        Cursor result = getReadableDatabase().rawQuery(sql,null);
        return result ;
    }

    public Cursor getDetail(String word){
        String sql = "select * from urdu where word = '"+word+"'" ;
        Log.e("Database",sql);
        Cursor result = getReadableDatabase().rawQuery(sql,null);
        return result ;
    }

    public String getWord(String wordid){
        String sql = "select word from urdu where _id = '"+wordid+"'" ;
        Log.e(TAG,sql);
        Cursor result = getReadableDatabase().rawQuery(sql,null);
        result.moveToFirst();
        String word = result.getString(0);
        return word;
    }


}
