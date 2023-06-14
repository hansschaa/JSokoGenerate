//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator;


import SokoGenerator.SokoBoard.SokoBoard;
import SokoGenerator.Tree.Pair;
import SokoGenerator.Tree.SokoNode;
import SokoGenerator.Tree.SokoTree;
import de.sokoban_online.jsoko.JSoko;
import de.sokoban_online.jsoko.leveldata.Level;
import de.sokoban_online.jsoko.leveldata.LevelCollection;
import de.sokoban_online.jsoko.leveldata.solutions.Solution;
import de.sokoban_online.jsoko.solver.AnySolution.SolverAnySolution;
import de.sokoban_online.jsoko.solver.SolverAStarPushesMoves;
import de.sokoban_online.jsoko.solver.SolverGUI;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import jenes.GeneticAlgorithm;
import jenes.population.Individual;
import jenes.population.Population;
import jenes.stage.AbstractStage;
import jenes.stage.operator.common.TournamentSelector;

public class Generator {
    
    public static JSoko application;
    public static SokobanGA sokobanGA;
    public static Random random;
    
    //Parameters
    public static final int P_MAX_GENERATIONS = 20;
    public static final int P_MAX_INDIVIDUALS = 12;
    public static final int P_TOURNAMENT_ATTEMPS = 1;
    public static final float P_CROSSOVER_PROB = 1f;
    public static int P_CROSSOVER_TOTAL;
    public static int P_CROSSOVER_FAILED;
    public static int P_MAX_BOXES = 7;
    public static int P_CROSS_SPACING = 2;
    public static final char[][] P_BASE_BOARD = {
    {'#', '#', '#', '#', '#', '#', '#'},
    {'#', ' ', ' ', ' ', ' ', ' ', '#'},
    {'#', ' ', ' ', ' ', ' ', ' ', '#'},
    {'#', ' ', ' ', ' ', ' ', ' ', '#'},
    {'#', ' ', ' ', ' ', ' ', ' ', '#'},
    {'#', ' ', ' ', ' ', ' ', ' ', '#'},
    {'#', '#', '#', '#', '#', '#', '#'}};

    
    //Stats
    public static int totalChangeBoxOrGoalCount;
    public static int totalChangePlayerMutationCount;
    public static int effectiveChangeBoxOrGoalCount;
    public static int effectiveChangePlayerMutation;
    
    //Others
    public Thread generatorThread;
    private static Level solverLevel;
    public static Solution sol;
    public static SolverGUI solverGUI;
    public static ArrayList<SokoTree> sokoTrees = new ArrayList();
    public static ArrayList<Pair> goalCandidates;
    public static LevelCollection levelCollection;
    private static Population<SokobanChromosome> importedPopulation;
    private ArrayList<SokobanChromosome> sokobanChromosomeList;

    public Generator(JSoko application) throws FileNotFoundException {
        Generator.application = application;
        //Generator.optimalSolver = new SolverAStarPushesMoves(application, new SolverGUI(application));
        //Generator.anySolutionSolver = new SolverAnySolution(application, new SolverGUI(application));
        Generator.solverLevel = new Level(application.levelIO.database);
        Generator.solverGUI = new SolverGUI(application);
        Generator.importedPopulation = new Population<>();
        random = new Random();
        sokobanChromosomeList = new ArrayList<>();
        
        Generator.solverLevel.setHeight(P_BASE_BOARD.length);
        Generator.solverLevel.setWidth(P_BASE_BOARD[0].length);
        
        GetInitialPopulation();
        // Ordenar el ArrayList de cromosomas por fitness de mayor a menor
        Collections.sort(sokobanChromosomeList, new Comparator<SokobanChromosome>() {
            @Override
            public int compare(SokobanChromosome c1, SokobanChromosome c2) {
                // Comparar los valores de fitness de los cromosomas en orden descendente
                return Double.compare(c2.fitnessValue, c1.fitnessValue);
            }
        });
        
        List<SokobanChromosome> topTierSokobanChromosomeList = sokobanChromosomeList.subList(0, 
                Math.min(P_MAX_INDIVIDUALS, sokobanChromosomeList.size()));
        sokobanChromosomeList = null;
        
        //GC
        System.gc();
        System.runFinalization();
        Runtime.getRuntime().gc();
        
        //Setup the 
        for(SokobanChromosome sc : topTierSokobanChromosomeList)
            importedPopulation.add(new Individual<SokobanChromosome>(sc));
        
        System.out.println("Población inicial");
        for(Individual<SokobanChromosome> sc : Generator.importedPopulation){
            GeneratorUtils.PrintCharArray(sc.getChromosome().genes);
            System.out.println("fitness: " + sc.getChromosome().fitnessValue);
            System.out.println("\n");
        }

        RunGA();
    }
    
    private Population<SokobanChromosome> GetInitialPopulation() {
        Population<SokobanChromosome> initialPopulation = new Population();

        SokobanChromosome sokobanChromosome = null;
        Solution solution = null; 
        
        int notSolutionCount = 0;
        int totalAttempts = 60;
        
        for(int i = 0 ; i < totalAttempts ; i++){
            do{
                sokobanChromosome = GetRandomInitialSokobanChromosome();
                GeneratorUtils.PrintCharArray(sokobanChromosome.genes);
                solution = GetSolution(sokobanChromosome.genes, false, 1);
                if(solution == null)
                    notSolutionCount++;
   
            }while(solution == null);

            sokobanChromosome.fitnessValue = solution.lurd.length();
            sokobanChromosomeList.add(sokobanChromosome);
            
            solution = null;
        }
        
        System.out.println("Sin solución totales: " + notSolutionCount);
        System.out.println("Total: " + totalAttempts);
        
        return initialPopulation;
    }
    
    static Solution GetSolution(char[][] genes, boolean optimal, int boxCount) {

        Generator.solverLevel.setBoardData(GeneratorUtils.ConvertCharArrayToString(genes));
        Generator.solverLevel.setBoxCount(boxCount);
        Generator.levelCollection = (new LevelCollection.Builder()).setLevels(new Level[]{Generator.solverLevel}).build();
        Generator.application.setCollectionForPlaying(levelCollection);
        Generator.application.setLevelForPlaying(1);
        Generator.application.currentLevel = Generator.solverLevel;
        
        //Generator.anySolutionSolver = any//new SolverAnySolution(Generator.application, Generator.solverGUI);
        Generator.sol = new SolverAnySolution(Generator.application, Generator.solverGUI).searchSolution();
        
        return Generator.sol;
    }

    private SokobanChromosome GetRandomInitialSokobanChromosome() {
        
        char[][] baseBoardClone = GeneratorUtils.CloneCharArray(P_BASE_BOARD);
         
        Pair boxPair = GeneratorUtils.GetEmptySpacePair(baseBoardClone);
        baseBoardClone[boxPair.i][boxPair.j] = '$';
        
        Pair goalPair = GeneratorUtils.GetEmptySpacePair(baseBoardClone);
        baseBoardClone[goalPair.i][goalPair.j] = '.';
        
        Pair playerPair = GeneratorUtils.GetEmptySpacePair(baseBoardClone);
        baseBoardClone[playerPair.i][playerPair.j] = '@';

        SokobanChromosome newRandChromosome = new SokobanChromosome(baseBoardClone);
        
        return newRandChromosome;
    }

    
    public void Init() throws FileNotFoundException {
        System.out.println("Init...");
        /*if (!this.RunGA()) {
            this.Init();
        }*/

    }

    public void RunGA() {
        System.out.println("----> Run GA....");
        sokobanGA = new SokobanGA(Generator.importedPopulation, P_MAX_GENERATIONS, application, this);
        
        SetupEvolutionaryAlgorithm();
        
        this.generatorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Generator.sokobanGA.evolve();
            }
        });
        this.generatorThread.start();
        
        /*boardBase = (char[][])this.sokoBoard.levels.get(0);
        this.Preprocess();
        if (sokoTrees.size() == 0) {
            return false;
        } else {
            this.ProcessSokoTrees();

            for(int i = 0; i < this.importedPopulation.size(); ++i) {
                System.out.println("Tablero: " + (i + 1));
                SokobanChromosome chromosome = (SokobanChromosome)this.importedPopulation.getIndividual(i).getChromosome();
                SokobanChromosomeUtils.PrintLongestRoute(chromosome.genes, ((MyBoxData)chromosome.boxDatas.get(0)).boxRoute, ((MyBoxData)chromosome.boxDatas.get(0)).box, ((MyBoxData)chromosome.boxDatas.get(0)).goal, ((MyBoxData)chromosome.boxDatas.get(0)).boxRoute.size() - 1);
                System.out.println();
            }

            AbstractStage<SokobanChromosome> selection = new TournamentSelector(1);
            AbstractStage<SokobanChromosome> crossover = new SokoCrossover(2.0);
            AbstractStage<SokobanChromosome> InvertBoxGoalMutation = new InvertBoxGoalPosMutator(0.4);
            AbstractStage<SokobanChromosome> ChangePlayerPosMutation = new ChangePlayerPosMutator(0.25);
            sokobanGA.addStage(selection);
            sokobanGA.addStage(crossover);
            sokobanGA.addStage(InvertBoxGoalMutation);
            sokobanGA.addStage(ChangePlayerPosMutation);
            sokobanGA.setElitism(1);
            this.generatorThread = new Thread(new Runnable() {
                public void run() {
                    Generator.sokobanGA.evolve();
                }
            });
            this.generatorThread.start();
            return true;
        }*/
    }

    
    private void SetupEvolutionaryAlgorithm() {
        AbstractStage<SokobanChromosome> selection = new TournamentSelector(Generator.P_TOURNAMENT_ATTEMPS);
        AbstractStage<SokobanChromosome> crossover = new SokoCrossover(P_CROSSOVER_PROB);
        AbstractStage<SokobanChromosome> InvertBoxGoalMutation = new InvertBoxGoalPosMutator(0.4);
        AbstractStage<SokobanChromosome> ChangePlayerPosMutation = new ChangePlayerPosMutator(0.25);
        sokobanGA.addStage(selection);
        sokobanGA.addStage(crossover);
        sokobanGA.addStage(InvertBoxGoalMutation);
        sokobanGA.addStage(ChangePlayerPosMutation);
        sokobanGA.setElitism(1);
    }
    
    private void Preprocess() {
        /*goalCandidates = SokobanChromosomeUtils.GetTilesPosMatrix(' ', boardBase);
        double maxInitialStates = Math.ceil((double)(goalCandidates.size() / 2));
        if (maxInitialStates > 10.0) {
            maxInitialStates = 10.0;
        }

        System.out.println("----> Create " + maxInitialStates + " initial states...");

        try {
            while((double)sokoTrees.size() < maxInitialStates && goalCandidates.size() > 0) {
                int emptySpaceElementIndex = random.nextInt(goalCandidates.size());
                Pair boxPos = (Pair)goalCandidates.get(emptySpaceElementIndex);
                goalCandidates.remove(emptySpaceElementIndex);
                this.PutBox(boardBase, boxPos);
            }

            if (sokoTrees.size() == 0) {
                System.out.println("No hay posibles cajas y metas");
            }
        } catch (Exception var5) {
            PrintStream var10000 = System.out;
            String var10001 = var5.getMessage();
            var10000.println("Error: " + var10001 + " Causa: " + var5.getCause());
        }*/

    }

    public void PutBox(char[][] genes, Pair boxPos) {
        /*SokoNode root = new SokoNode((SokoNode)null, boxPos);
        SokoTree sokoTree = new SokoTree(root, genes);
        boolean haveSolution = sokoTree.InitSearch();
        if (haveSolution) {
            char[][] genes2 = (char[][])Arrays.stream(boardBase).map((rec$) -> {
                return (char[])((char[])rec$).clone();
            }).toArray((x$0) -> {
                return new char[x$0][];
            });
            Pair boxPos2 = sokoTree.box.value;
            Pair goalPos2 = sokoTree.goal.value;
            if (SokobanChromosomeUtils.IsCollision(boxPos2, goalPos2)) {
                genes2[boxPos2.i][boxPos2.j] = '*';
            } else {
                genes2[boxPos2.i][boxPos2.j] = '$';
                genes2[goalPos2.i][goalPos2.j] = '.';
            }

            this.PutPlayer(genes2, sokoTree.boxRoute);
            Solution a = sokobanGA.GetAnySolution(genes2, 1);
            if (a != null) {
                sokoTree.genes = genes2;
                sokoTrees.add(sokoTree);
            }
        } else {
            System.out.println("No tiene solución");
        }*/

    }

    public void OnGenerationEnd() {
        GeneticAlgorithm.Statistics stats = sokobanGA.getStatistics();
        Population.Statistics.Group group = sokobanGA.getCurrentPopulation().getStatistics().getGroup(Population.LEGALS);
        Individual best = group.get(0);
        double distance = best.getScore();
        System.out.format("%s\n\tended in %d ms, \n\ttime spent per fitness evaluation %d \n\tfitness evaluations performed: %d\n\ttime for fitness evaluation/total time: %f%%\n\tdistance from target: %.2f (%s)\n", "End", stats.getExecutionTime(), stats.getTimeSpentForFitnessEval(), stats.getFitnessEvaluationNumbers(), (double)stats.getTimeSpentForFitnessEval() * 100.0 / (double)stats.getExecutionTime(), distance, distance == 0.0 ? "exact matching" : "");
        this.ExportLevels();
        this.generatorThread.interrupt();
    }

    private void ExportLevels() {
        try {
            System.out.println("----> Exporting results...");
            StringBuilder sb = new StringBuilder();
            Population<SokobanChromosome> pop = sokobanGA.getLastPopulation();
            
            for(Individual<SokobanChromosome> sc : pop){
                GeneratorUtils.PrintCharArray(sc.getChromosome().genes);
                System.out.println("fitness: " + sc.getChromosome().fitnessValue);
                System.out.println("\n");
            }

            System.out.println("Total player mutation: " + totalChangeBoxOrGoalCount);
            System.out.println("Effective player mutation: " + effectiveChangeBoxOrGoalCount);
            System.out.println("Total box invert mutation: " + totalChangePlayerMutationCount);
            System.out.println("Effective box invert mutation: " + effectiveChangePlayerMutation);
            System.out.println("Total crossover: " + Generator.P_CROSSOVER_TOTAL);
            System.out.println("Total crossover failed: " + Generator.P_CROSSOVER_FAILED);
            
        } catch (Exception var11) {
            System.out.println("Error");
        }

    }

    private void ProcessSokoTrees() {
        System.out.println("----> ProcessSokoTrees...");
        Iterator var1 = sokoTrees.iterator();

        while(var1.hasNext()) {
            SokoTree sokoTree = (SokoTree)var1.next();
            Pair boxPos = sokoTree.box.value;
            Pair goalPos = sokoTree.goal.value;
            ArrayList<Pair> boxRoute = sokoTree.boxRoute;
            ArrayList<MyBoxData> boxDatas = new ArrayList();
            boxDatas.add(new MyBoxData(goalPos, boxPos, boxRoute, boxRoute.size() - 1, 0));
            SokobanChromosome sokoChromosome = new SokobanChromosome(sokoTree.genes);
            this.importedPopulation.add(new Individual(sokoChromosome, new double[0]));
        }

    }

    private char[][] PutPlayer(char[][] genes, ArrayList<Pair> boxRoute) {
        Pair boxPos = (Pair)boxRoute.get(0);
        Pair nextBoxPos = (Pair)boxRoute.get(1);
        Pair direction = boxPos.minus(nextBoxPos);
        Pair playerPos = boxPos.plus(direction);
        if (genes[playerPos.i][playerPos.j] == '.') {
            genes[playerPos.i][playerPos.j] = '+';
        } else if (genes[playerPos.i][playerPos.j] == ' ') {
            genes[playerPos.i][playerPos.j] = '@';
        }

        return genes;
    }
}
