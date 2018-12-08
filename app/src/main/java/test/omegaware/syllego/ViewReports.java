package test.omegaware.syllego;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
    private ProgressBar firebaseLoadingProgressBar;
    private RecyclerView recyclerView;
    private TextView dateTextView;
    private MonthPickerDialog.Builder builder;

    private Date date;
    private Date dateStart;
    private Date dateEnd;
    private Calendar calendar;
    private DateFormat searchDateFormat;

    private final String[] semesterLabels = new String[]{ "1st Semester", "Summer Semester", "2nd Semester"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);

        databaseReference = FirebaseDatabase.getInstance().getReference("Book").child("BookList");
        firebaseLoadingProgressBar = findViewById(R.id.ViewReports_FirebaseLoadingProgressBar);
        recyclerView = findViewById(R.id.BooksAddedList);
        dateTextView = findViewById(R.id.DateTextView);

        date = Calendar.getInstance().getTime();
        dateStart = Calendar.getInstance().getTime();
        dateEnd = Calendar.getInstance().getTime();

        calendar = Calendar.getInstance();
        searchDateFormat = new SimpleDateFormat("MMM yyyy");

        registerForContextMenu(dateTextView);
        getSupportActionBar().setTitle("Reports");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showReportList(){
        DateFormat textViewDateFormat = new SimpleDateFormat("MMM yyyy");
        Query query = databaseReference.orderByChild("filterDateAdded").startAt(searchDateFormat.format(date)).endAt(searchDateFormat.format(date)+"\uf8ff");
        FirebaseRecyclerOptions booksOptions = new FirebaseRecyclerOptions.Builder<Book>().setQuery(query, Book.class).build();
        String semester = "";
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
            semester = makeSemesterText("2nd Semester");
        }
        else if (calendar.get(Calendar.MONTH) > 3 && calendar.get(Calendar.MONTH) <= 7){
            semester = makeSemesterText("Summer Semester");
        }
        else if (calendar.get(Calendar.MONTH) > 7 && calendar.get(Calendar.MONTH) <= 11){
            semester = makeSemesterText("1st Semester");
        }

        dateTextView.setText(semester+"\n"+textViewDateFormat.format(date));
        mRecyclerAdapter.startListening();
    }

    private void filterByDay(Date date){
        DateFormat textViewDateFormat = new SimpleDateFormat("MMM dd yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Query query = databaseReference.orderByChild("dateAdded").startAt(dateFormat.format(date)).endAt(dateFormat.format(date)+"\uf8ff");
        FirebaseRecyclerOptions booksOptions = new FirebaseRecyclerOptions.Builder<Book>().setQuery(query, Book.class).build();
        String semester = "";
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
            semester = makeSemesterText("2nd Semester");
        }
        else if (calendar.get(Calendar.MONTH) > 3 && calendar.get(Calendar.MONTH) <= 7){
            semester = makeSemesterText("Summer Semester");
        }
        else if (calendar.get(Calendar.MONTH) > 7 && calendar.get(Calendar.MONTH) <= 11){
            semester = makeSemesterText("1st Semester");
        }

        dateTextView.setText(semester+"\n"+textViewDateFormat.format(date));
        mRecyclerAdapter.startListening();
    }

    public void filterBySemester(String selectedItem){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD");

        if (selectedItem.matches("2nd Semester")){
            setDateStartAndDateEnd(0,3);
        }

        else if (selectedItem.matches("Summer Semester")){
            setDateStartAndDateEnd(4, 7);
        }

        else if (selectedItem.matches("1st Semester")) {
            setDateStartAndDateEnd(8,11);
        }

        Query semesterQuery = databaseReference.orderByChild("dateAdded").startAt(dateFormat.format(dateStart)).endAt(dateFormat.format(dateEnd)+"\uf8ff");
        FirebaseRecyclerOptions booksOptions = new FirebaseRecyclerOptions.Builder<Book>().setQuery(semesterQuery, Book.class).build();

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

        semesterQuery.addListenerForSingleValueEvent(new ValueEventListener() {
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

        dateTextView.setText(makeSemesterText(selectedItem));
        recyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.startListening();
    }

    public void setDateStartAndDateEnd(int startMonth, int endMonth){
        Calendar temp = (Calendar) calendar.clone();
        temp.set(Calendar.MONTH, startMonth);
        dateStart = temp.getTime();
        temp.set(Calendar.MONTH, endMonth);
        dateEnd = temp.getTime();
    }

    public String makeSemesterText(String selectedSemester){
        Calendar temp = (Calendar) calendar.clone();
        StringBuilder semester = new StringBuilder();
        semester.append(selectedSemester);
        if (selectedSemester.matches("2nd Semester")){
            temp.add(Calendar.YEAR, -1);
            semester.append(" of "+temp.get(Calendar.YEAR)+" - ");
            temp.add(Calendar.YEAR, 1);
            semester.append(temp.get(Calendar.YEAR));
        } else {
            semester.append(" of "+temp.get(Calendar.YEAR)+" - ");
            temp.add(Calendar.YEAR, 1);
            semester.append(temp.get(Calendar.YEAR));
        }

        return semester.toString();
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

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewreport_filtercontextmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ViewReports_Semester:
                semesterMenuPopup();
                return true;
            case R.id.ViewReports_Daily:
                dayMenuPopup();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void semesterMenuPopup() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final Spinner semesterSpinner = new Spinner(this);
        ArrayAdapter statusAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, semesterLabels);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alertDialogBuilder.setTitle("Select a Semester");
        semesterSpinner.setAdapter(statusAdapter);

        alertDialogBuilder.setView(semesterSpinner);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filterBySemester(semesterSpinner.getSelectedItem().toString());
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void dayMenuPopup() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDateCalendar = Calendar.getInstance();
                        Date selectedDate;
                        selectedDateCalendar.set(Calendar.YEAR, year);
                        selectedDateCalendar.set(Calendar.MONTH, monthOfYear);
                        selectedDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selectedDate = selectedDateCalendar.getTime();
                        filterByDay(selectedDate);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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
        builder.setMinYear(2016);
        builder.setMaxYear(Calendar.getInstance().get(Calendar.YEAR));
        builder.build().show();
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
