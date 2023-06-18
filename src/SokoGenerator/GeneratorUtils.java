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
    private static final ArrayList<Character> goalChars = new ArrayList<>(Arrays.asList('*', '.', '+'));

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
        GeneratorUtils.PrintCharArray(board);
        System.out.println("Carácter " + characterID + " de specific id: " + specificCount+  " no encontrado en la matriz.");
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
    
    public static int GetBoardManhattanDistance(char[][] genes){
    
        int manhattanDistance = 0;
        int cont = 0;
        int maxBox = GeneratorUtils.CountCharacters(1, genes);
        
        Pair boxPair, goalPair;
        
        while(cont < maxBox){
        
            //Get cont box and goal pair
            boxPair = GeneratorUtils.FindCharacterPairIndexBased(genes, 1, cont);
            goalPair = GeneratorUtils.FindCharacterPairIndexBased(genes, 2, cont);
            
            manhattanDistance += GetManhattamDistance(boxPair, goalPair);
            
            cont++;
        }
     
        return manhattanDistance;
    }
    
    public static int GetManhattamDistance(Pair boxPair, Pair goalPair){
        return Math.abs(boxPair.i - goalPair.i) + Math.abs(boxPair.j - goalPair.j);
    }
    
    public static int GetCounterIntuitiveMoves(char[][] genes, String LURD){
    
        System.out.println("GetCounterIntuitiveMoves");
        
        int counterIntuitiveMoves = 0;
        
        //Clone current board state
        char[][] cloneBoard = GeneratorUtils.CloneCharArray(genes);
        ManhattanSokoBoard manhattanSokoBoard = new ManhattanSokoBoard(cloneBoard, 0);
        manhattanSokoBoard.globalManhattanScore = GeneratorUtils.GetBoardManhattanDistance(cloneBoard);

        boolean nextMoveIsContraintuitive = false;
        for(int i = 0 ; i < LURD.length();i++){
            nextMoveIsContraintuitive = DoMove(manhattanSokoBoard, LURD.charAt(i));
            if(nextMoveIsContraintuitive){
                nextMoveIsContraintuitive = false;
                counterIntuitiveMoves++;
            }
        }
        
        System.out.println("GetCounterIntuitiveMoves end: " + counterIntuitiveMoves);
        return counterIntuitiveMoves;
    }
    
    public static boolean DoMove(ManhattanSokoBoard manhattanSokoBoard, char charAt) {
        
        System.out.println("do move: " + charAt);
        GeneratorUtils.PrintCharArray(manhattanSokoBoard.board);
        
        //cache
        int oldGlobalManhattanScore = manhattanSokoBoard.globalManhattanScore;

        Pair dirPair = null;
        boolean moveBox = false;
        
        switch(charAt){
            case 'l' -> dirPair = new Pair(0,-1);
            case 'u' -> dirPair = new Pair(-1,0);
            case 'r' -> dirPair = new Pair(0,1);
            case 'd' -> dirPair = new Pair(1,0);
        }
        
        
        //check if next to player have a box
        Pair playerPos = GeneratorUtils.FindCharacterPairIndexBased(manhattanSokoBoard.board, 0, 0);
        Pair nextToPlayer1 = playerPos.plus(dirPair);
        Pair nextToPlayer2 = nextToPlayer1.plus(dirPair);
        int pista = 0;
        
        //Reset player pos
        if(manhattanSokoBoard.board[playerPos.i][playerPos.j] == '+'){
            manhattanSokoBoard.board[playerPos.i][playerPos.j] = '.';
            pista = 1;
  
        }
            
        else if(manhattanSokoBoard.board[playerPos.i][playerPos.j] == '@'){
            pista = 2;
            manhattanSokoBoard.board[playerPos.i][playerPos.j] = ' ';
            
            if(manhattanSokoBoard.board[nextToPlayer1.i][nextToPlayer1.j] == ' '){
                manhattanSokoBoard.board[nextToPlayer1.i][nextToPlayer1.j] = '@';
            }  
        }
            
        
        
        //Next to player
        if(manhattanSokoBoard.board[nextToPlayer1.i][nextToPlayer1.j] == '.'){
            pista = 3;
            manhattanSokoBoard.board[nextToPlayer1.i][nextToPlayer1.j] = '+';
        }
            
        else if(manhattanSokoBoard.board[nextToPlayer1.i][nextToPlayer1.j] == '$'){
            manhattanSokoBoard.board[nextToPlayer1.i][nextToPlayer1.j] = '@';
            pista = 4;
            //next to next to player
            if(manhattanSokoBoard.board[nextToPlayer2.i][nextToPlayer2.j] == '.'){
                pista = 5;
                manhattanSokoBoard.board[nextToPlayer2.i][nextToPlayer2.j] = '*';
            }
                
            if(manhattanSokoBoard.board[nextToPlayer2.i][nextToPlayer2.j] == ' '){
                pista = 6;
                manhattanSokoBoard.board[nextToPlayer2.i][nextToPlayer2.j] = '$';
            }

            moveBox = true;
        }   
        
        else if(manhattanSokoBoard.board[nextToPlayer1.i][nextToPlayer1.j] == '*'){
            pista = 300;
            manhattanSokoBoard.board[nextToPlayer1.i][nextToPlayer1.j] = '+';
            
            //next to next to player
            if(manhattanSokoBoard.board[nextToPlayer2.i][nextToPlayer2.j] == '.'){
                pista = 5;
                manhattanSokoBoard.board[nextToPlayer2.i][nextToPlayer2.j] = '*';
            }
                
            if(manhattanSokoBoard.board[nextToPlayer2.i][nextToPlayer2.j] == ' '){
                pista = 6;
                manhattanSokoBoard.board[nextToPlayer2.i][nextToPlayer2.j] = '$';
            }
        }
        
        
        else{
            pista = 100;
            manhattanSokoBoard.board[nextToPlayer1.i][nextToPlayer1.j] = '@';
        }
            
           
        
        int playerCount = GeneratorUtils.CountCharacters(0, manhattanSokoBoard.board);
        if(playerCount==0){
            GeneratorUtils.PrintCharArray(manhattanSokoBoard.board);
            System.out.println("player 0: " + pista);
            System.out.println("error");
        }
        
        
        if(moveBox){
            manhattanSokoBoard.globalManhattanScore = GeneratorUtils.GetBoardManhattanDistance(manhattanSokoBoard.board);
        
            if(manhattanSokoBoard.globalManhattanScore > oldGlobalManhattanScore){
                return true;
            }
        }
        
        
        

        return false;
    }
}



