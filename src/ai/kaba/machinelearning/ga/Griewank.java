package ai.kaba.machinelearning.ga;

import ai.kaba.abstracts.GeneticAlgorithm;
import ai.kaba.machinelearning.Individual;
import ai.kaba.ui.AppWindow;

/**
 * Created by Yusuf on 4/9/2016
 */
public class Griewank extends GeneticAlgorithm {

    public Griewank(AppWindow appWindow, double lowerBound, double upperBound) {
        super(appWindow, lowerBound, upperBound);
    }

    @Override
    public double fitness(Individual individual) {
        double sum = 0;
        double product = 1;
        int i = 1;
        for(double chromosome : individual.xDecimal()){
            sum += Math.pow(chromosome, 2);
            product *= Math.cos(chromosome / Math.sqrt(i));
            i++;
        }
        return ((sum / 4000.0) - product + 1.0);
    }

    @Override
    public double getZ(double x, double y) {
        double sum = Math.pow(x, 2) + Math.pow(y, 2);
        double product = (Math.cos(x / Math.sqrt(1))) * (Math.cos(y / Math.sqrt(2)));
        return ((sum / 4000.0) - product + 1.0);
    }

    @Override
    public String returnName() {
        return "Griewank Function";
    }


}
