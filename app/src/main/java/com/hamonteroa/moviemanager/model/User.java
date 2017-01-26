package com.hamonteroa.moviemanager.model;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.hamonteroa.moviemanager.utility.MMConstants;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hamonteroa on 1/13/17.
 */

public class User  {

    private static User mInstance;
    private String requestToken;
    private Date requestTokenExpiresAt;
    private String sessionID;
    private String userID;

    public User() {
        requestToken = "";
        sessionID = "";
        userID = "";
    }

    public static User getInstance() {
        if (mInstance == null) {
            mInstance = new User();
        }

        return mInstance;
    }

    public static void setInstance(User user) {
        if (user == null) {
            mInstance = new User();
        }
        mInstance = user;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Date getRequestTokenExpiresAt() {
        return requestTokenExpiresAt;
    }

    public void setRequestTokenExpiresAt(Date requestTokenExpiresAt) {
        this.requestTokenExpiresAt = requestTokenExpiresAt;
    }

    public static void saveUserInstance(Activity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(MMConstants.SP_USER_INSTANCE, new Gson().toJson(User.getInstance()));
        editor.commit();
    }

    public static void retriveuserInstance(Activity activity) {
        SharedPreferences sharedPreferences = activity.getPreferences(MODE_PRIVATE);
        String json = sharedPreferences.getString(MMConstants.SP_USER_INSTANCE, "");

        User.setInstance(new Gson().fromJson(json, User.class));
    }

    public void print() {
        Log.v("User", String.valueOf((new StringBuilder("**User** userId:"))
                .append(mInstance.getUserID())
                .append(" userID: ")
                .append(mInstance.userID)
                .append(" sessionID: ")
                .append(mInstance.sessionID)
                .append(" requestToken: ")
                .append(mInstance.requestToken)
        ));
    }
}
