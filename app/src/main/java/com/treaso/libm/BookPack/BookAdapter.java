package com.treaso.libm.BookPack;

/**
 * Created by Kapil Malviya on 6/22/2016.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.treaso.libm.R;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private List<Book> bookList;
    Context context;
    private ClickListener clickListener;
    public interface ClickListener {
        void itemClicked(View v, int position);
    }
    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView bname,bauthor,bid,bpublisher,bprice,bcopy;
        public ImageView image;
        public MyViewHolder(View view) {
            super(view);
            bname = (TextView) view.findViewById(R.id.textViewbookName);
            bauthor = (TextView) view.findViewById(R.id.textViewBookAuthor);
            bid = (TextView) view.findViewById(R.id.textViewBookId);
            bpublisher = (TextView) view.findViewById(R.id.textViewPublisher);
            bprice = (TextView) view.findViewById(R.id.textViewBookPrice);
            bcopy = (TextView) view.findViewById(R.id.textViewBookCopy);
            image = (ImageView) view.findViewById(R.id.imageView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener!=null){
                clickListener.itemClicked(v,getPosition());
            }
        }
    }


    public BookAdapter(List<Book> moviesList,Context c) {
        this.bookList = moviesList;
        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.bid.setText("ID: "+book.getID());
        holder.bname.setText(book.getName());
        holder.bauthor.setText("Author: "+book.getAuthor());
        holder.bpublisher.setText(book.getPublisher());
        holder.bprice.setText("Price: "+book.getPrice());
        holder.bcopy.setText("Copy: "+book.getCopy());

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public void setFilter(List<Book> countryModels) {
        bookList = new ArrayList<>();
        bookList.addAll(countryModels);
        notifyDataSetChanged();
    }

}
