/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Lars Oetermann <lars.oetermann.com>
 */
public class SteinerTree {

    private int[] nodeI, nodeJ;
    private float totalWeight;

    public SteinerTree(int[] nodeI, int[] nodeJ, float totalWeight) {
        this.nodeI = nodeI;
        this.nodeJ = nodeJ;
        this.totalWeight = totalWeight;
    }

    public int[] getNodeI() {
        return nodeI;
    }

    public int[] getNodeJ() {
        return nodeJ;
    }

    public float getTotalWeight() {
        return totalWeight;
    }

}
