/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Lars Oetermann <lars.oetermann.com>
 */
public class Dijkstra {

    private final int[] predecessors;
    private final float[] distances;
    private final int source;

    public Dijkstra(AdjacencyList adjacencyList, int source) {
        this.source = source;
        predecessors = new int[adjacencyList.getNodeCount()];
        distances = new float[adjacencyList.getNodeCount()];
        boolean[] visited = new boolean[adjacencyList.getNodeCount()];
        distances[source] = 0;
        FibonacciHeap<Integer> fibonacciHeap = new FibonacciHeap<>();
        
        // For randomly taking nodes with same prio
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < predecessors.length; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);
        
        for (int i : indices) {
            if (i != source) {
                distances[i] = Float.POSITIVE_INFINITY;
                predecessors[i] = -1;
            }
            fibonacciHeap.enqueue(i, distances[i]);
        }
        
        while(!fibonacciHeap.isEmpty()) {
            FibonacciHeap.Entry<Integer> entry = fibonacciHeap.dequeueMin();
            int u = entry.getValue();
            visited[u] = true;
            for (int edge = adjacencyList.getStartOf(u); edge < adjacencyList.getEndOf(u); edge++) {
                int v = adjacencyList.getToNode(edge);
                if(!visited[v]) {
                    float alt = distances[u] + adjacencyList.getWeight(edge);
                    if(alt < distances[v]) {
                        distances[v] = alt;
                        predecessors[v] = u;
                        fibonacciHeap.decreaseKey(entry, alt);
                    }
                }
            }
        }
    }
    
    public float getDistanceTo(int n) {
        return distances[n];
    }
    
    public int getPredecessorOf(int n) {
        return predecessors[n];
    }
    
    public int getNodeStepsTo(int n) {
        int i = 0;
        while(n != source) {
            if(n < 0) {
                return -1;
            }
            n = predecessors[n];
            i++;
        }
        return i;
    }
    
    public int[] getShortestPathTo(int n) {
        int[] path = new int[getNodeStepsTo(n)];
        int i = n-1;
        while(i >= 0) {
            path[i] = n;
            n = predecessors[n];
            i--;
        }
        return path;
    }
    
    public static Dijkstra[] allToAll(AdjacencyList adjacencyList) {
        Dijkstra[] dijkstras = new Dijkstra[adjacencyList.getNodeCount()];
        for (int i = 0; i < adjacencyList.getNodeCount(); i++) {
            dijkstras[i] = new Dijkstra(adjacencyList, i);
        }
        return dijkstras;
    }

}
