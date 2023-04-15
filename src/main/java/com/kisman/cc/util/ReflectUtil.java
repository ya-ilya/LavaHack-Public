package com.kisman.cc.util;

public final class ReflectUtil extends SecurityManager {

    private ReflectUtil(){
    }

    private final static ReflectUtil instance;

    private static Class<?>[] stack;

    static {
        instance = new ReflectUtil();
        stack = instance.getClassContext();
    }

    public static Class<?> getCallerClass(){
        stack = instance.getClassContext();
        if(stack.length < 3)
            return null;
        return stack[2];
    }
}
