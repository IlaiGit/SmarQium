package com.example.beta;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
/**
 * @author		Ilai Shimoni ilaigithub@gmail.com
 * @version	    3.0
 * @since		12/10/22
 * this class provides the service for checking the internet connection of the user
 * and disables options that require internet connection and will cause problems
 */
public class NetworkConnectionReciever extends BroadcastReceiver {

    AlertDialog.Builder adb;

    /**
     * check internet connection and proceed with the according message
     */

    @Override
    public void onReceive(Context context, Intent intent) {

        try{
            if(! isNetworkAvailable(context)) {

                adb = new AlertDialog.Builder(context);

                adb.setTitle("Smarqium");
                adb.setMessage("Please connect to the internet before proceeding");
                adb.setIcon(R.drawable.no_profile);
                adb.setCancelable(false);
                adb.setPositiveButton("check your connection", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isNetworkAvailable(context)){
                            Toast.makeText(context, "Connected to internet", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else {
                            AlertDialog ad = adb.create();
                            ad.show();
                        }
                    }
                });
                AlertDialog ad = adb.create();
                ad.show();
            }

        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    /**
     * the method checks internet connection
     *
     * the method is called on the class and check for the user's connection to the internet
     * @return	boolean value depends on internet connection
     */

    public static boolean isNetworkAvailable(Context context) {
        if(context == null)  return false;


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        return true;
                    }
                }
            }

            else {

                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("update_status", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("update_status", "" + e.getMessage());
                }
            }
        }
        Log.i("update_status","Network is available : FALSE ");


        return false;
    }
}