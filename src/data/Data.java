package data;

import database.*;

import java.sql.SQLException;
import java.util.*;

public class Data {
  // Le visibilità di classi, attributi e metodi devono essere decise dagli studenti
  private final List<Example> data;
  private final int numberOfExamples;
  private final List<Attribute> attributeSet = new LinkedList<Attribute>();

  public Data() {

    TreeSet<Example> tempData = new TreeSet<Example>();
    Example ex0 = new Example();
    data = new ArrayList<Example>();
    ex0.add("sunny");
    ex0.add(37.5);
    ex0.add("high");
    ex0.add("weak");
    ex0.add("no");
    tempData.add(ex0);
    Example ex1 = new Example();
    ex1.add("sunny");
    ex1.add(38.7);
    ex1.add("high");
    ex1.add("strong");
    ex1.add("no");
    tempData.add(ex1);
    Example ex2 = new Example();
    ex2.add("overcast");
    ex2.add(37.5);
    ex2.add("high");
    ex2.add("weak");
    ex2.add("yes");
    tempData.add(ex2);
    Example ex3 = new Example();
    ex3.add("rain");
    ex3.add(20.5);
    ex3.add("high");
    ex3.add("weak");
    ex3.add("yes");
    tempData.add(ex3);
    Example ex4 = new Example();
    ex4.add("rain");
    ex4.add(20.7);
    ex4.add("normal");
    ex4.add("weak");
    ex4.add("yes");
    tempData.add(ex4);
    Example ex5 = new Example();
    ex5.add("rain");
    ex5.add(21.2);
    ex5.add("normal");
    ex5.add("strong");
    ex5.add("no");
    tempData.add(ex5);
    Example ex6 = new Example();
    ex6.add("overcast");
    ex6.add(20.5);
    ex6.add("normal");
    ex6.add("strong");
    ex6.add("yes");
    tempData.add(ex6);
    Example ex7 = new Example();
    ex7.add("sunny");
    ex7.add(21.2);
    ex7.add("high");
    ex7.add("weak");
    ex7.add("no");
    tempData.add(ex7);
    Example ex8 = new Example();
    ex8.add("sunny");
    ex8.add(21.2);
    ex8.add("normal");
    ex8.add("weak");
    ex8.add("yes");
    tempData.add(ex8);
    Example ex9 = new Example();
    ex9.add("rain");
    ex9.add(19.8);
    ex9.add("normal");
    ex9.add("weak");
    ex9.add("yes");
    tempData.add(ex9);
    Example ex10 = new Example();
    ex10.add("sunny");
    ex10.add(3.5);
    ex10.add("normal");
    ex10.add("strong");
    ex10.add("yes");
    tempData.add(ex10);
    Example ex11 = new Example();
    ex11.add("overcast");
    ex11.add(3.6);
    ex11.add("high");
    ex11.add("strong");
    ex11.add("yes");
    tempData.add(ex11);
    Example ex12 = new Example();
    ex12.add("overcast");
    ex12.add(3.5);
    ex12.add("normal");
    ex12.add("weak");
    ex12.add("yes");
    tempData.add(ex12);
    Example ex13 = new Example();
    ex13.add("rain");
    ex13.add(3.2);
    ex13.add("high");
    ex13.add("strong");
    ex13.add("no");
    tempData.add(ex13);
    data.addAll(tempData);
    numberOfExamples = data.size();

    String[] outLookValues = new String[3];
    outLookValues[0] = "overcast";
    outLookValues[1] = "rain";
    outLookValues[2] = "sunny";
    attributeSet.add(new DiscreteAttribute("Outlook", 0, outLookValues));
    attributeSet.add(new ContinuousAttribute("Temperature", 1, 3.2, 38.7));
    String[] humidityValues = new String[2];
    humidityValues[0] = "high";
    humidityValues[1] = "normal";
    attributeSet.add(new DiscreteAttribute("Humidity", 2, humidityValues));
    String[] windValues = new String[2];
    windValues[0] = "strong";
    windValues[1] = "weak";
    attributeSet.add(new DiscreteAttribute("Wind", 3, windValues));
    String[] playTennisValues = new String[2];
    playTennisValues[0] = "no";
    playTennisValues[1] = "yes";
    attributeSet.add(new DiscreteAttribute("Playtennis", 4, playTennisValues));

  }

  public Data(String server, String database, int port, String user_id, String pw, String table) throws DatabaseConnectionException, SQLException, NoValueException, EmptySetException {
    DbAccess db = new DbAccess(server, database, port, user_id, pw);
    db.initConnection();
    TableData tableData = new TableData(db);
    TableSchema tableSchema = new TableSchema(db, table);
    this.data = tableData.getDistinctTransazioni(table);
    this.numberOfExamples = tableData.getDistinctTransazioni(table).size();
    for (int i = 0; i < tableSchema.getNumberOfAttributes(); i++) {
      TableSchema.Column column = tableSchema.getColumn(i);
      if (column.isNumber()) {
        double minValue = (double) tableData.getAggregateColumnValue(table, column, QUERY_TYPE.MIN);
        double maxValue = (double) tableData.getAggregateColumnValue(table, column, QUERY_TYPE.MAX);
        attributeSet.add(new ContinuousAttribute(column.getColumnName(), i, minValue, maxValue));
      } else {
        String[] values = new String[tableData.getDistinctColumnValues(table, column).size()];
        tableData.getDistinctColumnValues(table,column).toArray(values);
        attributeSet.add(new DiscreteAttribute(column.getColumnName(), i, values));
      }
    }
  }

  public int getNumberOfExamples() {
    return numberOfExamples;
  }

  public int getNumberOfAttributes() {
    return attributeSet.size();
  }

  public Object getAttributeValue(int exampleIndex, int attributeIndex) {
    return data.get(exampleIndex).get(attributeIndex);
  }

  Attribute getAttribute(int index) {
    return attributeSet.get(index);
  }

  public String toString() {
    String s = "";
    for (int i = 0; i < attributeSet.size(); i++) {
      s += attributeSet.get(i).getName() + (i == attributeSet.size() - 1 ? "\n" : ",");
    }
    for (int i = 0; i < numberOfExamples; i++) {
      s += i + 1 + ": " + data.get(i).toString() + "\n";
    }
    return s;
  }

  public Tuple getItemSet(int index) {
    Tuple tuple = new Tuple(attributeSet.size());
    for (int i = 0; i < attributeSet.size(); i++) {
      if (attributeSet.get(i) instanceof ContinuousAttribute) {
        tuple.add(new ContinuousItem(attributeSet.get(i), (Double)
            data.get(index).get(attributeSet.get(i).getIndex())), i);
      } else if (attributeSet.get(i) instanceof DiscreteAttribute) {
        tuple.add(new DiscreteItem((DiscreteAttribute) attributeSet.get(i), (String)
            data.get(index).get(attributeSet.get(i).getIndex())), i);
      }
    }
    return tuple;
  }

  private boolean compare(int i, int j) {
    for (int k = 0; k < attributeSet.size(); k++) {
      if (!(this.data.get(i).get(k).equals(data.get(j).get(k)))) {
        return false;
      }
    }
    return true;
  }

  public int[] sampling(int k) throws OutOfRangeSampleSize {
    if (k < 0 || k > this.data.size()) {
      throw new OutOfRangeSampleSize("Il numero inserito è minore di 0 oppure maggiore di " + this.data.size());
    }
    int[] centroidIndexes = new int[k];
    //choose k random different centroids in data.
    Random rand = new Random();
    rand.setSeed(System.currentTimeMillis());
    for (int i = 0; i < k; i++) {
      boolean found = false;
      int c;
      do {
        found = false;
        c = rand.nextInt(getNumberOfExamples());
        // verify that centroid[c] is not equal to a centroide already stored in CentroidIndexes
        for (int j = 0; j < i; j++)
          if (compare(centroidIndexes[j], c)) {
            found = true;
            break;
          }
      }
      while (found);
      centroidIndexes[i] = c;
    }
    return centroidIndexes;
  }

  Double computePrototype(Set<Integer> idList, ContinuousAttribute attribute) {
    double sum = 0;
    for (Integer i : idList) {
      sum += (Double) data.get(i).get(attribute.getIndex());
    }
    return sum / idList.size();
  }

  String computePrototype(Set<Integer> idList, DiscreteAttribute attribute) {
    Iterator<String> it = attribute.iterator();
    String first = it.next();
    int max = attribute.frequency(this, idList, first);
    int tmp;
    String prototype = first;
    String tmp_string;
    while (it.hasNext()) {
      tmp_string = it.next();
      tmp = attribute.frequency(this, idList, tmp_string);
      if (tmp > max) {
        max = tmp;
        prototype = tmp_string;
      }
    }
    return prototype;
  }

  public Object computePrototype(Set<Integer> idList, Attribute attribute) {
    if (attribute instanceof ContinuousAttribute) {
      return computePrototype(idList, (ContinuousAttribute) attribute);
    }
    if (attribute instanceof DiscreteAttribute) {
      return computePrototype(idList, (DiscreteAttribute) attribute);
    }
    return null;
  }

}
