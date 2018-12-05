package test.omegaware.syllego;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewReports extends AppCompatActivity {

    private final String TAG = "ViewReports";
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter mRecyclerAdapter;
    private FirebaseAuth firebaseAuth;
    private ProgressBar firebaseLoadingProgressBar;
    private RecyclerView recyclerView;
    private TextView dateTextView;
    private Date date;
    private Calendar calendar;
    private MonthPickerDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Book").child("BookList");
        firebaseLoadingProgressBar = findViewById(R.id.ViewReports_FirebaseLoadingProgressBar);
        recyclerView = findViewById(R.id.BooksAddedList);
        dateTextView = findViewById(R.id.DateTextView);
        date = Calendar.getInstance().getTime();
        calendar = Calendar.getInstance();

        getSupportActionBar().setTitle("Reports");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void showMonthPickerDialog(View view){
        builder = new MonthPickerDialog.Builder(this, new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                calendar.set(Calendar.MONTH, selectedMonth);
                calendar.set(Calendar.YEAR, selectedYear);
                date = calendar.getTime();
                showReportList();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
        builder.setMinYear(2018);
        builder.setMaxYear(Calendar.getInstance().get(Calendar.YEAR));
        builder.build().show();

    }

    private void showReportList(){
        DateFormat textViewDateFormat = new SimpleDateFormat("MMM yyyy");
        DateFormat searchDateFormat = new SimpleDateFormat("MMM yyyy");
        StringBuilder semester = new StringBuilder();
        Query query = databaseReference.orderByChild("filterDateAdded").startAt(searchDateFormat.format(date)).endAt(searchDateFormat.format(date)+"\uf8ff");
        FirebaseRecyclerOptions booksOptions = new FirebaseRecyclerOptions.Builder<Book>().setQuery(query, Book.class).build();

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerAdapter = new FirebaseRecyclerAdapter<Book, BookList.BookListViewHolder>(booksOptions) {
            @Override
            protected void onBindViewHolder(@NonNull BookList.BookListViewHolder holder, int position, @NonNull final Book model) {
                holder.setBookName(model.getBookName());
                holder.setBookAuthor(model.getBookAuthor());
                holder.setBookYearReleased(model.getYearReleased());
            }
            @NonNull
            @Override
            public BookList.BookListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookcontentlist_item, parent, false);
                return new BookList.BookListViewHolder(view);
            }
        };

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
                toastMessage("Exception occurred:\n"+databaseError.toException());
            }
        });

        recyclerView.setAdapter(mRecyclerAdapter);

        if (calendar.get(Calendar.MONTH) <= 3){
            semester.append("2nd Semester");
        }
        else if (calendar.get(Calendar.MONTH) > 3 && calendar.get(Calendar.MONTH) <= 7){
            semester.append("Summer Semester");
        }
        else if (calendar.get(Calendar.MONTH) > 7 && calendar.get(Calendar.MONTH) <= 11){
            semester.append("1st Semester");
        }
        //TODO: Write a better way to show the year after the current one. This is not a good way to do it.
        semester.append(" of "+calendar.get(Calendar.YEAR)+" - ");
        calendar.add(Calendar.YEAR, 1);
        semester.append(calendar.get(Calendar.YEAR));
        calendar.add(Calendar.YEAR, -1);
        dateTextView.setText(semester.toString()+"\n"+textViewDateFormat.format(date));
        mRecyclerAdapter.startListening();
    }

    public void nextDay(View view){
        calendar.add(Calendar.MONTH, 1);
        date = calendar.getTime();
        showReportList();
    }

    public void previousDay(View view){
        calendar.add(Calendar.MONTH, -1);
        date = calendar.getTime();
        showReportList();
    }

    public void onStart(){
        super.onStart();
        showProgressBar();
        showReportList();
    }

    public void onStop(){
        super.onStop();
        mRecyclerAdapter.stopListening();
    }

    private void showProgressBar(){
        firebaseLoadingProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideProgressBar(){
        firebaseLoadingProgressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp(){
        this.finish();
        return true;
    }

    public void onBackPressed(){
        this.finish();
    }

    private void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
