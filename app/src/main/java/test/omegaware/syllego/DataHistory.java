package test.omegaware.syllego;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DataHistory extends AppCompatActivity {

    private static final String TAG = "BookList";
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter mRecyclerAdapter;
    private FirebaseAuth firebaseAuth;
    private ProgressBar dataHistoryLoadingProgressBar;
    private String userId;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_history);

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("History").child("Logs");
        dataHistoryLoadingProgressBar = findViewById(R.id.DataHistoryLoadingProgressBar);
        recyclerView = findViewById(R.id.DataHistoryList);
        showProgressBar();
        initializeRecyclerView();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeRecyclerView(){
        Query query = databaseReference.orderByChild("userID").equalTo(userId);
        FirebaseRecyclerOptions historyOptions = new FirebaseRecyclerOptions.Builder<History>().setQuery(query, History.class).build();

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this, 1);
        recyclerView.addItemDecoration(decoration);

        mRecyclerAdapter = new FirebaseRecyclerAdapter<History, DataHistory.DataHistoryListViewHolder>(historyOptions) {
            @Override
            protected void onBindViewHolder(@NonNull DataHistoryListViewHolder holder, int position, @NonNull History model) {
                holder.setDate(model.getDate());
                holder.setMessage(model.getMessage());
            }

            @NonNull
            @Override
            public DataHistory.DataHistoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.datahistorycontentlist_item, parent, false);
                return new DataHistory.DataHistoryListViewHolder(view);
            }
        };

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
                toastMessage("Exception occurred:\n"+databaseError.toException());
            }
        });

        recyclerView.setAdapter(mRecyclerAdapter);
    }

    public void onStart(){
        super.onStart();
        mRecyclerAdapter.startListening();
    }

    public void onStop(){
        super.onStop();
        mRecyclerAdapter.stopListening();
    }

    public void showProgressBar(){
        dataHistoryLoadingProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    public void hideProgressBar(){
        dataHistoryLoadingProgressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void onBackPressed(){
        this.finish();
    }

    public boolean onSupportNavigateUp(){
        this.finish();
        return true;
    }

    public static class DataHistoryListViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public DataHistoryListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setDate(String date){
            TextView dateTextView = mView.findViewById(R.id.DataHistoryDate);
            dateTextView.setText(date);
        }
        public void setMessage(String message){
            TextView messageTextView = mView.findViewById(R.id.DataHistoryMessage);
            messageTextView.setText(message);
        }
    }

    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
