package test.omegaware.syllego;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDataController {

    private static final String TAG = "UserDataController";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference userReference = database.getReference("User").child("UserList");

    public void addUser(User newUser){
        DatabaseReference childBookReference = userReference.push();
        childBookReference.setValue(newUser);
        Log.d(TAG, "ADDING BOOK: "+newUser.getName());
    }

    public void incrementBooksBorrowedCounter(User user){
        DatabaseReference databaseReference = userReference.child(user.getUserId());
        user.setNumberOfBooksBorrowed(user.getNumberOfBooksBorrowed() + 1);
        databaseReference.setValue(user);
    }

    public void incrementBooksReturnedCounter(User user){
        DatabaseReference databaseReference = userReference.child(user.getUserId());
        user.setNumberOfBooksReturned(user.getNumberOfBooksReturned() + 1);
        databaseReference.setValue(user);
    }
}
