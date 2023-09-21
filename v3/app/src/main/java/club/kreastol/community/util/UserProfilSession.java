package club.kreastol.community.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UserProfilSession {

    private static final String TAG = UserProfilSession.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "UserSession";

    public UserProfilSession(Context _context) {
        this._context = _context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void terminateSession(){
        _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE).edit().clear().apply();
    }

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_ADMIN = "isAdmin";
    private static final String KEY_USERNAME = "username";


    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setAdmin(boolean isAdmin) {

        editor.putBoolean(KEY_IS_ADMIN, isAdmin);
        // commit changes
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public void setUsername(String username) {

        editor.putString(KEY_USERNAME, username);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }


    //Getting backs
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public boolean isAdmin(){
        return pref.getBoolean(KEY_IS_ADMIN, false);
    }

    public String getUsername() {return pref.getString(KEY_USERNAME, "");}

}
