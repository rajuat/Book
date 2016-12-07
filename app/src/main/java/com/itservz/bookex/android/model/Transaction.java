package com.itservz.bookex.android.model;

/**
 * Created by Raju on 12/7/2016.
 */

public class Transaction {
    public Book book;
    public User seller;
    public User buyer;

    @Override
    public String toString() {
        return "Transaction{" +
                "book=" + book +
                ", seller=" + seller +
                ", buyer=" + buyer +
                '}';
    }
}
