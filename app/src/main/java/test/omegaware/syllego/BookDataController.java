package test.omegaware.syllego;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BookDataController extends SQLiteOpenHelper {

    private static final String TAG = "BookDataController";

    private static final String TABLENAME = "BookCatalogue";
    private static final String COLID = "_ID";
    private static final String COLUSERID = "USERID";
    private static final String COL1 = "Name";
    private static final String COL2 = "Author";
    private static final String COL3 = "YearReleased";
    private static final String COL4 = "ISBN";
    private static final String COL5 = "Status";
    private static final int DATABASEVERSION = 6;

    public BookDataController(Context context){
        super(context,TABLENAME,null,DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+ TABLENAME + "(_ID INTEGER PRIMARY KEY, "+
                COLUSERID+" TEXT, "+COL1 +" TEXT, "+ COL2 +" TEXT, "+ COL3 + " TEXT, "+ COL4 +" TEXT, "+ COL5 + " TEXT );";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int newVersion, int oldVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(db);
    }

    public boolean addData(Book newBook){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLID, newBook.getBookID());
        contentValues.put(COL1, newBook.getBookName());
        contentValues.put(COL2, newBook.getBookAuthor());
        contentValues.put(COL3, newBook.getYearReleased());
        contentValues.put(COL4, newBook.getISBN());
        contentValues.put(COL5, newBook.getStatus());

        Log.d(TAG, "ADDING BOOK: "+newBook.getBookName());
        long result = db.insert(TABLENAME,null,contentValues);
        return result != -1;

    }

    public boolean editData(Book selectedBook){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, selectedBook.getBookName());
        contentValues.put(COL2, selectedBook.getBookAuthor());
        contentValues.put(COL3, selectedBook.getYearReleased());
        contentValues.put(COL4, selectedBook.getISBN());
        contentValues.put(COL5, selectedBook.getStatus());

        Log.d(TAG, "UPDATING BOOK: "+selectedBook.getBookName());
        long result = db.update(TABLENAME, contentValues,COLID+" = ? ", new String[] {Integer.toString(selectedBook.getBookID())});
        return result != -1;
    }

    public boolean deleteData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "DELETING BOOK ID: "+id);
        long result = db.delete(TABLENAME,COLID+" = ? ",new String[]{(String.valueOf(id))});
        return result != -1;
    }


    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLENAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public Cursor getUserBookList(int userBookId){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLENAME+" WHERE "+COLUSERID+" = "+userBookId;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

}
