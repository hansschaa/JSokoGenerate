//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator;

import de.sokoban_online.jsoko.JSoko;
import de.sokoban_online.jsoko.leveldata.Level;
import de.sokoban_online.jsoko.solver.Solver;
import java.io.PrintStream;
import jenes.GenerationEventListener;
import jenes.GeneticAlgorithm;
import jenes.population.Population;

public class SokobanGA extends GeneticAlgorithm<SokobanChromosome> implements GenerationEventListener<SokobanChromosome> {
    private JSoko application;
    private Generator generator;
    private Solver solver;
    private Level solverLevel;
    public SokobanFitness sokobanFitness;

    public SokobanGA(Population<SokobanChromosome> importedPopulation, int GENERATION_LIMIT, JSoko application, Generator generator) {
        super(importedPopulation, GENERATION_LIMIT);
        
        //Setup fitness
        this.sokobanFitness = new SokobanFitness(true, application, generator);
        this.setFitness(this.sokobanFitness);
        
        this.application = application;
        this.generator = generator;
        this.solverLevel = new Level(application.levelIO.database);
        this.solverLevel.setLevelTitle("SolverLevel");
        this.addGenerationEventListener(this);
    }

    protected void onStop(long numGen) {
        System.out.println("Stop gen");
        //this.application.generatorGUI.OnGenerationGUIEnd();
        this.generator.OnGenerationEnd();
    }

    @Override
    public void onGeneration(GeneticAlgorithm<SokobanChromosome> ga, long time) {
        Population.Statistics stat = ga.getCurrentPopulation().getStatistics();
        Population.Statistics.Group legals = stat.getGroup(Population.LEGALS);
        System.out.println("Current generation: " + ga.getGeneration());
        PrintStream var10000 = System.out;
        double[] var10001 = legals.getMax();
        var10000.println("\tBest score: " + var10001[0]);
        var10000 = System.out;
        var10001 = legals.getMin();
        var10000.println("\tWorst score: " + var10001[0]);
        var10000 = System.out;
        var10001 = legals.getMean();
        var10000.println("\tAvg score : " + var10001[0]);
    }
}
