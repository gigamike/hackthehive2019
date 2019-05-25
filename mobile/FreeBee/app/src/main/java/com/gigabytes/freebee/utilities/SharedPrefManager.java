package com.gigabytes.freebee.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.gigabytes.freebee.FreeBeeApplication;
import com.gigabytes.freebee.utilities.constants.Roles;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mContext;

    private static final String SHARED_PREF_NAME = "mySharedPref";

    private static final String EMAIL_ADDRESS = "emailAddress";

    private static final String USER_ID = "USER_ID";
    private static final String IS_LOGIN = "IS_LOGIN";
    private static final String USER_ROLE = "USER_ROLE";
    private static final String USER_PICTURE_URL = "USER_PICTURE_URL";
    private static final String FULL_NAME = "FULL_NAME";

//    freeBeeApplication.fullName = volunteers.getFirstName() + " " + volunteers.getMiddleName() + " " +  volunteers.getLastName();
//    freeBeeApplication.isLogin = volunteers.isOnline();
//    freeBeeApplication.userId = volunteers.getId();
//    freeBeeApplication.userRole = Roles.VOLUNTEER;
//    freeBeeApplication.userPictureURL = volunteers.getProfilePic();


    public SharedPrefManager(Context context) {
        mContext = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean loginUser(String emailAddressOrUsername, FreeBeeApplication freeBeeApplication){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(EMAIL_ADDRESS, emailAddressOrUsername);

        editor.putString(USER_ID, freeBeeApplication.userId);
        editor.putBoolean(IS_LOGIN, freeBeeApplication.isLogin);
        editor.putString(USER_ROLE, freeBeeApplication.userRole);
        editor.putString(FULL_NAME, freeBeeApplication.fullName);
        editor.putString(USER_PICTURE_URL, freeBeeApplication.userPictureURL);

        editor.apply();

        return true;
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(EMAIL_ADDRESS, null) != null){
            return true;
        }
        return false;
    }

    public boolean logoutUser(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getEmailAddress(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(EMAIL_ADDRESS,null);
    }

    public String getUserId() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_ID,null);
    }

    public String getUserRole() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_ROLE,null);
    }

    public String getUserFullname() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(FULL_NAME,null);
    }

    public String getUserProfilePic() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_PICTURE_URL,null);
    }
}
