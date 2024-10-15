package ru.kochubey17;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

// 8.3.2
public class Maker {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("ru.kochubey17");
        MyBean myBean = ctx.getBean(MyBean.class);
        System.out.println(myBean);
    }
}

@Component
@ToString
class MyBean {
    @Default("defaultStringBean")
    String myString;

    @Default("defaultIntBean")
    Integer myInteger;
}

@Component
class BeanConfig {
    @Bean(name = "defaultStringBean")
    String helloWorld() {
        return "Hello world";
    }

    @Bean(name = "defaultIntBean")
    Integer defIntValue() {
        return 1000;
    }
}

@Getter
class DefaultUtils {
    private static ApplicationContext ctx;
    public static void setApplicationContext(ApplicationContext context) {
        ctx = context;
    }

    public static void reset(Object bean) throws IllegalAccessException {
        Class<?> c = bean.getClass();
        if (c.isAnnotationPresent(Default.class)) {             // если аннотирован класс
            Default defaultAnnotation = c.getAnnotation(Default.class);
            String beanName = defaultAnnotation.value();
            Object defaultBean = ctx.getBean(beanName);
            for (Field field : c.getDeclaredFields()) {
                field.setAccessible(true);
                field.set(bean, field.get(defaultBean));
            }
        } else {
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(Default.class)) { // если аннотировано поле
                    Default defaultAnnotation = field.getAnnotation(Default.class);
                    String beanName = defaultAnnotation.value();
                    Object defaultBean = ctx.getBean(beanName);
                    field.setAccessible(true);
                    field.set(bean, defaultBean);
                }
            }
        }
    }
}

// 8.3.2
// теперь @Default содержит строку - имя бина
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@interface Default {
    String value();
}

@Component
class MyBeanPostProcessor implements BeanPostProcessor {
    public MyBeanPostProcessor(ApplicationContext ctx) {
        DefaultUtils.setApplicationContext(ctx);
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        try {
            DefaultUtils.reset(bean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
