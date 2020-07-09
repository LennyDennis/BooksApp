package com.lennydennis.books.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.lennydennis.books.models.Book;
import com.lennydennis.books.R;
import com.lennydennis.books.databinding.ActivityBookDetailBinding;

public class BookDetailActivity extends AppCompatActivity {

    ActivityBookDetailBinding mBookDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Book book = getIntent().getParcelableExtra("Book");
        mBookDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_book_detail);
        mBookDetailBinding.setBook(book);

    }
}