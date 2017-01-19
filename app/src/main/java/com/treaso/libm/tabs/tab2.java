package com.treaso.libm.tabs;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.treaso.libm.R;
import com.treaso.libm.StudentPack.Student;
import com.treaso.libm.StudentPack.StudentAdapter;
import com.treaso.libm.StudentPack.StudentDatabaseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class tab2 extends Fragment implements SearchView.OnQueryTextListener,StudentAdapter.ClickListener,StudentAdapter.LongClickListener {
    private RecyclerView recyclerView;
    private List<Student> studentsList = new ArrayList<>();
    public StudentAdapter mAdapter;
    public NoInternetAdapter nAdapter;
    SwipeRefreshLayout srl;
    Menu menu_toolbar;
    public static String global_count_student;
    ProgressBar progressBar;
    private EmptyRecyclerViewAdapter eAdapter;
    public tab2() {
        // Required empty public constructor
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
                        mAdapter.setFilter(studentsList);
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
        final List<Student> filteredModelList = filter(studentsList, newText);
        mAdapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<Student> filter(List<Student> models, String query) {
        query = query.toLowerCase();
        final List<Student> filteredModelList = new ArrayList<>();
        for (Student model : models) {
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
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v  =  inflater.inflate(R.layout.fragment_tab2, container, false);
        progressBar = (ProgressBar)v.findViewById(R.id.progressbar);
        srl = (SwipeRefreshLayout)v.findViewById(R.id.swipetab2);
        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("nituk").child("student").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentsList.clear();
                if (dataSnapshot.exists()) {
                    ArrayList hashMap = (ArrayList) dataSnapshot.getValue();
                    int size = hashMap.size();
                    global_count_student = size+"";
                    for (int i = 0; i < size; i++) {
                        HashMap hashMap1 = (HashMap) hashMap.get(i);
                        Student stu = new Student(Integer.parseInt(hashMap1.get("id").toString()),
                                hashMap1.get("name").toString(),
                                hashMap1.get("email").toString(),
                                hashMap1.get("phone").toString(),
                                hashMap1.get("branch").toString(),
                                hashMap1.get("dpurl").toString());
                        studentsList.add(stu);

                    }


                }
                if (studentsList == null || studentsList.size() == 0){
                    recyclerView.setAdapter(eAdapter);
                }
                else {
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mAdapter = new StudentAdapter(studentsList,getActivity().getApplicationContext());
        mAdapter.setClickListener(this);
        mAdapter.setLongClickListener(this);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        eAdapter = new EmptyRecyclerViewAdapter(getResources().getDrawable(R.drawable.nodata));
        nAdapter = new NoInternetAdapter(getResources().getDrawable(R.drawable.sherly));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        return v;
    }

    @Override
    public void itemClicked(View v, int position) {
        Toast.makeText(getActivity(), "item clicked...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void longitemClicked(View v, int position) {
        Toast.makeText(getActivity(), "long item clicked...", Toast.LENGTH_SHORT).show();
    }
}