/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SokoGenerator;

/**
 *
 * @author Hans
 */
public class ManhattanSokoBoard {

    char[][] board;
    int globalManhattanScore;
    
    public ManhattanSokoBoard(char[][] board, int globalManhattanScore) {
        this.board = board;
        this.globalManhattanScore = globalManhattanScore;
    }
}
