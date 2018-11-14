package test.omegaware.syllego;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class BookList extends AppCompatActivity {

    private static final String TAG = "BookList";
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter mRecyclerAdapter;
    private FirebaseAuth firebaseAuth;
    private ProgressBar firebaseLoadingProgressBar;
    private String userId;
    private RecyclerView recyclerView;

    private boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Book").child("BookList");

        firebaseLoadingProgressBar = findViewById(R.id.FirebaseLoadingProgressBar);
        recyclerView = findViewById(R.id.BookContentList);
        showProgressBar();
        initializeRecyclerView();

        getSupportActionBar().setTitle(R.string.my_library);
    }

    private void initializeRecyclerView(){
        Query query = databaseReference.orderByChild("userID").equalTo(userId);
        FirebaseRecyclerOptions booksOptions = new FirebaseRecyclerOptions.Builder<Book>().setQuery(query, Book.class).build();

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this, 1);
        recyclerView.addItemDecoration(decoration);

        mRecyclerAdapter = new FirebaseRecyclerAdapter<Book, BookListViewHolder>(booksOptions) {
            @Override
            protected void onBindViewHolder(@NonNull BookListViewHolder holder, int position, @NonNull final Book model) {
                holder.setBookName(model.getBookName());
                holder.setBookAuthor(model.getBookAuthor());
                holder.setBookYearReleased(model.getYearReleased());
                holder.mView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        goToViewBook(model);
                    }
                });
            }
            @NonNull
            @Override
            public BookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookcontentlist_item, parent, false);
                return new BookListViewHolder(view);
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

    public void goToViewBook(Book book){
        Intent viewBook = new Intent(this, ViewBook.class);
        viewBook.putExtra("SelectedBook",book);
        startActivity(viewBook);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainactivity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.ShowHistory:
                Intent historyActivity = new Intent(this, DataHistory.class);
                startActivity(historyActivity);
                return true;
            case R.id.SignOutButton:
                signOutUser();
                return true;
            case R.id.AddItem:
                Intent addBookPage = new Intent(this, AddBook.class);
                addBookPage.putExtra("UserID", userId);
                startActivity(addBookPage);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void signOutUser(){
        Intent loginPage = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginPage);
        firebaseAuth.signOut();
        this.finish();
    }

    public void onBackPressed(){
        Log.d(TAG, "Back button has been pressed!");
        if (doubleBackToExitPressedOnce){
            moveTaskToBack(true);
            return;
        }
        doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(),getString(R.string.home_back_button_prompt), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public static class BookListViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public BookListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setBookName(String name){
            TextView bookName = mView.findViewById(R.id.BookName);
            bookName.setText(name);
        }

        public void setBookAuthor(String author){
            TextView bookAuthor = mView.findViewById(R.id.BookAuthor);
            bookAuthor.setText(author);
        }

        public void setBookYearReleased(String year){
            TextView bookYearReleased = mView.findViewById(R.id.BookYearReleased);
            bookYearReleased.setText(year);
        }
    }
}
