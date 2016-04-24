/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Lars Oetermann <lars.oetermann.com>
 */
public class SteinerTree {

    private int[] nodeI, nodeJ;
    private float[] weights;
    private float totalWeight;

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

    /**
     * Creates an AdjacencyList (also known as a Graph) from this SteinerTree.
     * This is needed for the prize collecting algorithm.
     * @return The graph, in form of an AdjacencyList.
     */
    public AdjacencyList toGraph()
    {
        // TODO: INSERT NODES & EDGES FROM ORIGINAL GRAPH (not correct yet)
        
        /* Add all nodes to set to compute nodecount. Sufficiently efficient? */
        Set<Integer> nodes = new HashSet<Integer>();
        for (int i = 0; i < nodeI.length; i++)
        {
            /* nodeI and nodeJ ought to have the same length,
               so this shouldn't miss anything or misstep. */
            nodes.add(nodeI[i]);
            nodes.add(nodeJ[i]);
        }
        
        /* get counts from obvious sources */
        int nodeCount = nodes.size();
        int edgeCount = 0;
        
        /* Create Graph (that is to say "AdjacencyList") from data */
        AdjacencyList graph = new AdjacencyList(nodeCount,
                                                edgeCount,
                                                this.nodeI, 
                                                this.nodeJ,
                                                this.weights);
        
        /* return freshly created graph */
        return graph;
    }
    
}
