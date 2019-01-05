package test.omegaware.syllego.Controller;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import test.omegaware.syllego.Model.User;

public class UserDataController {

    private static final String TAG = "UserDataController";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference userReference = database.getReference("User").child("UserList");

    public void addUser(User newUser){
        DatabaseReference childBookReference = userReference.push();
        childBookReference.setValue(newUser);
        Log.d(TAG, "ADDING BOOK: "+newUser.getName());
    }
}
