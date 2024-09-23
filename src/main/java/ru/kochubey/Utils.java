package ru.kochubey;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

public class Utils {
    public static void getDefaultValue(Object o, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        if (field.getType() == String.class) field.set(o, "default");
        if (field.getType() == Integer.class) field.set(o, 1);
        if (field.getType() == int.class) field.set(o, 1);
        if (field.getType() == Double.class) field.set(o, 1.1);
        if (field.getType() == double.class) field.set(o, 1.1);
        if (field.getType() == Object.class) field.set(o, new Object());
    }

    public static void reset(Object ...obj) throws IllegalAccessException {
        for (Object o : obj) {
            Class<?> c = o.getClass();
            if (o.getClass().isAnnotationPresent(Default.class)) { // если аннотирован класс
                for (Field field : c.getDeclaredFields()) {
                    getDefaultValue(o, field);
                }
            } else {
                for (Field field : c.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Default.class)) { // если аннотировано поле
                        getDefaultValue(o, field);
                    }
                }
            }
        }
    }
}
@Retention(RetentionPolicy.RUNTIME)
@interface Default {}
