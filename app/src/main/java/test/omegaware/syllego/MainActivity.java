package test.omegaware.syllego;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Book> bookList = new ArrayList<>();
    private BookDataController bdc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bdc = new BookDataController(this);
        bookList = getBookList();
        initializeRecyclerView();
    }

    private ArrayList<Book> getBookList(){
        Cursor bookData = bdc.getAllData();
        ArrayList<Book> temp = new ArrayList<>();
        while(bookData.moveToNext()){
            Book loadBook = new Book();
            loadBook.setBookID(bookData.getInt(0));
            loadBook.setBookName(bookData.getString(1));
            loadBook.setBookAuthor(bookData.getString(2));
            loadBook.setYearReleased(bookData.getString(3));
            loadBook.setISBN(bookData.getString(4));
            loadBook.setStatus(bookData.getString(5));
            temp.add(loadBook);
        }
        return temp;
    }


    private void initializeRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.BookContentList);
        BookContentListAdapter adapter = new BookContentListAdapter(this,bookList);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), 1);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                startActivity(addBookPage);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
