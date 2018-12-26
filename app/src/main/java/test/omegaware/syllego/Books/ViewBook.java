package test.omegaware.syllego.Books;

import android.content.ClipboardManager;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import test.omegaware.syllego.Controller.BookDataController;
import test.omegaware.syllego.Controller.WishlistDataController;
import test.omegaware.syllego.Model.Book;
import test.omegaware.syllego.R;
import test.omegaware.syllego.Wishlist.ViewWishlist;

public class ViewBook extends AppCompatActivity {

    private final String TAG = "ViewBook";
    private TextView viewBookNameField;
    private TextView viewBookAuthorField;
    private TextView viewBookYearReleasedField;
    private TextView viewBookISBNField;
    private Book selectedBook;

    private boolean wishList = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle data = getIntent().getExtras();
        wishList = data.getBoolean("WishList");
        //Switch to wishlist layout if user came from wishlist list activity.
        if (wishList){
            setContentView(R.layout.activity_view_wishlist_item);
        } else {
            setContentView(R.layout.activity_view_book);
        }

        viewBookNameField = findViewById(R.id.View_BookName);
        viewBookAuthorField = findViewById(R.id.View_BookAuthor);
        viewBookYearReleasedField = findViewById(R.id.View_BookYearReleased);
        viewBookISBNField = findViewById(R.id.View_BookISBN);


        selectedBook = (Book) data.get("SelectedBook");
        fillFields(selectedBook);

        getSupportActionBar().setTitle(viewBookNameField.getText().toString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void fillFields(Book selectedBook){
        viewBookNameField.setText(selectedBook.getBookName());
        viewBookAuthorField.setText("Author: "+ selectedBook.getBookAuthor());
        viewBookYearReleasedField.setText("Release Year: "+ selectedBook.getYearReleased());
        viewBookISBNField.setText("ISBN: "+ selectedBook.getISBN());
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewbook_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.EditItem:
                Intent editBookPage = new Intent(this, EditBook.class);
                editBookPage.putExtra("SelectedBook", selectedBook);
                if (wishList){
                    editBookPage.putExtra("WishList", true);
                }
                startActivity(editBookPage);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        goToPreviousActivity();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp(){
        goToPreviousActivity();
        this.finish();
        return true;
    }

    private void goToPreviousActivity(){
        Intent goToMainActivity = new Intent(this, ViewBookList.class);
        Intent goToWishlistActivity = new Intent(this, ViewWishlist.class);
        if (wishList){
            startActivity(goToWishlistActivity);
        } else {
            startActivity(goToMainActivity);
        }

    }

    public void setIsbnToClipboard(View view){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.setText(selectedBook.getISBN());
        toastMessage("ISBN copied to clipboard!");
    }

    public void bookObtained(View view){
        WishlistDataController wdc = new WishlistDataController();
        BookDataController bdc = new BookDataController();
        wdc.deleteData(selectedBook.getBookID());
        bdc.addData(selectedBook);


        toastMessage("Book added to library!");
        goToPreviousActivity();
        this.finish();
    }

    private void toastMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}
