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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AddBook extends AppCompatActivity {

    private static final String TAG = "AddBook";
    private AutoCompleteTextView addBookNameField;
    private EditText addBookAuthorField;
    private EditText addBookYearReleasedField;
    private EditText addBookISBNField;
    private IntentIntegrator barCodeScan;

    private RadioGroup addBookStatusRadioGroup;

    private BookDataController bdc;

    private HistoryDataController hdc;

    private EditText[] inputFields;

    private String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        Bundle getUserId = getIntent().getExtras();
        UserID = (String) getUserId.get("UserID");
        bdc = new BookDataController();
        hdc = new HistoryDataController();

        addBookNameField = findViewById(R.id.Add_BookName);
        addBookAuthorField = findViewById(R.id.Add_BookAuthor);
        addBookYearReleasedField = findViewById(R.id.Add_BookYearReleased);
        addBookISBNField = findViewById(R.id.Add_ISBN);
        addBookStatusRadioGroup = findViewById(R.id.Add_ProgressRadioGroup);
        inputFields = new EditText[]{addBookNameField, addBookAuthorField, addBookYearReleasedField, addBookISBNField};
        addBookStatusRadioGroup.check(R.id.Add_RadioButtonCompleted);

        barCodeScan = new IntentIntegrator(this);

        getSupportActionBar().setTitle(R.string.add_a_book);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addBook(View view){
        Book newBook = new Book();
        StringBuilder errorStringBuilder = new StringBuilder();
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

        RadioButton selectedRadioButton = findViewById(addBookStatusRadioGroup.getCheckedRadioButtonId());
        newBook.setStatus(selectedRadioButton.getText().toString());
        newBook.setUserID(UserID);

        if (errorStringBuilder.toString().equals("")) {
            bdc.addData(newBook);
            toastMessage("Successfully added book data!");
            clearFields();
            hdc.addToHistory("You've added "+newBook.getBookName()+" in your catalogue!");
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
                addBookISBNField.setText(result.getContents());
            } catch (Exception e) {
                e.printStackTrace();
                toastMessage(result.getContents());
            }
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

    @Override
    public void onBackPressed(){
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp(){
        this.finish();
        return true;
    }
}
