package com.epam.mishin.pojo;

import com.epam.mishin.annotation.Entity;
import com.epam.mishin.annotation.Value;

@Entity
public class Human {
    @Value(value = "10")
    private int age;
    @Value(value = "10")
    private String name;

    public int getAge() {
        return age;
    }

    @Value("20")
    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    @Value(value = "20", valuesTxtPath = "src\\main\\resources\\values.txt")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Human{" +
                "age=" + age +
                ", name=" + name +
                '}';
    }
}
