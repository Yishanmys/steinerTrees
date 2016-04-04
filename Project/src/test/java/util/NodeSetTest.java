/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cwitte
 */
public class NodeSetTest {
   
    
    public NodeSetTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    /**
     * Test of addElement method, of class NodeSet.
     */
    @Test
    public void testAddElement() {
        NodeSet nodeSet = new NodeSet();
        NodeSetElement elem1, elem2;
        nodeSet.addElement(5);
        assertEquals(nodeSet.getSize(), 1);
        
        elem1 = nodeSet.getFirstElement();
        elem2 = nodeSet.getLastElement();
        assertEquals(elem1, elem2);
        assertEquals(elem1.value, 5);
        
        nodeSet.addElement(6);
        elem1 = nodeSet.getFirstElement();
        elem2 = nodeSet.getLastElement();
        assertEquals(elem1.nextRef, elem2);
        assertEquals(elem2.value, 6);
        assertEquals(nodeSet.getSize(), 2);
        assertEquals(elem1.setRef, nodeSet);
        assertEquals(elem2.setRef, nodeSet);
    }
    
    
    /**
     * Test of union method, of class NodeSet.
     */
    @Test
    public void testUnion() {
        NodeSet nodeSet1 = new NodeSet();
        NodeSet nodeSet2 = new NodeSet();
        NodeSetElement elem1 = new NodeSetElement(1);
        NodeSetElement elem2 = new NodeSetElement(2);
        NodeSetElement elem3 = new NodeSetElement(3);
        nodeSet1.addElement(elem1);
        nodeSet1.addElement(elem2);
        nodeSet2.addElement(elem3);
        NodeSet.union(nodeSet1, nodeSet2);
        
        assertEquals(nodeSet1.getSize(), 3);
        assertEquals(nodeSet2.getSize(), 1);
        assertEquals(nodeSet1.getFirstElement(), elem1);
        assertEquals(nodeSet1.getFirstElement().nextRef.nextRef, elem3);
        assertEquals(nodeSet1.getLastElement(), elem3);
        
        assertEquals(nodeSet1.getFirstElement().setRef, nodeSet1);
        assertEquals(nodeSet1.getFirstElement().nextRef.setRef, nodeSet1);
        assertEquals(nodeSet1.getFirstElement().nextRef.nextRef.setRef, nodeSet1);
    }
    
}
