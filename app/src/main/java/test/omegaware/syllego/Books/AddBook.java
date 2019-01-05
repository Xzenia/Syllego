package test.omegaware.syllego.Books;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import test.omegaware.syllego.Controller.BookDataController;
import test.omegaware.syllego.Controller.HistoryDataController;
import test.omegaware.syllego.Controller.WishlistDataController;
import test.omegaware.syllego.Model.Book;
import test.omegaware.syllego.R;

public class AddBook extends AppCompatActivity {

    private static final String TAG = "AddBook";
    private AutoCompleteTextView addBookNameField;
    private EditText addBookAuthorField;
    private EditText addBookYearReleasedField;
    private EditText addBookISBNField;
    private IntentIntegrator barCodeScan;
    private EditText copiesAvailableField;
    private EditText[] inputFields;

    private BookDataController bdc;
    private HistoryDataController hdc;
    private WishlistDataController wdc;

    private CheckBox addToWishlistCheckBox;

    private String userID;

    private String bookName = "";
    private String bookAuthor = "";
    private String bookDatePublished = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        Bundle getUserId = getIntent().getExtras();
        userID = (String) getUserId.get("UserID");
        bdc = new BookDataController();
        hdc = new HistoryDataController();
        wdc = new WishlistDataController();

        addBookNameField = findViewById(R.id.Add_BookName);
        addBookAuthorField = findViewById(R.id.Add_BookAuthor);
        addBookYearReleasedField = findViewById(R.id.Add_BookYearReleased);
        addBookISBNField = findViewById(R.id.Add_ISBN);
        copiesAvailableField = findViewById(R.id.Add_CopiesAvailable);
        addToWishlistCheckBox = findViewById(R.id.Add_WishlistCheckBox);

        inputFields = new EditText[]{addBookNameField, addBookAuthorField, addBookYearReleasedField, addBookISBNField, copiesAvailableField};

        barCodeScan = new IntentIntegrator(this);

        getSupportActionBar().setTitle(R.string.add_a_book);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addBook(View view){
        Book newBook = new Book();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat reportsDateFormat = new SimpleDateFormat("MMM yyyy");
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

        if (copiesAvailableField.getText().toString().isEmpty()){
            errorStringBuilder.append("Copies Available field is empty!");
        } else {
            newBook.setNumberOfCopies(Integer.parseInt(copiesAvailableField.getText().toString()));
        }

        newBook.setUserID(userID);
        newBook.setDateAdded(dateFormat.format(date));
        newBook.setFilterDateAdded(reportsDateFormat.format(date));

        if (errorStringBuilder.toString().equals("")) {
            if (addToWishlistCheckBox.isChecked()){
                wdc.addData(newBook);
                toastMessage("Successfully added book data to your wish list!");
                clearFields();
                hdc.addToHistory("You've added "+newBook.getBookName()+" in your wishlist!");
            } else {
                bdc.addData(newBook);
                toastMessage("Successfully added book data to library!");
                clearFields();
                hdc.addToHistory("You've added "+newBook.getBookName()+" in the catalogue!");
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
                addBookISBNField.setText(result.getContents());
                getBookData(result.getContents());

                addBookNameField.setText(bookName);
                addBookAuthorField.setText(bookAuthor);
                addBookYearReleasedField.setText(bookDatePublished);
            } catch (Exception e) {
                e.printStackTrace();
                toastMessage(result.getContents());
            }
        }
    }

    private void getBookData(String isbn){
        String baseUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
        String url = baseUrl + isbn;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        if (response.length() > 0) {
                            try {
                                JSONArray responseArray = response.getJSONArray("items");
                                for (int counter = 0; counter < responseArray.length(); counter++) {
                                    JSONObject book = responseArray.getJSONObject(counter);
                                    JSONObject item = book.getJSONObject("volumeInfo");
                                    JSONArray authorArray = item.getJSONArray("authors");
                                    bookName = item.getString("title");
                                    bookDatePublished = item.getString("publishedDate");

                                    if (authorArray.length() > 1){
                                        for (int index = 0; index < authorArray.length(); index++){
                                            if (index == 0){
                                                bookAuthor = authorArray.get(index).toString();
                                            } else {
                                                bookAuthor += ", " + authorArray.get(index).toString();
                                            }
                                        }
                                    } else {
                                        bookAuthor = authorArray.get(0).toString();
                                    }
                                }
                            } catch (JSONException ex) {
                                Log.e(TAG, ex.toString());
                            }
                        } else {
                            toastMessage("No book found by that ISBN!");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        toastMessage("Exception Occurred: "+error.toString());
                        Log.e(TAG, error.toString());
                    }
        });
        queue.add(jsonObjReq);
    }

    private void clearFields(){
        for(EditText field: inputFields){
            field.setText("");
        }
        if (addToWishlistCheckBox.isChecked()){
            addToWishlistCheckBox.toggle();
        }
    }

    private void toastMessage(String message){
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
