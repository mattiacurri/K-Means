package data;

import java.io.Serializable;

abstract class Attribute implements Serializable {
    String name; // nome simbolico dell'attributo
    int index; // identificativo numerico dell'attributo

    Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    String getName() {
        return name;
    }

    int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
