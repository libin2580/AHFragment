package alhamdi.com.cybraum.meridianuae.app.alhameedifragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by anvin on 12/23/2016.
 */

public class SessionManager {


    Context AppContext;
    /*sharedpreference -init */
    public static  final String PREF_NAME="MyPref";
    public static final String IS_LOGIN="IsLoggedIn";
    public static final String KEY_ID="id";
    public static final String KEY_USER_NAME="username";
    public static final String KEY_NAME="name";
    public static final String KEY_EMAIL="email";
    public static final String KEY_LOCATION="location";
    public static final String KEY_PHONE="phone";
    public static final String KEY_CUR_PASSWORD="current_password";
    public static final String KEY_IS_SKIPED="false";
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    /*sharedpreference -end*/
    public SessionManager(Context appContext){
        AppContext=appContext;
        prefs=AppContext.getSharedPreferences(PREF_NAME,0);
        editor=prefs.edit();
    }


    public void CreateLoginSession(String jsonId,String jsonUserName,String jsonName,String jsonEmail,String jsonLocation,String jsonPhone,String cur_password,String isskiped){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_ID, jsonId);
        editor.putString(KEY_USER_NAME, jsonUserName);
        editor.putString(KEY_NAME, jsonName);
        editor.putString(KEY_EMAIL, jsonEmail);
        editor.putString(KEY_LOCATION, jsonLocation);
        editor.putString(KEY_PHONE, jsonPhone);
        editor.putString(KEY_CUR_PASSWORD,cur_password);
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_IS_SKIPED,isskiped);
        System.out.println("________________________________________________");
        System.out.println("(session manager )Cur Pass : "+cur_password);
        System.out.println("________________________________________________");
        editor.commit();
    }

    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(AppContext, Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            AppContext.startActivity(i);
        }
        else{
            Intent i = new Intent(AppContext, HomeMainActivityNew.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            AppContext.startActivity(i);
        }

    }


    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_ID, prefs.getString(KEY_ID, null));
        user.put(KEY_USER_NAME, prefs.getString(KEY_USER_NAME, null));
        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, prefs.getString(KEY_EMAIL, null));
        user.put(KEY_LOCATION, prefs.getString(KEY_LOCATION, null));
        user.put(KEY_PHONE, prefs.getString(KEY_PHONE, null));
        user.put(KEY_CUR_PASSWORD,prefs.getString(KEY_CUR_PASSWORD,null));
        user.put(KEY_IS_SKIPED,prefs.getString(KEY_IS_SKIPED,null));
        return user;
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(AppContext, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        AppContext.startActivity(i);
    }


    public void userSkipedLogin(){
        editor.putString(KEY_IS_SKIPED,"true");
        editor.commit();
    }


    public boolean isLoggedIn(){
        return prefs.getBoolean(IS_LOGIN, false);
    }
}
