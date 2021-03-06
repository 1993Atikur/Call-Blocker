package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import Data.UserData;
import Data.User;

public class Holder extends SQLiteOpenHelper {

    public static final String DataBaseName = "Mydatabase.db";
    public static final String TableName = "Mytable";
    public static final String COL_1 = "Name";
    public static final String COL_2 = "Number";


    Context context;

    public Holder(Context context) {
        super(context, DataBaseName, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TableName + " (Name TEXT,Number TEXT) ");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);
    }


    public void UserData(UserData data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, data.getName());
        contentValues.put(COL_2, data.getNumber());
        db.insert(TableName, null, contentValues);


    }

    public void Delete_Single_Number(String number, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "DELETE FROM " + TableName + " WHERE Number='" + number + "'" + " AND Name='" + name + "'";
        db.execSQL(SQL);


    }

    public void Delete_All_Number() {
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "DELETE FROM " + TableName;
        db.execSQL(SQL);


    }


    public void getData(User user,String incomingNumber) {
        boolean var=false;
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "SELECT Name,Number FROM " + TableName;
        Cursor cursor = db.rawQuery(SQL, null);
            while (cursor.moveToNext()){
                user.Details(cursor.getString(0),cursor.getString(1),incomingNumber);
                var=true;
            }
            if (!var){user.Details("-1","-1",incomingNumber);}

    }


}
