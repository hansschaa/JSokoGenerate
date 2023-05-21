//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator.Tree;

public class Pair {
    public int i;
    public int j;

    public Pair(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public Pair plus(Pair that) {
        return new Pair(this.i + that.i, this.j + that.j);
    }

    public Pair minus(Pair that) {
        return new Pair(this.i - that.i, this.j - that.j);
    }

    public Pair multiply(int factor) {
        return new Pair(this.i * factor, this.j * factor);
    }

    @Override
    public String toString() {
        return "Pair{i=" + this.i + ", j=" + this.j + "}";
    }

    public boolean IsEquals(Pair newPair) {
        return this.i == newPair.i && this.j == newPair.j;
    }
}
