/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.HashSet;

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
        this.nodeI = nodeI;
        this.nodeJ = nodeJ;
        this.weights = weights;
        this.totalWeight = totalWeight;
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

    public float getTotalWeight() {
        return totalWeight;
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
}
