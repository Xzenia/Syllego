package test.omegaware.syllego;

public class User {
    private String name;
    private String department;
    private String userId;
    private int numberOfBooksBorrowed;
    private int numberOfBooksReturned;
    private String latestBookBorrowed;
    private String latestBookReturned;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfBooksBorrowed() {
        return numberOfBooksBorrowed;
    }

    public void setNumberOfBooksBorrowed(int numberOfBooksBorrowed) {
        this.numberOfBooksBorrowed = numberOfBooksBorrowed;
    }

    public int getNumberOfBooksReturned() {
        return numberOfBooksReturned;
    }

    public void setNumberOfBooksReturned(int numberOfBooksReturned) {
        this.numberOfBooksReturned = numberOfBooksReturned;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getLatestBookBorrowed() {
        return latestBookBorrowed;
    }

    public void setLatestBookBorrowed(String latestBookBorrowed) {
        this.latestBookBorrowed = latestBookBorrowed;
    }

    public String getLatestBookReturned() {
        return latestBookReturned;
    }

    public void setLatestBookReturned(String latestBookReturned) {
        this.latestBookReturned = latestBookReturned;
    }
}
