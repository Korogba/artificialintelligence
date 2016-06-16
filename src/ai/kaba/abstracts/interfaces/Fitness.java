package ai.kaba.abstracts.interfaces;

import ai.kaba.machinelearning.ga.helper.Individual;

/**
 * Created by Yusuf on 4/5/2016
 */
public interface Fitness {
    double fitness(Individual individual);
}
