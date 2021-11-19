package com.xh.aop;

import android.util.Log;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;

public class SingerAgentInterceptor {
    private static final String TAG = "SingerAgentInterceptor";
    public Object interceptor(@This Object proxy, @Origin Method method,
                              @SuperMethod Method superMethod,
                              @AllArguments Object[] args) throws Exception {
        Log.e(TAG, "before");
        Object ret = superMethod.invoke(proxy, args);
        Log.e(TAG, "after");
        return ret;
    }
}
