package club.kreastol.community.User.DashboardFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import club.kreastol.community.R;
import club.kreastol.community.util.AppRunSession;
import club.kreastol.community.util.SessionManager;
import club.kreastol.community.util.UserProfilSession;

import static android.app.UiModeManager.MODE_NIGHT_AUTO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_TIME;

public class SettingsFragment extends Fragment {

    private Context mContext;
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    ImageView themeKreastol, themeRedPulse, themeFututristic, themeMiku;
    private AppRunSession appRun;
    Boolean switchState, checkState;
    SwitchCompat darkSwitch;
    CheckBox themeCheckBox, systemThemeCheckbox;
    TextView darkModeOnOff, forceDarkTag;
    Configuration configuration;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view =  inflater.inflate(R.layout.fragment_dashboard_settings, container, false);
        appRun = new AppRunSession(mContext);

        themeKreastol = view.findViewById(R.id.theme_kreastol);
        themeRedPulse = view.findViewById(R.id.theme_red_pulse);
        themeFututristic = view.findViewById(R.id.theme_futuristic);
        themeMiku = view.findViewById(R.id.theme_miku);
        darkSwitch = view.findViewById(R.id.dark_mode_switch);
        themeCheckBox = view.findViewById(R.id.dark_mode_checkbox);
        systemThemeCheckbox = view.findViewById(R.id.dark_mode_system_checkbox);
        darkModeOnOff = view.findViewById(R.id.dark_mode_on_off);
        forceDarkTag = view.findViewById(R.id.force_dark_mode_tag);
        configuration = getResources().getConfiguration();

        //Theme and color
        themeListeners();

        Button langSelect = view.findViewById(R.id.choose_language);

        langSelect.setOnClickListener(v -> {
            registerForContextMenu(v);
            requireActivity().openContextMenu(v);
        });

        return  view;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.language_menu, menu);
        MenuItem item_ja = menu.findItem(R.id.ja_lang);
        MenuItem item_en = menu.findItem(R.id.en_lang);
        MenuItem item_hu = menu.findItem(R.id.hu_lang);
        MenuItem item_sr = menu.findItem(R.id.sr_lang);

        switch (appRun.getLanguage()){
            case "ja":
                item_ja.setChecked(true);
                break;
            case "en":
                item_en.setChecked(true);
                break;
            case "hu":
                item_hu.setChecked(true);
                break;
            case "sr":
                item_sr.setChecked(true);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.ja_lang:
                setLocale("ja");
                break;
            case R.id.en_lang:
                setLocale("en");
                break;
            case R.id.hu_lang:
                setLocale("hu");
                break;
            case R.id.sr_lang:
                setLocale("sr");
                break;
        }
        return super.onContextItemSelected(item);

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        appRun.setLanguage(myLocale.getLanguage());
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(mContext, requireActivity().getClass());
        requireActivity().finish();
        startActivity(refresh);
    }

    //Theme and color
    public void themeListeners(){

        int currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                darkModeOnOff.setText(getResources().getString(R.string.settings_fragmet_theme_force_dark_off));
                darkSwitch.setChecked(false);
                appRun.setToNightMode(false);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                darkModeOnOff.setText(getResources().getString(R.string.settings_fragmet_theme_force_dark_on));
                darkSwitch.setChecked(true);
                appRun.setToNightMode(true);
                break;
        }
        if(appRun.getNightBySystem() || appRun.getIsAuto()){
            darkSwitch.setEnabled(false);
            darkModeOnOff.setTextColor(getResources().getColor(R.color.color_disabled));
            forceDarkTag.setTextColor(getResources().getColor(R.color.color_disabled));

        }
        else {
            darkModeOnOff.setTextColor(getColorId(R.attr.colorAccent));
            forceDarkTag.setTextColor(getColorId(R.attr.colorAccent));
        }
        darkSwitch.setChecked(appRun.getIsDark());
        systemThemeCheckbox.setChecked(appRun.getNightBySystem());
        themeCheckBox.setChecked(appRun.getIsAuto());
        themeCheckBox.setEnabled(!appRun.getNightBySystem());
        if(appRun.getNightBySystem()) themeCheckBox.setChecked(false);
        darkModeOnOff.setText(appRun.getIsDark() ? getResources().getString(R.string.settings_fragmet_theme_force_dark_on) : getResources().getString(R.string.settings_fragmet_theme_force_dark_off));
        checkState = themeCheckBox.isSelected();

        darkSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchState = darkSwitch.isChecked();
                appRun.setToNightMode(switchState);
                appRun.setNightByUser(switchState ? "dark" : "light");
                darkModeOnOff.setText(appRun.getIsDark() ?
                        getResources().getString(R.string.settings_fragmet_theme_force_dark_on) : getResources().getString(R.string.settings_fragmet_theme_force_dark_off));
                Log.d("Drag", switchState.toString());

                AppCompatDelegate.setDefaultNightMode(appRun.getIsDark() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        systemThemeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            themeCheckBox.setEnabled(!isChecked);
            darkSwitch.setEnabled(!isChecked);

            darkModeOnOff.setTextColor(!appRun.getNightBySystem() ? getResources().getColor(R.color.color_disabled) : getColorId(R.attr.colorAccent));
            forceDarkTag.setTextColor(!appRun.getNightBySystem() ? getResources().getColor(R.color.color_disabled) : getColorId(R.attr.colorAccent));

            darkSwitch.setEnabled(!isChecked);
            if(isChecked){
                darkModeOnOff.setTextColor(getResources().getColor(R.color.color_disabled));
                forceDarkTag.setTextColor(getResources().getColor(R.color.color_disabled));
                themeCheckBox.setChecked(false);
                appRun.autoNight(false);
                appRun.setBySystem(true);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
            else{
                appRun.setBySystem(false);
                if(appRun.getUserNightMode().equals("dark")){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    appRun.setToNightMode(true);
                }
                else if(appRun.getUserNightMode().equals("light")){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    appRun.setToNightMode(false);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(appRun.getIsDark() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                }
            }

            int currentNightMode1 = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode1) {
                case Configuration.UI_MODE_NIGHT_NO:
                    darkModeOnOff.setText(getResources().getString(R.string.settings_fragmet_theme_force_dark_off));
                    darkSwitch.setChecked(false);
                    appRun.setToNightMode(false);
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    darkModeOnOff.setText(getResources().getString(R.string.settings_fragmet_theme_force_dark_on));
                    darkSwitch.setChecked(true);
                    appRun.setToNightMode(true);
                    break;
            }
            Log.d("Checkbox", "Checked");
        });

        themeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                darkModeOnOff.setTextColor(!appRun.getIsAuto() ? getResources().getColor(R.color.color_disabled) : getColorId(R.attr.colorAccent));
                forceDarkTag.setTextColor(!appRun.getIsAuto() ? getResources().getColor(R.color.color_disabled) : getColorId(R.attr.colorAccent));

                darkSwitch.setEnabled(!isChecked);

                int currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
                switch (currentNightMode) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        darkModeOnOff.setText(getResources().getString(R.string.settings_fragmet_theme_force_dark_off));
                        darkSwitch.setChecked(false);
                        appRun.setToNightMode(false);
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        darkModeOnOff.setText(getResources().getString(R.string.settings_fragmet_theme_force_dark_on));
                        darkSwitch.setChecked(true);
                        appRun.setToNightMode(true);
                        break;
                }
                if(isChecked){
                    appRun.autoNight(true);
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_AUTO_TIME & MODE_NIGHT_AUTO);
                }
                else{
                    appRun.autoNight(false);
                    if(appRun.getUserNightMode().equals("dark")){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        appRun.setToNightMode(true);
                    }
                    else if(appRun.getUserNightMode().equals("light")){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        appRun.setToNightMode(false);
                    }
                    else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    }
                }

                Log.d("Checkbox", "Checked");
            }
        });

        themeKreastol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appRun.setThemeId(0);
                appRun.changeTheme(getActivity());
            }
        });

        themeRedPulse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appRun.setThemeId(1);
                appRun.changeTheme(getActivity());
            }
        });

        themeFututristic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appRun.setThemeId(2);
                appRun.changeTheme(getActivity());
            }
        });

        themeMiku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appRun.setThemeId(3);
                appRun.changeTheme(getActivity());
            }
        });
    }

    public int getColorId(int themeAttr){
        TypedValue typedValue = new TypedValue();
        requireActivity().getTheme().resolveAttribute(themeAttr, typedValue, true);
        return ContextCompat.getColor(requireActivity(), typedValue.resourceId);
    }
}