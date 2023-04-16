package com.example.beta;

import java.util.ArrayList;

public class UserInfo {

    public String FirstName;
    public String LastName;
    public ArrayList<String> arr;
    public String Uid;

    public UserInfo(){
    }

    public UserInfo(String FirstName, String LastName, ArrayList<String> arr, String Uid){
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.arr = arr;
        this.Uid = Uid;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setArr(ArrayList<String> arr) {
        this.arr = arr;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public ArrayList<String> getArr() {
        return arr;
    }

    public String getUid() {
        return Uid;
    }
}
