package test.omegaware.syllego;


import java.io.Serializable;

public class Book implements Serializable {

    private String Username = "";
    private String BookID = "";
    private String BookName = "";
    private String BookAuthor = "";
    private String YearReleased = "";
    private String ISBN = "";
    private int NumberOfCopies = 0;
    private String DateAdded;
    private String FilterDateAdded;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getBookID() {
        return BookID;
    }

    public void setBookID(String bookID) {
        BookID = bookID;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public String getBookAuthor() {
        return BookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        BookAuthor = bookAuthor;
    }

    public String getYearReleased() {
        return YearReleased;
    }

    public void setYearReleased(String yearReleased) {
        YearReleased = yearReleased;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getNumberOfCopies() {
        return NumberOfCopies;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        NumberOfCopies = numberOfCopies;
    }

    public String getDateAdded() {
        return DateAdded;
    }

    public void setDateAdded(String dateAdded) {
        DateAdded = dateAdded;
    }

    public String getFilterDateAdded() {
        return FilterDateAdded;
    }

    public void setFilterDateAdded(String filterDateAdded) {
        FilterDateAdded = filterDateAdded;
    }
}
