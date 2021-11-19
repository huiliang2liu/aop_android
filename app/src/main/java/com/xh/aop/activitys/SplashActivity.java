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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        try {
            AopManager.setConfig(inputStream2string(getAssets().open("test.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AopManager.loadClass(MainActivity.class)));
            }
        });
        setContentView(tv);
    }

    public static String inputStream2string(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024 * 1024];
        int len;
        while ((len = is.read(buff)) > 0)
            baos.write(buff, 0, len);
        return new String(baos.toByteArray());
    }
}
