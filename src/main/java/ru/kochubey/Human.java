package ru.kochubey;

import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Human {
    @Default
    @Setter @Getter
    String name;

    @Default
    @Setter @Getter
    int age;

    @Setter @Getter
    int height;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Human human = (Human) o;
        return age == human.age && height == human.height && Objects.equals(name, human.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, height);
    }
}
