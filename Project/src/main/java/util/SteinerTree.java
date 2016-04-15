/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

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

    /**
     * A new SteinerTree.
     * 
     * @param nodeI From nodes
     * @param nodeJ To nodes
     * @param weights weights
     * @param totalWeight sum of all weights
     */
    public SteinerTree(int[] nodeI, int[] nodeJ, float[] weights, float totalWeight) {
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

}
