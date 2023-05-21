package data;

import data.Attribute;

public class ContinuousAttribute extends Attribute {
    double max;
    double min;

    ContinuousAttribute(String name, int index, double min, double max) {
        super(name, index);
        this.max = max;
        this.min = min;
    }

    public Double getScaledValue(double v) {
        return (v - min) / (max - min);
    }
}
