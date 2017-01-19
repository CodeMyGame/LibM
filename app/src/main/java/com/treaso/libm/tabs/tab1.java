package com.treaso.libm.tabs;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.treaso.libm.BookPack.Book;
import com.treaso.libm.BookPack.BookAdapter;
import com.treaso.libm.BookPack.DatabaseHandler;
import com.treaso.libm.R;



public class tab1 extends Fragment implements BookAdapter.ClickListener, SearchView.OnQueryTextListener{

    private List<Book> bookList;
    private RecyclerView recyclerView;
    private BookAdapter mAdapter;
    private EmptyRecyclerViewAdapter eAdapter;
    DatabaseHandler db;
    Menu menu_toolbar;
    SwipeRefreshLayout srl;
    private AdView mAdView;
    private void prepareBookData() {
        bookList.clear();
        List<Book> book = db.getAllBooks();
        for (Book s : book) {
            Book books = new Book(s.getID(),
                    s.getName(), s.getAuthor(), s.getPublisher(), s.getPrice(), s.getCopy());
            bookList.add(books);
        }

        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tab1menu, menu);
        menu_toolbar = menu;
       // menu.clear();
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
                        mAdapter.setFilter(bookList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
// Do something when expanded
                        return true; // Return true to expand action view
                    }
                });

    }
    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Book> filteredModelList = filter(bookList, newText);
        mAdapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<Book> filter(List<Book> models, String query) {
        query = query.toLowerCase();
        final List<Book> filteredModelList = new ArrayList<>();
        for (Book model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_tab1,container,false);
        mAdView = (AdView)v.findViewById(R.id.adView);
        bookList = new ArrayList<>();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        db = new DatabaseHandler(getActivity().getApplicationContext());
        srl = (SwipeRefreshLayout)v.findViewById(R.id.swipetab1);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareBookData();
                srl.setRefreshing(false);
            }
        });
        mAdapter = new BookAdapter(bookList,getActivity().getApplicationContext());
        mAdapter.setClickListener(this);
        eAdapter = new EmptyRecyclerViewAdapter(getResources().getDrawable(R.drawable.nodata));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        prepareBookData();
        if (bookList == null || bookList.size() == 0){
            recyclerView.setAdapter(eAdapter);
        }else {
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();//here I call notifyDataSetChanged() with new list
        }



        return v;
    }
    @Override
    public void itemClicked(View v, int position) {
        try {
            Intent intent = new Intent(getActivity().getApplicationContext(),tab1NextActivity.class);
            ImageView imageView = (ImageView)v.findViewById(R.id.imageView);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),imageView,"mytransitionbook");
            ArrayList<String> information = new ArrayList<>();
            Book book = bookList.get(position);
            information.add(0,book.getName());
            information.add(1,book.getAuthor());
            information.add(2,book.getPublisher());
            information.add(3,""+book.getCopy());
            information.add(4,""+book.getID());
            information.add(5,""+book.getPrice());
            intent.putStringArrayListExtra("bookinformation",information);
            startActivity(intent,activityOptionsCompat.toBundle());

        }catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}