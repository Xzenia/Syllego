package test.omegaware.syllego;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewProfile extends AppCompatActivity {

    private static final String TAG = "ViewProfile";

    private TextView fullNameTextView;
    private TextView departmentTextView;
    private TextView numberOfBooksBorrowedTextView;
    private TextView numberofBooksReturnedTextView;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userReference = database.getReference("User").child("UserList");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        fullNameTextView = findViewById(R.id.FullNameTextView);
        departmentTextView = findViewById(R.id.DepartmentTextView);
        numberOfBooksBorrowedTextView = findViewById(R.id.NumberOfBooksBorrowedTextView);
        numberofBooksReturnedTextView = findViewById(R.id.NumberOfBooksReturnedTextView);

        retrieveProfileData();
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void retrieveProfileData(){
        Query profileQuery = userReference.orderByChild("userId").equalTo(firebaseAuth.getCurrentUser().getUid());
        profileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        User profileData = snapshot.getValue(User.class);

                        fullNameTextView.setText("Name: " + profileData.getName());
                        departmentTextView.setText("Department: " + profileData.getDepartment());
                        numberOfBooksBorrowedTextView.setText("Number of Books Borrowed: " + profileData.getNumberOfBooksBorrowed());
                        numberofBooksReturnedTextView.setText("Number of Books Returned: " + profileData.getNumberOfBooksReturned());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
                toastMessage("Exception occurred:\n"+databaseError.toException());
            }
        });
    }

    @Override
    public void onBackPressed(){
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp(){
        this.finish();
        return true;
    }

    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}