package test.omegaware.syllego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.Random;

public class AddBook extends AppCompatActivity {

    private static final String TAG = "AddBook";
    AutoCompleteTextView addBookNameField;
    EditText addBookAuthorField;
    EditText addBookYearReleasedField;
    EditText addBookISBNField;

    RadioGroup addBookStatusRadioGroup;

    BookDataController bdc;

    EditText[] inputFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        addBookNameField = findViewById(R.id.Add_BookName);
        addBookAuthorField = findViewById(R.id.Add_BookAuthor);
        addBookYearReleasedField = findViewById(R.id.Add_BookYearReleased);
        addBookISBNField = findViewById(R.id.Add_ISBN);

        addBookStatusRadioGroup = findViewById(R.id.Add_ProgressRadioGroup);
        inputFields = new EditText[]{addBookNameField, addBookAuthorField, addBookYearReleasedField, addBookISBNField};

        bdc = new BookDataController(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addBookStatusRadioGroup.check(R.id.Add_RadioButtonCompleted);

    }

    public void addBook(View view){
        Book newBook = new Book();
        Random random = new Random();
        StringBuilder errorStringBuilder = new StringBuilder("");
        if (addBookNameField.getText().toString().isEmpty()){
            errorStringBuilder.append(getString(R.string.BookNameEmptyError));
            errorStringBuilder.append("\n");
        } else {
            newBook.setBookName(addBookNameField.getText().toString());
        }

        if (addBookAuthorField.getText().toString().isEmpty()){
            errorStringBuilder.append(getString(R.string.BookAuthorEmptyError));
            errorStringBuilder.append("\n");
        } else {
            newBook.setBookAuthor(addBookAuthorField.getText().toString());
        }

        if (addBookYearReleasedField.getText().toString().isEmpty()){
            errorStringBuilder.append(getString(R.string.YearReleasedEmptyError));
            errorStringBuilder.append("\n");
        } else {
            newBook.setYearReleased(addBookYearReleasedField.getText().toString());
        }

        if (addBookISBNField.getText().toString().isEmpty()){
            errorStringBuilder.append(getString(R.string.ISBNEmptyError));
        } else {
            newBook.setISBN(addBookISBNField.getText().toString());
        }

        newBook.setBookID(random.nextInt(999999));

        RadioButton selectedRadioButton = findViewById(addBookStatusRadioGroup.getCheckedRadioButtonId());
        newBook.setStatus(selectedRadioButton.getText().toString());

        if (errorStringBuilder.toString().equals("")) {
            bdc.addData(newBook);
            toastMessage("Successfully added book data!");
            clearFields();
        } else {
            toastMessage(errorStringBuilder.toString());
        }
    }

    private void clearFields(){
        for(EditText field: inputFields){
            field.setText("");
        }
        addBookStatusRadioGroup.check(R.id.Add_RadioButtonCompleted);
    }

    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    /*
    /   TODO: BOTH OF THESE METHODS ARE HACKS!
    /   THERE NEEDS TO BE A WAY TO UPDATE RECYCLERVIEW WITHOUT RESORTING TO STARTING IT UP AGAIN.
    /   THERE IS NOTICEABLE LAG WHEN RESTARTING MAINACTIVITY.
    */
    @Override
    public void onBackPressed(){
        Intent goBackToHome = new Intent(this, MainActivity.class);
        startActivity(goBackToHome);
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent goBackToHome = new Intent(this, MainActivity.class);
        startActivity(goBackToHome);
        return true;
    }
}
