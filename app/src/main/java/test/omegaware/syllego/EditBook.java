package test.omegaware.syllego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class EditBook extends AppCompatActivity {

    private static final String TAG = "EditBook";

    EditText editBookNameField;
    EditText editBookAuthorField;
    EditText editBookYearReleasedField;
    EditText editBookISBNField;

    RadioGroup editBookStatusRadioGroup;

    Book selectedBook;

    BookDataController bdc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        editBookNameField = findViewById(R.id.Edit_BookName);
        editBookAuthorField = findViewById(R.id.Edit_BookAuthor);
        editBookYearReleasedField = findViewById(R.id.Edit_BookYearReleased);
        editBookISBNField = findViewById(R.id.Edit_ISBN);

        editBookStatusRadioGroup = findViewById(R.id.Edit_ProgressRadioGroup);

        bdc = new BookDataController(this);

        Bundle data = getIntent().getExtras();
        selectedBook = (Book) data.get("SelectedBook");
        fillFields();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void fillFields(){
        editBookNameField.setText(selectedBook.getBookName());
        editBookAuthorField.setText(selectedBook.getBookAuthor());
        editBookYearReleasedField.setText(selectedBook.getYearReleased());
        editBookISBNField.setText(selectedBook.getISBN());

        switch(selectedBook.getStatus()){
            case "Completed" :
                editBookStatusRadioGroup.check(R.id.Edit_RadioButtonCompleted);
                break;
            case "Pending":
                editBookStatusRadioGroup.check(R.id.Edit_RadioButtonPending);
                break;
            case "Dropped":
                editBookStatusRadioGroup.check(R.id.Edit_RadioButtonDropped);
                break;
            default:
                break;
        }
    }

    public void editBook(View view){

        Book updatedBook = new Book();

        StringBuilder errorStringBuilder = new StringBuilder("");

        if (editBookNameField.getText().toString().isEmpty()){
            errorStringBuilder.append(getString(R.string.BookNameEmptyError));
            errorStringBuilder.append("\n");
        } else {
            updatedBook.setBookName(editBookNameField.getText().toString());
        }

        if (editBookAuthorField.getText().toString().isEmpty()){
            errorStringBuilder.append(getString(R.string.BookAuthorEmptyError));
            errorStringBuilder.append("\n");
        } else {
            updatedBook.setBookAuthor(editBookAuthorField.getText().toString());
        }

        if (editBookYearReleasedField.getText().toString().isEmpty()){
            errorStringBuilder.append(getString(R.string.YearReleasedEmptyError));
            errorStringBuilder.append("\n");
        } else {
            updatedBook.setYearReleased(editBookYearReleasedField.getText().toString());
        }

        if (editBookISBNField.getText().toString().isEmpty()){
            errorStringBuilder.append(getString(R.string.ISBNEmptyError));
        } else {
            updatedBook.setISBN(editBookISBNField.getText().toString());
        }

        updatedBook.setBookID(selectedBook.getBookID());

        RadioButton selectedRadioButton = findViewById(editBookStatusRadioGroup.getCheckedRadioButtonId());
        updatedBook.setStatus(selectedRadioButton.getText().toString());

        if (errorStringBuilder.toString().equals("")) {
            bdc.editData(updatedBook);
            toastMessage("Successfully edited book data!");
            goToViewBook(updatedBook);
        } else {
            toastMessage(errorStringBuilder.toString());
        }

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editbook_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.DeleteItem:
                bdc.deleteData(selectedBook.getBookID());
                Intent goToMainActivity = new Intent (this, BookList.class);
                toastMessage("Book entry deleted!");
                startActivity(goToMainActivity);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToViewBook(Book updatedBook) {
        Intent goToViewBook = new Intent(this, ViewBook.class);
        goToViewBook.putExtra("SelectedBook", updatedBook);
        startActivity(goToViewBook);
    }

    public void onBackPressed(){
        goToViewBook(selectedBook);
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp(){
        goToViewBook(selectedBook);
        this.finish();
        return true;
    }

    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
