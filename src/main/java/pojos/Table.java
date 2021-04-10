package pojos;

import Annotations.Entity;
import Annotations.Value;
@Entity
public class Table {
    @Value("1")
    private int height;
    @Value("1")
    private String material;

    public int getHeight() {
        return height;
    }

    @Value("5")
    public void setHeight(int height) {
        this.height = height;
    }

    public String getMaterial() {
        return material;
    }

    @Value("5")
    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return "Table{" +
                "height=" + height +
                ", square=" + material +
                '}';
    }
}
