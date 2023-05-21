//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator;


import SokoGenerator.Tree.Pair;
import de.sokoban_online.jsoko.leveldata.Level;
import de.sokoban_online.jsoko.leveldata.LevelCollection;
import java.util.ArrayList;

public class SokobanChromosomeUtils {
    public static char[] boxesChar = new char[]{'$', '*'};

    public SokobanChromosomeUtils() {
    }

    public static int GetBoxCount(char[][] genes) {
        int iMax = genes.length;
        int jMax = genes[0].length;
        int count = 0;

        for(int i = 0; i < iMax; ++i) {
            for(int j = 0; j < jMax; ++j) {
                for(int k = 0; k < boxesChar.length; ++k) {
                    if (genes[i][j] == boxesChar[k]) {
                        ++count;
                    }
                }
            }
        }

        return count;
    }

    public static int GetCharCount(char[][] genes, int boardSize, char charTarget) {
        int count = 0;

        for(int i = 0; i < genes.length; ++i) {
            for(int j = 0; j < genes[0].length; ++j) {
                if (genes[i][j] == charTarget) {
                    ++count;
                }
            }
        }

        return count;
    }

    public static int GetCharsCount(char[][] genes, int boardSize, char[] charTarget) {
        int count = 0;

        for(int i = 0; i < genes.length; ++i) {
            for(int j = 0; j < genes[0].length; ++j) {
                for(int k = 0; k < charTarget.length; ++k) {
                    if (genes[i][j] == charTarget[k]) {
                        ++count;
                    }
                }
            }
        }

        return count;
    }

    public static void PrintMatrix(int[][] matrix) {
        int iMax = matrix.length;
        int jMax = matrix[0].length;

        for(int i = 0; i < iMax; ++i) {
            for(int j = 0; j < jMax; ++j) {
                System.out.print(matrix[i][j] + " ");
            }

            System.out.println("");
        }

    }

    public static void PrintValue(char[][] genes) {
        for(int i = 0; i < genes.length; ++i) {
            for(int j = 0; j < genes[0].length; ++j) {
                switch (genes[i][j]) {
                    case '$' -> System.out.print("\u001b[31m" + genes[i][j] + "\u001b[0m");
                    case '.' -> System.out.print("\u001b[33m" + genes[i][j] + "\u001b[0m");
                    case '@' -> System.out.print("\u001b[35m" + genes[i][j] + "\u001b[0m");
                    default -> System.out.print("\u001b[37m" + genes[i][j] + "\u001b[0m");
                }
            }

            System.out.println();
        }

        System.out.println();
    }

    public static ArrayList<Pair> GetTilesPosMatrix(char tileType, char[][] genes) {
        int iMax = genes.length;
        int jMax = genes[0].length;
        ArrayList<Pair> goalCandidates = new ArrayList();

        for(int i = 0; i < iMax; ++i) {
            for(int j = 0; j < jMax; ++j) {
                if (Generator.boardBase[i][j] == tileType) {
                    goalCandidates.add(new Pair(i, j));
                }
            }
        }

        return goalCandidates;
    }

    public static ArrayList<Pair> GetTilesPosMatrix(char[] tileTypes, char[][] genes) {
        int iMax = genes.length;
        int jMax = genes[0].length;
        ArrayList<Pair> goalCandidates = new ArrayList();

        for(int i = 0; i < iMax; ++i) {
            for(int j = 0; j < jMax; ++j) {
                for(int k = 0; k < tileTypes.length; ++k) {
                    if (genes[i][j] == tileTypes[k]) {
                        goalCandidates.add(new Pair(i, j));
                    }
                }
            }
        }

        return goalCandidates;
    }

    public static void PrintLongestRoute(char[][] board, ArrayList<Pair> route, Pair lastPos, Pair goalPos, int steps) {
        int iMax = board.length;
        int jMax = board[0].length;

        for(int i = 0; i < iMax; ++i) {
            for(int j = 0; j < jMax; ++j) {
                new Pair(i, j);
                boolean contains = false;

                for(int k = 0; k < route.size(); ++k) {
                    if (((Pair)route.get(k)).i == i && ((Pair)route.get(k)).j == j) {
                        contains = true;
                        break;
                    }
                }

                if (contains) {
                    if (i == lastPos.i && j == lastPos.j) {
                        System.out.print("\u001b[33mO\u001b[0m");
                    } else if (i == goalPos.i && j == goalPos.j) {
                        System.out.print("\u001b[32mG\u001b[0m");
                    } else {
                        System.out.print("\u001b[31mO\u001b[0m");
                    }
                } else if (board[i][j] == '@') {
                    System.out.print("\u001b[36m@\u001b[0m");
                } else {
                    System.out.print("\u001b[37m" + board[i][j] + "\u001b[0m");
                }
            }

            System.out.println();
        }

        System.out.println("Steps: " + steps);
    }

    public static void PrintGoalRange(char[][] board, int[][] goalRange, Pair root) {
        int iMax = board.length;
        int jMax = board[0].length;

        for(int i = 0; i < iMax; ++i) {
            for(int j = 0; j < jMax; ++j) {
                if (goalRange[i][j] == 1) {
                    if (i == root.i && j == root.j) {
                        System.out.print("\u001b[32mG\u001b[0m");
                    } else {
                        System.out.print("\u001b[31mO\u001b[0m");
                    }
                } else {
                    System.out.print("\u001b[37m" + board[i][j] + "\u001b[0m");
                }
            }

            System.out.println();
        }

    }

    public static boolean IsCollision(Pair pair1, Pair pair2) {
        return pair1.i == pair2.i && pair1.j == pair2.j;
    }

    static void Print(char[][] genes, MyBoxData boxData) {
        for(int i = 0; i < genes.length; ++i) {
            for(int j = 0; j < genes[0].length; ++j) {
                if (boxData.box.i == i && boxData.box.j == j) {
                    System.out.print("\u001b[35mC\u001b[0m");
                } else if (boxData.goal.i == i && boxData.goal.j == j) {
                    System.out.print("\u001b[35mG\u001b[0m");
                } else {
                    switch (genes[i][j]) {
                        case '#':
                            System.out.print("\u001b[32m" + genes[i][j] + "\u001b[0m");
                            break;
                        case '$':
                            System.out.print("\u001b[34m" + genes[i][j] + "\u001b[0m");
                            break;
                        case '.':
                            System.out.print("\u001b[31m" + genes[i][j] + "\u001b[0m");
                            break;
                        case '@':
                            System.out.print("\u001b[37m" + genes[i][j] + "\u001b[0m");
                            break;
                        default:
                            System.out.print("\u001b[33m" + genes[i][j] + "\u001b[0m");
                    }
                }
            }

            System.out.println();
        }

    }

    static void PrintChange(char[][] genes, MyBoxData aThis) {
        for(int i = 0; i < genes.length; ++i) {
            for(int j = 0; j < genes[0].length; ++j) {
                switch (genes[i][j]) {
                    case '#':
                        System.out.print("\u001b[32m" + genes[i][j] + "\u001b[0m");
                        break;
                    case '$':
                        if (aThis.box.i == i && aThis.box.j == j) {
                            System.out.print("\u001b[34m" + genes[i][j] + "\u001b[0m");
                            System.out.print("\u001b[31m" + genes[i][j] + "\u001b[0m");
                            break;
                        }

                        System.out.print("\u001b[34m" + genes[i][j] + "\u001b[0m");
                        break;
                    case '.':
                        if (aThis.goal.i == i && aThis.goal.j == j) {
                            System.out.print("\u001b[33m" + genes[i][j] + "\u001b[0m");
                            System.out.print("\u001b[31m" + genes[i][j] + "\u001b[0m");
                            break;
                        }

                        System.out.print("\u001b[33m" + genes[i][j] + "\u001b[0m");
                        break;
                    case '@':
                        System.out.print("\u001b[37m" + genes[i][j] + "\u001b[0m");
                        break;
                    default:
                        System.out.print("\u001b[32m" + genes[i][j] + "\u001b[0m");
                }
            }

            System.out.println();
        }

    }

    public static void WatchLevelSolver(char[][] genes, int boxCount) {
        Level solverLevel = new Level(Generator.application.levelIO.database);
        solverLevel.setBoardData(genes.toString());
        solverLevel.setHeight(genes.length);
        solverLevel.setWidth(genes[0].length);
        solverLevel.setBoxCount(boxCount);
        LevelCollection levelCollection = (new LevelCollection.Builder()).setLevels(new Level[]{solverLevel}).build();
        Generator.application.setCollectionForPlaying(levelCollection);
        Generator.application.setLevelForPlaying(0);
        Generator.application.currentLevel = solverLevel;
    }

    public static Pair GetPlayerPos(char[][] genes) {
        int height = genes.length;
        int width = genes[0].length;

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                if (genes[i][j] == '@' || genes[i][j] == '+') {
                    return new Pair(i, j);
                }
            }
        }

        return null;
    }
}
