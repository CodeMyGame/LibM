package com.treaso.libm.tabs;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.treaso.libm.R;

/**
 * Created by Kapil Gehlot on 09-01-2017.
 */

public class EmptyRecyclerViewAdapter extends RecyclerView.Adapter<EmptyRecyclerViewAdapter.ViewHolder> {

    public Drawable drawable_global;

    public EmptyRecyclerViewAdapter(){}

    public EmptyRecyclerViewAdapter(Drawable drawable){
        drawable_global = drawable;
    }

    @Override
    public EmptyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emptyview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.imageView.setImageDrawable(drawable_global);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmptyRecyclerViewAdapter.ViewHolder holder, int position) {}

    @Override
    public int getItemCount() {
        return 1;//must return one otherwise none item is shown
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageView = (ImageView)view.findViewById(R.id.nobooks);
        }
    }
}

