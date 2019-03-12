package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import Data.Helper;

public class RejectedCalls extends SQLiteOpenHelper {

    public static final String DataBaseName = "rejectedlist.db";
    public static final String TableName = "RejectTable";
    public static final String COL_1 = "Name";
    public static final String COL_2 = "Number";
    public static final String COL_3 = "CallDate";


    Context context;

    public RejectedCalls(Context context) {
        super(context, DataBaseName, null, 1);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TableName + " (Name TEXT,Number TEXT,CallDate TEXT) ");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);
    }


    public void insertRejected(String name, String number, String calldate) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, number);
        contentValues.put(COL_3, calldate);
        db.insert(TableName, null, contentValues);


    }

    public void Delete_Single_Number(String name, String number, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "DELETE FROM " + TableName + " WHERE Name='" + name + "'" + " AND Number='" + number + "'" + " AND CallDate='" + date + "'";
        db.execSQL(SQL);


    }

    public void Delete_All_Number() {
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "DELETE FROM " + TableName;
        db.execSQL(SQL);


    }


    public void getCallHistory(Helper helper) {
        boolean var = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "SELECT Name,Number,CallDate FROM " + TableName;
        Cursor cursor = db.rawQuery(SQL, null);
        int position = cursor.getCount() - 1;

        while (cursor.moveToPosition(position)) {
            helper.CallHistoryData(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            position--;
            var = true;
        }
        if (!var) {
            helper.CallHistoryData("-1", "-1", "-1");
        }

    }

    public long Lastlength() {
        SQLiteDatabase db = this.getWritableDatabase();
        long length = DatabaseUtils.queryNumEntries(db, TableName);
        return length;

    }

}
