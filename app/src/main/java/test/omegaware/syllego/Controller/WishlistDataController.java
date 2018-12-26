package test.omegaware.syllego.Controller;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import test.omegaware.syllego.Model.Book;

public class WishlistDataController {

    private static final String TAG = "WishlistDataController";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference wishlistReference = database.getReference("Wishlist").child(FirebaseAuth.getInstance().getUid());

    public void addData(Book newBook){
        DatabaseReference childBookReference = wishlistReference.push();
        newBook.setBookID(childBookReference.getKey());
        childBookReference.setValue(newBook);
        Log.d(TAG, "ADDING BOOK: "+newBook.getBookName());
    }

    public void editData(Book selectedBook){
        DatabaseReference childBookReference = wishlistReference.child(selectedBook.getBookID());
        childBookReference.setValue(selectedBook);
    }

    public void deleteData(String id){
        DatabaseReference childBookReference = wishlistReference.child(id);
        childBookReference.removeValue();
    }
}
