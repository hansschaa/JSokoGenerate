/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SokoGenerator.Tree;

/**
 *
 * @author Hans
 */
public class CrossPair {
    public Pair pair;
    public Pair dir;

    public CrossPair(Pair pair, Pair dir) {
        this.pair = pair;
        this.dir = dir;
    }
    
    public void Print(){
        System.out.println("Pair: " + pair.i + "," + pair.j);
        System.out.println("Dir: " + dir.i + "," + dir.j);
    }   

}
