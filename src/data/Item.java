package data;


import java.io.Serializable;
import java.util.Set;

public abstract class Item implements Serializable {
    Attribute attribute;
    Object value;

    Item(Attribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    abstract double distance(Object a);

    public void update(Data data, Set<Integer> clusteredData) {
        this.value = data.computePrototype(clusteredData, this.attribute);
    }
}
