package com.moh.clinicalguideline.views.main;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.moh.clinicalguideline.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME = 2000;

    private Runnable splash = new Runnable()
    {
        @Override
        public void run()
        {
            Intent begin = new Intent(SplashActivity.this, MenuActivity.class);
            startActivity(begin);
            finish();
        }
    };
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        handler = new Handler();
        handler.postDelayed(splash,SPLASH_TIME);
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        handler.removeCallbacks(splash);
    }
}
