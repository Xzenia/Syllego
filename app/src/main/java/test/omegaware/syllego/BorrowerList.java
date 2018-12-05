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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class BorrowerList extends AppCompatActivity {

    private static final String TAG = "BorrowerList";
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter mRecyclerAdapter;
    private ProgressBar firebaseLoadingProgressBar;
    private RecyclerView recyclerView;
    private Book selectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("Transaction");
        firebaseLoadingProgressBar = findViewById(R.id.BorrowersList_FirebaseLoadingProgressBar);
        recyclerView = findViewById(R.id.BorrowersList);

        Bundle data = getIntent().getExtras();
        selectedBook = (Book) data.get("SelectedBook");

        showProgressBar();
        initializeRecyclerView();

        getSupportActionBar().setTitle("List of Borrowers for "+selectedBook.getBookName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeRecyclerView(){
        Query query = databaseReference.orderByChild("bookId").equalTo(selectedBook.getBookID());
        FirebaseRecyclerOptions transactionsOptions = new FirebaseRecyclerOptions.Builder<Transaction>().setQuery(query, Transaction.class).build();

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerAdapter = new FirebaseRecyclerAdapter<Transaction, TransactionListViewHolder>(transactionsOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final TransactionListViewHolder holder, int position, @NonNull Transaction model) {
                holder.setBorrowerName(model.getUserFullName());
                if (model.getStatus().matches("Borrowing")){
                    holder.setTransactionDate(model.getDateBorrowed());
                } else {
                    holder.setTransactionDate(model.getDateReturned());
                }
                holder.setNumberOfCopiesBorrowed(model.getNumberOfBooksBorrowed());
                holder.setTransactionStatus(model.getStatus());
            }
            @NonNull
            @Override
            public BorrowerList.TransactionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.borrowerlist_item, parent, false);
                return new BorrowerList.TransactionListViewHolder(view);
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
        firebaseLoadingProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    public void hideProgressBar(){
        firebaseLoadingProgressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp(){
        this.finish();
        return true;
    }

    public void onBackPressed(){
        this.finish();
    }

    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public static class TransactionListViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public TransactionListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setBorrowerName(String name){
            TextView borrowerNameTextView = mView.findViewById(R.id.BorrowerNameTextView);
            borrowerNameTextView.setText(name);
        }
        public void setTransactionDate(String date){
            TextView transactionDateTextView = mView.findViewById(R.id.TransactionDateTextView);
            transactionDateTextView.setText(date);
        }
        public void setNumberOfCopiesBorrowed(int copiesBorrowed){
            TextView numberOfCopiesBorrowedTextView = mView.findViewById(R.id.CopiesBorrowedTextView);
            if (copiesBorrowed == 1){
                numberOfCopiesBorrowedTextView.setText("Copies Borrowed: "+String.valueOf(copiesBorrowed)+" copy");
            } else {
                numberOfCopiesBorrowedTextView.setText("Copies Borrowed: "+String.valueOf(copiesBorrowed)+" copies");
            }

        }
        public void setTransactionStatus(String status){
            TextView transactionStatusTextView = mView.findViewById(R.id.TransactionStatusTextView);
            transactionStatusTextView.setText("Status: "+status);
        }
    }
}
