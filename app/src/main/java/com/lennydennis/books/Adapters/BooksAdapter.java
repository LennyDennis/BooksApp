package com.lennydennis.books.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lennydennis.books.Models.Book;
import com.lennydennis.books.R;
import com.lennydennis.books.UI.BookDetailActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

    private ArrayList<Book> mBooks;
    private Context mContext;

    public BooksAdapter(ArrayList<Book> books, Context context) {
        mBooks = books;
        mContext = context;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item,parent,false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = mBooks.get(position);
        String authors = "";
        int i=0;
        for (String author:book.authors){
            authors+=author;
            i++;
            if(i<book.authors.length){
                authors += ", ";
            }
        }

        holder.title.setText(book.title);
        holder.authors.setText(authors);
        holder.publisher.setText(book.publisher);
        holder.date.setText(book.publishedDate);
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView authors;
        TextView date;
        TextView publisher;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            authors = itemView.findViewById(R.id.tv_authors);
            date = itemView.findViewById(R.id.published_date);
            publisher = itemView.findViewById(R.id.tv_publisher);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Book bookClicked = mBooks.get(position);
                    Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
                    intent.putExtra("Book",bookClicked);
                    v.getContext().startActivity(intent);
                }
            });
        }


    }
}
