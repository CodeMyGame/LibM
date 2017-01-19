package com.treaso.libm;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mvc.imagepicker.ImagePicker;
import com.treaso.libm.StudentPack.Student;
import com.treaso.libm.StudentPack.StudentDatabaseHandler;
import com.treaso.libm.services.FirebaseService;
import com.treaso.libm.BookPack.Book;
import com.treaso.libm.BookPack.DatabaseHandler;
import com.treaso.libm.tabs.tab1;
import com.treaso.libm.tabs.tab2;
import com.treaso.libm.tabs.tab3;

import java.util.ArrayList;
import java.util.List;

import static com.treaso.libm.R.id.imageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_book_name,
            R.drawable.ic_student_name,
            R.drawable.ic_faculty_name
    };
    EditText editTextid;
    EditText editTextname;
    EditText editTextauthor;
    EditText editTextpublisher;
    EditText editTextprice;
    EditText editTextcopy;
    ImageView imageViewSelect;
    DatabaseHandler db;
    Uri dpuri;
    ProgressDialog progressDialog;
    EditText editTextid_insert;
    String dpurl ;
    private StorageReference mStorageRef;
    StudentDatabaseHandler dbs;
    private DatabaseReference mDatabase;
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    public void showProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("uploading dp.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public  Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(),source.getHeight());
        int x = (source.getWidth()-size)/2;
        int y = (source.getHeight()-size)/2;
        Bitmap bitmap = Bitmap.createBitmap(source,x,y,size,size);
        if(bitmap!=source){
            source.recycle();

        }
        Bitmap nbitmap = Bitmap.createBitmap(size,size,source.getConfig());
        Canvas c = new Canvas(nbitmap);
        Paint p = new Paint();
        BitmapShader bs = new BitmapShader(bitmap,BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        p.setShader(bs);
        p.setColor(Color.BLACK);
        p.setShadowLayer(4.0f,0.0f,2.0f, Color.GRAY);
        p.setAntiAlias(true);
        float r = size/2f;
        c.drawCircle(r,r,r,p);
        bitmap.recycle();
        return nbitmap;


    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new tab1(), "ONE");
        adapter.addFragment(new tab2(), "TWO");
        adapter.addFragment(new tab3(), "THREE");
        viewPager.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImagePicker.setMinQuality(600, 600);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        db = new DatabaseHandler(this);
        dbs = new StudentDatabaseHandler(this);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        NavigationView nv = (NavigationView)findViewById(R.id.nav_view);
        View v = nv.getHeaderView(0);
        ImageView iv = (ImageView)v.findViewById(imageView);

        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.sherly);
        Bitmap bmcircle = transform(bm);
        iv.setImageBitmap(bmcircle);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startService(new Intent(this,FirebaseService.class));
        }
        if (id == R.id.action_share) {
            Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                    .setMessage(getString(R.string.invitation_message))
                    .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                    .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                    .setCallToActionText(getString(R.string.invitation_cta))
                    .build();
            startActivityForResult(intent, 4);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
        if (bitmap != null) {
            imageViewSelect.setImageBitmap(bitmap);
            showProgressDialog();
            dpuri = data.getData();
            StorageReference riversRef = mStorageRef.child(editTextid_insert.getText().toString()+"/dp.jpg");

            riversRef.putFile(dpuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            dpurl = downloadUrl.toString();
                            progressDialog.hide();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            progressDialog.hide();
                            Toast.makeText(MainActivity.this, "Select image again", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // TODO do something with the bitmap
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.nav_newbook){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View v = inflater.inflate(R.layout.new_book, null);
            editTextid=(EditText)v.findViewById(R.id.editTextBookId);
            editTextname=(EditText)v.findViewById(R.id.editTextBookName);
            editTextauthor=(EditText)v.findViewById(R.id.editTextAuthor);
            editTextpublisher=(EditText)v.findViewById(R.id.editTextPublisher);
            editTextprice=(EditText)v.findViewById(R.id.editTextPrice);
            editTextcopy=(EditText)v.findViewById(R.id.editTextCopy);
            builder.setView(v)
                    // Add action buttons
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            //textviewTotal=(TextView)findViewById(R.id.textViewTotalbooks);
                            String bidString = editTextid.getText().toString().trim();
                            String bpriceString = editTextprice.getText().toString().trim();
                            String bcopyString = editTextcopy.getText().toString().trim();
                            //  int bid = Integer.parseInt(bidString);
                            String bname = editTextname.getText().toString().trim();
                            String bauthor = editTextauthor.getText().toString().trim();
                            String bpublisher = editTextpublisher.getText().toString().trim();
                            if (!bname.isEmpty() && !bauthor.isEmpty() && !bpublisher.isEmpty() && !bidString.isEmpty()
                                    && !bpriceString.isEmpty() && !bcopyString.isEmpty()) {
                                try {
                                    long rows = db.addBook(new Book(Integer.parseInt(bidString), bname, bauthor, bpublisher, Integer.parseInt(bpriceString), Integer.parseInt(bcopyString)));
                                    if (rows == -1) {
                                        Toast.makeText(MainActivity.this, "BOOK ID already exists!!!", Toast.LENGTH_SHORT).show();
                                    } else {


                                        Toast.makeText(MainActivity.this, "Insert book successfully", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(MainActivity.this, "Please insert all fields...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        if(id==R.id.nav_student_insert){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View v = inflater.inflate(R.layout.new_student, null);
            editTextid_insert=(EditText)v.findViewById(R.id.studentId);
            final EditText editTextname_insert=(EditText)v.findViewById(R.id.studentName);
            final EditText editTextemail_insert=(EditText)v.findViewById(R.id.studentEmail);
            final EditText editTextphoneno_insert=(EditText)v.findViewById(R.id.studentPhoneNo);
            final EditText editTextbranch_insert=(EditText)v.findViewById(R.id.studentBranch);
            final Button button = (Button)v.findViewById(R.id.selectdp);
            imageViewSelect = (ImageView)v.findViewById(R.id.studentdp);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImagePicker.pickImage(MainActivity.this,"Select Student DP");
                }


            });
            builder.setView(v)
                    // Add action buttons
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String sid = editTextid_insert.getText().toString().trim();
                            String sname = editTextname_insert.getText().toString().trim();
                            String semail = editTextemail_insert.getText().toString().trim();
                            String sphone = editTextphoneno_insert.getText().toString().trim();
                            String sbranch = editTextbranch_insert.getText().toString().trim();

                            if (!sid.isEmpty() && !sname.isEmpty() && !semail.isEmpty() && !sphone.isEmpty()
                                    && !sbranch.isEmpty()) {
                                if (!Patterns.EMAIL_ADDRESS.matcher(semail).matches()) {
                                    Toast.makeText(MainActivity.this,"Please enter valid Email ID", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (editTextphoneno_insert.length()<10||editTextphoneno_insert.length()>12) {
                                    Toast.makeText(MainActivity.this,"Please enter valid Phone number", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                try {
                                    mDatabase.child("nituk").child("student").child(new tab2().global_count_student).setValue(new Student(Integer.parseInt(sid), sname, semail, sphone,sbranch,dpurl));
                                    Toast.makeText(MainActivity.this, "successs...", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(MainActivity.this, "Please enter all fields!!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void editButtonClick(View view) {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void dpClick(View view) {

        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,imageView,"mytransition");
        startActivity(intent,activityOptionsCompat.toBundle());

    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}


