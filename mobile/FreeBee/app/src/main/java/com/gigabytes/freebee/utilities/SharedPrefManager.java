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

    private static final String ORGANIZATION = "ORGANIZATION";
    private static final String CITY = "CITY";
    private static final String COUNTRY = "COUNTRY";
    private static final String MOBILE_NUMBER = "MOBILE_NUMBER";

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

        editor.putString(ORGANIZATION, freeBeeApplication.organization);
        editor.putString(CITY, freeBeeApplication.city);
        editor.putString(COUNTRY, freeBeeApplication.country);
        editor.putString(MOBILE_NUMBER, freeBeeApplication.mobileNumber);

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

    public String getOrganization() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ORGANIZATION,null);
    }

    public String getCity() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CITY,null);
    }

    public String getCountry() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(COUNTRY,null);
    }

    public String getMobileNumber() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(MOBILE_NUMBER,null);
    }
}
