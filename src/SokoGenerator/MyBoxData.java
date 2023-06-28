//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator;


import SokoGenerator.Tree.Pair;
import java.util.ArrayList;
import java.util.Iterator;

public class MyBoxData {
    public Pair box;
    public Pair goal;
    public ArrayList<Pair> boxRoute;
    public int boxRouteIndex;
    public int goalRouteIndex;

    public MyBoxData(Pair goal, Pair box, ArrayList<Pair> newBoxRoute, int goalRouteIndex, int boxRouteIndex) {
        this.goalRouteIndex = goalRouteIndex;
        this.boxRouteIndex = boxRouteIndex;
        this.boxRoute = (ArrayList)newBoxRoute.clone();
        this.goal = (Pair)this.boxRoute.get(goalRouteIndex);
        this.box = (Pair)this.boxRoute.get(boxRouteIndex);
    }

    boolean TrySetBoxPosInRoute(SokobanChromosome sokobanChromosome) {
        /*if (this.boxRouteIndex < this.boxRoute.size() && this.goalRouteIndex >= 0 && sokobanChromosome.boxDatas.size() < Generator.maxBox) {
            System.out.println("Do TrySetBoxPosInRoute");
            boolean haveSolution = false;

            do {
                this.UpdatePos();
                boolean boxCollision = this.CheckCollision(sokobanChromosome.boxDatas, TileTypes.BOX);
                if (!boxCollision && sokobanChromosome.genes[this.box.i][this.box.j] != '+' && sokobanChromosome.genes[this.box.i][this.box.j] != '@') {
                    boolean goalCollision = this.CheckCollision(sokobanChromosome.boxDatas, TileTypes.GOAL);
                    if (!goalCollision && sokobanChromosome.genes[this.goal.i][this.goal.j] == ' ') {
                        System.out.println("Box datas from box data class:");
                        SokobanChromosomeUtils.PrintValue(sokobanChromosome.genes);
                        Iterator var5 = sokobanChromosome.boxDatas.iterator();

                        while(var5.hasNext()) {
                            MyBoxData b = (MyBoxData)var5.next();
                            b.PrintValues();
                            System.out.println();
                        }

                        System.out.println("Caja del boxData: " + this.box.toString());
                        System.out.println("Meta del boxData: " + this.goal.toString());
                        System.out.println("En la posicion de la caja hay: " + sokobanChromosome.genes[this.box.i][this.box.j]);
                        System.out.println("En la posicion de la meta hay: " + sokobanChromosome.genes[this.goal.i][this.goal.j]);
                        haveSolution = this.PerformExchange(sokobanChromosome);
                    } else if (this.goalRouteIndex >= 3) {
                        this.goalRouteIndex -= Generator.random.nextInt(3) + 1;
                    } else if (this.goalRouteIndex == 2) {
                        this.goalRouteIndex -= Generator.random.nextInt(2) + 1;
                    } else if (this.goalRouteIndex == 1) {
                        --this.goalRouteIndex;
                    }
                }

                if (!haveSolution) {
                    ++this.boxRouteIndex;
                }
            } while(!haveSolution && this.boxRouteIndex < this.boxRoute.size() && this.goalRouteIndex >= 0);

            if (this.boxRouteIndex == this.boxRoute.size()) {
                System.out.println("Trasladé la caja a todos los nodos en box route y no encontró solución");
            }

            return haveSolution;
        } else {
            return false;
        }*/
        return false;
    }

    private boolean PerformExchange(SokobanChromosome chromosome) {
        /*char[][] newGenes = chromosome.UpdateGenes(this, chromosome.genes);
        int boxesCount = SokobanChromosomeUtils.GetBoxCount(newGenes);
        SokobanChromosomeUtils.WatchLevelSolver(chromosome.genes, boxesCount);
        if (chromosome.boxDatas.size() + 1 != SokobanChromosomeUtils.GetBoxCount(newGenes)) {
            return false;
        } else {
            Solution solution = Generator.sokobanGA.GetAnySolution(newGenes, boxesCount);
            if (solution != null) {
                System.out.println("if PerformExchange");
                chromosome.genes = (char[][])Arrays.stream(newGenes).map((rec$) -> {
                    return (char[])((char[])rec$).clone();
                }).toArray((x$0) -> {
                    return new char[x$0][];
                });
                return true;
            } else {
                System.out.println("else PerformExchange");
                return false;
            }
        }*/
        return false;
    }

    public boolean CheckCollision(ArrayList<MyBoxData> boxDatas, TileTypes tileType) {
        Iterator var3 = boxDatas.iterator();

        while(var3.hasNext()) {
            MyBoxData boxData = (MyBoxData)var3.next();
            if (tileType == TileTypes.BOX) {
                if (SokobanChromosomeUtils.IsCollision(this.box, boxData.box)) {
                    return true;
                }
            } else if (tileType == TileTypes.GOAL && SokobanChromosomeUtils.IsCollision(this.goal, boxData.goal)) {
                return true;
            }
        }

        return false;
    }

    public Pair GetDirection(Pair pair1, Pair pair2) {
        return pair2.minus(pair1);
    }

    public void PrintValues() {
        System.out.println("Goal: " + this.goal.toString());
        System.out.println("Box: " + this.box.toString());
        System.out.println("Goal Index: " + this.goalRouteIndex);
        System.out.println("Box Index: " + this.boxRouteIndex);
    }

    void UpdatePos() {
        if (this.goalRouteIndex >= 0) {
            this.goal = (Pair)this.boxRoute.get(this.goalRouteIndex);
        }

        if (this.boxRouteIndex < this.boxRoute.size()) {
            this.box = (Pair)this.boxRoute.get(this.boxRouteIndex);
        }

    }
}
