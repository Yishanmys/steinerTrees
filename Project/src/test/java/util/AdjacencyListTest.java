/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.stream.IntStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

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
    public void testEdgeIndex() {
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

    /**
     * Test of getEdgeIndex method, of class AdjacencyList.
     */
    @org.junit.Test
    public void testGetEdgeIndex() {
        System.out.println("getEdgeIndex");
        int i = 0;
        int j = 0;
        AdjacencyList instance = null;
        int expResult = 0;
        int result = instance.getEdgeIndex(i, j);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of streamEdgesFrom method, of class AdjacencyList.
     */
    @org.junit.Test
    public void testStreamEdgesFrom() {
        System.out.println("streamEdgesFrom");
        int n = 0;
        AdjacencyList instance = null;
        IntStream expResult = null;
        IntStream result = instance.streamEdgesFrom(n);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
