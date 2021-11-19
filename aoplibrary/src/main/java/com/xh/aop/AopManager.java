package com.xh.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class AopManager {
    public static Class loadActivity(Class parent, String[] methods) {
        return AopLoadClass.loadActivity(parent, methods);
    }


    public static Class loadService(Class parent, String[] methods) {
        return AopLoadClass.loadService(parent, methods);
    }


    public static Class loadReceiver(Class parent, String[] methods) {
        return AopLoadClass.loadReceiver(parent, methods);
    }


    public static Class loadOrdinary(Class parent, String[] methods) {
        return AopLoadClass.loadClass(parent, methods);
    }

    public static Class loadClass(Class parent, String name, String[] methods) {
        return AopLoadClass.loadClass(parent, name, methods);
    }


    public static String[] implement2method(Class[] implement) {
        List<Method> list = new ArrayList();
        for (Class clazz : implement) {
            list.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        }
        Set<String> methods = new HashSet<>();
        list.stream().map((method) -> method.getName()).forEach((name) -> methods.add(name));
        return methods.toArray(new String[methods.size()]);
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
