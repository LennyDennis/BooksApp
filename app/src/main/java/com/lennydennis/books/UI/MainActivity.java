package com.lennydennis.books.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.lennydennis.books.Adapters.BooksAdapter;
import com.lennydennis.books.Utils.ApiUtil;
import com.lennydennis.books.Utils.SharedPreferenceUtil;
import com.lennydennis.books.models.Book;
import com.lennydennis.books.R;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ProgressBar mLoadingProgress;
    private RecyclerView mRecyclerView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingProgress = findViewById(R.id.pb_loading);
        mRecyclerView = findViewById(R.id.books_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent searchIntent = getIntent();
        String query = searchIntent.getStringExtra("query");
        URL bookUrl;
        try {
            if(query == null||query.isEmpty()){
                bookUrl = ApiUtil.buildUrl("cooking");
            }else{
                bookUrl = new URL(query);
            }
            new BooksQueryTask().execute(bookUrl);
        }
        catch (Exception e){
            Log.d("error", e.getMessage());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        ArrayList<String> sharedPreferenceList = SharedPreferenceUtil.getQueryList(getApplicationContext());
        int listLength = sharedPreferenceList.size();
        MenuItem recentMenu;
        for(int i=0;i<listLength;i++){
            recentMenu = menu.add(Menu.NONE,i,Menu.NONE,sharedPreferenceList.get(i));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.advanced_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                int position = item.getItemId()+1;
                String preferenceName = SharedPreferenceUtil.QUERY+String.valueOf(position);
                String query = SharedPreferenceUtil.getPreferenceString(getApplicationContext(),preferenceName);
                String[] prefParams = query.split("\\,");
                String[] queryParams = new String[4];
                for(int i=0;i<prefParams.length;i++){
                    queryParams[i] = prefParams[i];
                }
                URL bookUrl = ApiUtil.buildUrl(
                        (queryParams[0]==null)?"":queryParams[0],
                        (queryParams[1]==null)?"":queryParams[1],
                        (queryParams[2]==null)?"":queryParams[2],
                        (queryParams[3]==null)?"":queryParams[3]
                        );
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            URL bookUrl = ApiUtil.buildUrl(query);
            new BooksQueryTask().execute(bookUrl);
        }catch (Exception e){
            Log.d("Error",e.getMessage());
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String result = null;
            try{
                result = ApiUtil.getJson(searchUrl);
            }
            catch (Exception e){
                Log.e("Error", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView tvError = findViewById(R.id.tv_error);
            mLoadingProgress.setVisibility(View.INVISIBLE);
            if(result == null){
                mRecyclerView.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);
            }else{
                mRecyclerView.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.INVISIBLE);

                ArrayList<Book> books = ApiUtil.getBooksFromJson(result);
                BooksAdapter booksAdapter = new BooksAdapter(books, mContext);
                mRecyclerView.setAdapter(booksAdapter);
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}