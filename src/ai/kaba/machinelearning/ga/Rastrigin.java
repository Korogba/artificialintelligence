package ai.kaba.machinelearning.ga;

import ai.kaba.abstracts.GeneticAlgorithm;
import ai.kaba.machinelearning.Individual;
import ai.kaba.ui.AppWindow;

/**
 * Created by Yusuf on 4/9/2016
 */
public class Rastrigin extends GeneticAlgorithm {

    public Rastrigin(AppWindow appWindow, double lowerBound, double upperBound) {
        super(appWindow, lowerBound, upperBound);
    }

    @Override
    public double fitness(Individual individual) {
        double sum = 0;
        for(double chromosome : individual.xDecimal()){
            sum += Math.pow(chromosome, 2) - 10.0 * Math.cos(2.0 * Math.PI * chromosome) + 10.0;
        }
        return  sum;
    }

    @Override
    public double getZ(double x, double y) {
        return (Math.pow(x, 2) - 10.0 * Math.cos(2.0 * Math.PI * x) + 10.0) + (Math.pow(y, 2) - 10.0 * Math.cos(2.0 * Math.PI * y) + 10.0);
    }

    @Override
    public String returnName() {
        return "Rastrigin Function";
    }

}
