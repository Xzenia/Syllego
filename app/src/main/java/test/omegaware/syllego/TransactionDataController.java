package test.omegaware.syllego;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TransactionDataController {

    private static final String TAG = "TransactionDataController";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference transactionReference = database.getReference("Transaction");
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void borrowBook(Transaction transaction){
        DatabaseReference borrowReference = transactionReference.push();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        transaction.setUserFullName(firebaseAuth.getCurrentUser().getDisplayName());
        transaction.setTransactionId(borrowReference.getKey());
        transaction.setDateBorrowed(dateFormat.format(date));
        transaction.setTimeBorrowed(timeFormat.format(date));
        transaction.setUserId(firebaseAuth.getCurrentUser().getUid());
        transaction.setStatus("Borrowing");
        transaction.setUserId_status(transaction.getUserId()+"_"+transaction.getStatus());
        borrowReference.setValue(transaction);
    }

    public void returnBook(Transaction transaction) {
        DatabaseReference returnReference = transactionReference.child(transaction.getTransactionId());
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm aa");
        transaction.setDateReturned(dateFormat.format(date));
        transaction.setTimeReturned(timeFormat.format(date));
        transaction.setStatus("Returned");
        transaction.setUserId_status(transaction.getUserId()+"_"+transaction.getStatus());
        returnReference.setValue(transaction);
    }

}
