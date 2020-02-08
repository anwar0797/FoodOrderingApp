package uk.ac.mmu.foodorderingapp.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import uk.ac.mmu.foodorderingapp.Model.User;

public class Common {
    public static User currentUser;

    public static final String DELETE = "Delete";
    public static final String USER_KEY = "Username";
    public static final String PWD_KEY = "Password";

    public static final String INTENT_FOOD_ID = "FoodId";


    public static boolean isConnectedToInternet (Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info != null)
            {
                for(int i=0;i<info.length;i++)
                {
                    if(info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
