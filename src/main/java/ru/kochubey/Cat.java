package ru.kochubey;

import lombok.Getter;

// 7.3.6
@Getter
public class Cat implements Animal {
    private int weight = 1;
    private int age = 0;

    public Cat(int weight, int age) {
        this.weight = weight;
        this.age = age;
    }

    @Mutator
    public void eat() {
        this.weight += 1;
    }

    @Cache
    public void whoIsIt() {
        System.out.println("this cat is " + this.weight + "kg and " + this.age + " years old");
    }

    public boolean equals(Cat other) {
        return this.age == other.getAge() && this.weight == other.getWeight();
    }
}
