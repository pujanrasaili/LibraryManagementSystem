package com.library.model;

public class Fine {
    private int fineId;
    private int transactionId;
    private String memberName;
    private String bookTitle;
    private double amount;
    private String paid;

    public Fine() {}

    public Fine(int fineId, int transactionId, String memberName,
                String bookTitle, double amount, String paid) {
        this.fineId = fineId;
        this.transactionId = transactionId;
        this.memberName = memberName;
        this.bookTitle = bookTitle;
        this.amount = amount;
        this.paid = paid;
    }

    public int getFineId() { return fineId; }
    public void setFineId(int fineId) { this.fineId = fineId; }
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getPaid() { return paid; }
    public void setPaid(String paid) { this.paid = paid; }
}