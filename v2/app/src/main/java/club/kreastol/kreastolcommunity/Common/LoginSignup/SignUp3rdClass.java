package club.kreastol.kreastolcommunity.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.textfield.TextInputLayout;

import club.kreastol.kreastolcommunity.R;

public class SignUp3rdClass extends AppCompatActivity {


    //Get data variables
    TextInputLayout password, passwordConfirm;

    String lNameS;
    String fNameS;
    String usernameS;
    String emailS;
    String genderS;
    String dateS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_retailer_3rd_class);

        //Hooks for getting data
        password = findViewById(R.id.signup_password);
        passwordConfirm = findViewById(R.id.signup_password_confirm);

        //Getting the data from previos activiy
        Intent intentS = getIntent();
        lNameS = intentS.getStringExtra("lName");
        fNameS = intentS.getStringExtra("fName");
        usernameS = intentS.getStringExtra("username");
        emailS = intentS.getStringExtra("email");
        genderS = intentS.getStringExtra("gender");
        dateS = intentS.getStringExtra("date");

    }

    public void callBackButtonClicked(View view) {
        super.onBackPressed();
    }

    public void callToLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    public void callSignUpNClicked(View view) {
        if (!validatePasswords()) {
            return;
        }

        Intent intent = new Intent(getApplicationContext(), RetailerStartupScreen.class);
        /*intent.putExtra("gender", genderS);
        intent.putExtra("date", dateS);
        intent.putExtra("lName", lNameS);
        intent.putExtra("fName", fNameS);
        intent.putExtra("username", usernameS);
        intent.putExtra("email", emailS);*/
        startActivity(intent);
    }

    private boolean validatePasswords() {
        String val = password.getEditText().getText().toString().trim();
        String val2 = passwordConfirm.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                //"(?=.*[0-9])" + //at least 1 digit
                //"(?=.*[a-z])" + //at least lowercase char
                //"(?=.*[A-Z])" + //at least 1 uppercase char
                "(?=.*[a-zA-Z])" + // any letter
                //"(?=.*[@#$%^&+=])" + //at least 1 special char
                "(?=\\S+$)" + // no white spaces
                ".{6,}" + //at least 6 length
                "$";
        String passwordLenght = "^" + ".{6,}" + "$";
        if (val.isEmpty()) {
            password.setError(this.getString(R.string.retailer_signup_error_cannot_be_empty));
            return false;
        } else if (!val.matches(passwordLenght)) {
            password.setError(this.getString(R.string.retailer_signup_error_password_at_least_size));
            return false;
        } else if (!val.matches(checkPassword)) {
            password.setError(this.getString(R.string.retailer_signup_error_password_at_least_chars));
            return false;
        } else {
            if (!val.equals(val2)) {
                password.setError(this.getString(R.string.retailer_signup_error_passwords_match));
                passwordConfirm.setError(this.getString(R.string.retailer_signup_error_passwords_match));
                return false;
            }else{
                password.setError(null);
                passwordConfirm.setError(null);
                password.setErrorEnabled(false);
                return true;
            }
        }
    }


}