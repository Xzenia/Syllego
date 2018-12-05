package test.omegaware.syllego;

import android.content.ClipboardManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ViewBook extends AppCompatActivity {

    private final String TAG = "ViewBook";
    private TextView viewBookNameField;
    private TextView viewBookAuthorField;
    private TextView viewBookYearReleasedField;
    private TextView viewBookISBNField;
    private TextView viewBookNumberOfCopiesField;
    private Book selectedBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        viewBookNameField = findViewById(R.id.View_BookName);
        viewBookAuthorField = findViewById(R.id.View_BookAuthor);
        viewBookYearReleasedField = findViewById(R.id.View_BookYearReleased);
        viewBookISBNField = findViewById(R.id.View_BookISBN);
        viewBookNumberOfCopiesField = findViewById(R.id.View_NumberOfCopies);
        Bundle data = getIntent().getExtras();

        selectedBook = (Book) data.get("SelectedBook");
        fillFields(selectedBook);

        getSupportActionBar().setTitle(viewBookNameField.getText().toString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void fillFields(Book selectedBook){
        viewBookNameField.setText(selectedBook.getBookName());
        viewBookAuthorField.setText("Author: "+ selectedBook.getBookAuthor());
        viewBookYearReleasedField.setText("Release Year: "+ selectedBook.getYearReleased());
        viewBookISBNField.setText("ISBN: "+ selectedBook.getISBN());
        viewBookNumberOfCopiesField.setText("Copies Available: "+selectedBook.getNumberOfCopies());
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
                return true;
            case R.id.ViewBorrowersListItem:
                Intent borrowersListActivity = new Intent(this, BorrowerList.class);
                borrowersListActivity.putExtra("SelectedBook", selectedBook);
                startActivity(borrowersListActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        goToBookListActivity();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp(){
        goToBookListActivity();
        this.finish();
        return true;
    }

    private void goToBookListActivity(){
        Intent goToMainActivity = new Intent(this, BookList.class);
        startActivity(goToMainActivity);
    }

    public void borrowBook(View view){
        Intent goToBorrowBook = new Intent(this, BorrowBook.class);
        goToBorrowBook.putExtra("SelectedBook",selectedBook);
        if (selectedBook.getNumberOfCopies() >= 1){
            startActivity(goToBorrowBook);
        } else {
            toastMessage("There are no copies available at the moment. Please try again later.");
        }
    }

    public void setIsbnToClipboard(View view){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setText(selectedBook.getISBN());
        toastMessage("ISBN copied to clipboard!");
    }

    private void toastMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}
