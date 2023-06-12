//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator;


import SokoGenerator.Tree.Pair;
import de.sokoban_online.jsoko.leveldata.solutions.Solution;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import jenes.chromosome.Chromosome;

public class SokobanChromosome implements Chromosome<SokobanChromosome> {
    public char[][] genes;

    public SokobanChromosome(char[][] boardData) {
        this.genes = boardData;
    }



    public SokobanChromosome clone() {
        System.out.println("clone");
        char[][] genesCopy = (char[][])Arrays.stream(this.genes).map((rec$) -> {
            return (char[])((char[])rec$).clone();
        }).toArray((x$0) -> {
            return new char[x$0][];
        });
        ArrayList<MyBoxData> boxDataCopy = new ArrayList();
       
        return new SokobanChromosome(genesCopy);
    }

    public int length() {
        return this.genes.length * this.genes[0].length;
    }

    public void randomize() {
        System.out.println("randomize 2");
    }

    public void randomize(int pos) {
        switch (pos) {
            case 0:
                ++Generator.totalMutationInvertBoxCount;
                this.InvertMutation();
                break;
            case 1:
                ++Generator.totalMutationInvertPlayerCount;
                this.ChangePlayer();
        }

    }

    public void ChangePlayerMutation() {
        /*if (this.boxDatas.size() != 1) {
            this.ChangePlayer();
        }*/
    }

    public void InvertMutation() {
        /*if (this.boxDatas.size() != 1) {
            try {
                int rand = Generator.random.nextInt(this.boxDatas.size());
                PrintStream var10000 = System.out;
                Object var10001 = this.boxDatas.get(rand);
                var10000.println("ANTES - Cambiando la caja número: " + var10001);
                System.out.println("BoxDatas del cromosoma:");
                Iterator var2 = this.boxDatas.iterator();

                MyBoxData b;
                while(var2.hasNext()) {
                    b = (MyBoxData)var2.next();
                    b.PrintValues();
                    System.out.println();
                }

                if (this.Invert((MyBoxData)this.boxDatas.get(rand))) {
                    ++Generator.effectiveInvertBoxMutation;
                    var10000 = System.out;
                    var10001 = this.boxDatas.get(rand);
                    var10000.println("DESPUES - Cambiando la caja número: " + var10001);
                    System.out.println("BoxDatas del cromosoma:");
                    var2 = this.boxDatas.iterator();

                    while(var2.hasNext()) {
                        b = (MyBoxData)var2.next();
                        b.PrintValues();
                        System.out.println();
                    }

                    System.out.println("Fin Invert success");
                }
            } catch (Exception var4) {
                System.out.println(var4);
                var4.printStackTrace();
                System.out.println();
            }
        }*/

    }

    private boolean ChangePlayer() {
        /*SokobanChromosomeUtils.WatchLevelSolver(this.genes, this.boxDatas.size());
        ArrayList<Pair> whiteTiles = SokobanChromosomeUtils.GetTilesPosMatrix(new char[]{' ', '.'}, this.genes);
        if (whiteTiles.size() == 0) {
            System.out.println("Hay 0 tiles vacios");
            return false;
        } else {
            Pair playerPos = SokobanChromosomeUtils.GetPlayerPos(this.genes);
            int randomNum = Generator.random.nextInt(whiteTiles.size());
            Pair newPos = (Pair)whiteTiles.get(randomNum);
            char[][] newGenes = (char[][])Arrays.stream(this.genes).map((rec$) -> {
                return (char[])((char[])rec$).clone();
            }).toArray((x$0) -> {
                return new char[x$0][];
            });
            if (newGenes[playerPos.i][playerPos.j] == '+') {
                newGenes[playerPos.i][playerPos.j] = '.';
            } else if (newGenes[playerPos.i][playerPos.j] == '@') {
                newGenes[playerPos.i][playerPos.j] = ' ';
            }

            if (newGenes[newPos.i][newPos.j] == '.') {
                newGenes[newPos.i][newPos.j] = '+';
            } else if (newGenes[newPos.i][newPos.j] == ' ') {
                newGenes[newPos.i][newPos.j] = '@';
            }

            Solution sol = Generator.sokobanGA.GetAnySolution(newGenes, this.boxDatas.size());
            if (sol != null) {
                ++Generator.effectiveInvertPlayerMutation;
                if (this.genes[playerPos.i][playerPos.j] == '+') {
                    this.genes[playerPos.i][playerPos.j] = '.';
                } else if (this.genes[playerPos.i][playerPos.j] == '@') {
                    this.genes[playerPos.i][playerPos.j] = ' ';
                }

                if (this.genes[newPos.i][newPos.j] == '.') {
                    this.genes[newPos.i][newPos.j] = '+';
                } else if (this.genes[newPos.i][newPos.j] == ' ') {
                    this.genes[newPos.i][newPos.j] = '@';
                }

                SokobanChromosomeUtils.WatchLevelSolver(this.genes, this.boxDatas.size());
                return true;
            } else {
                return false;
            }
        }*/
        
        return false;
    }

    public boolean Invert(MyBoxData boxData) {
        /*System.out.println(boxData);
        System.out.println("Invert antes");
        boxData.PrintValues();
        SokobanChromosomeUtils.PrintValue(this.genes);
        Pair boxPos = (Pair)boxData.boxRoute.get(boxData.boxRouteIndex);
        Pair goalPos = (Pair)boxData.boxRoute.get(boxData.goalRouteIndex);
        char boxTile = this.genes[boxPos.i][boxPos.j];
        char goalTile = this.genes[goalPos.i][goalPos.j];
        if (goalTile == '.' && boxTile == '$') {
            char[][] newGenes = (char[][])Arrays.stream(this.genes).map((rec$) -> {
                return (char[])((char[])rec$).clone();
            }).toArray((x$0) -> {
                return new char[x$0][];
            });
            newGenes[boxPos.i][boxPos.j] = goalTile;
            newGenes[goalPos.i][goalPos.j] = boxTile;
            if (this.boxDatas.size() != SokobanChromosomeUtils.GetBoxCount(this.genes)) {
                return false;
            } else {
                Solution sol = Generator.sokobanGA.GetAnySolution(newGenes, this.boxDatas.size());
                if (sol != null) {
                    int aux = boxData.goalRouteIndex;
                    boxData.goalRouteIndex = boxData.boxRouteIndex;
                    boxData.boxRouteIndex = aux;
                    boxData.UpdatePos();
                    this.genes[boxData.box.i][boxData.box.j] = '$';
                    this.genes[boxData.goal.i][boxData.goal.j] = '.';
                    System.out.println("Invert despues");
                    boxData.PrintValues();
                    SokobanChromosomeUtils.PrintValue(this.genes);
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }*/
        
        return false;
    }

    public void setAs(SokobanChromosome chromosome) {
        /*System.out.println("setas");
        this.genes = (char[][])Arrays.stream(chromosome.genes).map((rec$) -> {
            return (char[])((char[])rec$).clone();
        }).toArray((x$0) -> {
            return new char[x$0][];
        });
        this.boxDatas = new ArrayList();
        Iterator var2 = chromosome.boxDatas.iterator();

        while(var2.hasNext()) {
            MyBoxData boxData = (MyBoxData)var2.next();
            this.boxDatas.add(new MyBoxData(boxData.goal, boxData.box, boxData.boxRoute, boxData.goalRouteIndex, boxData.boxRouteIndex));
        }*/

    }

    public void cross(SokobanChromosome chromosome, int from, int to) {
        System.out.println("cross 1");
    }

    public String replace(String str, int index, char replace) {
        if (str == null) {
            return str;
        } else if (index >= 0 && index < str.length()) {
            char[] chars = str.toCharArray();
            chars[index] = replace;
            return String.valueOf(chars);
        } else {
            return str;
        }
    }

    public void cross(SokobanChromosome chromosome, int from) {
        System.out.println("cross");
        this.UniformCrossover(chromosome);
    }

    public void UniformCrossover(SokobanChromosome chromosome) {
        System.out.println("UniformCrossover");
        MyBoxData[] boxToPass_1 = null;
        MyBoxData[] boxToPass_2 = null;

        try {
            int r1 = this.GetRandomBoxesCountToPass(this);
            int r2 = this.GetRandomBoxesCountToPass(chromosome);
            boxToPass_1 = this.GetRandomBoxData(this, r1);
            boxToPass_2 = this.GetRandomBoxData(chromosome, r2);
        } catch (Exception var9) {
            System.out.println(var9);
            var9.printStackTrace();
            System.out.println();
        }

        try {
            this.UCrossover(boxToPass_2, this, chromosome);
        } catch (Exception var8) {
            System.out.println(var8);
            var8.printStackTrace();
            System.out.println();
        }

        try {
            this.UCrossover(boxToPass_1, chromosome, this);
        } catch (Exception var7) {
            System.out.println(var7);
            var7.printStackTrace();
            System.out.println();
        }

    }

    public void UCrossover(MyBoxData[] candidatesBoxDatas, SokobanChromosome destChromosome, SokobanChromosome sourceChromosome) {
        /*System.out.println("UCrossover");
        MyBoxData[] var5 = candidatesBoxDatas;
        int var6 = candidatesBoxDatas.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            MyBoxData candidateBoxData = var5[var7];
            boolean sucessBox = candidateBoxData.TrySetBoxPosInRoute(destChromosome);
            if (sucessBox) {
                System.out.println("Succes box");
                if (sourceChromosome.boxDatas.size() > 1) {
                    this.RemoveBox(sourceChromosome, candidateBoxData);
                }

                this.AddBox(destChromosome, candidateBoxData);
            }
        }

        SokobanChromosomeUtils.PrintValue(destChromosome.genes);
        Iterator var9 = destChromosome.boxDatas.iterator();

        while(var9.hasNext()) {
            MyBoxData b = (MyBoxData)var9.next();
            b.PrintValues();
        }*/

    }

    public void AddBox(SokobanChromosome sokobanChromosome, MyBoxData boxData) {
        /*System.out.println("AddBox");
        sokobanChromosome.boxDatas.add(new MyBoxData(boxData.goal, boxData.box, boxData.boxRoute, boxData.goalRouteIndex, boxData.boxRouteIndex));
        System.out.println("end AddBox");*/
    }

    private void RemoveBox(SokobanChromosome sourceChromosome, MyBoxData candidatesBoxData) {
        /*Iterator var3 = sourceChromosome.boxDatas.iterator();

        while(var3.hasNext()) {
            MyBoxData boxData = (MyBoxData)var3.next();
            if (SokobanChromosomeUtils.IsCollision(boxData.box, candidatesBoxData.box) && SokobanChromosomeUtils.IsCollision(boxData.goal, candidatesBoxData.goal)) {
                Pair boxPos = boxData.box;
                char boxChar = sourceChromosome.genes[candidatesBoxData.box.i][candidatesBoxData.box.j];
                Pair goalPos = boxData.goal;
                char goalChar = sourceChromosome.genes[candidatesBoxData.goal.i][candidatesBoxData.goal.j];
                switch (boxChar) {
                    case '$':
                        sourceChromosome.genes[boxPos.i][boxPos.j] = ' ';
                        break;
                    case '*':
                        sourceChromosome.genes[boxPos.i][boxPos.j] = '.';
                }

                switch (goalChar) {
                    case '*':
                        sourceChromosome.genes[goalPos.i][goalPos.j] = '$';
                        break;
                    case '+':
                        sourceChromosome.genes[goalPos.i][goalPos.j] = '@';
                    case ',':
                    case '-':
                    default:
                        break;
                    case '.':
                        sourceChromosome.genes[goalPos.i][goalPos.j] = ' ';
                }

                sourceChromosome.boxDatas.remove(boxData);
                break;
            }
        }*/

    }

    public char[][] UpdateGenes(MyBoxData boxData, char[][] chromosomeGenes) {
        char[][] backup = (char[][])Arrays.stream(chromosomeGenes).map((rec$) -> {
            return (char[])((char[])rec$).clone();
        }).toArray((x$0) -> {
            return new char[x$0][];
        });
        Pair box = boxData.box;
        Pair goal = boxData.goal;
        if (backup[box.i][box.j] == ' ') {
            backup[box.i][box.j] = '$';
        } else if (backup[box.i][box.j] == '.') {
            backup[box.i][box.j] = '*';
        }

        if (backup[goal.i][goal.j] == ' ') {
            backup[goal.i][goal.j] = '.';
        } else if (backup[goal.i][goal.j] == '$') {
            backup[goal.i][goal.j] = '*';
        } else if (backup[goal.i][goal.j] == '@') {
            backup[goal.i][goal.j] = '+';
        }

        if (SokobanChromosomeUtils.IsCollision(goal, box)) {
            backup[goal.i][goal.j] = '*';
        }

        System.out.println("Fin UpdateGenes");
        return backup;
    }

    public int GetRandomBoxesCountToPass(SokobanChromosome chromosome) {
        //return chromosome.boxDatas.size() > 2 ? Generator.random.nextInt(chromosome.boxDatas.size() - 1) + 1 : Generator.random.nextInt(chromosome.boxDatas.size()) + 1;
        return 0;
    }

    public MyBoxData[] GetRandomBoxData(SokobanChromosome chromosome, int r) {
        /*MyBoxData[] temp = new MyBoxData[r];

        for(int i = 0; i < r; ++i) {
            int randomIndex = Generator.random.nextInt(chromosome.boxDatas.size());
            MyBoxData bd = (MyBoxData)chromosome.boxDatas.get(randomIndex);
            temp[i] = new MyBoxData(bd.goal, bd.box, bd.boxRoute, bd.goalRouteIndex, bd.boxRouteIndex);
        }

        return temp;*/
        return new MyBoxData[r];
    }

    public boolean equals(SokobanChromosome chromosome) {
        char[][] otherGenes = chromosome.genes;

        for(int i = 0; i < this.genes.length; ++i) {
            for(int j = 0; j < this.genes[0].length; ++j) {
                if (this.genes[i][j] != otherGenes[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public void difference(SokobanChromosome chromosome, double[] diff) {
        System.out.println("difference");
    }

    public Object[] toArray() {
        System.out.println("toArray");
        return null;
    }

    public int GetBoxChanges() {
        System.out.println("GetBoxChanges");
        int total = 0;
        return total;
    }
    
        public void swap(int pos1, int pos2) {
        System.out.println("swap");
    }

    public void leftShift(int from, int to) {
        System.out.println("leftShift");
    }

    public void rightShift(int from, int to) {
        System.out.println("rightShift");
    }

    public void setDefaultValueAt(int pos) {
        System.out.println("setDefaultValueAt");
    }
}
