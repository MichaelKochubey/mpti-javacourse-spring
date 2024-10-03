package ru.kochubey;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

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

    public static int validate(Object ...obj) throws Exception {
        for (Object o: obj) {
            if (!o.getClass().isAnnotationPresent(Validate.class)) continue;
            Validate v = o.getClass().getAnnotation(Validate.class);
            Class<?>[] testClasses = v.value();
            for (Class c : testClasses) {
                Object testObj = c.getConstructor().newInstance();
                for (Method method : c.getDeclaredMethods()) {
                    try {
                        method.invoke(testObj, o);
                    } catch (InvocationTargetException e) {
                        System.out.println(e.getTargetException().getMessage());
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    public static <T> T cache(T object) {
        return (T) Proxy.newProxyInstance(
                object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),
                new CacheHandler(object));
    }
}
// 7.3.2
@Retention(RetentionPolicy.RUNTIME)
@interface Default {}

// 7.3.4
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@interface Validate {
    Class<?>[] value();
}

// 7.3.6
class CacheHandler implements InvocationHandler {
    Map<Method, Object> cache = new HashMap<>();
    Object object;

    public CacheHandler(Object obj) {
        this.object = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("proxy!");
        if (proxy == null) return object;

        Method thisMethod = object.getClass().getMethod(method.getName(), method.getParameterTypes());
        if (thisMethod.isAnnotationPresent(Mutator.class)) cache.clear(); // т.к. объект был изменен
        if (!thisMethod.isAnnotationPresent(Cache.class)) return method.invoke(object, args);
        // если метод аннотирован, то нужно выяснить, вызывать ли его либо достать уже вычисленный результат
        if (cache.containsKey(method)) return cache.get(method);

        Object res = method.invoke(object, args);
        cache.put(method, res);
        return res;
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@interface Cache {}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@interface Mutator {}