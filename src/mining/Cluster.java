package mining;

import data.Data;
import data.Tuple;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

class Cluster implements Serializable {
    private Tuple centroid;

    private Set<Integer> clusteredData;

    Cluster(Tuple centroid) {
        this.centroid = centroid;
        clusteredData = new HashSet<Integer>();
    }

    Tuple getCentroid() {
        return centroid;
    }

    void computeCentroid(Data data) {
        for (int i = 0; i < centroid.getLength(); i++) {
            centroid.get(i).update(data, clusteredData);
        }
    }

    //return true if the tuple is changing cluster
    boolean addData(int id) {
        return clusteredData.add(id);

    }

    //verifica se una transazione e' clusterizzata nell'array corrente
    boolean contain(int id) {
        return clusteredData.contains(id);
    }


    //remove the tuple that has changed the cluster
    void removeTuple(int id) {
        clusteredData.remove(id);
    }

    public String toString() {
        String str = "Centroid=(";
        for (int i = 0; i < centroid.getLength(); i++)
            str += centroid.get(i);
        str += ")";
        return str;

    }


    public String toString(Data data) {
        String str = "Centroid=(";
        for (int i = 0; i < centroid.getLength(); i++)
            str += centroid.get(i) + " ";
        str += ")\nExamples:\n";
        for (int i:clusteredData) {
            str += "[";
            for (int j = 0; j < data.getNumberOfAttributes(); j++)
                str += data.getAttributeValue((Integer) i, j) + " ";
            str += "] dist=" + getCentroid().getDistance(data.getItemSet(i)) + "\n";
        }
        str += "\nAvgDistance=" + getCentroid().avgDistance(data, clusteredData);
        return str;

    }

}
