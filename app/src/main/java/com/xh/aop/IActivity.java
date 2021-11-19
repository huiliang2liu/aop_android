package com.xh.aop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

public interface IActivity {


    void onCreate(Bundle savedInstanceState);

    void setContentView(View view);

    void setContentView(int layoutResID);

    Window getWindow();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(@NonNull Bundle outState);

    void onDestroy();
}