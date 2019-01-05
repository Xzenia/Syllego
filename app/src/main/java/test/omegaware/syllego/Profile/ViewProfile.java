package test.omegaware.syllego.Profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import test.omegaware.syllego.DataHistory.ViewHistory;
import test.omegaware.syllego.Model.User;
import test.omegaware.syllego.R;

public class ViewProfile extends AppCompatActivity {

    private static final String TAG = "ViewProfile";

    private TextView fullNameTextView;
    private TextView departmentTextView;

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference userReference = database.getReference("User").child("UserList");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        fullNameTextView = findViewById(R.id.FullNameTextView);
        departmentTextView = findViewById(R.id.DepartmentTextView);

        retrieveProfileData();
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void retrieveProfileData(){
        Query profileQuery = userReference.orderByChild("userId").equalTo(firebaseAuth.getCurrentUser().getUid());
        profileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        User profileData = snapshot.getValue(User.class);

                        fullNameTextView.setText("Name: " + profileData.getName());
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewprofile_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ShowHistory:
                Intent historyActivity = new Intent(this, ViewHistory.class);
                startActivity(historyActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
