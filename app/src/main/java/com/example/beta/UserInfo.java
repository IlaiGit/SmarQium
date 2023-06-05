package com.example.beta;


/**
 * @author		Ilai Shimoni ilaigithub@gmail.com
 * @version	    3.0
 * @since		12/10/22
 * this class creates a user object which consists of His Name, Email, Password and is uploaded to the database upon verification
 */

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
