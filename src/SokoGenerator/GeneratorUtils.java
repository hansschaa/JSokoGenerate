/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SokoGenerator;

import static SokoGenerator.Generator.P_BASE_BOARD;
import static SokoGenerator.Generator.random;
import SokoGenerator.Tree.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Hans
 */
public class GeneratorUtils {
    
    private static final ArrayList<Character> playerChars = new ArrayList<>(Arrays.asList('@', '+'));
    private static final ArrayList<Character> boxChars = new ArrayList<>(Arrays.asList('$', '*'));
    private static final ArrayList<Character> goalChars = new ArrayList<>(Arrays.asList('*', '.'));

    public static String ConvertCharArrayToString(char[][] charArray) {
        StringBuilder sb = new StringBuilder();

        for (char[] row : charArray) {
            for (char c : row) {
                sb.append(c);
            }
            sb.append('\n');
        }

        return sb.toString();
    }
    
    public static void PrintCharArray(char[][] charArray) {
        for (char[] row : charArray) {
            for (char c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
    }
    
    public static char[][] CloneCharArray(char[][] originalArray) {
        int rows = originalArray.length;
        int cols = originalArray[0].length;

        char[][] clonedArray = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            clonedArray[i] = originalArray[i].clone();
        }

        return clonedArray;
    }


    static Pair GetEmptySpacePair(char[][] board) {
        Pair pair = new Pair(0,0);
        do{
            pair.i = random.nextInt( P_BASE_BOARD.length );
            pair.j = random.nextInt( P_BASE_BOARD[0].length );
        }while(board[pair.i][pair.j] != ' ');
        
        return pair;
    }
    
    public static Pair FindCharacterPairIndexBased(char[][] board, int characterID, int specificCount) {
        int rows = board.length;
        int columns = board[0].length;
        
        Pair pair = new Pair(0,0);
        int currentCount = 0;
        
        ArrayList<Character> chars = null;
        switch(characterID){
            case 0 -> chars = playerChars;
            case 1 -> chars = boxChars;
            case 2 -> chars = goalChars;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                char currentChar = board[i][j];
                if (chars.contains(currentChar)) {
                    if(currentCount == specificCount){
                        pair.i = i;
                        pair.j = j;
                        return pair;
                    }
                    
                    currentCount++;
                }
            }
        }

        // El carácter no ha sido encontrado
        System.out.println("Carácter no encontrado en la matriz.");
        return null;
    }
    
    //ID 0: Player, ID 1: Boxes, ID 2: Goals
    public static int CountCharacters(int characterID, char[][] board) {
        int rows = board.length;
        int columns = board[0].length;
        int count = 0;
        
        ArrayList<Character> chars = null;
        switch(characterID){
            case 0 -> chars = playerChars;
            case 1 -> chars = boxChars;
            case 2 -> chars = goalChars;
        }
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                char currentChar = board[i][j];
                if (chars.contains(currentChar)) {
                    count++;
                }
            }
        }

        return count; // Carácter no encontrado
    }
}
