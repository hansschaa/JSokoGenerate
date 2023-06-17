//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package SokoGenerator;

import jenes.population.Individual;
import jenes.stage.operator.Mutator;

public class ChangeSokoElementPosMutator<T extends SokobanChromosome> extends Mutator<T> {
    public ChangeSokoElementPosMutator(double probability) {
        super(probability);
    }

    protected void mutate(Individual<T> t) {
        SokobanChromosome c = (SokobanChromosome)t.getChromosome();
        c.randomize();
    }
}
