package club.kreastol.kreastolcommunity.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.cuberto.liquid_swipe.LiquidPager;

import club.kreastol.kreastolcommunity.HelperClasses.ViewPager;
import club.kreastol.kreastolcommunity.R;
import club.kreastol.kreastolcommunity.User.UserDashboard;

public class OnBoarding extends AppCompatActivity {

    LiquidPager pager;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding);
        pager = findViewById(R.id.pager);
        viewPager = new ViewPager(getSupportFragmentManager(), 1);
        pager.setAdapter(viewPager);


    }
    public void skipCall(View view){
        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
    }
}