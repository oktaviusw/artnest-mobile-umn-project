package id.ac.umn.mobile.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActivitySplash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        System.out.println(pref.getBoolean("LoggedIn",false));
        if(pref.getBoolean("LoggedIn",false)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(getBaseContext(), SignInActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            },SPLASH_TIME_OUT);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(getBaseContext(), DescribeActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            },SPLASH_TIME_OUT);
        }

    }
}
