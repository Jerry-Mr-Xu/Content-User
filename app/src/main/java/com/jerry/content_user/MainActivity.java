package com.jerry.content_user;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String bookProviderStr = "content://com.jerry.content_provider.authority/book";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGetBook = (Button) findViewById(R.id.btn_get_book);
        Button btnAddBook = (Button) findViewById(R.id.btn_add_book);
        Button btnDelBook = (Button) findViewById(R.id.btn_del_book);
        Button btnUpdBook = (Button) findViewById(R.id.btn_upd_book);

        btnGetBook.setOnClickListener(this);
        btnAddBook.setOnClickListener(this);
        btnDelBook.setOnClickListener(this);
        btnUpdBook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_book:
                getBook();
                break;
            case R.id.btn_add_book:
                addBook();
                break;
            case R.id.btn_del_book:
                delBook();
                break;
            case R.id.btn_upd_book:
                updBook();
                break;
            default:
                break;
        }
    }

    private void updBook() {
        Uri bookUri = Uri.parse(bookProviderStr);
        if (bookUri == null) {
            return;
        }

        Cursor bookCursor = getContentResolver().query(bookUri, null, null, null, "id ASC");
        if (bookCursor == null) {
            return;
        }
        if (!bookCursor.moveToFirst()) {
            Log.e(TAG, "updBook: No book");
            return;
        }
        int updBookId = -1;
        if (bookCursor.moveToNext()) {
            updBookId = bookCursor.getInt(0);
        }
        bookCursor.close();
        if (updBookId < 0) {
            Log.e(TAG, "updBook: Only one book");
            return;
        }

        ContentValues bookValues = new ContentValues();
        bookValues.put("name", "被修改的第" + (updBookId + 1) + "本书");
        bookValues.put("price", 99.9f);
        int updNum = getContentResolver().update(bookUri, bookValues, "id=?", new String[]{updBookId + ""});
        Log.i(TAG, "updBook: updNum = " + updNum);
    }

    private void delBook() {
        Uri bookUri = Uri.parse(bookProviderStr);
        if (bookUri == null) {
            return;
        }

        Cursor bookCursor = getContentResolver().query(bookUri, null, null, null, "id ASC");
        if (bookCursor == null) {
            return;
        }
        if (!bookCursor.moveToFirst()) {
            Log.e(TAG, "delBook: No book");
            return;
        }
        int delBookId = bookCursor.getInt(0);
        bookCursor.close();

        int delNum = getContentResolver().delete(bookUri, "id=?", new String[]{delBookId + ""});
        Log.i(TAG, "delBook: delNum = " + delNum);
    }

    private void addBook() {
        Uri bookUri = Uri.parse(bookProviderStr);
        if (bookUri == null) {
            return;
        }

        Cursor bookCursor = getContentResolver().query(bookUri, null, null, null, "id DESC");
        if (bookCursor == null) {
            return;
        }
        int bookNo = -1;
        if (bookCursor.moveToFirst()) {
            bookNo = bookCursor.getInt(0);
        }
        bookCursor.close();

        ContentValues bookValues = new ContentValues();
        bookValues.put("id", bookNo + 1);
        bookValues.put("name", "第" + (bookNo + 2) + "本书");
        bookValues.put("price", 20.8f);
        getContentResolver().insert(bookUri, bookValues);
    }

    private void getBook() {
        Uri bookUri = Uri.parse(bookProviderStr);
        if (bookUri == null) {
            return;
        }

        Cursor bookCursor = getContentResolver().query(bookUri, null, null, null, null);
        if (bookCursor == null) {
            return;
        }

        int index = 0;
        while (bookCursor.moveToNext()) {
            int bookId = bookCursor.getInt(0);
            String bookName = bookCursor.getString(1);
            float bookPrice = bookCursor.getFloat(2);
            Log.i(TAG, "getBook: book[" + index++ + "] = {id: " + bookId + ", name: " + bookName + ", price: " + bookPrice + "}");
        }
        bookCursor.close();
    }
}
