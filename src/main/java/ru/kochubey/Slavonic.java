package ru.kochubey;

@Validate(SlavonicTest.class)
public class Slavonic extends Human{
    Tribe tribe;
    public Slavonic(String name, int age, int height, Tribe tribe) {
        super(name, age, height);
        this.tribe = tribe;
    }
}

class SlavonicTest {
    public SlavonicTest(){}
    void testName(Slavonic slav) throws Exception {
        if (slav.name == null)
            throw new Exception("testName: нет имени");
    }
    void testAge(Slavonic slav) throws Exception {
        if ((slav.age < 0) || (slav.age > 100))
            throw new Exception("testAge: неправдоподобный возраст");
    }
    void testHeight(Slavonic slav) throws Exception {
        if ((slav.height < 40) || (slav.height > 300))
            throw new Exception("testHeight: ненастоящий рост");
    }
    void checkTribe(Slavonic slav) throws Exception {
        if ((slav.tribe == Tribe.Francs) || (slav.tribe == Tribe.Balts) || (slav.tribe == Tribe.Turks) || (slav.tribe == Tribe.Saxs))
            throw new Exception("checkTribe: неправильное племя");
    }
}