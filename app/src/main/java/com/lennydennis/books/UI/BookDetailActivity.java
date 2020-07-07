package com.lennydennis.books.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lennydennis.books.Models.Book;
import com.lennydennis.books.R;

public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Book book = getIntent().getParcelableExtra("Book");
    }
}