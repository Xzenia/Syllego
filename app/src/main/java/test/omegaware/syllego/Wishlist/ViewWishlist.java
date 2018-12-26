package test.omegaware.syllego.Wishlist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.SearchView;
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

import test.omegaware.syllego.Books.ViewBook;
import test.omegaware.syllego.Books.ViewBookList;
import test.omegaware.syllego.Model.Book;
import test.omegaware.syllego.R;

public class ViewWishlist extends AppCompatActivity {

    private static final String TAG = "ViewBookList";
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter mRecyclerAdapter;
    private FirebaseAuth firebaseAuth;
    private ProgressBar firebaseLoadingProgressBar;
    private RecyclerView recyclerView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wishlist);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Wishlist").child(firebaseAuth.getCurrentUser().getUid());
        databaseReference.keepSynced(true);
        firebaseLoadingProgressBar = findViewById(R.id.FirebaseLoadingProgressBar);
        recyclerView = findViewById(R.id.WishlistContentList);

        getSupportActionBar().setTitle("Wish List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void queryList(String search){
        Query query = databaseReference.orderByChild("bookName").startAt(search).endAt(search+"\uf8ff");
        FirebaseRecyclerOptions booksOptions = new FirebaseRecyclerOptions.Builder<Book>().setQuery(query, Book.class).build();

        mRecyclerAdapter = new FirebaseRecyclerAdapter<Book, ViewBookList.BookListViewHolder>(booksOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewBookList.BookListViewHolder holder, int position, @NonNull final Book model) {
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
            public ViewBookList.BookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookcontentlist_item, parent, false);
                return new ViewBookList.BookListViewHolder(view);
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
        mRecyclerAdapter.startListening();
    }

    public void onStart(){
        super.onStart();
        showProgressBar();
        initializeRecyclerView();
    }

    public void onStop(){
        super.onStop();
        mRecyclerAdapter.stopListening();
    }

    private void showProgressBar(){
        firebaseLoadingProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideProgressBar(){
        firebaseLoadingProgressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void initializeRecyclerView(){
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        queryList("");
        recyclerView.setAdapter(mRecyclerAdapter);
    }

    private void goToViewBook(Book book){
        Intent viewBook = new Intent(this, ViewBook.class);
        viewBook.putExtra("WishList", true);
        viewBook.putExtra("SelectedBook",book);
        startActivity(viewBook);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewwishlist_menu,menu);

        MenuItem item = menu.findItem(R.id.SearchItem);
        searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryList(newText);
                return false;
            }
        });

        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed(){
        goToPreviousActivity();
    }

    @Override
    public boolean onSupportNavigateUp(){
        goToPreviousActivity();
        return true;
    }

    private void goToPreviousActivity(){
        Intent goToMainActivity = new Intent(this, ViewBookList.class);
        startActivity(goToMainActivity);
        this.finish();
    }

    private void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
