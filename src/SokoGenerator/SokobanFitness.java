//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator;

import de.sokoban_online.jsoko.JSoko;
import de.sokoban_online.jsoko.leveldata.solutions.Solution;
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
            
            //var counterIntuitiveMoves = GeneratorUtils.GetCounterIntuitiveMoves(chromosome.genes, solution.lurd);
            //individual.setScore(counterIntuitiveMoves);
            //chromosome.counterIntuitives = counterIntuitiveMoves;
            
            //var counterIntuitiveMoves = GetCounterIntuitiveMoves(chromosome.genes, solution.lurd);
            individual.setScore(this.application.movesHistory.getPushesCount());
            
            
            chromosome.pushes = this.application.movesHistory.getPushesCount();
            
        }
            
        else
            individual.setScore(-1);
        
        
      
        //int movesCount = this.application.movesHistory.getMovementsCount();
        //int pushesCount = this.application.movesHistory.getPushesCount();
        /*chromosome.moves = movesCount;
        chromosome.pushes = pushesCount;*/
    }

        
        
        
 
    
    
    
    
}
