package com.xh.aop;

import android.app.Application;
import android.content.Context;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AopLoadClass {
    private static Context context;
    //    private static ClassLoadingStrategy wrapping;
    private static ClassLoadingStrategy injecting;
    private static ClassLoader loader;
//    private static Map<String, Class> nameMap = new HashMap<>();

    public static void setContext(Context context) {
        AopLoadClass.context = context.getApplicationContext();
    }

    private synchronized static void init() {
        createContext();
        if (context == null)
            throw new RuntimeException("context is null,you must set context");
        loader = context.getClassLoader();
        if (injecting != null)
            return;
        synchronized (AopLoadClass.class) {
//            if (wrapping == null)
//                wrapping = new AndroidClassLoadingStrategy.Wrapping(context.getDir("wrapping", Context.MODE_PRIVATE));
            if (injecting == null)
                injecting = new AndroidClassLoadingStrategy.Injecting(context.getDir("injecting", Context.MODE_PRIVATE));
        }
    }

    private synchronized static void createContext() {
        if (context != null)
            return;
        synchronized (AopLoadClass.class) {
            if (context != null)
                return;
            try {
                Class activityThreadClass = Class.forName("android.app.ActivityThread");
                Method currentApplication = activityThreadClass.getDeclaredMethod("currentApplication");
                context = (Context) currentApplication.invoke(null);
                if (context != null)
                    return;
                Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
                if (!sCurrentActivityThreadField.isAccessible())
                    sCurrentActivityThreadField.setAccessible(true);
                Object activityThread = sCurrentActivityThreadField.get(null);
                Field mInitialApplicationField = activityThreadClass.getDeclaredField("mInitialApplication");
                if (!mInitialApplicationField.isAccessible())
                    mInitialApplicationField.setAccessible(true);
                context = (Application) mInitialApplicationField.get(activityThread);
                if (context != null)
                    return;
                context = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    static Class loadActivity(Class parent, String[] methods) {
        return loadPrimary(parent, "Activity", methods);
    }


    static Class loadService(Class parent, String[] methods) {
        return loadPrimary(parent, "Service", methods);
    }


    static Class loadReceiver(Class parent, String[] methods) {
        return loadPrimary(parent, "Receiver", methods);
    }


    static Class loadOrdinary(Class parent, String[] methods) {
        init();
        return createClass(parent, String.format("%s$XH", parent.getName()), methods, injecting);
    }

    static Class loadClass(Class parent, String name, String[] methods) {
        init();
        return createClass(parent, name, methods, injecting);
    }

    static Class loadOrdinary(Class[] implement, String[] methods) {
        init();
        return createClass(Object.class, String.format("%s$XH", implement[0].getName()), methods, injecting);
    }

    static Class loadClass(Class[] implement, String name, String[] methods) {
        init();
        return createClass(Object.class, implement, name, methods, injecting);
    }


    private static Class loadPrimary(Class parent, String name, String[] methods) {
        init();
        String packageName = context.getPackageName();
        return createClass(parent, String.format("%s.%s%s", packageName, parent.getSimpleName(), name), methods, injecting);
    }

    private static Class createClass(Class parent, String name, String[] methods, ClassLoadingStrategy strategy) {
        return createClass(parent, null, name, methods, strategy);
    }

    private static Class createClass(Class parent, Class[] implement, String name, String[] methods, ClassLoadingStrategy strategy) {
//        if (nameMap.containsKey(name))
//            return nameMap.get(name);
        try {
            return loader.loadClass(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DynamicType.Builder builder = new ByteBuddy().subclass(parent);
        if (implement != null && implement.length > 0) {
            for (Class clazz : implement)
                builder = builder.implement(clazz);
        }
        if (methods != null && methods.length > 0) {
            SingerAgentInterceptor interceptor = new SingerAgentInterceptor();
            for (String method : methods)
                builder = builder.method(ElementMatchers.named(method))
                        .intercept(MethodDelegation.to(interceptor));
        }
        Class clazz = builder.name(name).make().load(loader, strategy).getLoaded();
//        nameMap.put(name, clazz);
        return clazz;
    }

}
