package test.omegaware.syllego;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class BookList extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter mRecyclerAdapter;
    private FirebaseAuth firebaseAuth;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Book").child("BookList");
        initializeRecyclerView();
    }

    private void initializeRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.BookContentList);
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
            case R.id.AddItem:
                Intent addBookPage = new Intent(this, AddBook.class);
                addBookPage.putExtra("UserID", userId);
                startActivity(addBookPage);
            default:
                return super.onOptionsItemSelected(item);
        }
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
