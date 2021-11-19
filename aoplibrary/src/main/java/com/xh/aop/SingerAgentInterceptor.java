package com.xh.aop;

import android.util.Log;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;

public class SingerAgentInterceptor {
    private static final String TAG = "SingerAgentInterceptor";

    @RuntimeType
    public Object interceptor(@This Object proxy, @Origin Method method,
                              @SuperMethod Method superMethod,
                              @AllArguments Object[] args) throws Exception {
        String name = method.getName();
        Log.e(TAG, "before " + name);
        Object ret = superMethod.invoke(proxy, args);
        Log.e(TAG, "after " + name);
        return ret;
    }
}
