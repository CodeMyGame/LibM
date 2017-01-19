package com.treaso.libm.StudentPack;

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
public class StudentDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StudentDb";
    private static final String TABLE_STUDENT = "student";
    private static final String KEY_ID = "sid";
    private static final String KEY_NAME = "sname";
    private static final String KEY_EMAIL= "semail";
    private static final String KEY_PHONE = "sphone";
    private static final String KEY_BRANCH= "sbranch";
    private static final String KEY_DP= "sdp";
    private SQLiteDatabase sqLiteDatabase;
    public StudentDatabaseHandler(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_STUDENT+" ("
                +KEY_ID+" INTEGER PRIMARY KEY, "
                +KEY_NAME+" TEXT NOT NULL, "
                +KEY_EMAIL+" TEXT NOT NULL, "
                +KEY_DP+" TEXT NOT NULL, "
                +KEY_PHONE+" TEXT NOT NULL, "
                +KEY_BRANCH+" TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int olderversion, int newversion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+TABLE_STUDENT);
        onCreate(sqLiteDatabase);
    }
    public long addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, student.getID());
        values.put(KEY_NAME, student.getName());
        values.put(KEY_EMAIL, student.getEmail());
        values.put(KEY_DP, student.getDpurl());
        values.put(KEY_PHONE,student.getPhone());
        values.put(KEY_BRANCH, student.getBranch());
        long rows = db.insert(TABLE_STUDENT, null, values);
        db.close(); // Closing database connection
        return rows;
    }

    Student getStudent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Student s =null;
        Cursor cursor = db.query(TABLE_STUDENT, new String[] {KEY_ID,KEY_NAME,KEY_EMAIL,KEY_PHONE,KEY_BRANCH}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null&&cursor.getCount()>0) {
            cursor.moveToFirst();
            s = new Student(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
            // return contact
            return s;
        }else{
            return s;
        }

    }

    // Getting All Books
    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STUDENT+" ORDER BY "+KEY_ID+" ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Student s = new Student();
                s.setID(cursor.getInt(0));
                s.setName(cursor.getString(1));
                s.setEmail(cursor.getString(2));
                s.setDpurl(cursor.getString(3));
                s.setPhone(cursor.getString(4));
                s.setBranch(cursor.getString(5));


                // Adding book to list
                studentList.add(s);
            } while (cursor.moveToNext());
        }

        // return contact list
        return studentList;
    }

    // Updating single book
    public int updateBook(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, student.getName());
        values.put(KEY_EMAIL, student.getEmail());
        values.put(KEY_DP, student.getDpurl());
        values.put(KEY_PHONE, student.getPhone());
        values.put(KEY_BRANCH, student.getBranch());

        // updating row
        return db.update(TABLE_STUDENT, values, KEY_ID + " = ?",
                new String[] { String.valueOf(student.getID()) });
    }

    // Deleting single book
    public int deleteBook(Student s) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_STUDENT, KEY_ID + " = ?",
                new String[] { String.valueOf(s.getID()) });
        db.close();
        return rows;
    }


    // Getting book Count
    public int getBookCount() {
        String countQuery = "SELECT  * FROM " + TABLE_STUDENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();
    }
}
