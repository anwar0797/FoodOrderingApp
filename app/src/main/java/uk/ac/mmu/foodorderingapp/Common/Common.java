package uk.ac.mmu.foodorderingapp.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.util.Calendar;
import java.util.Locale;

import uk.ac.mmu.foodorderingapp.Model.Request;
import uk.ac.mmu.foodorderingapp.Model.User;

public class Common {
    public static User currentUser;
    public static Request currentRequest;

    public static final String DELETE = "Delete";
    public static final String USER_KEY = "Username";
    public static final String PWD_KEY = "Password";

    public static final String INTENT_FOOD_ID = "FoodId";

    public static final String UPDATE = "Update";

    public static final int PICK_IMAGE__REQUEST = 71;

    public static String convertCodeToStatus(String code) {
        if (code.equals("0"))
            return "Placed";
        else if (code.equals("1"))
            return "On my way";
        else
            return "Shipped";
    }


    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public static String getDate(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date = new StringBuilder(
                android.text.format.DateFormat.format("dd-MM-yyyy HH:mm"
                        , calendar)
                        .toString());
        return date.toString();
    }

    public static String restaurantSelected = "";
}
