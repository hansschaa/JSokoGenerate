//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator;


import SokoGenerator.Tree.CrossPair;
import SokoGenerator.Tree.Pair;
import de.sokoban_online.jsoko.leveldata.solutions.Solution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jenes.GeneticAlgorithm;
import jenes.chromosome.Chromosome;
import jenes.tutorials.utils.Utils;

public class SokobanChromosome implements Chromosome<SokobanChromosome> {
    public char[][] genes;
    public int fitnessValue;
    
    public SokobanChromosome(char[][] boardData) {
        this.genes = boardData;
    }



    @Override
    public SokobanChromosome clone() {
        System.out.println("clone");
        //Clone current board state
        char[][] cloneBoard = GeneratorUtils.CloneCharArray(genes);
       
        return new SokobanChromosome(cloneBoard);
    }

    @Override
    public int length() {
        return this.genes.length * this.genes[0].length;
    }

    @Override
    public void randomize() {
        System.out.println("randomize 2");
    }

    @Override
    public void randomize(int pos) {
        System.out.println("randomize 1");
        switch (pos) {
            case 0 -> {
                Generator.totalChangeBoxOrGoalCount++;
                this.ChangeBoxOrGoal();
            }
            case 1 -> {
                Generator.totalChangePlayerMutationCount++;
                this.ChangePlayer();
            }
        }

    }

    public void ChangeBoxOrGoal() {
        
        //Clone current board state
        char[][] cloneBoard = GeneratorUtils.CloneCharArray(genes);
        
        //Change box or goal?
        int boxOrGoalChoice = 0;
        boxOrGoalChoice = Generator.random.nextInt(2);
         
        //Get max boxes
        int maxBoxesOrGoals = GeneratorUtils.CountCharacters(boxOrGoalChoice == 0 ? 1 : 2, cloneBoard);
        //System.out.println("maxBoxesOrGoals: " + maxBoxesOrGoals);
        int id = Generator.random.nextInt(maxBoxesOrGoals); 
       
        //Find Box or goal
        Pair boxOrGoalPair = GeneratorUtils.FindCharacterPairIndexBased(cloneBoard, boxOrGoalChoice == 0 ? 1 : 2,id);
        
        //Find a new place
        Pair newBoxOrGoalPair = GeneratorUtils.GetEmptySpacePair(cloneBoard);
        
        //Update board
        if(boxOrGoalChoice == 0){
            if(cloneBoard[boxOrGoalPair.i][boxOrGoalPair.j] == '*')
                cloneBoard[boxOrGoalPair.i][boxOrGoalPair.j] = '.';
            else
                cloneBoard[boxOrGoalPair.i][boxOrGoalPair.j] = ' ';
                
            cloneBoard[newBoxOrGoalPair.i][newBoxOrGoalPair.j] = '$';
        }
        
        else if(boxOrGoalChoice == 1){
            if(cloneBoard[boxOrGoalPair.i][boxOrGoalPair.j] == '*')
                cloneBoard[boxOrGoalPair.i][boxOrGoalPair.j] = '$';
            else
                cloneBoard[boxOrGoalPair.i][boxOrGoalPair.j] = ' ';
                
            cloneBoard[newBoxOrGoalPair.i][newBoxOrGoalPair.j] = '.';
        }
        
        //Effective mutation
        if(Generator.GetSolution(cloneBoard, false, maxBoxesOrGoals) != null){
            if(boxOrGoalChoice == 0){
                if(genes[boxOrGoalPair.i][boxOrGoalPair.j] == '*')
                    genes[boxOrGoalPair.i][boxOrGoalPair.j] = '.';
                else
                    genes[boxOrGoalPair.i][boxOrGoalPair.j] = ' ';

                genes[newBoxOrGoalPair.i][newBoxOrGoalPair.j] = '$';
            }

            else if(boxOrGoalChoice == 1){
                if(genes[boxOrGoalPair.i][boxOrGoalPair.j] == '*')
                    genes[boxOrGoalPair.i][boxOrGoalPair.j] = '$';
                else
                    genes[boxOrGoalPair.i][boxOrGoalPair.j] = ' ';

                genes[newBoxOrGoalPair.i][newBoxOrGoalPair.j] = '.';
            }
        }
    }

    private void ChangePlayer() {
        
        char[][] cloneBoard = GeneratorUtils.CloneCharArray(genes);
        
        //Find player
        Pair playerPos = GeneratorUtils.FindCharacterPairIndexBased(genes, 0,0);
        
        //Find a new place
        Pair newPlayerPlace = GeneratorUtils.GetEmptySpacePair(genes);
        
        //Update board
        if(cloneBoard[playerPos.i][playerPos.j] == '+')
            cloneBoard[playerPos.i][playerPos.j] = '.';
        else
            cloneBoard[playerPos.i][playerPos.j] = ' ';
        
        cloneBoard[newPlayerPlace.i][newPlayerPlace.j] = '@';
        
        int boxesCount = GeneratorUtils.CountCharacters(1, cloneBoard);
        
        //Effective mutation
        if(Generator.GetSolution(cloneBoard, false, boxesCount) != null){
            //Update board
            if(genes[playerPos.i][playerPos.j] == '+')
                genes[playerPos.i][playerPos.j] = '.';
            else
                genes[playerPos.i][playerPos.j] = ' ';
            
            genes[newPlayerPlace.i][newPlayerPlace.j] = '@';
        }
    }

    public boolean Invert(MyBoxData boxData) {
        /*System.out.println(boxData);
        System.out.println("Invert antes");
        boxData.PrintValues();
        SokobanChromosomeUtils.PrintValue(this.genes);
        Pair boxPos = (Pair)boxData.boxRoute.get(boxData.boxRouteIndex);
        Pair goalPos = (Pair)boxData.boxRoute.get(boxData.goalRouteIndex);
        char boxTile = this.genes[boxPos.i][boxPos.j];
        char goalTile = this.genes[goalPos.i][goalPos.j];
        if (goalTile == '.' && boxTile == '$') {
            char[][] newGenes = (char[][])Arrays.stream(this.genes).map((rec$) -> {
                return (char[])((char[])rec$).clone();
            }).toArray((x$0) -> {
                return new char[x$0][];
            });
            newGenes[boxPos.i][boxPos.j] = goalTile;
            newGenes[goalPos.i][goalPos.j] = boxTile;
            if (this.boxDatas.size() != SokobanChromosomeUtils.GetBoxCount(this.genes)) {
                return false;
            } else {
                Solution sol = Generator.sokobanGA.GetAnySolution(newGenes, this.boxDatas.size());
                if (sol != null) {
                    int aux = boxData.goalRouteIndex;
                    boxData.goalRouteIndex = boxData.boxRouteIndex;
                    boxData.boxRouteIndex = aux;
                    boxData.UpdatePos();
                    this.genes[boxData.box.i][boxData.box.j] = '$';
                    this.genes[boxData.goal.i][boxData.goal.j] = '.';
                    System.out.println("Invert despues");
                    boxData.PrintValues();
                    SokobanChromosomeUtils.PrintValue(this.genes);
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }*/
        
        return false;
    }

    public void setAs(SokobanChromosome chromosome) {
        /*System.out.println("setas");*/
        genes = GeneratorUtils.CloneCharArray(chromosome.genes);
    }

    public void cross(SokobanChromosome chromosome, int from, int to) {
        System.out.println("cross 1");
    }

    public String replace(String str, int index, char replace) {
        if (str == null) {
            return str;
        } else if (index >= 0 && index < str.length()) {
            char[] chars = str.toCharArray();
            chars[index] = replace;
            return String.valueOf(chars);
        } else {
            return str;
        }
    }

    @Override
    public void cross(SokobanChromosome chromosome, int from) {
        System.out.println("cross");

        //Setup
        Solution solution;
        ArrayList<CrossPair> interestingPivotsList = new ArrayList<>();
        char[][] cloneBoard = GeneratorUtils.CloneCharArray(genes);
        
        //Get candidates
        interestingPivotsList = GetInterestingPivots(chromosome.genes);
        
        //Crossover
        if(interestingPivotsList.isEmpty()){
            System.out.println("-> interestingPivotsList de largo 0");
        }
            
        else{
            //Select a random region
            var randomInterestingPivot = interestingPivotsList.get(Generator.random.nextInt(interestingPivotsList.size()));

            //Put random region in clone
            for(int i = 0 ; i < Generator.P_CROSS_SPACING ; i++){
                var charClone =  chromosome.genes
                        [randomInterestingPivot.pair.i][randomInterestingPivot.pair.j];

                cloneBoard[randomInterestingPivot.pair.i][randomInterestingPivot.pair.j] = charClone;

                randomInterestingPivot.pair = randomInterestingPivot.pair.plus(randomInterestingPivot.dir);
            }
            
            //Check if clone is legal
            boolean isLegal = IsLegal(cloneBoard);
            if(isLegal){
                int boxCount = GeneratorUtils.CountCharacters(1, cloneBoard);
                solution = Generator.GetSolution(genes, false, boxCount);
                if(solution != null){
                     genes = GeneratorUtils.CloneCharArray(cloneBoard);
                }
            }
            else{
                //repair illegal
                RepairIllegal(cloneBoard);
                
                //Retry
                int boxCount = GeneratorUtils.CountCharacters(1, cloneBoard);
                solution = Generator.GetSolution(genes, false, boxCount);
                if(solution != null){
                     genes = GeneratorUtils.CloneCharArray(cloneBoard);
                }
            }
        }
    }
    
    public void RepairIllegal(char[][] cloneBoard){
        
        System.out.println("----------------------");
        System.out.println("Antes");
        GeneratorUtils.PrintCharArray(cloneBoard);
        
        //Check illegality
        int playerCount = GeneratorUtils.CountCharacters(0, cloneBoard);
        int boxCount = GeneratorUtils.CountCharacters(1, cloneBoard);
        int goalCount = GeneratorUtils.CountCharacters(2, cloneBoard);
        
        //For player
        if(playerCount > 1){
            int specificPlayerCount = Generator.random.nextInt(2);
            Pair playerIndex = GeneratorUtils.FindCharacterPairIndexBased(cloneBoard, 0, specificPlayerCount);
            if(cloneBoard[playerIndex.i][playerIndex.j] == '+')
                cloneBoard[playerIndex.i][playerIndex.j] = '.';
            else
                cloneBoard[playerIndex.i][playerIndex.j] = ' ';
        }
        else if (playerCount == 0){
            Pair emptySpace = GeneratorUtils.GetEmptySpacePair(cloneBoard);
            cloneBoard[emptySpace.i][emptySpace.j] = '@';
        }
        
        //For boxes
        if(boxCount > goalCount){
            int diff = boxCount - goalCount;
            
            while(diff != 0){
                
                Pair emptySpace = GeneratorUtils.GetEmptySpacePair(cloneBoard);
                cloneBoard[emptySpace.i][emptySpace.j] = '.';
                goalCount++;
                diff--;
            }
        }
        
        else if(boxCount == 0 ){
            Pair emptySpace = GeneratorUtils.GetEmptySpacePair(cloneBoard);
            cloneBoard[emptySpace.i][emptySpace.j] = '$';
            boxCount++;
        }
        
        //For goals
        if(goalCount > boxCount){
            int diff = goalCount - boxCount;
            
            while(diff != 0){
                
                Pair emptySpace = GeneratorUtils.GetEmptySpacePair(cloneBoard);
                cloneBoard[emptySpace.i][emptySpace.j] = '$';
                boxCount++;
                diff--;
            }
        }
        
        else if(goalCount == 0 ){
            Pair emptySpace = GeneratorUtils.GetEmptySpacePair(cloneBoard);
            cloneBoard[emptySpace.i][emptySpace.j] = '.';
            goalCount++;
        }
        
        System.out.println("Despúes");
        GeneratorUtils.PrintCharArray(cloneBoard);
        System.out.println("----------------------");
    }
    
    public Pair GetPivot(char[][] otherGenes, Pair dir){
        
        //setup
        Pair pivot = new Pair(0,0);
                
        //Select tiles region
        boolean isColliding = true;
        do{
            pivot = GeneratorUtils.GetEmptySpacePair(otherGenes);
            
            isColliding = IsCollided(pivot, dir);
            
        }while(isColliding);
               
        return pivot;
    }
    
    public Pair GetInterestingPivot(char[][] otherGenes, Pair dir){
        
        //setup
        Pair interestingPivot = new Pair(0,0);
        Pair nextToPivot = new Pair(0,0);
        
        int boxesCount = GeneratorUtils.CountCharacters(1, otherGenes);
        int goalsCount = GeneratorUtils.CountCharacters(2, otherGenes);
        
        //Select tiles region

        do{
            //0 for player, 1 for boxes, 2 for goals
            int elementID = Generator.random.nextInt(3);
            
            //Get Specific ID
            int specificID = 0;
            switch(elementID){
                case 0 -> specificID = 0;
                case 1 -> specificID = Generator.random.nextInt(boxesCount);
                case 2 -> specificID = Generator.random.nextInt(goalsCount);
            }
            
            interestingPivot = GeneratorUtils.FindCharacterPairIndexBased(otherGenes, elementID, specificID);
            nextToPivot = interestingPivot.plus(dir);
            
        }while(IsOutside(nextToPivot));
               
        return interestingPivot;
    }
    
    public ArrayList<CrossPair> GetInterestingPivots(char[][] otherGenes){
        
        //setup
        /*Pair interestingPivot = new Pair(0,0);
        Pair nextToPivot = new Pair(0,0);
        
        int boxesCount = GeneratorUtils.CountCharacters(1, otherGenes);
        int goalsCount = GeneratorUtils.CountCharacters(2, otherGenes);
        
        //Select tiles region

        do{
            //0 for player, 1 for boxes, 2 for goals
            int elementID = Generator.random.nextInt(3);
            
            //Get Specific ID
            int specificID = 0;
            switch(elementID){
                case 0 -> specificID = 0;
                case 1 -> specificID = Generator.random.nextInt(boxesCount);
                case 2 -> specificID = Generator.random.nextInt(goalsCount);
            }
            
            interestingPivot = GeneratorUtils.FindCharacterPairIndexBased(otherGenes, elementID, specificID);
            nextToPivot = interestingPivot.plus(dir);
            
        }while(IsOutside(nextToPivot));
               
        return interestingPivot;*/
        
        System.out.println("GetInterestingPivots...");
        
        ArrayList<CrossPair> interestingPivots = new ArrayList<>();
        var pivot = new Pair(0,0);
        var pivotAux = new Pair(0,0);
        var right = new Pair(0,1);
        var down = new Pair(1,0);
        ArrayList<Character> regionChars = new ArrayList<>();
        
        for(int i = 0 ; i < otherGenes.length ; i++){
            for(int j = 0 ; j < otherGenes[0].length ; j++){
                
                pivot = new Pair(i,j);
                if(otherGenes[pivot.i][pivot.j] == '#') continue;
                
                pivotAux.i = pivot.i;
                pivotAux.j = pivot.j;

                regionChars.clear();
                //Check horizontal
                for(int k = 0 ; k < Generator.P_CROSS_SPACING; k++){
                   
                    if(pivotAux.j > otherGenes[0].length - Generator.P_CROSS_SPACING || otherGenes[pivotAux.i][pivotAux.j] == '#') {
                        break;
                    }
                    
                    regionChars.add(otherGenes[pivotAux.i][pivotAux.j]);
                    pivotAux = pivotAux.plus(right);

                }
                
                //Try add
                if(regionChars.size() == Generator.P_CROSS_SPACING){
                    if((regionChars.contains('@') || regionChars.contains('+') || regionChars.contains('$') || 
                           regionChars.contains('.') || regionChars.contains('*')) && !regionChars.contains('#')){
                        interestingPivots.add(new CrossPair( new Pair(pivot.i, pivot.j), new Pair(0,1)));
                    }
                }
                
                pivotAux.i = pivot.i;
                pivotAux.j = pivot.j;
                regionChars.clear();
                //Check vertical
                for(int k = 0 ; k < Generator.P_CROSS_SPACING; k++){
                    
                    if(pivotAux.i > otherGenes.length - Generator.P_CROSS_SPACING || otherGenes[pivotAux.i][pivotAux.j] == '#'){
                        break;
                    } 
                    
                    regionChars.add(otherGenes[pivotAux.i][pivotAux.j]);
                    pivotAux = pivotAux.plus(down);
                }
                
                  //Try add
                if(regionChars.size() == Generator.P_CROSS_SPACING){
                    if(regionChars.contains('@') || regionChars.contains('+') || regionChars.contains('$') || 
                           regionChars.contains('.') || regionChars.contains('*')){
                    interestingPivots.add(new CrossPair( new Pair(pivot.i, pivot.j), new Pair(1,0)));
                    }
                }
            }
        }
        
        /*System.out.println("Mi tablero es: ");
        GeneratorUtils.PrintCharArray(otherGenes);
        System.out.println("Candidatos son: ");
        for(CrossPair crossPair : interestingPivots){
            crossPair.Print();
            System.out.println("");
        }*/
        
        return interestingPivots;
    }
    
    private boolean IsCollided(Pair pivot, Pair dir) {
        
        var spacing = Generator.P_CROSS_SPACING;
        Pair current = pivot.plus(dir);
        for(int i=0; i != spacing; i++){
        
            //check if current is outside
            if(IsOutside(current)){
                return true;
            }
               
            else{
                if(genes[current.i][current.j]=='#')
                    return true;
                
                current.plus(dir);
            } 
        }
        
        return false;
    }
    
    public boolean IsOutside(Pair current){
    
        if(current.i < 0 || current.i >= genes.length)
            return true;
        
        else if(current.j < 0 || current.j >= genes[0].length)
            return true;
            
        return false;
    }
    
        public boolean IsLegal(char[][] board){
    
        int playerCount =  GeneratorUtils.CountCharacters(0, board);
        int boxCount = GeneratorUtils.CountCharacters(1, board);
        int goalCount = GeneratorUtils.CountCharacters(2, board);
      
        System.out.println("playercount: " + playerCount);
        System.out.println("boxCount: " + boxCount);
        System.out.println("goalCount: " + goalCount);
        if(playerCount != 1)
            return false;
        
        if(boxCount == 0 || goalCount == 0)
            return false;
        
        if(boxCount != goalCount)
            return false;
        
        return true;
    }

    public void UniformCrossover(SokobanChromosome chromosome) {
        System.out.println("UniformCrossover");
        MyBoxData[] boxToPass_1 = null;
        MyBoxData[] boxToPass_2 = null;

        try {
            int r1 = this.GetRandomBoxesCountToPass(this);
            int r2 = this.GetRandomBoxesCountToPass(chromosome);
            boxToPass_1 = this.GetRandomBoxData(this, r1);
            boxToPass_2 = this.GetRandomBoxData(chromosome, r2);
        } catch (Exception var9) {
            System.out.println(var9);
            var9.printStackTrace();
            System.out.println();
        }

        try {
            this.UCrossover(boxToPass_2, this, chromosome);
        } catch (Exception var8) {
            System.out.println(var8);
            var8.printStackTrace();
            System.out.println();
        }

        try {
            this.UCrossover(boxToPass_1, chromosome, this);
        } catch (Exception var7) {
            System.out.println(var7);
            var7.printStackTrace();
            System.out.println();
        }

    }

    public void UCrossover(MyBoxData[] candidatesBoxDatas, SokobanChromosome destChromosome, SokobanChromosome sourceChromosome) {
        /*System.out.println("UCrossover");
        MyBoxData[] var5 = candidatesBoxDatas;
        int var6 = candidatesBoxDatas.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            MyBoxData candidateBoxData = var5[var7];
            boolean sucessBox = candidateBoxData.TrySetBoxPosInRoute(destChromosome);
            if (sucessBox) {
                System.out.println("Succes box");
                if (sourceChromosome.boxDatas.size() > 1) {
                    this.RemoveBox(sourceChromosome, candidateBoxData);
                }

                this.AddBox(destChromosome, candidateBoxData);
            }
        }

        SokobanChromosomeUtils.PrintValue(destChromosome.genes);
        Iterator var9 = destChromosome.boxDatas.iterator();

        while(var9.hasNext()) {
            MyBoxData b = (MyBoxData)var9.next();
            b.PrintValues();
        }*/

    }

    public void AddBox(SokobanChromosome sokobanChromosome, MyBoxData boxData) {
        /*System.out.println("AddBox");
        sokobanChromosome.boxDatas.add(new MyBoxData(boxData.goal, boxData.box, boxData.boxRoute, boxData.goalRouteIndex, boxData.boxRouteIndex));
        System.out.println("end AddBox");*/
    }

    private void RemoveBox(SokobanChromosome sourceChromosome, MyBoxData candidatesBoxData) {
        /*Iterator var3 = sourceChromosome.boxDatas.iterator();

        while(var3.hasNext()) {
            MyBoxData boxData = (MyBoxData)var3.next();
            if (SokobanChromosomeUtils.IsCollision(boxData.box, candidatesBoxData.box) && SokobanChromosomeUtils.IsCollision(boxData.goal, candidatesBoxData.goal)) {
                Pair boxPos = boxData.box;
                char boxChar = sourceChromosome.genes[candidatesBoxData.box.i][candidatesBoxData.box.j];
                Pair goalPos = boxData.goal;
                char goalChar = sourceChromosome.genes[candidatesBoxData.goal.i][candidatesBoxData.goal.j];
                switch (boxChar) {
                    case '$':
                        sourceChromosome.genes[boxPos.i][boxPos.j] = ' ';
                        break;
                    case '*':
                        sourceChromosome.genes[boxPos.i][boxPos.j] = '.';
                }

                switch (goalChar) {
                    case '*':
                        sourceChromosome.genes[goalPos.i][goalPos.j] = '$';
                        break;
                    case '+':
                        sourceChromosome.genes[goalPos.i][goalPos.j] = '@';
                    case ',':
                    case '-':
                    default:
                        break;
                    case '.':
                        sourceChromosome.genes[goalPos.i][goalPos.j] = ' ';
                }

                sourceChromosome.boxDatas.remove(boxData);
                break;
            }
        }*/

    }

    public char[][] UpdateGenes(MyBoxData boxData, char[][] chromosomeGenes) {
        char[][] backup = (char[][])Arrays.stream(chromosomeGenes).map((rec$) -> {
            return (char[])((char[])rec$).clone();
        }).toArray((x$0) -> {
            return new char[x$0][];
        });
        Pair box = boxData.box;
        Pair goal = boxData.goal;
        if (backup[box.i][box.j] == ' ') {
            backup[box.i][box.j] = '$';
        } else if (backup[box.i][box.j] == '.') {
            backup[box.i][box.j] = '*';
        }

        if (backup[goal.i][goal.j] == ' ') {
            backup[goal.i][goal.j] = '.';
        } else if (backup[goal.i][goal.j] == '$') {
            backup[goal.i][goal.j] = '*';
        } else if (backup[goal.i][goal.j] == '@') {
            backup[goal.i][goal.j] = '+';
        }

        if (SokobanChromosomeUtils.IsCollision(goal, box)) {
            backup[goal.i][goal.j] = '*';
        }

        System.out.println("Fin UpdateGenes");
        return backup;
    }

    public int GetRandomBoxesCountToPass(SokobanChromosome chromosome) {
        //return chromosome.boxDatas.size() > 2 ? Generator.random.nextInt(chromosome.boxDatas.size() - 1) + 1 : Generator.random.nextInt(chromosome.boxDatas.size()) + 1;
        return 0;
    }

    public MyBoxData[] GetRandomBoxData(SokobanChromosome chromosome, int r) {
        /*MyBoxData[] temp = new MyBoxData[r];

        for(int i = 0; i < r; ++i) {
            int randomIndex = Generator.random.nextInt(chromosome.boxDatas.size());
            MyBoxData bd = (MyBoxData)chromosome.boxDatas.get(randomIndex);
            temp[i] = new MyBoxData(bd.goal, bd.box, bd.boxRoute, bd.goalRouteIndex, bd.boxRouteIndex);
        }

        return temp;*/
        return new MyBoxData[r];
    }

    public boolean equals(SokobanChromosome chromosome) {
        char[][] otherGenes = chromosome.genes;

        for(int i = 0; i < this.genes.length; ++i) {
            for(int j = 0; j < this.genes[0].length; ++j) {
                if (this.genes[i][j] != otherGenes[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public void difference(SokobanChromosome chromosome, double[] diff) {
        System.out.println("difference");
    }

    public Object[] toArray() {
        System.out.println("toArray");
        return null;
    }

    public int GetBoxChanges() {
        System.out.println("GetBoxChanges");
        int total = 0;
        return total;
    }
    
        public void swap(int pos1, int pos2) {
        System.out.println("swap");
    }

    public void leftShift(int from, int to) {
        System.out.println("leftShift");
    }

    public void rightShift(int from, int to) {
        System.out.println("rightShift");
    }

    public void setDefaultValueAt(int pos) {
        System.out.println("setDefaultValueAt");
    }


}
