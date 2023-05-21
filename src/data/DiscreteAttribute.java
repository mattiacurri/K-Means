package data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

class DiscreteAttribute extends Attribute implements Comparable<DiscreteAttribute>, Iterable<String> {
    TreeSet<String> values;

    DiscreteAttribute(String name, int index, String[] values) {
        super(name, index);
        this.values = new TreeSet<String>();
        this.values.addAll(Arrays.asList(values));
    }

    public Iterator<String> iterator() {
        return this.values.iterator();
    }

    int getNumberOfDistinctValues() {
        return values.size();
    }

    public int compareTo(DiscreteAttribute o) {
        return this.getName().compareTo(o.getName());
    }

    int frequency(Data data, Set<Integer> idList, String v) {
        /* Determina il numero di volte che il valore v compare
        in corrispondenza dell'attributo corrente (che funge da indice di colonna) negli
        esempi memorizzati in data e indicizzate (per riga) da idList */
        int count = 0;
        for (int i: idList) {
            if (data.getAttributeValue(i,this.getIndex()).equals(v))
                count++;
        }
        return count;
    }
}
