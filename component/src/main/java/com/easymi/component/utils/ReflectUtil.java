package com.easymi.component.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by xyin on 2017/2/23.
 * 反射工具类.
 */

public class ReflectUtil {

    /**
     * 获取类名上泛型参数的实例.
     *
     * @param o   类的实例对象
     * @param i   需要获取泛型在参数列表中的index
     * @param <T> 实际需要返回的类型
     * @return 返回需要泛型的实例对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getT(Object o, int i) {
        //ParameterizedType 表示参数化类型
        //Type getGenericSuperclass(),返回本类的父类,包含泛型参数信息
        Type t = o.getClass().getGenericSuperclass();
        if (!(t instanceof ParameterizedType)) {
            return null;
        }
        ParameterizedType pt = (ParameterizedType) t;
        Type[] types = pt.getActualTypeArguments();
        Type type = types[i]; //获取i位置泛型参数类型
        try {
            return ((Class<T>) type).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过类名获取生成类对象.
     *
     * @param className 类名
     * @return 对象
     */
    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
