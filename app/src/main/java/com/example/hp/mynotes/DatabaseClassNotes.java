package com.example.hp.mynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseClassNotes extends SQLiteOpenHelper{
    private static final String TAG = "DatabaseClassNotes";
    private static final String TABLE_NAME="notes_table";
    private static final String DATABASE_NAME="Notes.db";
    private static final String COL1="ID";
    private static final String COL2="title";
    private static final String COL3="content";
    private static final String COL4="date";
    private static final String COL5="isImportant";

    public DatabaseClassNotes(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable="CREATE TABLE "+TABLE_NAME+ " (ID INTEGER PRIMARY KEY, "+ COL2 +" VARCHAR, " +COL3 + " TEXT, "+COL4+" VARCHAR, "+COL5+" INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop="DROP IF TABLE EXISTS ";
        db.execSQL(""+drop+TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String title, String content,String date,int isImportant){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL2,title);
        contentValues.put(COL3,content);
        contentValues.put(COL4,date);
        contentValues.put(COL5,isImportant);
        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1){
            return false;
        }
        else {
            return true;
        }
    }
    public Cursor getData(String query){
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor data=db.rawQuery(query,null);
        return data;
    }



    public boolean delete(String title){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,"title = ?",new String[] { title });
        return true;
    }

    public boolean deleteAll(String command){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,"1",null);
        return true;
    }

    public boolean updateNote(String oldTitle,String title,String content,String date,String number){
        ContentValues cv=new ContentValues();
        SQLiteDatabase db=this.getWritableDatabase();
//        cv.put(COL1,id);
        cv.put(COL2,title);
        cv.put(COL3,content);
        cv.put(COL4,date);
        cv.put(COL5,number);
        if(db.update(TABLE_NAME, cv ,"title = ?",new String[] { oldTitle })>0){
            return true;
        }
        return false;
    }

   public boolean update(String id,String title,String content,String date,String number){
        ContentValues cv=new ContentValues();
        SQLiteDatabase db=this.getWritableDatabase();
//        cv.put(COL1,id);
        cv.put(COL2,title);
        cv.put(COL3,content);
        cv.put(COL4,date);
        cv.put(COL5,number);
        db.update(TABLE_NAME, cv ,"title = ?",new String[] { title });
        return true;
   }
}
