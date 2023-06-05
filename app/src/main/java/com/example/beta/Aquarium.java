package com.example.beta;

/**
 * @author		Ilai Shimoni ilaigithub@gmail.com
 * @version	    3.0
 * @since		12/10/22
 *  this class creates an aquarium object with its required parameters
 */
public class Aquarium {

    /**
     * creating the object's attributes
     */

    public String NickName;
    public String FishAmount;
    public String AQwidth;
    public String AQheight;
    public String AQdepth;

    public String MAC;

    public Aquarium(){
    }

    public Aquarium(String NickName, String FishAmount, String AQwidth, String AQheight, String AQdepth, String MAC){
        this.NickName = NickName;
        this.FishAmount = FishAmount;
        this.AQwidth = AQwidth;
        this.AQheight = AQheight;
        this.AQdepth = AQdepth;
        this.MAC = MAC;
    }

}
