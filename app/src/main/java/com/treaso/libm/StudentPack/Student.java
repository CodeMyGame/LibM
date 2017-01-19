package com.treaso.libm.StudentPack;

/**
 * Created by Kapil Malviya on 6/24/2016.
 */
public class Student {
    int id;
    String name;
    String email;
    String phone;
    String branch;
    String dpurl;

    // Empty constructor
    public Student(){

    }
    public Student(int id){
        this.id = id;
    }
    // constructor
    public Student(int id, String name, String email,String phone,String branch,String dpurl){

        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.branch = branch;
        this.dpurl = dpurl;
    }

    // getting ID
    public int getID(){
        return this.id;
    }

    // setting id
    public void setID(int id){
        this.id = id;
    }

    // getting name
    public String getName(){
        return this.name;
    }

    // setting name
    public void setName(String name){
        this.name = name;
    }

    // getting phone number
    public String getEmail(){
        return this.email;
    }

    // setting phone number
    public void setEmail(String email){
        this.email = email;
    }
    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getBranch(){
        return this.branch;
    }
    public void setBranch(String branch){
        this.branch = branch;
    }
    public String getDpurl(){
        return this.dpurl;
    }
    public void setDpurl(String dpurl){
        this.dpurl = dpurl;
    }
}
