package ru.kochubey17;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;

public class Starter {
    @SneakyThrows
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("ru.kochubey17");
        System.out.println(ctx.getBean("helloBean"));

        Predicate<Integer> pr = ctx.getBean("checkDiap", Predicate.class);
        System.out.println(pr.test(1));

        System.out.println(ctx.getBean("miniMax"));
        System.out.println(ctx.getBean("miniMax"));
        System.out.println(ctx.getBean("miniMax"));

        TrafficLight tf = ctx.getBean(TrafficLight.class);
        tf.on();
        tf.next();
        tf.next();
        tf.off();
    }
}

// 8.1:
@Configuration
class ConfigClass {
    @Bean
    String helloBean() {
        return "Hello world";
    }

    // от 0 до 99
    @Bean
    @Scope("prototype")
    int intRandom() {
        return new Random().nextInt(100);
    }

    @Bean
    @Lazy
    Date currentDate() {
        return Date.from(Instant.now());
    }

    @Bean
    Predicate<Integer> checkDiap() {
        return x -> (x >= 2) && (x <= 5);
    }

    @Bean
    int min() {
        return -1;
    }

    @Bean
    int max() {
        return 3;
    }
}

// 8.2.1
@Configuration
class RandomConfig {
    private List<Integer> values = new ArrayList<>();

    @Bean
    @Scope("prototype")
    int miniMax(@Qualifier("min") int min, @Qualifier("max") int max) {
        int size = values.size();
        if (size == 0) {
            fillValues(min, max);
            size = values.size();
        }
        Random random = new Random();
        int index = random.nextInt(0, size);
        int val = values.get(index);
        values.remove(index);
        return val;
    }

    void fillValues(int min, int max) {
        for (int i = min; i <= max; i ++) {
            values.add(i);
        }
    }
}

// 8.2.7
interface Color {
    Color next();
}

@Component("TrafficLight")
class TrafficLight {
    @Autowired
    @Qualifier("Red")
    private Color base;

    private Color current;

    @Autowired
    @Qualifier("Black")
    private Color waiting;

    void on() {
        current = base;
    }
    void next() throws Exception {
        if (current == null) {
            throw new Exception("Traffic light is off");
        }
        System.out.println(current);
        current = current.next();
    }
    void off() {
        current = waiting;
    }
}

@Qualifier("Red")
@Component
class Red implements Color {
    @Autowired
    @Qualifier("YellowRed")
    Color next;
    public Color next() {
        return next;
    }

    @Override
    public String toString() {
        return "RED";
    }
}

@Qualifier("YellowRed")
@Component
class YellowRed implements Color {
    @Autowired
    @Qualifier("GreenYellow")
    Color next;
    public Color next() {
        return next;
    }

    @Override
    public String toString() {
        return "YELLOW";
    }
}

@Qualifier("GreenYellow")
@Component
class GreenYellow implements Color {
    @Autowired
    @Qualifier("YellowGreen")
    Color next;
    public Color next() {
        return next;
    }
    @Override
    public String toString() {
        return "GREEN";
    }
}

@Qualifier("YellowGreen")
@Component
class YellowGreen implements Color {
    @Autowired
    @Qualifier("Red")
    Color next;
    public Color next() {
        return next;
    }
    @Override
    public String toString() {
        return "YELLOW";
    }
}

@Qualifier("Black")
@Component
class Black implements Color {
    public Color next() {
        return this;
    }
    @Override
    public String toString() {
        return "BLACK";
    }
}
