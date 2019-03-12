package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

public class State extends SQLiteOpenHelper {

    public static final String DataBaseName = "state.db";
    public static final String TableName = "STable";
    public static final String COL_1 = "RunState";
    public static final String COL_2 = "CheckState";


    Context context;
    Insert insert;
    public State(Context context) {
        super(context, DataBaseName, null, 1);
        this.context = context;
        insert=new Insert();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TableName + " (RunState INTEGER,CheckState INTEGER) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);
    }

    public void initials() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, 0);
        contentValues.put(COL_2, 0);
        db.insert(TableName, null, contentValues);
    }

    public void updateRunState(int value) {

        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE " + TableName + " SET RunState =" + value;
        db.execSQL(SQL);
    }

    public void updateCheckState(int value) {

        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE " + TableName + " SET CheckState =" + value;
        db.execSQL(SQL);
    }


    public boolean isRunning() {
        SQLiteDatabase db = this.getWritableDatabase();
        int var=0;
        String SQL = "SELECT RunState FROM " + TableName;
        Cursor cursor = db.rawQuery(SQL, null);
        if(cursor.moveToNext()){
            var=cursor.getInt(0);
        }else {
            insert.execute();
        }
        return (var==1);
    }

    public boolean isChecked() {
        SQLiteDatabase db = this.getWritableDatabase();
        int var=0;
        String SQL = "SELECT CheckState FROM " + TableName;
        Cursor cursor = db.rawQuery(SQL, null);
        if (cursor.moveToNext()) {
        var=cursor.getInt(0);
        }
        return (var==1);
    }
    public class Insert extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            initials();
            return null;
        }
    }
}
