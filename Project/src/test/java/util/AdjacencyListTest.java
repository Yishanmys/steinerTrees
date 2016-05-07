/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Arrays;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
/**
 *
 * @author Lars Oetermann <lars.oetermann.com>
 */
public class AdjacencyListTest {

    private static int nodeCount, edgeCount;
    private static int[] nodeI, nodeJ;
    private static float[] weights;
    private AdjacencyList instance;

    public AdjacencyListTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        nodeCount = 8;
        nodeI = new int[]{0, 1, 3, 4, 6, 3, 3, 6, 6, 5, 1};
        nodeJ = new int[]{2, 2, 2, 2, 3, 1, 4, 4, 1, 5, 1};
        // starts {0, 1, 3, 3, 6, 7, 8, 11}
        // adjacencies {2, 1, 2, 1, 2, 4, 2, 5, 1, 3, 4}
        edgeCount = nodeI.length;
        weights = new float[]{2, 2, 2, 2, 3, 1, 4, 4, 1, 5, 1};
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = new AdjacencyList(nodeCount, edgeCount, nodeI, nodeJ, weights, (int) (3 * Math.random()));
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getEndOf method, of class AdjacencyList.
     */
    @org.junit.Test
    public void testGetEndOf() {
        System.out.println("getEndOf");
        int[] testN = new int[]{0, 1, 2, 5, 7};
        int[] expResult = new int[]{1, 3, 3, 8, 11};
        for (int i = 0; i < testN.length; i++) {
            int result = instance.getEndOf(testN[i]);
            assertEquals(expResult[i], result);
        }
    }

    /**
     * Test of getStartOf method, of class AdjacencyList.
     */
    @org.junit.Test
    public void testGetStartOf() {
        System.out.println("getStartOf");
        int[] testN = new int[]{0, 1, 2, 5, 7};
        int[] expResult = new int[]{0, 1, 3, 7, 11};
        for (int i = 0; i < testN.length; i++) {
            int result = instance.getStartOf(testN[i]);
            assertEquals(expResult[i], result);
        }
    }

    /**
     * Test of getToNode method, of class AdjacencyList.
     */
    @org.junit.Test
    public void testGetToNode() {
        System.out.println("getToNode");
        int[] testM = new int[]{0, 1, 2, 5, 7};
        int[] expResult = new int[]{2, 1, 2, 4, 5};
        for (int i = 0; i < testM.length; i++) {
            int result = instance.getToNode(testM[i]);
            assertEquals(expResult[i], result);
        }
    }

    /**
     * Test of getFromNode method, of class AdjacencyList.
     */
    @org.junit.Test
    public void testGetFromNode() {
        System.out.println("getFromNode");
        for (int i = 0; i < nodeCount; i++) {
            for (int j = instance.getStartOf(i); j < instance.getEndOf(i); j++) {
                int result = instance.getFromNode(j);
                assertEquals(i, result);
            }
        }
    }

    /**
     * Test of getWeight method, of class AdjacencyList.
     */
    @org.junit.Test
    public void testGetWeight() {
        System.out.println("getWeight");
        int[] testM = new int[]{0, 1, 2, 5, 7};
        int[] expResult = new int[]{2, 1, 2, 4, 5};
        for (int i = 0; i < testM.length; i++) {
            float result = instance.getWeight(testM[i]);
            assertEquals(expResult[i], result, 0.1);
        }
    }

    /**
     * Test of getEdgeIndex method, of class AdjacencyList.
     */
    @org.junit.Test
    public void testGetEdgeIndex() {
        System.out.println("getEdgeIndex");
        int[] testI = new int[]{0, 1, 2, 3, 5, 7};
        int[] testJ = new int[]{0, 7, 2, 4, 5, 1};
        int[] expResult = new int[]{-1, -1, -1, 5, 7, -1};
        for (int i = 0; i < testI.length; i++) {
            int result = instance.getEdgeIndex(testI[i], testJ[i]);
            assertEquals(expResult[i], result);
        }
    }

    /**
     * Test of getDegree method, of class AdjacencyList.
     */
    @org.junit.Test
    public void testGetDegree() {
        System.out.println("getDegree");
        int[] testN = new int[]{0, 1, 2, 3, 5, 7};
        int[] expResult = new int[]{1, 2, 0, 3, 1, 0};
        for (int i = 0; i < testN.length; i++) {
            int result = instance.getDegree(testN[i]);
            assertEquals(expResult[i], result);
        }
    }

    /**
     * Test of getEdgesFrom method, of class AdjacencyList.
     */
    @org.junit.Test
    public void testGetEdgesFrom() {
        System.out.println("getEdgesFrom");
        int[] testN = new int[]{0, 1, 2, 3, 5, 7};
        for (int i = 0; i < testN.length; i++) {
            String expectedResult = "";
            for (int j = instance.getStartOf(testN[i]); j < instance.getEndOf(testN[i]); j++) {
                expectedResult += j;
            }
            String result = "";
            for (Integer edge : instance.getEdgesFrom(testN[i])) {
                result += edge;
            }

            assertEquals(expectedResult, result);
        }
    }

//    /**
//     * Test of mst method, of class AdjacencyList.
//     */
//    @org.junit.Test
//    public void testMST() {
//        System.out.println("MST");
////        int[] nodeI = new int[]{0, 1, 1, 2, 3, 4, 4, 4, 4, 5, 6, 1, 2, 4, 3, 4, 5, 6, 7, 8, 6, 7};
//        int[] nodeI = new int[]{4, 1, 0, 1, 2, 1, 3, 1};
////        int[] nodeJ = new int[]{1, 2, 4, 3, 4, 5, 6, 7, 8, 6, 7, 0, 1, 1, 2, 3, 4, 4, 4, 4, 5, 6};
//        int[] nodeJ = new int[]{1, 4, 1, 0, 1, 2, 1, 3};
////        float[] weights = new float[]{0.7f, 0.3f, 0.3f, 0.1f, 0.1f, 0.3f, 0.5f, 0.2f, 1.0f, 0.4f, 0.3f, 0.7f, 0.3f, 0.3f, 0.1f, 0.1f, 0.3f, 0.5f, 0.2f, 1.0f, 0.4f, 0.3f};
//        float[] weights = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
////        SteinerTree tree = new AdjacencyList(9, nodeI.length, nodeI, nodeJ, weights, AdjacencyList.INIT_PARTLY_COUNTING_SORT).mst(new int[] {0, 2, 4, 6});
//        SteinerTree tree = new AdjacencyList(5, nodeI.length, nodeI, nodeJ, weights).mst(new int[] {0, 1, 2, 3});
//        System.out.print("NodeI: [");
//        for (int o : tree.getNodeI()) {
//            System.out.print(o+", ");
//        }
//        System.out.println("]");
//        System.out.print("NodeJ: [");
//        for (int o : tree.getNodeJ()) {
//            System.out.print(o+", ");
//        }
//        System.out.println("]");
//        System.out.println("Total Weight: "+tree.getTotalWeight(false));
//    }
    
    
    /**
     * Test of kruskal method, of class AdjacencyList.
     */
    @org.junit.Test
    public void testKruskal() {
        System.out.println("Kruskal");
        int nodeCount = 6;
        int edgeCount = 10;
        int nodesI[] = new int[]{ 1,0,3,5,4,1,0,3,5,4 };
        int nodesJ[] = new int[]{ 0,3,5,4,1,2,2,2,2,2 };
        float weights[] = new float[]{ 6,5,2,6,3,5,1,5,4,6 };
        
        AdjacencyList alist = AdjacencyList.kruskal(nodeCount, edgeCount, nodesI, nodesJ, weights, AdjacencyList.INIT_ARRAY_SORT);
        float sum = 0;
        System.out.println("edge count:" + alist.getEdgeCount());
        for(int i=0; i<alist.getEdgeCount(); i++) {
            sum += alist.getWeight(i);
        }
        
        System.out.println(sum);
        assertEquals(sum, 15, 0.0001);
    }
    
    @org.junit.Test
    public void testPrizeColleting(){
        int nodeCount = 6;
        int edgeCount = 10;
        int nodesI[] = new int[]{ 1,0,3,5,4,1,0,3,5,4 };
        int nodesJ[] = new int[]{ 0,3,5,4,1,2,2,2,2,2 };
        float weights[] = new float[]{ 6,5,2,6,3,5,1,5,4,6 };
        
        int nodeCount_prime = 12;
        int edgeCount_prime = 20;
        //int nodesI_prime[] = nodesI + nodesJ;
        int nodesI_prime[] = ArrayUtils.addAll(nodesI, nodesJ);
        int nodesJ_prime[] = ArrayUtils.addAll(nodesJ, nodesI);
        float weights_prime[] = ArrayUtils.addAll(weights, weights);
        
        AdjacencyList al2 = new AdjacencyList(nodeCount, edgeCount_prime, nodesI_prime, nodesJ_prime, weights_prime);
        
//        boolean[] t;
//        t = new boolean[]{false,false,false,false,true,false};
//        Dijkstra d = new Dijkstra(al2, 0, t, 1);
        
//        System.out.println("------------------------------");
//        System.out.println("Nodes: ");
//        for (int i = 0; i < d.getNodesOfShortestPathTo(4).length; i++) {
//            System.out.println(d.getNodesOfShortestPathTo(4)[i]);
//        }
//        System.out.println("Edges: ");
//        for (int i = 0; i < d.getEdgesOfShortestPathTo(4).length; i++) {
//            System.out.println(d.getEdgesOfShortestPathTo(4)[i]);
//            System.out.println("");
//        }
//        System.out.println("------------------------------");

        
        float[] prizes = {2.7f, 3.1f, 0.6f, 7.2f, 2.2f, 4.0f};
        AdjacencyList prizeList = PrizeCollecting.MSTPCST(al2, new boolean[]{true,false,false,true,true,false}, prizes);
        
        assertNotNull(prizeList);
    }
    

    /**
     * Test of a not connected graph.
     */
    @org.junit.Test
    public void testNotConnected() {
        System.out.println("NotConnected");
//        int[] nodeI = new int[]{0, 1, 1, 2, 3, 4, 4, 4, 4, 5, 6, 1, 2, 4, 3, 4, 5, 6, 7, 8, 6, 7};
        int[] nodeI = new int[]{4, 1, 0, 1, 3, 1};
//        int[] nodeJ = new int[]{1, 2, 4, 3, 4, 5, 6, 7, 8, 6, 7, 0, 1, 1, 2, 3, 4, 4, 4, 4, 5, 6};
        int[] nodeJ = new int[]{1, 4, 1, 0, 1, 3};
//        float[] weights = new float[]{0.7f, 0.3f, 0.3f, 0.1f, 0.1f, 0.3f, 0.5f, 0.2f, 1.0f, 0.4f, 0.3f, 0.7f, 0.3f, 0.3f, 0.1f, 0.1f, 0.3f, 0.5f, 0.2f, 1.0f, 0.4f, 0.3f};
        float[] weights = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
        AdjacencyList list = new AdjacencyList(5, nodeI.length, nodeI, nodeJ, weights);
//        list.print();
        Dijkstra dijkstra = new Dijkstra(list, 3);
        for(int i=0; i<5; i++) {
            dijkstra.getDistanceTo(i);
            dijkstra.getEdgesOfShortestPathTo(i);
            dijkstra.getNodeStepsTo(i);
            dijkstra.getNodesOfShortestPathTo(i);
            dijkstra.getPreEdgeOf(i);
            dijkstra.getPredecessorOf(i);
        }
        assertArrayEquals(new int[] {0, 1, 3, 4}, list.getConnectedNodes());
//        System.out.println(Arrays.toString(list.mst(new int[] {3, 4}).getConnectedNodes()));
    }
}
