/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
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

    /**
     * Creates an AdjacencyList (also known as a Graph) from this SteinerTree.
     * This is needed for the prize collecting algorithm.
     *
     * @param dijkstras all-to-all dijkstras
     * @return The graph, in form of an AdjacencyList.
     */
    public AdjacencyList toGraph(Dijkstra[] dijkstras) {
        /* create and clear lists for new weights, nodeI and nodeJ */
        ArrayList<Integer> newNodeI = new ArrayList<>();
        ArrayList<Integer> newNodeJ = new ArrayList<>();
        ArrayList<Float> newWeights = new ArrayList<>();

        newNodeI.clear();
        newNodeJ.clear();
        newWeights.clear();

        /* Every edge in this.nodeI/this.nodeJ represents a path in the
           original Graph. We need to add all edges from that original 
           graph plus their according weights to a new nodeI and nodeJ */
        for (int i = 0; i < nodeI.length; i++) {
            /* One direction (hurr hurr) */

 /* get complete arc from Dijkstra */
            int[] arc = dijkstras[nodeI[i]].getNodesOfShortestPathTo(nodeJ[i]);

            int source, target;

            /* Step by step add all edges along arc and corresponding weights */
            for (int j = 0; j < (arc.length - 1); j++) {
                source = arc[j];
                target = arc[j + 1];

                newNodeI.add(source);
                newNodeJ.add(target);
                newWeights.add(dijkstras[source].getDistanceTo(target));
            }

            /* Other direction */
            arc = dijkstras[nodeJ[i]].getNodesOfShortestPathTo(nodeI[i]);

            for (int j = 0; j < (arc.length - 1); j++) {
                source = arc[j];
                target = arc[j + 1];

                newNodeI.add(source);
                newNodeJ.add(target);
                newWeights.add(dijkstras[source].getDistanceTo(target));
            }
        }

        /* Add all nodes to set to avoid duplicates */
        HashSet<Integer> nodes = new HashSet<>();

        for (int i = 0; i < newNodeI.size(); i++) {
            nodes.add(newNodeI.get(i));
            nodes.add(newNodeJ.get(i));
        }

        /* Copy manually because Java can automatically convert
           Integer to int but apparently not Integer[] to int[]... */
        int[] nuI = new int[newNodeI.size()];
        int[] nuJ = new int[newNodeJ.size()];

        for (int i = 0; i < newNodeI.size(); i++) {
            nuI[i] = newNodeI.get(i);
            nuJ[i] = newNodeJ.get(i);
        }

        /* Same for weights */
        float[] nuWeights = new float[newWeights.size()];
        for (int i = 0; i < nuWeights.length; i++) {
            nuWeights[i] = newWeights.get(i);
        }

        /* get Counts from obvious sources */
        int nodeCount = nodes.size();
        int edgeCount = newWeights.size();

        /* Create Graph (that is to say "AdjacencyList") from data */
        AdjacencyList graph = new AdjacencyList(nodeCount,
                edgeCount,
                nuI,
                nuJ,
                nuWeights);

        /* return freshly created graph */
        return graph;
    }

}
