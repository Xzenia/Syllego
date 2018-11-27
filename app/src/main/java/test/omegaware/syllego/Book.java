package test.omegaware.syllego;


import java.io.Serializable;

public class Book implements Serializable {

    private String UserID = "";
    private String BookID = "";
    private String BookName = "";
    private String BookAuthor = "";
    private String YearReleased = "";
    private String ISBN = "";
    private int NumberOfCopies = 0;

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

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public int getNumberOfCopies() {
        return NumberOfCopies;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        NumberOfCopies = numberOfCopies;
    }
}
