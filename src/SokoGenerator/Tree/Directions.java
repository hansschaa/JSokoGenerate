//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator.Tree;

public enum Directions {
    LEFT(new Pair(0, -1)),
    UP(new Pair(-1, 0)),
    DOWN(new Pair(1, 0)),
    RIGHT(new Pair(0, 1));

    private Pair direction;

    public Pair getDirection() {
        return this.direction;
    }

    private Directions(Pair action) {
        this.direction = action;
    }
}
