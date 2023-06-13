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
        GeneratorUtils.PrintCharArray(chromosome.genes);
        int boxCount = GeneratorUtils.CountCharacters(1, chromosome.genes);
        
        this.solution = Generator.GetSolution(chromosome.genes, false, boxCount);
        //if(solution != null)
            individual.setScore(solution.lurd.length());
        //else
            //individual.setScore(-1);
      
        //int movesCount = this.application.movesHistory.getMovementsCount();
        //int pushesCount = this.application.movesHistory.getPushesCount();
        /*chromosome.moves = movesCount;
        chromosome.pushes = pushesCount;*/
        
    }
}
