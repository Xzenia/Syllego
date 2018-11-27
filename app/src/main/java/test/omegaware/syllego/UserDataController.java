package test.omegaware.syllego;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDataController {

    private static final String TAG = "UserDataController";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userReference = database.getReference("User").child("UserList");

    public void addUser(User newUser){
        DatabaseReference childBookReference = userReference.push();
        childBookReference.setValue(newUser);
        Log.d(TAG, "ADDING BOOK: "+newUser.getName());
    }


}
