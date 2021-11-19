package com.xh.aop;

import android.util.Log;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SingerAgentInterceptor {
    private static final String TAG = "SingerAgentInterceptor";

    @RuntimeType
    public Object interceptor(@This Object proxy, @Origin Method method,
                              @SuperMethod Method superMethod,
                              @AllArguments Object[] args) throws Exception {
        long start = System.currentTimeMillis();
//        String name = method.getName();
//        Log.e(TAG, "before " + name);
        Object ret = null;
        try {
            ret = superMethod.invoke(proxy, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Log.e(TAG, String.format("执行时间是：%s", System.currentTimeMillis() - start));
//        Log.e(TAG, "after " + name);
        return ret;
    }
}
