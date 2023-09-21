package club.kreastol.community.User;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import club.kreastol.community.Common.Adapters.DrawerAdapter;
import club.kreastol.community.Common.Items.DrawerItem;
import club.kreastol.community.Common.Items.SimpleItem;
import club.kreastol.community.Common.Items.SpaceItem;
import club.kreastol.community.R;
import club.kreastol.community.User.DashboardFragments.CovidFragment;
import club.kreastol.community.User.DashboardFragments.GalleryFragment;
import club.kreastol.community.User.DashboardFragments.HomeFragment;
import club.kreastol.community.User.DashboardFragments.MyProfileFragment;
import club.kreastol.community.User.DashboardFragments.PostsFragment;
import club.kreastol.community.User.DashboardFragments.SettingsFragment;
import club.kreastol.community.User.LoginSignup.Login;
import club.kreastol.community.User.LoginSignup.RetailerStartupScreen;
import club.kreastol.community.User.LoginSignup.SignUp;
import club.kreastol.community.util.AppRunSession;
import club.kreastol.community.util.UserProfilSession;


public class UserDashboard extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private UserProfilSession userProfile;
    private AppRunSession appRun;

    //For the user data getting
    private Context mContext;
    /*public List<Object> dataList = new ArrayList<>();*/
    public String username, firstName, lastName, email, birthday;
    public int gender, points;
    final String TAG = this.getClass().getSimpleName();
    private static final int POS_PROFILE = 0;
    // 1 --> space
    private static final int POS_HOME = 2;
    private static final int POS_POSTS = 3;
    private static final int POS_GALLERY = 4;
    private static final int POS_SETTINGS = 5;
    // 6 --> space
    private static final int POS_LOG = 7;
    // 8 --> space
    private static final int POS_COVID = 9;
    private static final int POS_ADMIN = 10;


    private DrawerItem logButton;
    private DrawerItem manageButton;
    private String[] screenTitles;
    private Drawable[] screenIcons;
    private Drawable[] miscIcons;
    private String[] miscTitles;


    private SlidingRootNav slidingRootNav;

    //Getting the variables for the myProfileFragment
    MyProfileFragment profile;
    LinearLayout saveCancelView;
    EditText fNameModify;
    EditText lNameModify;
    EditText emailModify;
    TextInputLayout fNameTil;
    TextInputLayout lNameTil;
    TextInputLayout emailTil;
    Button cancelBtn;
    Button saveBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        appRun = new AppRunSession(this);
        userProfile = new UserProfilSession(this);

        setLocaleStart(appRun.getLanguage());

        switch(appRun.getTheme()){
            case 0:
                setTheme(R.style.Theme_Kreastol);
                break;
            case 1:
                setTheme(R.style.Theme_Kreastol_Red_Pulse);
                break;
            case 2:
                setTheme(R.style.Theme_Kreastol_Futuristic);
                break;
            case 3:
                setTheme(R.style.Theme_Kreastol_Miku);
                break;
        }
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_user_dashboard);

        miscIcons = loadMiscIcons();
        miscTitles = loadMiscTitles();




        // Check if user is already logged in or not
        if (userProfile.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            logButton = new SimpleItem(miscIcons[1], miscTitles[1])
                    .withIconTint(getColorId(R.attr.colorAccent))
                    .withTextTint(getColorId(R.attr.colorAccent))
                    .withSelectedIconTint(getColorId(R.attr.colorSecondary))
                    .withSelectedTextTint(getColorId(R.attr.colorSecondary));
        } else {
            logButton = new SimpleItem(miscIcons[0], miscTitles[0])
                    .withIconTint(getColorId(R.attr.colorAccent))
                    .withTextTint(getColorId(R.attr.colorAccent))
                    .withSelectedIconTint(getColorId(R.attr.colorSecondary))
                    .withSelectedTextTint(getColorId(R.attr.colorSecondary));
        }
        manageButton = new SimpleItem(miscIcons[2], miscTitles[2])
                .withIconTint(getColorId(R.attr.colorAccent))
                .withTextTint(getColorId(R.attr.colorAccent))
                .withSelectedIconTint(getColorId(R.attr.colorSecondary))
                .withSelectedTextTint(getColorId(R.attr.colorSecondary));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(" ");

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_layout)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = null;
        adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_PROFILE),
                new SpaceItem(38),
                createItemFor(POS_HOME).setChecked(true),
                createItemFor(POS_POSTS),
                createItemFor(POS_GALLERY),
                createItemFor(POS_SETTINGS),
                new SpaceItem(28),
                logButton,
                new SpaceItem(28),
                createItemFor(POS_COVID)));

        if (userProfile.isAdmin()) {
            adapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(POS_PROFILE),
                    new SpaceItem(38),
                    createItemFor(POS_HOME).setChecked(true),
                    createItemFor(POS_POSTS),
                    createItemFor(POS_GALLERY),
                    createItemFor(POS_SETTINGS),
                    new SpaceItem(28),
                    logButton,
                    new SpaceItem(28),
                    createItemFor(POS_COVID),
                    manageButton));
        }


        adapter.setListener(this);
        RecyclerView list = findViewById(R.id.drawer_list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setSelected(POS_HOME);
    }


    @Override
    public void onItemSelected(int position) {

        if (position == POS_LOG) {
            if (userProfile.isLoggedIn()) {
                userProfile.terminateSession();
                startActivity(getIntent());
                finish();
            } else {
                startActivity(new Intent(getApplicationContext(), RetailerStartupScreen.class));
                finish();
            }
            slidingRootNav.closeMenu();
        }
        if (position == POS_ADMIN) {
            startActivity(new Intent(getApplicationContext(), ManagePage.class));
            finish();
        }

        Fragment selectedScreen = null;
        switch (position) {
            case POS_PROFILE:
                selectedScreen = new MyProfileFragment();
                break;
            case POS_HOME:
                selectedScreen = new HomeFragment();
                break;
            case POS_POSTS:
                selectedScreen = new PostsFragment();
                break;
            case POS_GALLERY:
                selectedScreen = new GalleryFragment();
                break;
            case POS_SETTINGS:
                selectedScreen = new SettingsFragment();
                break;
            case POS_COVID:
                selectedScreen = new CovidFragment();
                break;
            default:
                selectedScreen = new HomeFragment();
                break;
        }
        showFragment(selectedScreen);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
    @Override
    public void onBackPressed() {
        if(slidingRootNav.isMenuOpened()){
            slidingRootNav.closeMenu();
        }
        else{
            super.onBackPressed();
        }

    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(getColorId(R.attr.colorAccent))
                .withTextTint(getColorId(R.attr.colorAccent))
                .withSelectedIconTint(getColorId(R.attr.colorSecondary))
                .withSelectedTextTint(getColorId(R.attr.colorSecondary));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.id_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.id_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    private String[] loadMiscTitles() {
        return getResources().getStringArray(R.array.id_miscTitles);
    }

    private Drawable[] loadMiscIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.id_miscIons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    public int getColorId(int themeAttr){
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(themeAttr, typedValue, true);
        int color = ContextCompat.getColor(this, typedValue.resourceId);
        return color;
    }




    //My Profile Framgent view gets
    private boolean isEditing = false;

    //My Profile Framgent click methods
    public void callLoginScreenProfile(View view){

        Intent intent = new Intent(getApplicationContext(), Login.class);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.please_login_btn), "transition_login");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserDashboard.this, pairs);
        startActivity(intent, options.toBundle());
    }

    public void callSignUpScreenProfile(View view){

        Intent intent = new Intent(getApplicationContext(), SignUp.class);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.please_signup_btn), "transition_signup");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UserDashboard.this, pairs);
        startActivity(intent, options.toBundle());
    }

    public void callModifyUserData(View view){


        saveCancelView = findViewById(R.id.profile_save_cancel_button);
        fNameModify = findViewById(R.id.profile_first_name);
        lNameModify = findViewById(R.id.profile_last_name);
        emailModify = findViewById(R.id.profile_email);
        fNameTil = findViewById(R.id.profile_first_name_til);
        lNameTil = findViewById(R.id.profile_last_name_til);
        emailTil = findViewById(R.id.profile_email_til);
        cancelBtn = findViewById(R.id.profile_cancel_button);
        saveBtn = findViewById(R.id.profile_save_button);

        firstName = fNameModify.getText().toString();
        lastName = lNameModify.getText().toString();

        saveBtn.setEnabled(false);

        fNameModify.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {   }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                saveBtn.setEnabled(true);
            }
        });

        lNameModify.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                saveBtn.setEnabled(true);
            }
        });

        emailModify.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                saveBtn.setEnabled(true);
            }
        });


        if(isEditing == false){
            saveCancelView.setVisibility(View.VISIBLE);
            lNameTil.setHelperTextEnabled(true);
            fNameTil.setHelperTextEnabled(true);
            lNameTil.setHelperText(getString(R.string.profile_last_name));
            fNameTil.setHelperText(getString(R.string.profile_first_name));
            fNameModify.setEnabled(true);
            lNameModify.setEnabled(true);
            isEditing = true;
        }

    }

    public void callCancelEdit(View view){

        EditText fNameModify = (EditText) findViewById(R.id.profile_first_name);
        EditText lNameModify = (EditText) findViewById(R.id.profile_last_name);
        EditText emailModify = (EditText) findViewById(R.id.profile_email);
        TextInputLayout fNameTil = (TextInputLayout) findViewById(R.id.profile_first_name_til);
        TextInputLayout lNameTil = (TextInputLayout) findViewById(R.id.profile_last_name_til);
        TextInputLayout emailTil = (TextInputLayout) findViewById(R.id.profile_email_til);
        lNameModify.setText(lastName);
        fNameModify.setText(firstName);
            //Log.d("GotName", firstNameU);
            lNameTil.setHelperTextEnabled(false);
            fNameTil.setHelperTextEnabled(false);
            fNameModify.setEnabled(false);
            lNameModify.setEnabled(false);
            isEditing = false;
            saveCancelView.setVisibility(View.GONE);
    }

    public void callSaveEdit(View view){

    }

    public void setLocaleStart(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

}

