package com.xh.aop.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xh.aop.AopManager;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setBackgroundColor(Color.WHITE);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(50);
        tv.setGravity(Gravity.CENTER);
        tv.setText("这是开品");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AopManager.loadActivity(MainActivity.class, new String[]{"onCreate", "onSupportNavigateUp", "getApplicationContext"})));
            }
        });
        setContentView(tv);
    }
}
