package anis.sofiyanti.com.threes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSharedPreferences {

    private static final String APP_PREF = "app_status";
    private static final String LOGIN_PREF = "login_status";
    private static final String ID_PREF = "id_user";

    private static SharedPreferences getPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void login(Context context, String id){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGIN_PREF, true);
        editor.putString(ID_PREF, id);

        //Melakukan perubahan terhadap Shared Preferences secara asinkron
        //lebih cepat, namun tidak memiliki nilai balikan
        editor.apply();
    }

    public static void logout(Context context){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGIN_PREF, false);
        editor.putString(ID_PREF, "");

        editor.apply();
    }

    public static void setFirstTime(Context context){
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(APP_PREF, false);
        editor.apply();
    }

    public static boolean isFirstTime(Context context){
        return getPreferences(context).getBoolean(APP_PREF, true);
    }

    public static boolean isLoggedIn(Context context){
        return getPreferences(context).getBoolean(LOGIN_PREF, false);
    }

    public static String getId(Context context){
        return getPreferences(context).getString(ID_PREF, "");
    }
}