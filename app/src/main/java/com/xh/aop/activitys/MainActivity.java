package com.xh.aop.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xh.aop.AopManager;
import com.xh.aop.IActivity;
import com.xh.aop.R;
import com.xh.aop.Test;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("ddd", onSupportNavigateUp() + "");
        Log.e("ddd", getApplicationContext() + "");
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        return super.onSupportNavigateUp();
//    }

//    @Override
//    public Context getApplicationContext() {
//        return super.getApplicationContext();
//    }

    public void test(View view) {
        try {
            Class[] classes = new Class[]{IActivity.class};
            IActivity activity = AopManager.createObject(Test.class, new String[]{"test"});
            Log.e("ddd", activity.test());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}