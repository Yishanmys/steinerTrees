/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author Lars Oetermann <lars.oetermann.com>
 */
public class AdjacencyList {

    private final int[] listStarts, adjacencyList;
    private final float[] adjacencyWeights;
    public final static int INIT_PARTLY_COUNTING_SORT = 0, INIT_PURE_COUNTING_SORT = 1, INIT_ARRAY_SORT = 2;

    /**
     * Creates a new AdjacencyList based on the following input with the given
     * initMethod.
     *
     * @param nodeCount numer of Nodes -> N
     * @param edgeCount numer of Edges -> M
     * @param nodeI represents an edge from nodeI[m] to nodeJ[m]
     * @param nodeJ represents an edge from nodeI[m] to nodeJ[m]
     * @param weights the weight of edge m
     */
    public AdjacencyList(int nodeCount, int edgeCount, int[] nodeI, int[] nodeJ, float[] weights) {
        this(nodeCount, edgeCount, nodeI, nodeJ, weights, edgeCount > nodeCount * nodeCount / 100 ? INIT_PURE_COUNTING_SORT : INIT_PARTLY_COUNTING_SORT);
    }

    /**
     * Creates a new AdjacencyList based on the following input with the given
     * initMethod.
     *
     * @param nodeCount numer of Nodes -> N
     * @param edgeCount numer of Edges -> M
     * @param nodeI represents an edge from nodeI[m] to nodeJ[m]
     * @param nodeJ represents an edge from nodeI[m] to nodeJ[m]
     * @param weights the weight of edge m
     * @param initMethod INIT_(PARTLY_COUNTING_SORT|PURE_COUNTING_SORT|ARRAY_SORT)
     */
    public AdjacencyList(int nodeCount, int edgeCount, int[] nodeI, int[] nodeJ, float[] weights, int initMethod) {
        adjacencyList = new int[edgeCount];
        listStarts = new int[nodeCount];
        adjacencyWeights = new float[edgeCount];
        switch (initMethod) {
            case INIT_PARTLY_COUNTING_SORT:
                initPartlyCountingSort(nodeCount, edgeCount, nodeI, nodeJ, weights);
                break;
            case INIT_PURE_COUNTING_SORT:
                initPureCountingSort(nodeCount, edgeCount, nodeI, nodeJ, weights);
            case INIT_ARRAY_SORT:
                initArraysSort(nodeCount, edgeCount, nodeI, nodeJ, weights);
                break;
        }
    }

    /**
     * Runs in O(M * log(M)) or O(M * M), depending of Java's sort
     * implementation. Very efficient if nodeI is sorted.
     *
     * @param nodeCount
     * @param edgeCount
     * @param nodeI
     * @param nodeJ
     * @param weights
     */
    private void initArraysSort(int nodeCount, int edgeCount, int[] nodeI, int[] nodeJ, float[] weights) {
        Integer[] indices = new Integer[edgeCount];
        for (int i = 0; i < edgeCount; i++) {
            indices[i] = i;
        }
//        Arrays.sort(indices, (Integer o1, Integer o2) -> nodeI[o1] == nodeI[o2] ? nodeJ[o2] - nodeJ[o1] : nodeI[o2] - nodeI[o1]);
        Arrays.sort(indices, new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return nodeI[o1] == nodeI[o2] ? nodeJ[o1] - nodeJ[o2] : nodeI[o1] - nodeI[o2];
            }
        });

        int currentNode = 0;
        listStarts[0] = 0;
        for (int i = 0; i < edgeCount; i++) {
            adjacencyList[i] = nodeJ[indices[i]];
            adjacencyWeights[i] = weights[indices[i]];
            while (nodeI[indices[i]] > currentNode) {
                currentNode++;
                listStarts[currentNode] = i;
            }
        }
        while (currentNode + 1 < nodeCount) {
            currentNode++;
            listStarts[currentNode] = edgeCount;
        }
    }

    /**
     * Runs in O(N * N * 2).
     *
     * @param nodeCount
     * @param edgeCount
     * @param nodeI
     * @param nodeJ
     * @param weights
     */
    private void initPureCountingSort(int nodeCount, int edgeCount, int[] nodeI, int[] nodeJ, float[] weights) {
        int[] possibleEdges = new int[nodeCount * nodeCount];
        for (int i = 0; i < edgeCount; i++) {
            possibleEdges[nodeI[i] * nodeCount + nodeJ[i]] = i + 1;
        }
        int cursor = 0;
        for (int i = 0; i < nodeCount; i++) {
            listStarts[i] = cursor;
            int in = i * nodeCount;
            for (int j = 0; j < nodeCount; j++) {
                if (possibleEdges[in + j] > 0) {
                    adjacencyList[cursor] = j;
                    adjacencyWeights[cursor] = weights[possibleEdges[in + j] - 1];
                    cursor++;
                }
            }
        }
    }

    /**
     * Runs in O(M + N * (M/N + M/N * log(M/N))) = O(N * N * log(N)).
     *
     * @param nodeCount
     * @param edgeCount
     * @param nodeI
     * @param nodeJ
     * @param weights
     */
    private void initPartlyCountingSort(int nodeCount, int edgeCount, int[] nodeI, int[] nodeJ, float[] weights) {

        List<Edge>[] adjacencyLists = new List[nodeCount];
        for (int i = 0; i < edgeCount; i++) {
            if (adjacencyLists[nodeI[i]] == null) {
                adjacencyLists[nodeI[i]] = new ArrayList<>();
            }
            adjacencyLists[nodeI[i]].add(new Edge(nodeJ[i], weights[i]));
        }

        // Sort each List and insert it into the Array. O(N * (N + N * log(N)))
        int currentStart = 0;
        for (int i = 0; i < nodeCount; i++) {
            listStarts[i] = currentStart;
            List<Edge> adjacencies = adjacencyLists[i];
            if (adjacencies != null) {
                // Sort the adjacencies for faster lookup. O(N * log(N))
                Collections.sort(adjacencies);
                // Copy the adjacencies from the List to the Array.O(N) 
                for (Edge adjacency : adjacencies) {
                    adjacencyList[currentStart] = adjacency.to;
                    adjacencyWeights[currentStart] = adjacency.weight;
                    currentStart++;
                }
            }
        }
    }

    /**
     * Store the toNode and the Weight of this edge. Only used on Initialization
     * for INIT_PARTLY_COUNTING_SORT.
     */
    private static class Edge implements Comparable<Edge> {

        private int to;
        private float weight;

        public Edge(int to, float weight) {
            this.to = to;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge o) {
            return to - o.to;
        }

    }
    
    
    // ---- Methods ----
   

    /**
     * Get the starting index of (Sub)AdjacencyList for Node n. This index might
     * be equal to length of the total AdjacencyList, so make sure that
     * getStartOf(n) is lower than getEndOf(n) before addressing the edge. Runs
     * in O(1);
     *
     * @param n the node
     * @return the starting index of the adjacencyList for the node
     */
    public int getStartOf(int n) {
        return listStarts[n];
    }
    
    /**
     * Get the index of (Sub)AdjacencyList for Node n+1. This index might be
     * equal to length of the total AdjacencyList, so make sure that
     * getStartOf(n) is lower than getEndOf(n) before addressing the edge. Runs
     * in O(3);
     *
     * @param n the node
     * @return the starting index of the adjacencyList for the node
     */
    public int getEndOf(int n) {
        return n + 1 < listStarts.length ? listStarts[n + 1] : adjacencyList.length;
    }

    /**
     * Get the index of the Node to which the given Edge directs. Runs in O(1).
     *
     * @param m the edge
     * @return the index
     */
    public int getToNode(int m) {
        return adjacencyList[m];
    }
    
    /**
     * Get the index of the Node from which the given Edge comes. Runs in O(log(N)), so use wisely.
     * 
     * @param m the edge
     * @return the index
     */
    public int getFromNode(int m) {
        int mindex = 0;
        int maxdex = listStarts.length-1;
        while(mindex < maxdex) {
            int index = maxdex+mindex/2;
            if (getStartOf(index) > m) {
                maxdex = index-1;
            } else if (getEndOf(index) <= m) {
                mindex = index+1;
            } else {
                return index;
            }
        }
        return mindex;
    }

    /**
     * Get the weight of Edge with index m. Runs in O(1);
     *
     * @param m the edge
     * @return the weight
     */
    public float getWeight(int m) {
        return adjacencyWeights[m];
    }

    /**
     * Get the Degree of Node n. The degree of a Node is the number of Edges
     * starting from this Node (including Edges to Self). Runs in O(1).
     *
     * @param n the node
     * @return the degree
     */
    public int getDegree(int n) {
        return getEndOf(n) - getStartOf(n);
    }

    /**
     * Get the index of the Edge from node i to node j or -1 if this Edge does
     * not exist. Runs in O(log(N)).
     *
     * @param i from node
     * @param j to node
     * @return index of Edge or -1 if not adjacent
     */
    public final int getEdgeIndex(int i, int j) {
        int mindex = getStartOf(i);
        int maxdex = getEndOf(i) - 1;
        while (maxdex >= mindex) {
            int index = (maxdex + mindex) / 2;
            if (adjacencyList[index] == j) {
                return index;
            } else if (adjacencyList[index] > j) {
                maxdex = index - 1;
            } else {
                mindex = index + 1;
            }
        }
        return -1;
    }

    public Iterable<Integer> getEdgesFrom(int n) {
        final int start = getStartOf(n);
        final int end = getEndOf(n);
        return new Iterable<Integer>() {

            private final Iterator<Integer> iterator = new Iterator<Integer>() {

                private int i = 0;

                @Override
                public boolean hasNext() {
                    return start + i < end;
                }

                @Override
                public Integer next() {
                    return start + i++;
                }
            };

            @Override
            public Iterator<Integer> iterator() {
                return iterator;
            }
        };
    }

    public IntStream streamEdgesFrom(int n) {
        return Arrays.stream(adjacencyList, getStartOf(n), getEndOf(n));
    }

    
    
    public static AdjacencyList kruskal(int nodeCount, int edgeCount, int[] nodeI, int[] nodeJ, final float[] weights, int initMethod) {
        NodeSetElement[] setElements = new NodeSetElement[nodeCount];
        NodeSet[] nodeSets = new NodeSet[nodeCount];
        Integer indices[] = new Integer[edgeCount];
        int newNodeI[] = new int[edgeCount];
        int newNodeJ[] = new int[edgeCount];
        float newWeights[] = new float[edgeCount];
        
        for(int i=0; i<nodeCount; i++) {
            setElements[i] = new NodeSetElement(i);
            nodeSets[i] = new NodeSet(setElements[i]);
        }
        
        for(int i=0; i<edgeCount; i++) {
            indices[i] = i;
        }
        
        Arrays.sort(indices, new Comparator<Integer>() {

            @Override
            public int compare(Integer i1, Integer i2) {
                return (int) (weights[i1] - weights[i2]);
            }
        });
        
        int newEdgeCount = 0;
        for(int i=0; i<edgeCount; i++) {
            NodeSetElement iNode = setElements[nodeI[indices[i]]];
            NodeSetElement jNode = setElements[nodeJ[indices[i]]];
            
            if(iNode.setRef != jNode.setRef) {
                newNodeI[edgeCount] = iNode.value;
                newNodeJ[edgeCount] = jNode.value;
                newWeights[edgeCount] = weights[indices[i]];
                NodeSet.union(iNode.setRef, jNode.setRef);
                edgeCount++;
            }
        }
        
        return new AdjacencyList(nodeCount, newEdgeCount, newNodeI, newNodeJ, newWeights, initMethod);
    }
}
