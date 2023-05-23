package mining;

import data.Data;
import data.OutOfRangeSampleSize;
import data.Tuple;

import java.io.Serializable;

public class ClusterSet implements Serializable {
    private Cluster[] C;
    private int i = 0;

    ClusterSet(int k) throws OutOfRangeSampleSize {
        if (k <= 0) {
            throw new OutOfRangeSampleSize("k deve essere maggiore di 0");
        }
        C = new Cluster[k];
    }

    void add(Cluster c) {
        C[i] = c;
        i++;
    }

    Cluster get(int i) {
        return C[i];
    }

    void initializeCentroids(Data data) throws OutOfRangeSampleSize {
        int[] centroidIndexes = data.sampling(C.length);
        for (int i = 0; i < centroidIndexes.length; i++) {
            Tuple centroidI = data.getItemSet(centroidIndexes[i]);
            add(new Cluster(centroidI));
        }
    }

    Cluster nearestCluster(Tuple tuple) {
        double min = tuple.getDistance(C[0].getCentroid());
        Cluster c = C[0];
        double tmp;
        for (int i = 1; i < C.length; i++) {
            tmp = tuple.getDistance(C[i].getCentroid());
            if (tmp < min) {
                min = tmp;
                c = C[i];
            }
        }
        return c;
    }

    Cluster currentCluster(int id) {
        for (Cluster cluster : C) {
            if (cluster.contain(id)) {
                return cluster;
            }
        }
        return null;
    }

    void updateCentroids(Data data) {
        for (Cluster cluster : C) {
            cluster.computeCentroid(data);
        }
    }

    public String toString() {
        String output = "";
        for (int i = 0; i < C.length; i++) {
            output += C[i].toString()+"\n";
        }
        return output;
    }

    public String toString(Data data) {
        String str = "";
        for (int i = 0; i < C.length; i++) {
            if (C[i] != null) {
                str += i + ":" + C[i].toString(data) + "\n";
            }
        }
        return str;
    }


}
