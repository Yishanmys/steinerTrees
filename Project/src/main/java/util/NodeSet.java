/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author cwitte
 */

class NodeSetElement {
    public int value;
    
    public NodeSetElement nextRef;
    
    public NodeSet setRef;
    
    public NodeSetElement(int value) {
        this.value = value;
    }
}




public class NodeSet {
    private int size;
    
    private NodeSetElement firstElement;
    
    private NodeSetElement lastElement;
    
    /**
     * Creates a new set containing only the given element.
     * @param element Element to be added to the new set
     */
    public NodeSet(NodeSetElement element) {
        size = 1;
        firstElement = element;
        element.setRef = this;
        lastElement = element;
    }
    
    
    public NodeSet() {
        
    }
    
    
    public int getSize() {
        return size;
    }
    
    public NodeSetElement getFirstElement() {
        return firstElement;
    }
    
    public NodeSetElement getLastElement() {
        return lastElement;
    }
    
    
    public void addElement(NodeSetElement elem) {
        elem.setRef = this;
        
        if(size == 0) {
            firstElement = elem;
            lastElement = elem;
        } else {
            lastElement.nextRef = elem;
            lastElement = elem;
        }
        
        size++;
    }
    
    
    public void addElement(int value) {
        addElement(new NodeSetElement(value));
    }
    
    
    
    /**
     * Adds the elements of the smaller set to bigger one and returns the modified set.
     * @param set1 First set
     * @param set2 Second set
     * @return Union set of the two given sets
     */
    public static NodeSet union(NodeSet set1, NodeSet set2) {
        NodeSet smallerSet, biggerSet;
        
        if(set1.size <= set2.size) {
            smallerSet = set1;
            biggerSet = set2;
        } else {
            smallerSet = set2;
            biggerSet = set1;
        }
        
        NodeSetElement element = smallerSet.firstElement;
        element.setRef = biggerSet;
        while(element.nextRef != null) {
            element = element.nextRef;
            element.setRef = biggerSet;
        }
        
        biggerSet.lastElement.nextRef = smallerSet.firstElement;
        biggerSet.lastElement = smallerSet.lastElement;
        biggerSet.size += smallerSet.size;
        
        return biggerSet;
    }
}
