//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator.Tree;

import java.util.ArrayList;

public class SokoNode {
    public SokoNode parent;
    public Pair value;
    public ArrayList<SokoNode> neighBourds;

    public SokoNode(SokoNode parent, Pair value) {
        this.parent = parent;
        this.value = value;
        this.neighBourds = new ArrayList();
    }

    public void PrintValues() {
        System.out.println("I: " + this.value.i + " J: " + this.value.j);
    }
}
