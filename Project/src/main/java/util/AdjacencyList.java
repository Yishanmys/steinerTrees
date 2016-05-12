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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lars Oetermann <lars.oetermann.com>
 */
public class AdjacencyList {

    private int[] listStarts, adjacencyList;
    private float[] adjacencyWeights;
    public final static int INIT_PARTLY_COUNTING_SORT = 0, INIT_PURE_COUNTING_SORT = 1, INIT_ARRAY_SORT = 2;

    /**
     * Creates a new AdjacencyList based on the following input with the given
     * initMethod.
     *
     * @param nodeCount number of Nodes -> N
     * @param edgeCount number of Edges -> M
     * @param nodeI represents an edge from nodeI[m] to nodeJ[m]
     * @param nodeJ represents an edge from nodeI[m] to nodeJ[m]
     * @param weights the weight of edge m
     */
    public AdjacencyList(int nodeCount, int edgeCount, int[] nodeI, int[] nodeJ, float[] weights) {
//        this(nodeCount, edgeCount, nodeI, nodeJ, weights, edgeCount > nodeCount * nodeCount / 100 ? INIT_PURE_COUNTING_SORT : INIT_PARTLY_COUNTING_SORT);
        this(nodeCount, edgeCount, nodeI, nodeJ, weights, INIT_PARTLY_COUNTING_SORT);
    }

    /**
     * Creates a new AdjacencyList based on the following input with the given
     * initMethod.
     *
     * @param nodeCount number of Nodes -> N
     * @param edgeCount number of Edges -> M
     * @param nodeI represents an edge from nodeI[m] to nodeJ[m]
     * @param nodeJ represents an edge from nodeI[m] to nodeJ[m]
     * @param weights the weight of edge m
     * @param initMethod
     * INIT_(PARTLY_COUNTING_SORT|PURE_COUNTING_SORT|ARRAY_SORT)
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

    public void print() {
        System.out.println("ListStarts: " + Arrays.toString(listStarts));;
        System.out.println("AdjacencyList: " + Arrays.toString(adjacencyList));;
        for (int i = 0; i < getEdgeCount(); i++) {
            int from = getFromNode(i);
            int to = getToNode(i);
            float weight = getWeight(i);

            System.out.println("Edge from node " + from + " to node " + to + "(weight: " + weight + ")");
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

    public void setToNode(int m, int n) {
        adjacencyList[m] = n;
    }

    /**
     * Get the index of the Node from which the given Edge comes. Runs in
     * O(log(N)), so use wisely.
     *
     * @param m the edge
     * @return the index
     */
    public int getFromNode(int m) {
        int mindex = 0;
        int maxdex = listStarts.length - 1;
        while (mindex < maxdex) {
            int index = maxdex + mindex / 2;
            if (getStartOf(index) > m) {
                maxdex = index - 1;
            } else if (getEndOf(index) <= m) {
                mindex = index + 1;
            } else {
                return index;
            }
        }
        return mindex;
    }

    /**
     * Get the weight of Edge with index m. Runs in O(1).
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
     * The number of nodes. Runs in O(1).
     *
     * @return the nodecount
     */
    public int getNodeCount() {
        return listStarts.length;
    }

    /**
     * The number of edges. Runs in O(1).
     *
     * @return the edgecount
     */
    public int getEdgeCount() {
        return adjacencyList.length;
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
        int maxdex = getEndOf(i);

        // O(n) alternative
        for (int k = mindex; k < maxdex; k++) {
            if (adjacencyList[k] == j) {
                assert getFromNode(k) == i;
                assert getToNode(k)   == j;
                return k;
            }
        }

//        while (maxdex >= mindex) {
//            int index = (maxdex + mindex) / 2;
//            if (adjacencyList[index] == j) {
//                return index;
//            } else if (adjacencyList[index] > j) {
//                maxdex = index - 1;
//            } else {
//                mindex = index + 1;
//            }
//        }
        return -1;
    }

    /**
     * The edges of node n as an Iterable. This is much slower than for(int
     * i=getStartOf(n); i&lt;getEndOf(n); i++);
     *
     * @param n the from node
     * @return the edges as an Iterable.
     */
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

    /**
     * All edges from node n as an IntStream. This is slightly (with a constant
     * amount of time) slower than for(int i=getStartOf(n); i&lt;getEndOf(n);
     * i++);
     *
     * @param n the from node
     * @return the edges as IntStream
     */
    public IntStream streamEdgesFrom(int n) {
        return Arrays.stream(adjacencyList, getStartOf(n), getEndOf(n));
    }

    /**
     * Returns a Steier Tree using Kruskal's Algorithm.
     * @param nodeCount Total number of nodes
     * @param edgeCount Total number of edges
     * @param nodeI NodeI (edge sources) for the graph
     * @param nodeJ NodeJ (edge targets) for the graph
     * @param weights Weights of edges in the graph
     * @param initMethod initMethod
     * @return Minimum Spanning tree
     */
    public static AdjacencyList kruskalMST(int nodeCount, int edgeCount, int[] nodeI, int[] nodeJ, final float[] weights, int initMethod) {
        NodeSetElement[] setElements = new NodeSetElement[nodeCount];
        NodeSet[] nodeSets = new NodeSet[nodeCount];
        Integer indices[] = new Integer[edgeCount];
        int newNodeI[] = new int[edgeCount];
        int newNodeJ[] = new int[edgeCount];
        float newWeights[] = new float[edgeCount];

        for (int i = 0; i < nodeCount; i++) {
            setElements[i] = new NodeSetElement(i);
            nodeSets[i] = new NodeSet(setElements[i]);
        }

        for (int i = 0; i < edgeCount; i++) {
            indices[i] = i;
        }

        Arrays.sort(indices, (Integer i1, Integer i2) -> weights[i1] > weights[i2] ? 1 : weights[i1] > weights[i2] ? 0 : -1);

        int newEdgeCount = 0;
        for (int i = 0; i < edgeCount; i++) {
            NodeSetElement iNode = setElements[nodeI[indices[i]]];
            NodeSetElement jNode = setElements[nodeJ[indices[i]]];

            if (iNode.setRef != jNode.setRef) {
                newNodeI[newEdgeCount] = iNode.value;
                newNodeJ[newEdgeCount] = jNode.value;
                newWeights[newEdgeCount] = weights[indices[i]];
                NodeSet.union(iNode.setRef, jNode.setRef);
                newEdgeCount++;
            }
        }

        return new AdjacencyList(nodeCount, newEdgeCount, newNodeI, newNodeJ, newWeights, initMethod);
    }

    /**
     * Retruns a SteinerTree which is a 2-approximation of the minimum steiner
     * tree of the graph represented by this AdjacencyList. The resulting
     * SteinerTree is in fact a connected, directed sub graph which contains all
     * targets and nodes in the shortest path between the targets. The total
     * weight of all edges is approximately as low as possible.
     *
     * @param targets The targets that have to be in the resulting tree (graph)
     * @param original The original graph for the steiner Trees.
     * @param ds Dijkstra Objects for all paths in the original graph. 
     * @return The SteinerTree representing a graph in the form of to node
     * arrays. The graph is described via the edges from nodeI[n] to nodeJ[n].
     */
    public AdjacencyList steinerTree(final int[] targets, AdjacencyList original, Dijkstra[] ds) {

        final boolean[] isTarget = new boolean[getNodeCount()];

        // Dijkstra from all Targets to all Nodes
        int edgeCount = targets.length * (targets.length - 1);
        final int[] nodeI = new int[edgeCount];
        final int[] nodeJ = new int[edgeCount];
        final float[] distances = new float[edgeCount];
        final Dijkstra[] dijkstras = new Dijkstra[targets.length];

        ExecutorService executor = Executors.newFixedThreadPool(targets.length);
        for (int i = 0; i < targets.length; i++) {
            final int target = i;
            isTarget[targets[target]] = true;
            executor.execute(new Runnable() {

                @Override
                public void run() {

                    dijkstras[target] = new Dijkstra(AdjacencyList.this, targets[target], isTarget, targets.length);
                    int counter = target * (targets.length - 1);
                    for (int j = 0; j < targets.length; j++) {
                        if (j != target) {
                            nodeI[counter] = targets[target];
                            nodeJ[counter] = targets[j];
                            distances[counter] = dijkstras[target].getDistanceTo(targets[j]);
                            counter++;
                        }
                    }
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException ex) {
            Logger.getLogger(AdjacencyList.class.getName()).log(Level.SEVERE, null, ex);
        }

        // First Kruskal
        AdjacencyList td = kruskalMST(getNodeCount(), nodeI.length, nodeI, nodeJ, distances, INIT_PARTLY_COUNTING_SORT);

        // First Kruskal to shortest Paths
        List<Integer> nodeIList = new ArrayList<>();
        List<Integer> nodeJList = new ArrayList<>();
        List<Float> weightList = new ArrayList<>();
        for (int i = 0; i < targets.length; i++) {
            for (int j = td.getStartOf(targets[i]); j < td.getEndOf(targets[i]); j++) {
                int[] edgesPath = dijkstras[i].getEdgesOfShortestPathTo(td.getToNode(j));
                int fromNode = targets[i];
                for (int k = 0; k < edgesPath.length; k++) {
                    int m = edgesPath[k];
                    nodeIList.add(fromNode);
                    nodeJList.add(getToNode(m));
                    fromNode = getToNode(m);
                    weightList.add(getWeight(m));
                }
            }
        }
        int[] nodeI2 = new int[nodeIList.size()];
        int[] nodeJ2 = new int[nodeI2.length];
        float[] weights = new float[nodeJ2.length];
        for (int i = 0; i < nodeI2.length; i++) {
            nodeI2[i] = nodeIList.get(i);
            nodeJ2[i] = nodeJList.get(i);
            weights[i] = weightList.get(i);
        }

        // Second Kruskal
        AdjacencyList t = kruskalMST(getNodeCount(), nodeI2.length, nodeI2, nodeJ2, weights, INIT_PARTLY_COUNTING_SORT);

        // Remove LEAVES 
        int newEdgeCount = t.getEdgeCount();
        boolean removed = true;
        while (removed) {
            removed = false;
            for (int i = 0; i < t.getEdgeCount(); i++) {
                int toNode = t.getToNode(i);
                if (toNode >= 0 && !isTarget[toNode]) {
                    boolean isLeaf = true;
                    for (int j = t.getStartOf(toNode); j < t.getEndOf(toNode) && isLeaf; j++) {
                        if (t.getToNode(j) >= 0) {
                            isLeaf = false;
                        }
                    }
                    if (isLeaf) {
                        t.setToNode(i, -1);
                        removed = true;
                        newEdgeCount--;
                    }
                }
            }
        }

        nodeI2 = new int[newEdgeCount];
        nodeJ2 = new int[newEdgeCount];
        float[] weights2 = new float[newEdgeCount];
        float totalWeight = 0;
        int current = 0;
        for (int i = 0; i < t.getNodeCount(); i++) {
            for (int m = t.getStartOf(i); m < t.getEndOf(i); m++) {
                int j = t.getToNode(m);
                if (j >= 0) {
                    nodeI2[current] = i;
                    nodeJ2[current] = j;
                    weights2[current] = t.getWeight(m);
                    totalWeight += t.getWeight(m);
                    current++;
                }
            }
        }

        List<Integer> IList = new ArrayList<>();
        List<Integer> JList = new ArrayList<>();
        List<Float> WList = new ArrayList<>();

        for (int i = 0; i < nodeI2.length; i++) {

            Dijkstra correctDijkstra = null;
            for (int j = 0; j < ds.length; j++) {
                if (ds[j].getSource() == nodeI2[i]) {
                    correctDijkstra = ds[j];
                }
            }

            int[] foo = correctDijkstra.getNodesOfShortestPathTo(nodeJ2[i]);
            for (Integer edge : correctDijkstra.getEdgesOfShortestPathTo(nodeJ2[i])) {
                IList.add(original.getFromNode(edge));
                JList.add(original.getToNode(edge));
                WList.add(original.getWeight(edge));
            }
        }

        assert IList.size() == JList.size();
        assert JList.size() == WList.size();

        int[] newNodeI = new int[IList.size()];
        int[] newNodeJ = new int[JList.size()];
        float[] newWeights = new float[WList.size()];

        for (int i = 0; i < newNodeJ.length; i++) {
            newNodeI[i] = IList.get(i);
            newNodeJ[i] = JList.get(i);
            newWeights[i] = WList.get(i);
        }

        int fooNodeCount = this.getNodeCount();
        int fooEdgeCount = newNodeI.length;

        return new AdjacencyList(fooNodeCount,
                2 * fooEdgeCount,
                ArrayUtils.addAll(newNodeI, newNodeJ),
                ArrayUtils.addAll(newNodeJ, newNodeI),
                ArrayUtils.addAll(newWeights, newWeights));
    }

    /**
     * Generates a complete graph from target nodes and distances between them.
     * This is used in phase I of the prize-collecting algorithm.
     *
     * @param targets The target nodes.
     * @param ds all-to-all Dijkstras, for distances between nodes.
     * @return
     */
    public static AdjacencyList toCompleteGraph(int nodeCount, int[] targets, Dijkstra[] ds) {

        /* There are n*(n-1) edges in a complete, directed graph with n nodes */
        int edgeCount = targets.length * (targets.length - 1);

        int[] nodeI = new int[edgeCount];
        int[] nodeJ = new int[edgeCount];
        float[] weights = new float[edgeCount];

        /* Fill nodeI and nodeJ like this (example for n = 5) */
        /* nodeI = [0,0,0,0,1,1,1,1,2,2,2,2,3,3,3,3,4,4,4,4] */
        /* nodeJ = [1,2,3,4,0,2,3,4,0,1,3,4,0,1,2,4,0,1,2,3] */
        
        List<Integer> nodeIList = new ArrayList<>();
        List<Integer> nodeJList = new ArrayList<>();
        for (Integer t : targets) {
            for (int i = 0; i < targets.length - 1; i++) {
                nodeIList.add(t);
            }
            for (Integer s : targets) {
                if (s != t) {
                    nodeJList.add(s);
                }
            }
        }

        for (int i = 0; i < nodeI.length; i++) {
            nodeI[i] = nodeIList.get(i);
            nodeJ[i] = nodeJList.get(i);
            weights[i] = ds[nodeI[i]].getDistanceTo(nodeJ[i]);
        }

        /* Generate complete Graph from the data just computed */
        AdjacencyList completeGraph = new AdjacencyList(nodeCount,
                edgeCount,
                nodeI,
                nodeJ,
                weights);

        /* Return complete Graph */
        return completeGraph;
    }

    /**
     * Returns all nodes in this graph.
     *
     * @return int[] of all nodes
     */
    public int[] getConnectedNodes() {
        HashSet<Integer> connected = new HashSet<>();
        for (int n = 0; n < getNodeCount(); n++)
        {
            if (getDegree(n) > 0)
                { connected.add(n); }
        }
        for (int m = 0; m < getEdgeCount(); m++) {
            connected.add(getToNode(m));
        }
        return connected.stream().mapToInt(Integer::intValue).toArray();

    }

    /**
     * Compute the connection cost of a node.
     *
     * @param node What node is being examined.
     * @return The total connection cost as a float.
     */
    public float getConnectionCost(int node) {
        assert getDegree(node) == 1;
        return getWeight(getEdgeIndex(node, adjacencyList[listStarts[node]]));
    }

    public float getTotalWeight() {
        int res = 0;
        for (Float f : adjacencyWeights) {
            res += f;
        }
        return res;
    }

    /**
     * Prune a leaf node from Graph.
     *
     * @param g The unpruned graph.
     * @param node The node to be pruned.
     * @return The pruned graph.
     */
    public static AdjacencyList prune(AdjacencyList g, int node)
    {
        assert g.getDegree(node) == 1;
        int newEdgeCount = g.getEdgeCount() - 2;
       
        int[] newI   = new int[newEdgeCount];
        int[] newJ   = new int[newEdgeCount];
        float[] newW = new float[newEdgeCount];
        
        int k = 0;
        
        for (int i = 0; i < g.getEdgeCount(); i++)
        {
            if (!((g.getFromNode(i) == node) || (g.getToNode(i) == node)))
            {
                newI[k] = g.getFromNode(i);
                newJ[k] = g.getToNode(i);
                newW[k] = g.getWeight(i);
                k++;
            }
        }
        
        AdjacencyList h = new AdjacencyList(g.getNodeCount(), newEdgeCount, newI, newJ, newW);
        return h;
    }
}
