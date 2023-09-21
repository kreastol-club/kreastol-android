package club.kreastol.community.User.LoginSignup;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import club.kreastol.community.R;
import club.kreastol.community.util.AppRunSession;
import club.kreastol.community.util.SessionManager;


public class SignUp2ndClass extends AppCompatActivity {

    //Varr
    ImageView backButton;
    Button next, login;
    TextView titleText;


    //Getting the data from previos activiy
    String lNameS;
    String fNameS;
    String usernameS;
    String emailS;

    //getting the data
    RadioGroup radioGroup;
    RadioButton selectGender;
    DatePicker datePicker;

    private AppRunSession appRun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appRun = new AppRunSession(this);
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
        setContentView(R.layout.activity_retailer_signup2);

        Intent intentG = getIntent();
        lNameS = intentG.getStringExtra("lName");
        fNameS = intentG.getStringExtra("fName");
        usernameS = intentG.getStringExtra("username");
        emailS = intentG.getStringExtra("email");

        //hooks

        backButton = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_btn);
        login = findViewById(R.id.signup_login_btn);
        titleText = findViewById(R.id.signup_title_text);

        radioGroup = findViewById(R.id.signup_radio_group);
        datePicker = findViewById(R.id.signup_age_picker);
    }

    public void callNextSignScreen(View view) {

        if (!validateGender() | !validateAge()) {
            return;
        }

        selectGender = findViewById(radioGroup.getCheckedRadioButtonId());
        String gender = selectGender.getText().toString();

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        String date = year + "-" + month + "-" + day;
        Log.d("Birthday", date);

        Intent intent = new Intent(getApplicationContext(), SignUp3rdClass.class);

        String genderS = gender;
        String dateS = date;

        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(backButton, "transition_back_arrow_btn");
        pairs[1] = new Pair<View, String>(next, "transition_next_btn");
        pairs[2] = new Pair<View, String>(login, "transition_login_btn");
        pairs[3] = new Pair<View, String>(titleText, "transition_title_text");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp2ndClass.this, pairs);
        intent.putExtra("gender", genderS);
        intent.putExtra("date", dateS);
        intent.putExtra("lName", lNameS);
        intent.putExtra("fName", fNameS);
        intent.putExtra("username", usernameS);
        intent.putExtra("email", emailS);
        startActivity(intent, options.toBundle());
    }

    public void callBackButtonClicked(View view) {
        super.onBackPressed();
    }

    public void callToLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    private boolean validateGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, this.getString(R.string.retailer_signup_error_no_gender_selected), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;

        if (isAgeValid < 2) {
            Toast.makeText(this, this.getString(R.string.retailer_signup_error_wrong_age_selected), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}