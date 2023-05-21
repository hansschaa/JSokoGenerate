/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SokoGenerator.SokoBoard;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import SokoGenerator.Coordenate;
import java.util.ArrayList;

public class FloodFill {
    public ArrayList<Coordenate> visited = new ArrayList();
    public int[][] visitedAux;

    public FloodFill() {
    }

    public ArrayList<Coordenate> TryConnected(char[][] level) {
        if (this.visited.size() > 0) {
            this.visited.clear();
        }

        this.visitedAux = new int[level.length][level[0].length];
        this.Fill(level, this.GetAnyWhiteSpace(level));
        return this.visited;
    }

    private void Fill(char[][] level, Coordenate coordenate) {
        if (coordenate.x >= 0 && coordenate.x < level.length && coordenate.y >= 0 && coordenate.y < level[0].length) {
            if (this.visitedAux[coordenate.x][coordenate.y] != 1) {
                int i = coordenate.x;
                int j = coordenate.y;
                this.visitedAux[i][j] = 1;
                if (level[i][j] == ' ') {
                    this.visited.add(coordenate);
                    this.Fill(level, new Coordenate(i + 1, j));
                    this.Fill(level, new Coordenate(i - 1, j));
                    this.Fill(level, new Coordenate(i, j + 1));
                    this.Fill(level, new Coordenate(i, j - 1));
                }

            }
        }
    }

    public int GetWhiteSpaces(char[][] level) {
        int count = 0;

        for(int i = 0; i < level.length; ++i) {
            for(int j = 0; j < level[0].length; ++j) {
                if (level[i][j] == ' ') {
                    ++count;
                }
            }
        }

        return count;
    }

    public Coordenate GetAnyWhiteSpace(char[][] level) {
        int iMax = level.length;
        int jMax = level[0].length;

        for(int i = 0; i < iMax; ++i) {
            for(int j = 0; j < jMax; ++j) {
                if (level[i][j] == ' ') {
                    return new Coordenate(i, j);
                }
            }
        }

        return null;
    }
}

