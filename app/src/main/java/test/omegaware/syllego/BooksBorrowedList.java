package test.omegaware.syllego;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class BooksBorrowedList extends AppCompatActivity {

    private static final String TAG = "BorrowerList";
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter mRecyclerAdapter;
    private ProgressBar firebaseLoadingProgressBar;
    private RecyclerView recyclerView;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_borrowed_list);
        databaseReference = FirebaseDatabase.getInstance().getReference("Transaction");
        firebaseLoadingProgressBar = findViewById(R.id.BooksBorrowedList_FirebaseLoadingProgressBar);
        recyclerView = findViewById(R.id.BooksBorrowedList);
        firebaseAuth = FirebaseAuth.getInstance();
        showProgressBar();
        initializeRecyclerView();

        getSupportActionBar().setTitle("Books you are borrowing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeRecyclerView(){
        Query query = databaseReference.orderByChild("userId_status").equalTo(firebaseAuth.getCurrentUser().getUid()+"_"+"Borrowing");
        FirebaseRecyclerOptions transactionsOptions = new FirebaseRecyclerOptions.Builder<Transaction>().setQuery(query, Transaction.class).build();

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerAdapter = new FirebaseRecyclerAdapter<Transaction, BooksBorrowedList.TransactionListViewHolder>(transactionsOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final BooksBorrowedList.TransactionListViewHolder holder, int position, @NonNull final Transaction model) {
                holder.setBookName(model.getBookName());
                holder.setTransactionDate(model.getDateBorrowed());
                holder.setNumberOfCopiesBorrowed(model.getNumberOfBooksBorrowed());
                holder.setTransactionStatus(model.getStatus());
                holder.mView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        goToReturnBookActivity(model);
                    }
                });
            }
            @NonNull
            @Override
            public BooksBorrowedList.TransactionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.borrowerlist_item, parent, false);
                return new BooksBorrowedList.TransactionListViewHolder(view);
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

    public void goToReturnBookActivity(Transaction selectedTransaction){
        Intent returnBookActivity = new Intent(this, ReturnBook.class);
        returnBookActivity.putExtra("SelectedTransaction", selectedTransaction);
        startActivity(returnBookActivity);
    }

    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp(){
        goToBookListActivity();
        return true;
    }

    public void onBackPressed(){
        goToBookListActivity();
        this.finish();
    }

    private void goToBookListActivity(){
        Intent goToMainActivity = new Intent(this, BookList.class);
        startActivity(goToMainActivity);
    }

    //TODO: Might have a separate layout and view holder for listing books borrowed by the user.
    public static class TransactionListViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        public TransactionListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setBookName(String name){
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
