//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator;

import jenes.population.Individual;
import jenes.stage.operator.Crossover;

public class SokoCrossover<T extends SokobanChromosome> extends Crossover<T> {
    public SokoCrossover(double probability) {
        super(probability);
    }

    public int spread() {
        return 2;
    }

    protected void cross(Individual<T>[] offsprings) {
        SokobanChromosome chromC1 = (SokobanChromosome)offsprings[0].getChromosome();
        SokobanChromosome chromC2 = (SokobanChromosome)offsprings[1].getChromosome();
        chromC1.cross(chromC2, 1);
        chromC2.cross(chromC1, 1);
    }
}
