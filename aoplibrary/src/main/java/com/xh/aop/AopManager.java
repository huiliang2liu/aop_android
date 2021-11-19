package com.xh.aop;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AopManager {
    private static List<Delegation> delegations = new ArrayList<>();

    private static boolean debug = false;

    public static void setDebug(boolean debug) {
        AopManager.debug = debug;
    }

    public static void setConfig(String config) {
        if (config == null || config.isEmpty())
            return;
        try {
            JSONArray array = new JSONArray(config);
            int i = 0;
            int len = array.length();
            for (; i < len; i++) {
                Delegation delegation = new Delegation();
                if (!delegation.parse(array.optJSONObject(i)))
                    continue;
                delegations.add(delegation);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

    public static Class loadClass(Class parent) {
        if (!debug)
            return parent;
        Delegation delegation = new Delegation();
        delegation.clazz = parent;
        int index = delegations.indexOf(delegation);
        if (index < 0)
            return parent;
        delegation = delegations.get(index);
        if (delegation.type == 0) {
            if (delegation.name == null || delegation.name.isEmpty())
                return loadOrdinary(parent, delegation.methods);
            return loadClass(parent, delegation.name, delegation.methods);
        }
        if (delegation.type == 1)
            return loadActivity(parent, delegation.methods);
        if (delegation.type == 2)
            return loadService(parent, delegation.methods);
        return loadReceiver(parent, delegation.methods);
    }

    public static <T> T createObject(Class<T> parent) throws IllegalAccessException, InstantiationException {
        return (T) newInstance(loadClass(parent));
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
        return (T) newInstance(loadOrdinary(parent, methods));
    }

    public static <T> T createObject(Class<T> parent, String name, String[] methods) throws IllegalAccessException, InstantiationException {
        return (T) newInstance(loadClass(parent, name, methods));
    }

    private static Object newInstance(Class clazz) throws IllegalAccessException, InstantiationException {
        return clazz.newInstance();
    }

    private static class Delegation {
        String[] methods;
        Class clazz;
        String name;
        int type;// 0 普通 1 activity 2 service 3 receiver

        private boolean parse(JSONObject object) {
            String className = object.optString("class");
            name = object.optString("name", null);
            type = object.optInt("type", 0);
            if (className == null || className.isEmpty())
                return false;
            JSONArray array = object.optJSONArray("methods");
            if (array == null || array.length() <= 0)
                return false;
            try {
                clazz = Class.forName(className);
            } catch (Exception e) {
                return false;
            }
            int len = array.length();
            int i = 0;
            List<String> list = new ArrayList<>(len);
            for (; i < len; i++) {
                list.add(array.optString(i));
            }
            methods = list.toArray(new String[len]);
            return true;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null)
                return false;
            if (!(o instanceof Delegation))
                return false;
            Class clazz = ((Delegation) o).clazz;
            return clazz.isAssignableFrom(this.clazz);
        }

        @Override
        public int hashCode() {
            return clazz.hashCode();
        }
    }

}
