//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator;


import SokoGenerator.Tree.Pair;
import SokoGenerator.Tree.SokoTree;
import de.sokoban_online.jsoko.JSoko;
import de.sokoban_online.jsoko.leveldata.Level;
import de.sokoban_online.jsoko.leveldata.LevelCollection;
import de.sokoban_online.jsoko.leveldata.solutions.Solution;
import de.sokoban_online.jsoko.solver.AnySolution.SolverAnySolution;
import de.sokoban_online.jsoko.solver.SolverAStarPushesMoves;
import de.sokoban_online.jsoko.solver.SolverGUI;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
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
    public static final int P_MAX_GENERATIONS = 22;
    public static final int P_MAX_INDIVIDUALS = 12;
    public static final int P_TOURNAMENT_ATTEMPS = 1;
    public static final float P_CROSSOVER_PROB = 1f;
    public static int P_MAX_BOXES = 8;
    public static int P_CROSS_SPACING = 2;
    public static final char[][] P_BASE_BOARD = {
    {'#', '#', '#', '#', '#', '#', '#','#'},
    {'#', ' ', ' ', ' ', ' ', ' ', ' ','#'},
    {'#', ' ', ' ', ' ', ' ', ' ', ' ','#'},
    {'#', ' ', ' ', '#', ' ', ' ', ' ','#'},
    {'#', '#', '#', '#', ' ', ' ', ' ','#'},
    {'#', ' ', ' ', ' ', ' ', ' ', ' ','#'},
    {'#', ' ', ' ', ' ', ' ', ' ', ' ','#'},
    {'#', '#', '#', '#', '#', '#', '#','#'}};
    
    public static final char[][] P_TEST_BOARD = {
    {'#', '#', '#', '#', '#', '#', '#','#'},
    {'#', ' ', ' ', ' ', ' ', ' ', ' ','#'},
    {'#', ' ', ' ', ' ', ' ', ' ', ' ','#'},
    {'#', '.', '.', '#', ' ', ' ', ' ','#'},
    {'#', '#', '#', '#', ' ', ' ', ' ','#'},
    {'#', '@', '$', ' ', '$', ' ', ' ','#'},
    {'#', ' ', ' ', ' ', ' ', ' ', ' ','#'},
    {'#', '#', '#', '#', '#', '#', '#','#'}};


    
    //Stats
    public static int R_TOTAL_CROSSOVER;
    public static int R_TOTAL_EFFECTIVE_CROSSOVER;
    public static int R_TOTAL_MUTATION;
    public static int R_TOTAL_EFFECTIVE_MUTATION;
    public static int R_TOTAL_REPAIR;
    public static int R_TOTAL_EFFECTIVE_REPAIR;
    
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
        Generator.solverLevel = new Level(application.levelIO.database);
        Generator.solverGUI = new SolverGUI(application);
        Generator.importedPopulation = new Population<>();
        random = new Random();
        sokobanChromosomeList = new ArrayList<>();
        
        Generator.solverLevel.setHeight(P_BASE_BOARD.length);
        Generator.solverLevel.setWidth(P_BASE_BOARD[0].length);
        
        //Check Heuristic
        /*int boxCount = GeneratorUtils.CountCharacters(1, P_TEST_BOARD);
        Solution solutionTest = GetSolution(P_TEST_BOARD, true, boxCount);
        int value = GeneratorUtils.GetCounterIntuitiveMoves(P_TEST_BOARD, solutionTest.lurd);
        System.out.println("Value: " + value);*/
        
        
        
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
        
        //System.out.println("Población inicial");
        /*for(Individual<SokobanChromosome> sc : Generator.importedPopulation){
            GeneratorUtils.PrintCharArray(sc.getChromosome().genes);
            //System.out.println("fitness: " + sc.getChromosome().fitnessValue);
            //System.out.println("\n");
        }*/

        RunGA();
    }
    
    private Population<SokobanChromosome> GetInitialPopulation() {
        Population<SokobanChromosome> initialPopulation = new Population();

        SokobanChromosome sokobanChromosome = null;
        Solution solution = null; 
        
        int notSolutionCount = 0;
        int totalAttempts = 100;
        
        for(int i = 0 ; i < totalAttempts ; i++){
            do{
                sokobanChromosome = GetRandomInitialSokobanChromosome();
                solution = GetSolution(sokobanChromosome.genes, true, 1);
                if(solution == null)
                    notSolutionCount++;
   
            }while(solution == null);

            //System.out.println("Original: ");
            //GeneratorUtils.PrintCharArray(sokobanChromosome.genes);
            //System.out.println("===========================");
            
            //var counterIntuitivesMoves = GeneratorUtils.GetCounterIntuitiveMoves(sokobanChromosome.genes, solution.lurd);
            //System.out.println("Counter Intuitive Moves: " + counterIntuitivesMoves);
            
            /*if(counterIntuitivesMoves > 0)
                System.out.println("Mayor a 0");*/
            
            sokobanChromosome.fitnessValue = this.application.movesHistory.getPushesCount();
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

        if(optimal)
            Generator.sol = new SolverAStarPushesMoves(Generator.application, Generator.solverGUI).searchSolution();
        else
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
    }

    
    private void SetupEvolutionaryAlgorithm() {
        AbstractStage<SokobanChromosome> selection = new TournamentSelector(Generator.P_TOURNAMENT_ATTEMPS);
        AbstractStage<SokobanChromosome> crossover = new SokoCrossover(P_CROSSOVER_PROB);
        AbstractStage<SokobanChromosome> ChangeSokoElementPosMutator = new ChangeSokoElementPosMutator(0.35);
        sokobanGA.addStage(selection);
        sokobanGA.addStage(crossover);
        sokobanGA.addStage(ChangeSokoElementPosMutator);
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
            
            ArrayList<SokobanChromosome> finalChromosomesSorted = new ArrayList<>();
            
            for(Individual<SokobanChromosome> sc : pop){
             
                finalChromosomesSorted.add(sc.getChromosome());
            }
            
            Collections.sort(finalChromosomesSorted, new Comparator<SokobanChromosome>() {
            @Override
            public int compare(SokobanChromosome c1, SokobanChromosome c2) {
                // Comparar los valores de fitness de los cromosomas en orden descendente
                return Double.compare(c2.pushes, c1.pushes);
            }
            });
            
            for(SokobanChromosome sc : finalChromosomesSorted){
                /*System.out.println("Moves, Pushes , boxChanges: " + sc.getChromosome().moves + "," + sc.getChromosome().pushes + "," +
                        sc.getChromosome().boxChanges);
                System.out.println("\n");*/
                GeneratorUtils.PrintCharArray(sc.genes);
                System.out.println(sc.moves + "," + sc.pushes + "," + sc.boxChanges + "," + sc.counterIntuitives);
            }

            System.out.println("Total crossover: " + Generator.R_TOTAL_CROSSOVER);
            System.out.println("Total effective crossover: " + Generator.R_TOTAL_EFFECTIVE_CROSSOVER);
            System.out.println("Total Mutation: " + Generator.R_TOTAL_MUTATION);
            System.out.println("Total effective Mutation: " + Generator.R_TOTAL_EFFECTIVE_MUTATION);
            System.out.println("Total repair: " + Generator.R_TOTAL_REPAIR);
            System.out.println("Total effective repair: " + Generator.R_TOTAL_EFFECTIVE_REPAIR);
            
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
