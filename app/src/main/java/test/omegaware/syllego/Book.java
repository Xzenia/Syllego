package test.omegaware.syllego;

import java.io.Serializable;

public class Book implements Serializable{

    private int BookID;
    private String BookName;
    private String BookAuthor;
    private String YearReleased;
    private String ISBN;
    private String Status;

    public int getBookID() {
        return BookID;
    }

    public void setBookID(int bookID) {
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


}
