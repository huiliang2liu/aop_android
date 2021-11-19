package com.xh.aop;

import java.util.IllformedLocaleException;

public class AopManager {
    static Class loadActivity(Class parent, String[] methods) {
        return AopLoadClass.loadActivity(parent, methods);
    }


    static Class loadService(Class parent, String[] methods) {
        return AopLoadClass.loadService(parent, methods);
    }


    static Class loadReceiver(Class parent, String[] methods) {
        return AopLoadClass.loadReceiver(parent, methods);
    }


    static Class loadOrdinary(Class parent, String[] methods) {
        return AopLoadClass.loadOrdinary(parent, methods);
    }

    static Class loadClass(Class parent, String name, String[] methods) {
        return AopLoadClass.loadClass(parent, name, methods);
    }

    static Class loadOrdinary(Class[] implement, String[] methods) {
        return AopLoadClass.loadOrdinary(implement, methods);
    }

    static Class loadClass(Class[] implement, String name, String[] methods) {
        return AopLoadClass.loadClass(implement, name, methods);
    }


    public static <T> T createObject(Class<T> parent, String[] methods) throws IllegalAccessException, InstantiationException {
        return (T) createObject(loadOrdinary(parent, methods));
    }

    public static <T> T createObject(Class<T> parent, String name, String[] methods) throws IllegalAccessException, InstantiationException {
        return (T) createObject(loadClass(parent, name, methods));
    }

    private static Object createObject(Class clazz) throws IllegalAccessException, InstantiationException {
        return clazz.newInstance();
    }

}
