package ai.kaba.machinelearning.ga;

import ai.kaba.abstracts.GeneticAlgorithm;
import ai.kaba.machinelearning.ga.helper.Individual;
import ai.kaba.ui.AppWindow;

/**
 * Created by Yusuf on 4/9/2015
 */
public class Rosenbrock  extends GeneticAlgorithm {

    /**
	 * Default generated SerialUID
	 */
	private static final long serialVersionUID = -4645093059700754148L;

	public Rosenbrock(AppWindow appWindow, double lowerBound, double upperBound) {
        super(appWindow, lowerBound, upperBound);
    }

    @Override
    public double fitness(Individual individual) {
        double sum = 0.0, a, b;
        double[] xArray = individual.xArray();
        for(int i = 0; i < xArray.length - 1; i++){
            a = Math.pow(xArray[i], 2) - xArray[i + 1];
            b = 1.0 - xArray[i];
            sum += 100.0 * (Math.pow(a, 2) + Math.pow(b, 2));
        }
        return  sum;
    }

    @Override
    public double getZ(double x, double y) {
        double a = Math.pow(x, 2) - y;
        double b = 1.0 - x;
        return 100.0 * (Math.pow(a, 2) + Math.pow(b, 2));
    }

    @Override
    public String returnName() {
        return "Rosenbrock Function";
    }

}
