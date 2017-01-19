package com.treaso.libm.BookPack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kapil Malviya on 6/23/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "library";
    private static final String TABLE_BOOK = "bookinfo";
    private static final String KEY_ID = "bid";
    private static final String KEY_NAME = "bname";
    private static final String KEY_AUTHOR = "bauthor";
    private static final String KEY_PUBLISHER = "bpublisher";
    private static final String KEY_PRICE = "bprice";
    private static final String KEY_COPY = "bcopy";
    private SQLiteDatabase sqLiteDatabase;
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_BOOK + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT NOT NULL, "
                + KEY_AUTHOR + " TEXT NOT NULL, "
                + KEY_PUBLISHER + " TEXT NOT NULL, "
                + KEY_PRICE + " INTEGER NOT NULL, "
                + KEY_COPY + " INTEGER NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int olderversion, int newversion) {
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+TABLE_BOOK);
        onCreate(sqLiteDatabase);
    }
    public long addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,book.getID());
        values.put(KEY_NAME,book.getName());
        values.put(KEY_AUTHOR,book.getAuthor());
        values.put(KEY_PUBLISHER, book.getPublisher());
        values.put(KEY_PRICE, book.getPrice());
        values.put(KEY_COPY, book.getCopy());
        long rows = db.insert(TABLE_BOOK, null, values);
        db.close(); // Closing database connection
        return rows;
    }

    // Getting single book
    Book getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Book book =null;
        Cursor cursor = db.query(TABLE_BOOK, new String[]{KEY_ID, KEY_NAME, KEY_AUTHOR, KEY_PUBLISHER, KEY_PRICE, KEY_COPY}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null&&cursor.getCount()>0) {
            cursor.moveToFirst();
            book = new Book(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5));
            // return contact
            return book;
        }else{
            return book;
        }

    }

    // Getting All Books
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<Book>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BOOK+" ORDER BY "+KEY_ID+" ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setID(cursor.getInt(0));
                book.setName(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
                book.setPublisher(cursor.getString(3));
                book.setPrice(cursor.getInt(4));
                book.setCopy(cursor.getInt(5));

                // Adding book to list
                bookList.add(book);
            } while (cursor.moveToNext());
        }

        // return contact list
        return bookList;
    }

    // Updating single book
    public int updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, book.getName());
        values.put(KEY_AUTHOR, book.getAuthor());
        values.put(KEY_PUBLISHER, book.getPublisher());
        values.put(KEY_PRICE, book.getPrice());
        values.put(KEY_COPY,book.getCopy());
        // updating row
        return db.update(TABLE_BOOK, values, KEY_ID + " = ?",
                new String[] { String.valueOf(book.getID()) });
    }
    public int updateBookCopy(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COPY,book.getCopy());
        // updating row
        return db.update(TABLE_BOOK, values, KEY_ID + " = ?",
                new String[] { String.valueOf(book.getID()) });
    }

    // Deleting single book
    public int deleteBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_BOOK, KEY_ID + " = ?",
                new String[] { String.valueOf(book.getID()) });
        db.close();
        return rows;
    }


    // Getting book Count
    public int getBookCount() {
        String countQuery = "SELECT  * FROM " + TABLE_BOOK;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();
    }
}
