package com.example.guls.phonecontact;


/**
 * Created by GÜLSEREN on 26.3.2016.
 */


public class Person {

    private String name;
    private String numbers;
    private  String numbers2;
    private int pictureResourceID;
    public Person(){}



    public Person(String name,String numbers,int pictureResourceID){
        this.name = name;
        this.numbers = numbers;
        this.pictureResourceID = pictureResourceID;

    }

    public String getNumbers() {
        return numbers;
    }
    public String getName() {
        return name;
    }
    public int getPictureResourceID() {
        return pictureResourceID;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setNumbers(String numbers) { this.numbers = numbers;    }
    public void setPictureResourceID(int pictureResourceID) { this.pictureResourceID = pictureResourceID; }


}

