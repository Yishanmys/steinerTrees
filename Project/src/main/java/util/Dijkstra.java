/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 * Calculate the shortest paths from one source node to each other nodes or at
 * least to an array of target nodes.
 *
 * @author Lars Oetermann <lars.oetermann.com>
 */
public class Dijkstra {

    private final int[] predecessors, preEdges;
    private final float[] distances;
    private final int source;

    /**
     * Calculate the shortest path from source to each other node.
     *
     * @param adjacencyList the graph to work on
     * @param source the node from which all paths are calculated
     */
    public Dijkstra(AdjacencyList adjacencyList, int source) {
        this(adjacencyList, source, new boolean[adjacencyList.getNodeCount()], 0);
    }

    /**
     * Calculate the shortest path from source to each node for which isTarget
     * is true. Other nodes might be calculated but not might also through an
     * exception if tried to get shortest path to them. If every element of
     * isTarget is false, the shortest path to each node is computed. Make
     * sure that targetCount is equal to the number of elements of isTarget
     * which are true. If targetCount is greater, all paths are computed, if it
     * lower not all targets might be in the result.
     *
     * @param adjacencyList the graph to work on
     * @param source the node from which all paths are calculated
     * @param isTarget the nodes to which the shortest path has to be
     * calculated.
     * @param targetCount the number of targets
     */
    public Dijkstra(AdjacencyList adjacencyList, int source, boolean[] isTarget, int targetCount) {
        this.source = source;
        predecessors = new int[adjacencyList.getNodeCount()];
        preEdges = new int[adjacencyList.getNodeCount()];
        distances = new float[adjacencyList.getNodeCount()];
        boolean[] visited = new boolean[adjacencyList.getNodeCount()];
        distances[source] = 0;
        FibonacciHeap<Integer> fibonacciHeap = new FibonacciHeap<>();

        // For randomly taking nodes with same prio
//        List<Integer> indices = new ArrayList<>();
//        for (int i = 0; i < predecessors.length; i++) {
//            indices.add(i);
//        }
//        Collections.shuffle(indices);
        FibonacciHeap.Entry<Integer>[] entries = new FibonacciHeap.Entry[adjacencyList.getNodeCount()];
        for (int i = 0; i < predecessors.length; i++) {
//        for (int i : indices) { // for random order
            if (i != source) {
                distances[i] = Float.POSITIVE_INFINITY;
                predecessors[i] = -1;
                preEdges[i] = -1;
            }
            entries[i] = fibonacciHeap.enqueue(i, distances[i]);
        }

        while (!fibonacciHeap.isEmpty()) {
            FibonacciHeap.Entry<Integer> entry = fibonacciHeap.dequeueMin();
            int u = entry.getValue();
            visited[u] = true;
            if (isTarget[u]) {
                targetCount--;
                if (targetCount <= 0) {
                    break;
                }
            }
            for (int edge = adjacencyList.getStartOf(u); edge < adjacencyList.getEndOf(u); edge++) {
                int v = adjacencyList.getToNode(edge);
                if (!visited[v]) {
                    float alt = distances[u] + adjacencyList.getWeight(edge);
                    if (alt < distances[v]) {
                        distances[v] = alt;
                        predecessors[v] = u;
                        preEdges[v] = edge;
                        fibonacciHeap.decreaseKey(entries[v], alt);
                    }
                }
            }
        }
    }

    /**
     * The sum of all weights on shortest path from source to n. Might return a
     * false result if n is not a target but targets are specified.
     *
     * @param n the ToNode
     * @return the distance from source to n
     */
    public float getDistanceTo(int n) {
        return distances[n];
    }

    /**
     * The node before n on the shortest path from source to n. Might return a
     * false result if n is not a target but targets are specified.
     *
     * @param n the ToNode
     * @return the node before n
     */
    public int getPredecessorOf(int n) {
        return predecessors[n];
    }

    /**
     * The index of the edge which leads to n in the shortest path from source
     * to n. Might return a false result if n is not a target but targets are
     * specified.
     *
     * @param n the ToNode
     * @return the edge from getPredecessorOf(n) to n
     */
    public int getPreEdgeOf(int n) {
        return preEdges[n];
    }

    /**
     * Get the number of nodes which are part of the shortest path from source
     * to n. Might return a false result if n is not a target but targets are
     * specified.
     *
     * @param n the ToNode
     * @return -1 if there is no path from source to n
     */
    public int getNodeStepsTo(int n) {
        int i = 0;
        while (n != source) {
            if (n < 0) {
                return -1;
            }
            n = predecessors[n];
            i++;
        }
        return i;
    }

    /**
     * Get an array of nodes which are part of the shortest path from source to
     * n. Throws a NegativeArraySizeException if there is no path from source to
     * n. Might return a false result if n is not a target but targets are
     * specified.
     *
     * @param n the ToNode
     * @return the shortest path as an array of nodes
     */
    public int[] getNodesOfShortestPathTo(int n) {
        int[] path = new int[getNodeStepsTo(n)];
        int i = getNodeStepsTo(n) - 1;
        while (i >= 0) {
            path[i] = n;
            n = predecessors[n];
            i--;
        }
        return path;
    }

    /**
     * Get an array of edges which are part of the shortest path from source to
     * n. Throws a NegativeArraySizeException if there is no path from source to
     * n. Might return a false result if n is not a target but targets are
     * specified.
     *
     * @param n the ToNode
     * @return the shortest path as an array of edges
     */
    public int[] getEdgesOfShortestPathTo(int n) {
        int[] path = new int[getNodeStepsTo(n)];
        int i = getNodeStepsTo(n) - 1;
        while (i >= 0) {
            path[i] = preEdges[n];
            n = predecessors[n];
            i--;
        }
        return path;
    }

    /**
     * Calculate a Complete Distance Network. Runs in O(n^2).
     *
     * @param adjacencyList the graph to work on
     * @return an array of Dijkstra results
     */
    public static Dijkstra[] allToAll(AdjacencyList adjacencyList) {
        Dijkstra[] dijkstras = new Dijkstra[adjacencyList.getNodeCount()];
        for (int i = 0; i < adjacencyList.getNodeCount(); i++) {
            dijkstras[i] = new Dijkstra(adjacencyList, i);
        }
        return dijkstras;
    }

}
