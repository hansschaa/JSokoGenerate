//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator.Tree;

import SokoGenerator.Generator;
import SokoGenerator.SokobanChromosomeUtils;
import java.io.PrintStream;
import java.util.ArrayList;

public class SokoTree {
    public SokoNode goal;
    public SokoNode box;
    public SokoNode player;
    public char[][] board;
    public int[][] goalRange;
    public char[][] genes;
    public ArrayList<Pair> boxRoute;
    public int max = 0;

    public SokoTree(SokoNode root, char[][] board) {
        this.goal = root;
        this.board = board;
    }

    public boolean InitSearch() {
        this.goalRange = new int[this.board.length][this.board[0].length];
        this.boxRoute = new ArrayList();
        this.box = this.goal;
        this.Execute(this.goal, 0);
        SokoNode last = this.box;
        if (last.parent == null) {
            Generator.goalCandidates.remove(this.goal);
            return false;
        } else {
            this.boxRoute = this.GetBoxRoute();
            if (this.boxRoute.size() <= 3) {
                return false;
            } else {
                PrintStream var10000 = System.out;
                int var10001 = Generator.sokoTrees.size();
                var10000.println("----------- Board: " + (var10001 + 1));
                SokobanChromosomeUtils.PrintGoalRange(this.board, this.goalRange, this.goal.value);
                SokobanChromosomeUtils.PrintLongestRoute(this.board, this.boxRoute, this.box.value, last.value, this.boxRoute.size());
                System.out.println("-------------------------------------");
                System.out.println();
                System.out.println();
                return true;
            }
        }
    }

    public void Execute(SokoNode currentSokoNode, int count) {
        int m_count = count + 1;
        currentSokoNode.neighBourds = this.GetNeighbourds(currentSokoNode);

        for(int i = 0; i < currentSokoNode.neighBourds.size(); ++i) {
            this.Execute((SokoNode)currentSokoNode.neighBourds.get(i), m_count);
        }

        if (m_count > this.max) {
            this.max = m_count;
            this.box = currentSokoNode;
        }

    }

    public ArrayList<SokoNode> GetNeighbourds(SokoNode currentSokoNode) {
        ArrayList<SokoNode> neighbourds = new ArrayList();
        Pair pos = currentSokoNode.value;
        int maxI = this.board.length;
        int maxJ = this.board[0].length;
        if (pos.j > 2 && this.goalRange[pos.i][pos.j - 1] != 1 && this.CheckObstacles(Directions.LEFT, pos)) {
            neighbourds.add(new SokoNode(currentSokoNode, new Pair(pos.i, pos.j - 1)));
        }

        if (pos.j < maxJ - 3 && this.goalRange[pos.i][pos.j + 1] != 1 && this.CheckObstacles(Directions.RIGHT, pos)) {
            neighbourds.add(new SokoNode(currentSokoNode, new Pair(pos.i, pos.j + 1)));
        }

        if (pos.i > 2 && this.goalRange[pos.i - 1][pos.j] != 1 && this.CheckObstacles(Directions.UP, pos)) {
            neighbourds.add(new SokoNode(currentSokoNode, new Pair(pos.i - 1, pos.j)));
        }

        if (pos.i < maxI - 3 && this.goalRange[pos.i + 1][pos.j] != 1 && this.CheckObstacles(Directions.DOWN, pos)) {
            neighbourds.add(new SokoNode(currentSokoNode, new Pair(pos.i + 1, pos.j)));
        }

        this.goalRange[pos.i][pos.j] = 1;
        return neighbourds;
    }

    public boolean CheckObstacles(Directions direction, Pair pos) {
        Pair nearPos = pos.plus(direction.getDirection());
        Pair farPos = pos.plus(direction.getDirection().multiply(2));
        return this.board[nearPos.i][nearPos.j] == ' ' && this.board[farPos.i][farPos.j] == '.' || this.board[nearPos.i][nearPos.j] == '.' && this.board[farPos.i][farPos.j] == ' ' || this.board[nearPos.i][nearPos.j] == '.' && this.board[farPos.i][farPos.j] == '.' || this.board[nearPos.i][nearPos.j] == ' ' && this.board[farPos.i][farPos.j] == ' ';
    }

    public ArrayList<Pair> GetBoxRoute() {
        ArrayList<Pair> route = new ArrayList();
        route.add(this.box.value);
        SokoNode last = this.box;

        while(last.parent != null) {
            last = last.parent;
            route.add(last.value);
        }

        return route;
    }
}
