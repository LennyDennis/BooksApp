package com.lennydennis.books.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lennydennis.books.R;
import com.lennydennis.books.Utils.ApiUtil;
import com.lennydennis.books.Utils.SharedPreferenceUtil;

import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText searchTitle = findViewById(R.id.search_title);
        final EditText searchAuthor = findViewById(R.id.search_author);
        final EditText searchPublisher = findViewById(R.id.search_publisher);
        final EditText searchIsbn = findViewById(R.id.search_isbn);
        final Button searchButton = findViewById(R.id.button_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = searchTitle.getText().toString().trim();
                String authorText = searchAuthor.getText().toString().trim();
                String publisherText = searchPublisher.getText().toString().trim();
                String isbnText = searchIsbn.getText().toString().trim();

                if(titleText.isEmpty()&&authorText.isEmpty()&&publisherText.isEmpty()&&isbnText.isEmpty()){
                    String validMessage = getString(R.string.search_validity);
                    Toast.makeText(getApplicationContext(), validMessage, Toast.LENGTH_SHORT).show();

                }else{
                    URL queryUrl = ApiUtil.buildUrl(titleText,authorText,publisherText,isbnText);

                    Context context = getApplicationContext();
                    int position = SharedPreferenceUtil.getPreferenceInt(context,SharedPreferenceUtil.POSITION);
                    if(position == 0||position == 5){
                        position = 1;
                    }else{
                        position++;
                    }
                    String key = SharedPreferenceUtil.QUERY + String.valueOf(position);
                    String value = titleText + "," + authorText + "," + publisherText +  "," + isbnText;
                    SharedPreferenceUtil.setPreferenceString(context,key,value);
                    SharedPreferenceUtil.setPreferenceInt(context,SharedPreferenceUtil.POSITION,position);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("query", queryUrl.toString());
                    startActivity(intent);
                }

            }
        });
    }
}