package in.calibrage.wsm.localData;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.List;

import in.calibrage.wsm.model.AddressModel;
import in.calibrage.wsm.model.ResLastTrip;
import in.calibrage.wsm.model.ResTrip;
import in.calibrage.wsm.model.ResUserData;

import static in.calibrage.wsm.common.Common.LastTrip_Details;
import static in.calibrage.wsm.common.Common.Trip_Details;
import static in.calibrage.wsm.common.Common.USER_Details;


public class SharedPrefsData {
    private static SharedPrefsData instance = null;
    private static final String DEFAULTVALUESTRING = "N/A";
    private static final int DEFAULTVALUINT = 0;
    private static final String WSM = "wsm";
    private SharedPreferences WsmSharedPrefs = null;
    private static final String USER_ID = "user_id";

    public static SharedPrefsData getInstance(Context context) {

        if (instance == null) {
            instance = new SharedPrefsData();
            instance.getPitchItSharedPrefs(context);
        }
        return instance;
    }

    private void getPitchItSharedPrefs(Context context) {
        if (this.WsmSharedPrefs == null) {
            this.WsmSharedPrefs = context.getSharedPreferences(WSM, Context.MODE_PRIVATE);
        }
    }

    private void loadFreshPitchItSharedPrefs(Context context) {
        this.WsmSharedPrefs = context.getSharedPreferences(WSM, Context.MODE_PRIVATE);
    }

    public String getStringFromSharedPrefs(String key) {
        return WsmSharedPrefs.getString(key, DEFAULTVALUESTRING);
    }

    public int getIntFromSharedPrefs(String key) {
        return WsmSharedPrefs.getInt(key, DEFAULTVALUINT);
    }

    public void updateMultiValue(Context context, List<SharedPrefsBean> sharedPrefsBeans) {
        //getPitchItSharedPrefs(context);
        SharedPreferences.Editor editor = this.WsmSharedPrefs.edit();

        for (SharedPrefsBean eachShrePref : sharedPrefsBeans) {
            if (eachShrePref.getIsInt()) {
                editor.putInt(eachShrePref.getKey(), eachShrePref.getValueInt());
            } else {
                editor.putString(eachShrePref.getKey(), eachShrePref.getValueString());
            }
        }
        editor.commit();
        loadFreshPitchItSharedPrefs(context);
    }

    public void updateStringValue(Context context, String key, String value) {
        //getPitchItSharedPrefs(context);
        SharedPreferences.Editor editor = this.WsmSharedPrefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void updateIntValue(Context context, String key, int value) {
        //getPitchItSharedPrefs(context);
        SharedPreferences.Editor editor = this.WsmSharedPrefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void ClearData(Context context) {
        getPitchItSharedPrefs(context);
        SharedPreferences profilePref = context.getSharedPreferences(WSM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = profilePref.edit();
        editor.clear();
        editor.apply();
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }

    public void saveUserId(Context context, String userId) {
        if (context != null) {
            SharedPreferences profilePref = context.getSharedPreferences(WSM, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = profilePref.edit();
            editor.putString(USER_ID, userId);

            // Commit the edits!
            editor.apply();

        }
    }

    public String getUserId(Context context) {
        SharedPreferences profilePref = context.getSharedPreferences(WSM,
                Context.MODE_PRIVATE);
        return profilePref.getString(USER_ID, "");

    }

    public static void putString(Context context, String key, String value, String pref) {
        if (context != null && key != null) {
            if (pref != null && !pref.isEmpty()) {
                context.getSharedPreferences(pref, 0).edit().putString(key, value).apply();
            } else {
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).apply();
            }

        }
    }

    public static String getString(Context context, String key, String pref) {
        return context != null && key != null ? (pref != null && !pref.isEmpty() ?
                context.getSharedPreferences(pref, 0).getString(key, (String) null)
                : PreferenceManager.getDefaultSharedPreferences(context).getString(key, (String) null)) : null;
    }

    public static void putInt(Context context, String key, int value, String pref) {
        if (context == null || key == null) {
            return;
        }
        if (pref == null || pref.isEmpty()) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, value).apply();
        } else {
            context.getSharedPreferences(pref, Context.MODE_PRIVATE).edit().putInt(key, value).apply();
        }
    }

    public static int getInt(Context context, String key, String pref) {
        if (context == null || key == null) {
            return 0;
        }
        if (pref == null || pref.isEmpty()) {
            return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, 0);
        } else {
            return context.getSharedPreferences(pref, Context.MODE_PRIVATE).getInt(key, 0);
        }
    }
    public static boolean getBool(Context context, String key) {
        SharedPreferences profilePref = context.getSharedPreferences(WSM,
                Context.MODE_PRIVATE);
        return profilePref.getBoolean(key, false);
    }
    public static void putBool(Context context, String key, boolean value) {

        if (context != null) {
            SharedPreferences profilePref = context.getSharedPreferences(WSM, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = profilePref.edit();
            editor.putBoolean(key, value);
            editor.apply();

        }
    }



    public static void  saveUserDetails(Context mContext, ResUserData userData)
    {
        Gson gson = new Gson();
        if (mContext != null) {
            String json = gson.toJson(userData);
            SharedPreferences profilePref = mContext.getSharedPreferences(WSM, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = profilePref.edit();
            editor.putString(USER_Details, json);
            // Commit the edits!
            editor.apply();

        }
    }

    public static ResUserData getUserDetails(Context mContext)
    {
        Gson gson = new Gson();
        SharedPreferences profilePref = mContext.getSharedPreferences(WSM,
                Context.MODE_PRIVATE);
        String json = profilePref.getString(USER_Details, "");
        ResUserData obj = gson.fromJson(json, ResUserData.class);
        return obj;

    }


    public static void  saveTripDetails(Context mContext, ResLastTrip.Tripdate TripData)
    {
        Gson gson = new Gson();
        if (mContext != null) {
            String json = gson.toJson(TripData);
            SharedPreferences profilePref = mContext.getSharedPreferences(WSM, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = profilePref.edit();
            editor.putString(Trip_Details, json);
            editor.apply();
        }
    }

    public static ResLastTrip.Tripdate getTripDetails(Context mContext)
    {
        Gson gson = new Gson();
        SharedPreferences profilePref = mContext.getSharedPreferences(WSM,
                Context.MODE_PRIVATE);
        String json = profilePref.getString(Trip_Details, "");
        ResLastTrip.Tripdate obj = gson.fromJson(json, ResLastTrip.Tripdate.class);
        return obj;
    }


    public static void  saveAds(Context mContext, AddressModel TripData)
    {
        Gson gson = new Gson();
        if (mContext != null) {
            String json = gson.toJson(TripData);
            SharedPreferences profilePref = mContext.getSharedPreferences(WSM, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = profilePref.edit();
            editor.putString("ads", json);
            editor.apply();
        }
    }

    public static AddressModel getAds(Context mContext)
    {
        Gson gson = new Gson();
        SharedPreferences profilePref = mContext.getSharedPreferences(WSM,
                Context.MODE_PRIVATE);
        String json = profilePref.getString("ads", "");
        AddressModel obj = gson.fromJson(json,AddressModel.class);
        return obj;
    }



}
