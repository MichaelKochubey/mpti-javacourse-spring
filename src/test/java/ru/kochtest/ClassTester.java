package ru.kochtest;

import junit.framework.AssertionFailedError;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;
import ru.kochubey.Human;
import ru.kochubey.Utils;

public class ClassTester {
    @Test
    public void testName() {
        Human me = new Human("Mikhail", 23, 187);
        String res = me.toString();
        if (!res.contains("Mikhail")) throw new AssertionFailedError("toString must contain name");
    }

    @Test
    @SneakyThrows
    public void testReset() {
        Human me = new Human("Mikhail", 23, 187);
        Human meReseted = new Human("default", 1, 187);
        Utils.reset(me);
        Assert.assertEquals(me, meReseted);
    }
}