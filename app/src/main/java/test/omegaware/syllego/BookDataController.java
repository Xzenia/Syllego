package test.omegaware.syllego;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookDataController {

    private static final String TAG = "BookDataController";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference bookReference = database.getReference("Book");


    public boolean addData(Book newBook){
        DatabaseReference childBookReference = bookReference.child("BookList").push();
        newBook.setBookID(childBookReference.getKey());
        childBookReference.setValue(newBook);
        Log.d(TAG, "ADDING BOOK: "+newBook.getBookName());
        return true;
    }

    public boolean editData(Book selectedBook){
        DatabaseReference childBookReference = bookReference.child("BookList").child(selectedBook.getBookID());
        childBookReference.setValue(selectedBook);
        return true;
    }

    public boolean deleteData(String id){
        DatabaseReference childBookReference = bookReference.child("BookList").child(id);
        childBookReference.removeValue();
        return true;
    }
}
