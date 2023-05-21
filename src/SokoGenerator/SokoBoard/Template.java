//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator.SokoBoard;

import java.util.Arrays;

public class Template {
    public char[][] template;

    public Template(char[][] template) {
        this.template = template;
    }

    void Print(char[][] template) {
        for(int i = 0; i < template.length; ++i) {
            for(int j = 0; j < template.length; ++j) {
                System.out.print(template[i][j]);
            }

            System.out.println();
        }

    }

    public Template Clone() {
        Template clone = new Template((char[][])Arrays.stream(this.template).map((rec$) -> {
            return (char[])((char[])rec$).clone();
        }).toArray((x$0) -> {
            return new char[x$0][];
        }));
        return clone;
    }

    public void Rotate(int count) {
        for(int k = 0; k < count; ++k) {
            int n = this.template.length;

            for(int i = 0; i < (n + 1) / 2; ++i) {
                for(int j = 0; j < n / 2; ++j) {
                    char temp = this.template[n - 1 - j][i];
                    this.template[n - 1 - j][i] = this.template[n - 1 - i][n - j - 1];
                    this.template[n - 1 - i][n - j - 1] = this.template[j][n - 1 - i];
                    this.template[j][n - 1 - i] = this.template[i][j];
                    this.template[i][j] = temp;
                }
            }
        }

    }

    public void FlipX() {
        int length = this.template.length;

        for(int x = 0; x < length; ++x) {
            for(int y = 0; y < length / 2; ++y) {
                char tmp = this.template[x][length - y - 1];
                this.template[x][length - y - 1] = this.template[x][y];
                this.template[x][y] = tmp;
            }
        }

    }

    public void FlipY() {
        int length = this.template.length;

        for(int y = 0; y < length; ++y) {
            for(int x = 0; x < length / 2; ++x) {
                char tmp = this.template[length - x - 1][y];
                this.template[length - x - 1][y] = this.template[x][y];
                this.template[x][y] = tmp;
            }
        }

    }
}
