package pojos;

import Annotations.Entity;
import Annotations.Value;
@Entity
public class Table {
    @Value(1)
    private int height;
    @Value(1)
    private int square;

    public int getHeight() {
        return height;
    }

    @Value(5)
    public void setHeight(int height) {
        this.height = height;
    }

    public int getSquare() {
        return square;
    }

    @Value(5)
    public void setSquare(int square) {
        this.square = square;
    }

    @Override
    public String toString() {
        return "Table{" +
                "height=" + height +
                ", square=" + square +
                '}';
    }
}
