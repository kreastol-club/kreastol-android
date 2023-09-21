package club.kreastol.kreastolcommunity.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import club.kreastol.kreastolcommunity.R;

public class SignUp extends AppCompatActivity {

    //Varr
    ImageView backButton;
    Button next, login;
    TextView titleText;


    //Get data variables
    TextInputLayout lastName, firstName, username, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_retailer_sign_up);

        //hooks for animation
        backButton = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_btn);
        login = findViewById(R.id.signup_login_btn);
        titleText = findViewById(R.id.signup_title_text);

        //Hooks for getting data
        lastName = findViewById(R.id.signup_last_name);
        firstName = findViewById(R.id.signup_first_name);
        username = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void callNextSignScreen(View view) {

        if(!validateLastName() | !validateFirstName() | !validateUsername() | !validateEmail()){
            return;
        }

        Intent intent = new Intent(getApplicationContext(), SignUp2ndClass.class);
        String lNameS = lastName.getEditText().getText().toString();
        String fNameS = firstName.getEditText().getText().toString();
        String usernameS = username.getEditText().getText().toString();
        String emailS = email.getEditText().getText().toString();


        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(backButton, "transition_back_arrow_btn");
        pairs[1] = new Pair<View, String>(next, "transition_next_btn");
        pairs[2] = new Pair<View, String>(login, "transition_login_btn");
        pairs[3] = new Pair<View, String>(titleText, "transition_title_text");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
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

    /*
     * Validation Forms
     */
    private boolean validateLastName() {
        String val = lastName.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            lastName.setError(this.getString(R.string.retailer_signup_error_cannot_be_empty));
            return false;
        } else {
            lastName.setError(null);
            lastName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateFirstName() {
        String val = firstName.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            firstName.setError(this.getString(R.string.retailer_signup_error_cannot_be_empty));
            return false;
        } else {
            firstName.setError(null);
            firstName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUsername() {
        String val = username.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            username.setError(this.getString(R.string.retailer_signup_error_cannot_be_empty));
            return false;
        } else if (val.length() > 20) {
            username.setError(this.getString(R.string.retailer_signup_error_long_username));
            return false;
        } else if (val.length() < 5) {
            username.setError(this.getString(R.string.retailer_signup_error_short_username));
            return false;
        } else if (!val.matches(checkspaces)) {
            username.setError(this.getString(R.string.retailer_signup_error_no_whitespace_username));
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            email.setError(this.getString(R.string.retailer_signup_error_cannot_be_empty));
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

}