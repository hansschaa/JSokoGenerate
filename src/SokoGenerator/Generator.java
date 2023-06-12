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
import java.util.Iterator;
import java.util.LinkedList;
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
    private static final int P_MAX_GENERATIONS = 10;
    private static final int P_MAX_INDIVIDUALS = 4;
    public static int P_MAX_BOXES;
    public static final char[][] P_BASE_BOARD = {
    {'#', '#', '#', '#', '#', '#', '#'},
    {'#', ' ', ' ', ' ', ' ', ' ', '#'},
    {'#', ' ', ' ', ' ', ' ', ' ', '#'},
    {'#', ' ', ' ', ' ', ' ', ' ', '#'},
    {'#', ' ', ' ', ' ', ' ', ' ', '#'},
    {'#', ' ', ' ', ' ', ' ', ' ', '#'},
    {'#', '#', '#', '#', '#', '#', '#'}};
    private Population<SokobanChromosome> importedPopulation;
   
    
    //Stats
    public static int totalMutationInvertBoxCount;
    public static int effectiveInvertBoxMutation;
    public static int totalMutationInvertPlayerCount;
    public static int effectiveInvertPlayerMutation;
    
    //Others
    public Thread generatorThread;
    private static Level solverLevel;
    public static SolverAStarPushesMoves optimalSolver;
    public static SolverAnySolution anySolutionSolver;
    public static SolverGUI solverGUI;
    public static ArrayList<SokoTree> sokoTrees = new ArrayList();
    public static ArrayList<Pair> goalCandidates;
    
    public LevelCollection levelCollection;
    public SokoBoard sokoBoard;
    
    Queue<Pair> queue = new LinkedList();


    public Generator(JSoko application) throws FileNotFoundException {
        Generator.application = application;
        Generator.optimalSolver = new SolverAStarPushesMoves(application, new SolverGUI(application));
        Generator.anySolutionSolver = new SolverAnySolution(application, new SolverGUI(application));
        Generator.solverLevel = new Level(application.levelIO.database);
        Generator.solverGUI = new SolverGUI(application);
        random = new Random();
        System.out.println("Generator constructor");
        
        this.importedPopulation = GetInitialPopulation();
        System.out.println("termino");
        for(Individual<SokobanChromosome> sc : this.importedPopulation){
            GeneratorUtils.PrintCharArray(sc.getChromosome().genes);
            System.out.println("\n");
        }
        
        /*this.sokoBoard = new SokoBoard();
        this.importedPopulation = new Population();
        random = new Random();*/
    }
    
    private Population<SokobanChromosome> GetInitialPopulation() {
        Population<SokobanChromosome> initialPopulation = new Population();

        SokobanChromosome sokobanChromosome = null;
        for(int i = 0 ; i < P_MAX_INDIVIDUALS ; i++){
            do{
            sokobanChromosome = GetRandomInitialSokobanChromosome();
            
            }while(GetSolution(sokobanChromosome.genes, false) == null);
            
            Individual<SokobanChromosome> individual = new Individual<SokobanChromosome>(sokobanChromosome);
            initialPopulation.add(individual);
        }
        
        
        return initialPopulation;
    }
    
    Solution GetSolution(char[][] genes, boolean optimal) {
        //System.out.println("Get solution");
        Generator.solverLevel.setBoardData(GeneratorUtils.ConvertCharArrayToString(genes));
        //GeneratorUtils.PrintCharArray(genes);
        Generator.solverLevel.setHeight(genes.length);
        Generator.solverLevel.setWidth(genes[0].length);
        Generator.solverLevel.setBoxCount(1);
        LevelCollection levelCollection = (new LevelCollection.Builder()).setLevels(new Level[]{this.solverLevel}).build();
        Generator.application.setCollectionForPlaying(levelCollection);
        Generator.application.setLevelForPlaying(1);
        Generator.application.currentLevel = Generator.solverLevel;
        
        Generator.anySolutionSolver = new SolverAnySolution(Generator.application, Generator.solverGUI);
        Solution solution = anySolutionSolver.searchSolution();
        
        System.out.println("empezo");
        GeneratorUtils.PrintCharArray(genes);
        //GeneratorUtils.PrintCharArray(genes);
        /*Solution solution = null;
        try{
        solution = anySolutionSolver.searchSolution();}
        catch(Exception e){}
        //System.out.println("termino");
        
       // while(!anySolutionSolver.getProgress() != 100){}
        
        
        
        if(solution == null){
            System.out.println("No tiene solución");
        }*/
        
        return solution;
    }
    
    

    private SokobanChromosome GetRandomInitialSokobanChromosome() {
        
        char[][] baseBoardClone = GeneratorUtils.CloneCharArray(P_BASE_BOARD);
         
        Pair boxPair = GetEmptySpacePair(baseBoardClone);
        baseBoardClone[boxPair.i][boxPair.j] = '$';
        
        Pair goalPair = GetEmptySpacePair(baseBoardClone);
        baseBoardClone[goalPair.i][goalPair.j] = '.';
        
        Pair playerPair = GetEmptySpacePair(baseBoardClone);
        baseBoardClone[playerPair.i][playerPair.j] = '@';

        SokobanChromosome newRandChromosome = new SokobanChromosome(baseBoardClone);
        
        return newRandChromosome;
    }
    
    private Pair GetEmptySpacePair(char[][] board){
        Pair pair = new Pair(0,0);
        do{
            pair.i = random.nextInt( P_BASE_BOARD.length );
            pair.j = random.nextInt( P_BASE_BOARD[0].length );
        }while(board[pair.i][pair.j] != ' ');
        
      return pair;
    }
    
    public void Init() throws FileNotFoundException {
        System.out.println("Init...");
        this.InitBoards();
        if (!this.RunGA()) {
            this.Init();
        }

    }

    private void InitBoards() throws FileNotFoundException {
        System.out.println("----> InitBoards....");
        this.sokoBoard.GenerateLevels();
    }

    public boolean RunGA() {
        System.out.println("----> Run GA....");
        /*sokobanGA = new SokobanGA(this.importedPopulation, 12, application, this);
        boardBase = (char[][])this.sokoBoard.levels.get(0);
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
        
        return true;
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
        this.generatorThread.suspend();
    }

    private void ExportLevels() {
        try {
            System.out.println("----> Exporting results...");
            StringBuilder sb = new StringBuilder();
            Population<SokobanChromosome> pop = sokobanGA.getLastPopulation();
            Iterator var3 = pop.iterator();

            while(var3.hasNext()) {
                Individual<SokobanChromosome> sc = (Individual)var3.next();
                char[][] board = ((SokobanChromosome)sc.getChromosome()).genes;
                char[][] var6 = board;
                int var7 = board.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    char[] row = var6[var8];

                    for(int j = 0; j < row.length; ++j) {
                        sb.append(row[j]);
                    }

                    sb.append("\r\n");
                }

                /*int var10001 = ((SokobanChromosome)sc.getChromosome()).pushes;
                sb.append("P: " + var10001 + "- M: " + ((SokobanChromosome)sc.getChromosome()).moves);
                sb.append("\n");
                sb.append("\n");*/
            }

            sb.append("Total player mutation: " + totalMutationInvertPlayerCount + "\n");
            sb.append("Effective player mutation: " + effectiveInvertPlayerMutation + "\n");
            sb.append("Total box invert mutation: " + totalMutationInvertBoxCount + "\n");
            sb.append("Effective box invert mutation: " + effectiveInvertBoxMutation + "\n");
            Path path = Paths.get("C:\\Users\\hansschaa\\Desktop\\SokoResults.txt");
            Files.write(path, sb.toString().getBytes(), new OpenOption[0]);
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
