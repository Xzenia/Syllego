package test.omegaware.syllego;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ReturnBook extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private Transaction selectedTransaction;
    private TransactionDataController tdc;
    private IntentIntegrator barCodeScan;
    private BookDataController bdc;
    private Book updatedBook;

    private TextView copiesBorrowedTextView;
    private EditText isbnTextField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);
        Bundle data = getIntent().getExtras();

        selectedTransaction = (Transaction) data.get("SelectedTransaction");
        barCodeScan = new IntentIntegrator(this);
        copiesBorrowedTextView = findViewById(R.id.ReturnBook_CopiesBorrowedTextView);
        copiesBorrowedTextView.setText("Copies Borrowed: "+selectedTransaction.getNumberOfBooksBorrowed());

        isbnTextField = findViewById(R.id.Return_ISBN);

        tdc = new TransactionDataController();
        bdc = new BookDataController();
        getSupportActionBar().setTitle("Return "+selectedTransaction.getBookName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                if (result.getContents().matches(selectedTransaction.getBookIsbn())){
                    processTransaction();
                } else {
                    toastMessage("Barcode data does not match with saved record. Please try again.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                toastMessage(result.getContents());
            }
        }
    }

    public void getManualInput(View view){
        if (!isbnTextField.getText().toString().matches("")){
            if (isbnTextField.getText().toString().matches(selectedTransaction.getBookIsbn())){
                processTransaction();
            } else {
                toastMessage("ISBN entered does not match with saved record. Please try again.");
            }
        } else {
            toastMessage("ISBN text field is empty! Please enter the book's ISBN and try again.");
        }
    }

    private void processTransaction(){
        DatabaseReference updateBookReference = firebaseDatabase.getReference().child("Book").child("BookList");
        Query bookQuery = updateBookReference.orderByChild("isbn").equalTo(selectedTransaction.getBookIsbn());
        bookQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        updatedBook = snapshot.getValue(Book.class);
                        updatedBook.setNumberOfCopies(updatedBook.getNumberOfCopies() + selectedTransaction.getNumberOfBooksBorrowed());
                        sendToDatabase();
                    }
                } else {
                    toastMessage("Book is not available in the database!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendToDatabase(){
        Intent booksBorrowedListActivity = new Intent(this, BooksBorrowedList.class);
        tdc.returnBook(selectedTransaction);
        bdc.editData(updatedBook);
        toastMessage("Return of book(s) complete!");
        startActivity(booksBorrowedListActivity);
    }

    @Override
    public boolean onSupportNavigateUp(){
        goToBooksBorrowedListActivity();
        this.finish();
        return true;
    }

    @Override
    public void onBackPressed(){
        goToBooksBorrowedListActivity();
        this.finish();
    }

    private void goToBooksBorrowedListActivity(){
        Intent booksBorrowedListActivity = new Intent(this, BooksBorrowedList.class);
        startActivity(booksBorrowedListActivity);
    }

    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
