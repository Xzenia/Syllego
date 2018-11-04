package test.omegaware.syllego;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HistoryDataController {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference historyReference = database.getReference("History").child("Logs");
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void addToHistory(String message){
        DatabaseReference newHistoryReference = historyReference.push();
        History newHistory = new History();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy");
        String userId = firebaseAuth.getUid();
        newHistory.setDate(dateFormat.format(date));
        newHistory.setMessage(message);
        newHistory.setUserID(userId);
        newHistoryReference.setValue(newHistory);
    }
}
