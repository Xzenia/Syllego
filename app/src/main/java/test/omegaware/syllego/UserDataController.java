package test.omegaware.syllego;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDataController extends SQLiteOpenHelper {

    private static final String TAG = "UserDataController";

    private static final String TABLENAME = "UserData";
    private static final String COLID = "_USERID";
    private static final String COL1 = "USERNAME";
    private static final String COL2 = "PASSWORD";
    private static final String COL3 = "FULLNAME";
    private static final String COL4 = "BOOKLISTID";
    private static final int DATABASEVERSION = 1;


    public UserDataController(Context context){
        super(context,TABLENAME,null,DATABASEVERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+ TABLENAME + "(_USERID INTEGER PRIMARY KEY, "+
                COL1 +" TEXT, "+ COL2 +" TEXT, "+ COL3 + " TEXT, "+ COL4 + " TEXT );";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int newVersion, int oldVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(db);
    }

    public boolean createUser(User newUser){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLID, newUser.getUserId());
        contentValues.put(COL1, newUser.getUsername());
        contentValues.put(COL2, newUser.getPasswordHash());
        contentValues.put(COL3, newUser.getFullName());
        contentValues.put(COL4, newUser.getBookListId());

        Log.d(TAG, "ADDING USER: "+newUser.getUsername());
        long result = db.insert(TABLENAME,null,contentValues);
        return result != -1;
    }

    public boolean authenticateUser(String username, String password) throws NoSuchAlgorithmException {
        String hashedPassword = getHashPassword(password);
        Cursor authenticationCursor = getReadableDatabase().rawQuery("SELECT "+COL1+","+COL2+" FROM "+TABLENAME+" WHERE "+COL1+" = '"+username+"' AND "+ COL2 + " = '"+hashedPassword+"'",null);

        if (authenticationCursor.getCount() > 0){
            return true;
        } else {
            return false;
        }
    }

    public String getHashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        String hashedPassword = getHashedString(encodedHash);
        return hashedPassword;
    }

    private String getHashedString(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
