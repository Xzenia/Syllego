package test.omegaware.syllego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class ViewBook extends AppCompatActivity {

    private static final String TAG = "ViewBook";
    TextView viewBookNameField;
    TextView viewBookAuthorField;
    TextView viewBookYearReleasedField;
    TextView viewBookISBNField;
    TextView viewBookStatusField;
    Book selectedBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        viewBookNameField = findViewById(R.id.View_BookName);
        viewBookAuthorField = findViewById(R.id.View_BookAuthor);
        viewBookYearReleasedField = findViewById(R.id.View_BookYearReleased);
        viewBookISBNField = findViewById(R.id.View_BookISBN);
        viewBookStatusField = findViewById(R.id.View_BookStatus);

        Bundle data = getIntent().getExtras();

        selectedBook = (Book) data.get("SelectedBook");
        fillFields(selectedBook);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void fillFields(Book selectedBook){
        viewBookNameField.setText("Book Name: "+ selectedBook.getBookName());
        viewBookAuthorField.setText("Book Author: "+ selectedBook.getBookAuthor());
        viewBookYearReleasedField.setText("Book Release Year: "+ selectedBook.getYearReleased());
        viewBookISBNField.setText("Book ISBN: "+ selectedBook.getISBN());
        viewBookStatusField.setText("Status: "+ selectedBook.getStatus());
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewbook_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.EditItem:
                Intent editBookPage = new Intent(this, EditBook.class);
                editBookPage.putExtra("SelectedBook", selectedBook);
                startActivity(editBookPage);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        Intent goToMainActivity = new Intent(this, MainActivity.class);
        startActivity(goToMainActivity);
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent goToMainActivity = new Intent(this, MainActivity.class);
        startActivity(goToMainActivity);
        this.finish();
        return true;
    }


}
