package seatech.alam.urdudictionary.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by yesalam on 1/23/16.
 * This class provide a interface between the userspecific database . This class create , delete and update any row
 * into the user database for Favorites , Word of the day and History requirements .
 */
public class UserData  {

    public static final String DATABASE_NAME = "user";
    public static final String DATABASE_TABLE = "User";
    private static final int DATABASE_VERSION = 1 ;
    private static final String TAG = "UserData";
    private static SQLiteDatabase mDb;
    Context mContext ;
    UserDataOpenHelper openHelper ;


    public UserData(Context paramContext){
        mContext = paramContext ;
    }

    public UserData open() throws SQLException {
        this.openHelper = new UserDataOpenHelper(mContext,DATABASE_NAME,DATABASE_VERSION);
        mDb = openHelper.getWritableDatabase() ;
        return this;
    }

    public void close(){
        if(openHelper!=null) openHelper.close();
    }

    public void insertValue(int type,String word){
        ContentValues contentValues = new ContentValues();
        contentValues.put("STAMP",Long.valueOf(System.currentTimeMillis()));
        contentValues.put("TYPE",Integer.valueOf(type));
        contentValues.put("WORD",word);
        mDb.insert(DATABASE_TABLE,null,contentValues);
    }

    public Cursor getAllValues(int type) throws SQLException{
        String query = "Select * from "+DATABASE_TABLE+" where TYPE = "+type+" order by STAMP desc ;";
        return mDb.rawQuery(query,null);
    }

    public Cursor isPresent(int type,String word) throws SQLException {
        String query = "Select _id from "+DATABASE_TABLE+" where TYPE = "+type+" and WORD = '"+word+"' ;"  ;
        return mDb.rawQuery(query,null);
    }

    public boolean deleteValues(long stamp){
        return mDb.delete(DATABASE_TABLE,"STAMP ="+stamp,null)>0;
    }

    private static class UserDataOpenHelper extends SQLiteOpenHelper {


        public UserDataOpenHelper(Context context,String database_name,int version) {
            super(context, database_name, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
                db.execSQL("create table if not exists "+DATABASE_TABLE+" (_id integer PRIMARY KEY autoincrement, STAMP INTEGER,TYPE INTEGER,WORD TEXT);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("SQLiteAdapter", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS Preference");
            onCreate(db);
        }
    }

}
