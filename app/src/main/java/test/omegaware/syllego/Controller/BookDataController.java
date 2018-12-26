package test.omegaware.syllego.Controller;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import test.omegaware.syllego.Model.Book;

public class BookDataController {

    private static final String TAG = "BookDataController";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference bookReference = database.getReference("Book").child("BookList").child(FirebaseAuth.getInstance().getUid());

    public void addData(Book newBook){
        DatabaseReference childBookReference = bookReference.push();
        newBook.setBookID(childBookReference.getKey());
        childBookReference.setValue(newBook);
        Log.d(TAG, "ADDING BOOK: "+newBook.getBookName());
    }

    public void editData(Book selectedBook){
        DatabaseReference childBookReference = bookReference.child(selectedBook.getBookID());
        childBookReference.setValue(selectedBook);
    }

    public void deleteData(String id){
        DatabaseReference childBookReference = bookReference.child(id);
        childBookReference.removeValue();
    }
}
