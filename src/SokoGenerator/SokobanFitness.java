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
    private SokobanGA sokobanGA;
    private Solution solution;

    public SokobanFitness(boolean maximize, JSoko application, SokobanGA sokobanGA) {
        super(new boolean[]{maximize});
        this.application = application;
        this.sokobanGA = sokobanGA;
    }

    public void evaluate(Individual<SokobanChromosome> individual) {
        SokobanChromosome chromosome = (SokobanChromosome)individual.getChromosome();
        this.solution = this.sokobanGA.GetSolution(chromosome.genes, chromosome.boxDatas.size());
        int movesCount = this.application.movesHistory.getMovementsCount();
        int pushesCount = this.application.movesHistory.getPushesCount();
        chromosome.moves = movesCount;
        chromosome.pushes = pushesCount;
        individual.setScore(new double[]{(double)pushesCount});
    }
}
