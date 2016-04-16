package ai.kaba.machinelearning.ga;

import ai.kaba.abstracts.GeneticAlgorithm;
import ai.kaba.machinelearning.Individual;
import ai.kaba.ui.AppWindow;

/**
 * Created by Yusuf on 4/6/2016
 */
public class Ackley extends GeneticAlgorithm {

    public Ackley(AppWindow appWindow, double lowerBound, double upperBound) {
        super(appWindow, lowerBound, upperBound);
    }

    @Override
    public double fitness(Individual individual) {
        double powerSum = 0;
        double exponentSum = 0;
        int n = individual.xDecimal().size();
        for(double chromosome : individual.xDecimal()){
            powerSum += Math.pow(chromosome, 2);
            exponentSum += Math.cos(2.0 * Math.PI * chromosome);
        }
        return  -20.0 * Math.exp(-0.2 * Math.sqrt(powerSum / n)) - Math.exp(exponentSum / n) + 20.0 + Math.E;
    }

    @Override
    public double getZ(double x, double y) {
        double sum = Math.pow(x, 2) + Math.pow(y, 2);
        double secondSum = Math.cos(2.0 * Math.PI * x) + Math.cos(2.0 * Math.PI * y);
        return -20.0 * Math.exp(-0.2 * Math.sqrt(sum / 2)) - Math.exp(secondSum / 2) + 20.0 + Math.E;
    }

    @Override
    public String returnName() {
        return "Ackley Function";
    }
}
