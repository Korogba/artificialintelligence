package ai.kaba.machinelearning.ga.helper;

import ai.kaba.abstracts.GeneticAlgorithm;
import org.jzy3d.maths.Coord3d;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Yusuf on 4/14/2016
 * Handler Swing animation for Genetic Algorithm
 */
public class TaskHandler extends SwingWorker<Void, List<Individual>> {

    private GeneticAlgorithm geneticAlgorithm;
    private List<List<Individual>> populationList;
    private Timer traversal = new Timer(200, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int listIndex = 0;
            List<Individual> current = populationList.remove(listIndex);
            geneticAlgorithm.getChart().getScene().remove(geneticAlgorithm.getScatter());
            geneticAlgorithm.getScatter().clear();
            Coord3d[] points = new Coord3d[geneticAlgorithm.getPopulation().size()];
            for(int i = 0; i < geneticAlgorithm.getPopulation().size(); i++){
                Individual test = current.get(i);
                double z = geneticAlgorithm.getZ(test.xDecimal().get(0), test.xDecimal().get(1));
                points[i] = new Coord3d(test.xDecimal().get(0), test.xDecimal().get(1), z);
            }
            geneticAlgorithm.getScatter().setData(points);
            geneticAlgorithm.getChart().getScene().add(geneticAlgorithm.getScatter());
            if(listIndex >= populationList.size()){
                traversal.stop();
                System.out.println("Final Coordinates: \n" + printCoordinates(geneticAlgorithm.getScatter().getData()) + "\nEnd");
                geneticAlgorithm.getAppWindow().changeStatus("Done Running " + geneticAlgorithm.returnName() + ".");
                geneticAlgorithm.getAppWindow().disableExceptClear();
            }
        }
    });

    public TaskHandler(GeneticAlgorithm geneticAlgorithm) {
        this.geneticAlgorithm = geneticAlgorithm;
    }

    @Override
    protected Void doInBackground() throws Exception {
        geneticAlgorithm.getAppWindow().allStatus(false);
        geneticAlgorithm.getAppWindow().changeStatus("Running " + geneticAlgorithm.returnName() + "...");
        for(int i = 0; i < 100; i++){
            geneticAlgorithm.evolve();
            List<Individual> individuals = geneticAlgorithm.getPopulation();
            publish(individuals);
        }
        return null;
    }

    @Override
    protected void done() {
        geneticAlgorithm.setTotalFitness();
        geneticAlgorithm.setAverageFitness();
        System.out.println("Start: \nAverage Fitness: " + geneticAlgorithm.getAverageFitness() + "\t" + "Total Fitness: " + geneticAlgorithm.getTotalFitness());
        System.out.println("=====================================================================================================");
        for(Individual test : geneticAlgorithm.getPopulation()){
            System.out.println(test.xDecimal());
        }
        System.out.println("=====================================================================================================");
        geneticAlgorithm.getPopulation().sort((firstIndividual, secondIndividual) -> firstIndividual.getFitness() <= secondIndividual.getFitness() ? -1 : 1);
        System.out.println("Best Fit: " + geneticAlgorithm.fitness(geneticAlgorithm.getPopulation().get(0)));
    }

    private String printCoordinates(Coord3d[] data) {
        String ret = "";
        for(Coord3d coord3d : data){
            ret += coord3d.toString() + "\n";
        }
        return ret;
    }

    @Override
    protected void process(List<List<Individual>> list) {
        populationList = list;
        traversal.start();
    }
}
