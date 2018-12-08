package test.omegaware.syllego;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class EditBook extends AppCompatActivity {

    private static final String TAG = "EditBook";

    private EditText editBookNameField;
    private EditText editBookAuthorField;
    private EditText editBookYearReleasedField;
    private EditText editBookISBNField;
    private EditText editBookNumberOfCopiesField;
    private IntentIntegrator barCodeScan;

    private Book selectedBook;

    private BookDataController bdc;
    private HistoryDataController hdc;

    private DialogInterface.OnClickListener deleteDialogInterfaceListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        editBookNameField = findViewById(R.id.Edit_BookName);
        editBookAuthorField = findViewById(R.id.Edit_BookAuthor);
        editBookYearReleasedField = findViewById(R.id.Edit_BookYearReleased);
        editBookISBNField = findViewById(R.id.Edit_ISBN);
        editBookNumberOfCopiesField = findViewById(R.id.Edit_CopiesAvailable);
        bdc = new BookDataController();
        hdc = new HistoryDataController();
        Bundle data = getIntent().getExtras();
        selectedBook = (Book) data.get("SelectedBook");

        fillFields();

        barCodeScan = new IntentIntegrator(this);

        deleteDialogInterfaceListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteItem();
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        getSupportActionBar().setTitle("Edit "+selectedBook.getBookName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void fillFields(){
        editBookNameField.setText(selectedBook.getBookName());
        editBookAuthorField.setText(selectedBook.getBookAuthor());
        editBookYearReleasedField.setText(selectedBook.getYearReleased());
        editBookISBNField.setText(selectedBook.getISBN());
        editBookNumberOfCopiesField.setText(""+selectedBook.getNumberOfCopies());
    }

    public void editBook(View view){
        Book updatedBook = new Book();
        StringBuilder errorStringBuilder = new StringBuilder();

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

        if (editBookNumberOfCopiesField.getText().toString().isEmpty()){
            errorStringBuilder.append("Copies Available field is empty!");
        } else {
            updatedBook.setNumberOfCopies(Integer.parseInt(editBookNumberOfCopiesField.getText().toString()));
        }

        updatedBook.setBookID(selectedBook.getBookID());
        updatedBook.setUsername(selectedBook.getUsername());

        if (errorStringBuilder.toString().equals("")) {
            bdc.editData(updatedBook);
            toastMessage("Successfully edited book data!");
            goToViewBook(updatedBook);

            if (!updatedBook.getBookName().matches(selectedBook.getBookName())){
                hdc.addToHistory("You've edited "+selectedBook.getBookName()+"'s information in your catalogue! "+selectedBook.getBookName()+" was renamed to "+updatedBook.getBookName());
            } else {
                hdc.addToHistory("You've edited "+selectedBook.getBookName()+"'s information in your catalogue!");
            }

        } else {
            toastMessage(errorStringBuilder.toString());
        }
    }

    public void startScanActivity(View view){
        barCodeScan.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result == null) {
            toastMessage("Result not found!");
        } else {
            try {
                editBookISBNField.setText(result.getContents());
            } catch (Exception e) {
                e.printStackTrace();
                toastMessage(result.getContents());
            }
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
                //Band-aid fix to Theme.AppCompat related crash when delete prompt is started.
                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setMessage(R.string.delete_prompt_question)
                        .setPositiveButton("Yes", deleteDialogInterfaceListener)
                        .setNegativeButton("No",deleteDialogInterfaceListener).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteItem(){
        bdc.deleteData(selectedBook.getBookID());
        Intent goToMainActivity = new Intent (this, BookList.class);
        toastMessage("Book entry deleted!");
        hdc.addToHistory("You've deleted "+selectedBook.getBookName()+" from your catalogue!");
        startActivity(goToMainActivity);
    }

    private void goToViewBook(Book updatedBook) {
        Intent goToViewBook = new Intent(this, ViewBook.class);
        goToViewBook.putExtra("SelectedBook", updatedBook);
        startActivity(goToViewBook);
    }

    @Override
    public boolean onSupportNavigateUp(){
        goToViewBook(selectedBook);
        this.finish();
        return true;
    }

    public void onBackPressed(){
        goToViewBook(selectedBook);
        this.finish();
    }

    private void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
