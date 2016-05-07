/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.HashSet;
import org.apache.commons.lang3.ArrayUtils;

/**
 * A SteinerTree represented as a graph which consists of all edges from
 * nodeI[n] to nodeJ[n].
 *
 * @author Lars Oetermann <lars.oetermann.com>
 */
public class SteinerTree {

    private final int[] nodeI, nodeJ;
    private final float[] weights;
    private final float totalWeight;
    private final int nodeCount;

    /**
     * A new SteinerTree.
     *
     * @param nodeCount node count
     * @param nodeI From nodes
     * @param nodeJ To nodes
     * @param weights weights
     * @param totalWeight sum of all weights
     */
    public SteinerTree(int nodeCount, int[] nodeI, int[] nodeJ, float[] weights, float totalWeight) {
        this.nodeCount = nodeCount;
        this.nodeI = ArrayUtils.addAll(nodeI, nodeJ);
        this.nodeJ = ArrayUtils.addAll(nodeJ, nodeI);
        this.weights = ArrayUtils.addAll(weights, weights);
        this.totalWeight = 2*totalWeight;
    }

    public int[] getNodeI() {
        return nodeI;
    }

    public int[] getNodeJ() {
        return nodeJ;
    }

    public float[] getWeights() {
        return weights;
    }

    /**
     * Get total weight of graph
     * @param directed Count double edges twice or not.
     * @return total weight of the graph
     */
    public float getTotalWeight(boolean directed) {
        if (directed)
        {
            return totalWeight;
        }
        else
        {
            return totalWeight / 2;
        }
    }

    public int getNodeCount() {
        return nodeCount;
    }
    
    public AdjacencyList toGraph() {
        return new AdjacencyList(nodeCount, weights.length, nodeI, nodeJ, weights);
    }
    
    /**
     * Returns all nodes in this graph.
     *
     * @return int[] of all nodes
     */
    public int[] getConnectedNodes() {
        HashSet<Integer> connected = new HashSet<>();
        for (int i : nodeI) {
            connected.add(i);
        }
        for (int j : nodeJ) {
            connected.add(j);
        }
        return connected.stream().mapToInt(Integer::intValue).toArray();

    }
    
    public String toString() {
        String res = "SteinerTree";
        for (int i=0; i<nodeI.length; i++) {
            res += "START\n";
            res += nodeI[i] + " ";
            res += nodeJ[i] + " ";
            res += weights[i] + " ";
        }
        return res;
    }
}
