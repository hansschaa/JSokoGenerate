/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SokoGenerator;

import SokoGenerator.Tree.Pair;

/**
 *
 * @author Hans
 */
public class BoxGoal {
    Pair boxPos;
    Pair goalPos;
    int manhattanDistance;
    
    public BoxGoal(Pair BoxPos, Pair GoalPos) {
        this.boxPos = BoxPos;
        this.goalPos = GoalPos;
        manhattanDistance = 0;
    }

    void UpdateManhattanDistance() {
        manhattanDistance = GeneratorUtils.GetManhattamDistance(boxPos, goalPos);
    }
    
    void Print(){
        System.out.println("Box: " + boxPos.i + "," + boxPos.j);
        System.out.println("Goal: " + goalPos.i + "," + goalPos.j);
    }

}
