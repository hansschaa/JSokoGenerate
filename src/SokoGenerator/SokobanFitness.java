//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator;

import SokoGenerator.Tree.Pair;
import de.sokoban_online.jsoko.JSoko;
import de.sokoban_online.jsoko.leveldata.solutions.Solution;
import java.util.ArrayList;
import jenes.population.Fitness;
import jenes.population.Individual;

public class SokobanFitness extends Fitness<SokobanChromosome> {
    private JSoko application;
    private Generator generator;
    private Solution solution;

    public SokobanFitness(boolean maximize, JSoko application, Generator generator) {
        super(new boolean[]{maximize});
        this.application = application;
        this.generator = generator;
    }

    @Override
    public void evaluate(Individual<SokobanChromosome> individual) {
        System.out.println("Evaluate");
        
        SokobanChromosome chromosome = (SokobanChromosome)individual.getChromosome();
        int boxCount = GeneratorUtils.CountCharacters(1, chromosome.genes);
        
        this.solution = Generator.GetSolution(chromosome.genes, true, boxCount);
        if(solution != null){
            
            //var counterIntuitiveMoves = GetCounterIntuitiveMoves(chromosome.genes, solution.lurd);
            
            System.out.println("LURD: " + solution.lurd);
            GeneratorUtils.PrintCharArray(chromosome.genes);
            
            var counterIntuitiveMoves = GeneratorUtils.GetCounterIntuitiveMoves(chromosome.genes, solution.lurd.toLowerCase());
            
            //individual.setScore(this.application.movesHistory.getPushesCount());
            
            individual.setScore(counterIntuitiveMoves);
            chromosome.pushes = this.application.movesHistory.getPushesCount();
            chromosome.counterIntuitives = counterIntuitiveMoves;
        }
            
        else
            individual.setScore(-1);
        
        
      
        //int movesCount = this.application.movesHistory.getMovementsCount();
        //int pushesCount = this.application.movesHistory.getPushesCount();
        /*chromosome.moves = movesCount;
        chromosome.pushes = pushesCount;*/
    }

        /*char[][] winBoard = GeneratorUtils.CloneCharArray(genes);
        //Set to win state
        for(int i = 0; i < winBoard.length;i++){
            for(int j = 0; j < winBoard[0].length;j++){
                if(winBoard[i][j] == '$'){
                    winBoard[i][j] = ' ';
                }
                else if(winBoard[i][j] == '.'){
                    winBoard[i][j] = '*';
                }
            }
        }*/
        
        
 
    
    
    
    
}
