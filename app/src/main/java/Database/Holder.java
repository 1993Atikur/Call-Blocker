package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Holder extends SQLiteOpenHelper {

    public static final String DataBaseName="Mydatabase.db";
    public static final String TableName="Mytable";
    public static final String COL_1="Id";
    public static final String COL_2="RunState";
    public static final String COL_3="CheckState";
    public static final String COL_4="Name";
    public static final String COL_5="Number";


    Context context;

    public Holder(Context context) {
        super(context,DataBaseName, null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TableName + " (Id INTEGER ,RunState INTEGER,CheckState INTEGER,Name TEXT,Number TEXT) ");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);
    }

    public void initials(){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,0);
        contentValues.put(COL_2,0);
        contentValues.put(COL_3,0);
        contentValues.put(COL_4,"");
        contentValues.put(COL_5,"");
        db.insert(TableName,null,contentValues);
    }

    public void UserData(String name ,String number){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,1);
        contentValues.put(COL_4,name);
        contentValues.put(COL_5,number);
        db.insert(TableName,null,contentValues);


    }
    public void Delete_Single_Number(String number){
        SQLiteDatabase db=this.getWritableDatabase();
        String SQL="DELETE Name,Number FROM "+TableName+" WHERE Number='"+number+"'";
        db.execSQL(SQL);


    }

    public void Delete_All_Number(){
        SQLiteDatabase db=this.getWritableDatabase();
        String SQL="DELETE FROM "+TableName+" WHERE Id="+1;
        db.execSQL(SQL);


    }
    public  Cursor getRunState(){
        SQLiteDatabase db=this.getWritableDatabase();

        String SQL="SELECT RunState FROM "+TableName;
        Cursor cursor=db.rawQuery(SQL,null);
        return cursor;
    }

    public  Cursor getData(){
        SQLiteDatabase db=this.getWritableDatabase();

        String SQL="SELECT Id,CheckState,Name,Number FROM "+TableName;
        Cursor cursor=db.rawQuery(SQL,null);
        return cursor;
    }
    public void updateRunState(int value){

        SQLiteDatabase db=this.getWritableDatabase();
        String SQL="UPDATE "+TableName+" SET RunState ="+value+" WHERE Id="+0;
        db.execSQL(SQL);
    }

    public void updateCheckState(int value){

        SQLiteDatabase db=this.getWritableDatabase();
        String SQL="UPDATE "+TableName+" SET CheckState ="+value+" WHERE Id="+0;
        db.execSQL(SQL);
    }


}
