package ru.kochtest;

import junit.framework.AssertionFailedError;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;
import ru.kochubey.*;

public class ClassTester {
    // 7.3.2
    @Test
    @SneakyThrows
    public void testReset() {
        Human me = new Human("Mikhail", 23, 187);
        Human meReseted = new Human("default", 1, 187);
        Utils.reset(me);
        Assert.assertEquals(me, meReseted);
    }

    // 7.3.4
    @Test
    @SneakyThrows
    public void testValidate() {
        Slavonic slav = new Slavonic("Mikhail", 23, 187, Tribe.Turks);
        Assert.assertEquals(1, Utils.validate(slav)); // 1 при ошибке, 0 при ее отсутствии
    }

    // 7.3.6
    @Test
    @SneakyThrows
    public void testCache() {
        Cat cat1 = new Cat(10, 2);
        Animal a1 = Utils.cache(cat1);
        cat1.whoIsIt();
        Assert.assertEquals(cat1, a1); // java.lang.AssertionError: expected: ru.kochubey.Cat<ru.kochubey.Cat@5a01ccaa> but was: jdk.proxy2.$Proxy7<ru.kochubey.Cat@5a01ccaa>
        //Expected :ru.kochubey.Cat<ru.kochubey.Cat@5a01ccaa>
        //Actual   :jdk.proxy2.$Proxy7<ru.kochubey.Cat@5a01ccaa>
    }
}