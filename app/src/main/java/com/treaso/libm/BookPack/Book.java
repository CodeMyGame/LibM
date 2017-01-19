package com.treaso.libm.BookPack;

/**
 * Created by Kapil Malviya on 6/24/2016.
 */
public class Book {
    int id;
    String name;
    String author;
    String publisher;
    int price;
    int copy;
    int imageid;

    // Empty constructor
    public Book(){

    }
    public Book(int bcpy,int bid){
        this.copy = bcpy;
        this.id = bid;
    }
    public Book(int id){
        this.id = id;
    }
    // constructor
    public Book(int id, String name, String author,String publisher,int price,int copy){

        this.id = id;
        this.name = name;
        this.author = author;
        this.copy = copy;
        this.price = price;
        this.publisher = publisher;
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
    public String getAuthor(){
        return this.author;
    }

    // setting phone number
    public void setAuthor(String author){
        this.author = author;
    }
    public String getPublisher(){
        return this.publisher;
    }

    // setting phone number
    public void setPublisher(String publisher){
        this.publisher = publisher;
    }
    public int getPrice(){
        return this.price;
    }

    // setting phone number
    public void setPrice(int price){
        this.price = price;
    }
    public int getCopy(){
        return this.copy;
    }

    // setting phone number
    public void setCopy(int copy){
        this.copy = copy;
    }
}
