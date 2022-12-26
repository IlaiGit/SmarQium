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
}
