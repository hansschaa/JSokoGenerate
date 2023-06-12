/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SokoGenerator;

/**
 *
 * @author Hans
 */
public class GeneratorUtils {
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
}
