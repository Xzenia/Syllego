package test.omegaware.syllego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BorrowBook extends AppCompatActivity {

    private TextView numberOfCopiesLabel;
    private EditText numberOfCopiesToBorrowField;
    private Book selectedBook;
    private TransactionDataController tdc;
    private BookDataController bdc;
    private int numberOfCopiesToBorrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);

        tdc = new TransactionDataController();
        bdc = new BookDataController();
        Bundle data = getIntent().getExtras();
        selectedBook = (Book) data.get("SelectedBook");

        numberOfCopiesLabel = findViewById(R.id.NumberOfCopiesLeftTextView);
        numberOfCopiesToBorrowField = findViewById(R.id.CopiesToBorrowField);
        numberOfCopiesToBorrow = 1;
        numberOfCopiesToBorrowField.setText(""+numberOfCopiesToBorrow);
        numberOfCopiesLabel.setText("Number of Copies Available: "+selectedBook.getNumberOfCopies());

        getSupportActionBar().setTitle("Borrow "+selectedBook.getBookName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addValue(View view){
       if (numberOfCopiesToBorrow < selectedBook.getNumberOfCopies()){
           numberOfCopiesToBorrow++;
           numberOfCopiesToBorrowField.setText(String.valueOf(numberOfCopiesToBorrow));
       } else {
           toastMessage("The number of copies you want to borrow exceed what is available!");
       }
    }

    public void decreaseValue(View view){
        if (numberOfCopiesToBorrow > 1){
            numberOfCopiesToBorrow--;
            numberOfCopiesToBorrowField.setText(String.valueOf(numberOfCopiesToBorrow));
        } else {
            toastMessage("You cannot borrow 0 copies!");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    public void processTransaction(View view){
        Transaction newTransaction = new Transaction();
        newTransaction.setBookName(selectedBook.getBookName());
        newTransaction.setNumberOfBooksBorrowed(numberOfCopiesToBorrow);
        newTransaction.setBookId(selectedBook.getBookID());
        newTransaction.setBookIsbn(selectedBook.getISBN());
        selectedBook.setNumberOfCopies(selectedBook.getNumberOfCopies() - numberOfCopiesToBorrow);
        tdc.borrowBook(newTransaction);
        bdc.editData(selectedBook);
        toastMessage("Transaction has been recorded to database!");
        backToPreviousPage();
    }

    @Override
    public void onBackPressed(){
        backToPreviousPage();
    }

    @Override
    public boolean onSupportNavigateUp(){
        backToPreviousPage();
        return true;
    }

    private void backToPreviousPage(){
        Intent goToMainActivity = new Intent(this, BookList.class);
        startActivity(goToMainActivity);
        this.finish();
    }



}
