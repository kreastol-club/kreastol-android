package club.kreastol.community.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Locale;

import club.kreastol.community.R;

public class AppRunSession {
    private static final String TAG = AppRunSession.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "RunSession";

    public AppRunSession(Context _context) {
        this._context = _context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    String currentLang = Locale.getDefault().getDisplayLanguage(Locale.US);

    private static final String KEY_THEME = "theme";
    private static final String KEY_AUTO_NIGHT = "isAuto";
    private static final String KEY_IS_NIGHT = "isforceNight";
    private static final String KEY_NIGHT_BY_SYSTEM = "isNightBySystem";
    private static final String KEY_USER_NIGHT = "userNight";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_IS_FIRSTTIME = "firstTime";

    public void setThemeId(int theme){
        editor.putInt(KEY_THEME, theme);
        // commit changes
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public void autoNight(boolean isAuto){
        editor.putBoolean(KEY_AUTO_NIGHT, isAuto);
        // commit changes
        editor.commit();
        Log.d(TAG, "Auto_night session modified!");
    }

    public void setToNightMode(boolean isForceNight){
        editor.putBoolean(KEY_IS_NIGHT, isForceNight);
        // commit changes
        editor.commit();
        Log.d(TAG, "Force_night session modified!");
    }

    public void setNightByUser(String nightMode){
        editor.putString(KEY_USER_NIGHT, nightMode);

        // commit changes
        editor.commit();

        Log.d(TAG, "User Night Mode session modified!");
    }

    public void setBySystem(boolean systemTheme){
        editor.putBoolean(KEY_NIGHT_BY_SYSTEM, systemTheme);

        // commit changes
        editor.commit();

        Log.d(TAG, "Night Mode By System session modified!");
    }

    public void setLanguage(String language){
        editor.putString(KEY_LANGUAGE, language);

        // commit changes
        editor.commit();

        Log.d(TAG, "User Language session modified!");
    }

    public void setFirstTime(boolean firstTime){
        editor.putBoolean(KEY_IS_FIRSTTIME, firstTime);
        editor.commit();
        Log.d(TAG, "User First time session modified!");

    }


    public int getTheme() {return pref.getInt(KEY_THEME, 0);}

    public boolean getIsAuto(){
        return pref.getBoolean(KEY_AUTO_NIGHT, false);
    }

    public boolean getIsDark(){
        return pref.getBoolean(KEY_IS_NIGHT, false);
    }

    public String getUserNightMode(){return pref.getString(KEY_USER_NIGHT, "");}

    public boolean getNightBySystem(){
        return pref.getBoolean(KEY_NIGHT_BY_SYSTEM, false);
    }

    public String getLanguage(){ return pref.getString(KEY_LANGUAGE, currentLang);}

    public boolean getFirstTime(){
        return pref.getBoolean(KEY_IS_FIRSTTIME, true);
    }

    public void changeTheme(Activity activity){
        switch(getTheme()){
            case 0:
                activity.setTheme(R.style.Theme_Kreastol);
                break;
            case 1:
                activity.setTheme(R.style.Theme_Kreastol_Red_Pulse);
                break;
            case 2:
                activity.setTheme(R.style.Theme_Kreastol_Futuristic);
                break;
            case 3:
                activity.setTheme(R.style.Theme_Kreastol_Miku);
                break;
        }
        Intent intent = new Intent(_context, activity.getClass());
        activity.startActivity(intent);
        activity.finish();
    }

}
