//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator;

import de.sokoban_online.jsoko.JSoko;
import de.sokoban_online.jsoko.leveldata.Level;
import de.sokoban_online.jsoko.leveldata.LevelCollection;
import de.sokoban_online.jsoko.leveldata.solutions.Solution;
import de.sokoban_online.jsoko.solver.Solver;
import de.sokoban_online.jsoko.solver.SolverAStarPushesMoves;
import de.sokoban_online.jsoko.solver.SolverGUI;
import de.sokoban_online.jsoko.solver.AnySolution.SolverAnySolution;
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
        this.sokobanFitness = new SokobanFitness(true, application, this);
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

    Solution GetSolution(char[][] genes, int boxCount) {
        this.solverLevel.setBoardData(genes.toString());
        this.solverLevel.setHeight(genes.length);
        this.solverLevel.setWidth(genes[0].length);
        this.solverLevel.setBoxCount(boxCount);
        LevelCollection levelCollection = (new LevelCollection.Builder()).setLevels(new Level[]{this.solverLevel}).build();
        this.application.setCollectionForPlaying(levelCollection);
        this.application.setLevelForPlaying(1);
        this.application.currentLevel = this.solverLevel;
        this.solver = new SolverAStarPushesMoves(this.application, new SolverGUI(this.application));
        Solution solution = this.solver.searchSolution();
        return solution;
    }

    Solution GetAnySolution(char[][] genes, int boxCount) {
        this.solverLevel.setBoardData(genes.toString());
        this.solverLevel.setHeight(genes.length);
        this.solverLevel.setWidth(genes[0].length);
        this.solverLevel.setBoxCount(boxCount);
        LevelCollection levelCollection = (new LevelCollection.Builder()).setLevels(new Level[]{this.solverLevel}).build();
        this.application.setCollectionForPlaying(levelCollection);
        this.application.setLevelForPlaying(1);
        this.application.currentLevel = this.solverLevel;
        this.solver = new SolverAnySolution(this.application, new SolverGUI(this.application));
        Solution solution = this.solver.searchSolution();
        return solution;
    }
}
