package pojos;

import Annotations.Entity;
import Annotations.Value;

@Entity
public class Human {
    @Value(value = "20")
    private int age;
    @Value(value = "20")
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
