package com.treaso.libm.StudentPack;

/**
 * Created by Kapil Malviya on 6/22/2016.
 */
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.treaso.libm.BookPack.Book;
import com.treaso.libm.BookPack.BookAdapter;
import com.treaso.libm.PicasoClient;
import com.treaso.libm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    private List<Student> studentList;
    Context context;

    private StudentAdapter.ClickListener clickListener;
    private StudentAdapter.LongClickListener longClickListener;
    public interface ClickListener {
        void itemClicked(View v, int position);
    }
    public void setClickListener(StudentAdapter.ClickListener clickListener){
        this.clickListener = clickListener;
    }
    public interface LongClickListener {
        void longitemClicked(View v, int position);
    }
    public void setLongClickListener(StudentAdapter.ClickListener clickListener){
        this.clickListener = clickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        public TextView sname,sid,semail,sphone,sbranch;
        public TextView image;
        public ImageView imageViewdp;
        public MyViewHolder(View view) {
            super(view);
            sname = (TextView) view.findViewById(R.id.textViewStudentName);
            sid = (TextView) view.findViewById(R.id.textViewStudentId);
            semail= (TextView) view.findViewById(R.id.textViewStudentEmail);
            sphone = (TextView) view.findViewById(R.id.textViewStudentPhone);
            sbranch = (TextView) view.findViewById(R.id.textViewStudentBranch);
            imageViewdp = (ImageView)view.findViewById(R.id.imageView);
           // image = (TextView)view.findViewById(R.id.imageView);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (clickListener!=null){
                clickListener.itemClicked(v,getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            longClickListener.longitemClicked(v,getPosition());
            return false;
        }
    }


    public StudentAdapter(List<Student> moviesList, Context c) {
        this.studentList = moviesList;
        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.sid.setText("ID: " + student.getID());
        holder.sname.setText(student.getName());
        holder.semail.setText(student.getEmail());
        holder.sphone.setText(student.getPhone());
        holder.sbranch.setText(student.getBranch());
        PicasoClient.downLoadImg(context,student.getDpurl(),holder.imageViewdp);

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
    public void setFilter(List<Student> countryModels) {
        studentList = new ArrayList<>();
        studentList.addAll(countryModels);
        notifyDataSetChanged();
    }

}
