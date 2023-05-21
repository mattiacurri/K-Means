package data;

class ContinuousItem extends Item {
  ContinuousItem(Attribute attribute, Double value) {
    super(attribute, value);
  }

  double distance(Object a) {
    ContinuousAttribute attribute = (ContinuousAttribute) this.getAttribute();
    return Math.abs(attribute.getScaledValue((Double) this.getValue()) - attribute.getScaledValue((Double) a));
  }

}
