package com.example.beta;

import java.util.ArrayList;

public class UserInfo {

    public String FirstName;
    public String LastName;
    public String Email;
    public String Password;
    public String Uid;

    public UserInfo(String FirstName, String LastName,String Email, String Password, String Uid){
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Email = Email;
        this.Password = Password;
        this.Uid = Uid;
    }
    public UserInfo(){

    }
}
