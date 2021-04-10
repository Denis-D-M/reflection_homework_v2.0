package pojos;

import Annotations.Entity;
import Annotations.Value;

@Entity
public class Human {
    @Value(20)
    private int age;
    @Value(87)
    private int weight;

    public int getAge() {
        return age;
    }

    @Value(20)
    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    @Value(87)
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Human{" +
                "age=" + age +
                ", weight=" + weight +
                '}';
    }
}
