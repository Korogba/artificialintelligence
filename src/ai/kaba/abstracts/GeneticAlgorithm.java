package ai.kaba.abstracts;

import ai.kaba.abstracts.interfaces.Fitness;
import ai.kaba.abstracts.interfaces.Runner;
import ai.kaba.handlers.GAHandler;
import ai.kaba.machinelearning.ga.helper.Individual;
import ai.kaba.machinelearning.ga.helper.TaskHandler;
import ai.kaba.ui.AppWindow;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Yusuf on 4/4/2016
 */
public abstract class GeneticAlgorithm extends JPanel implements Fitness, Runner {

    protected int populationSize;
    protected double crossoverRate;
    protected double mutationRate;
    protected double randomAcceptance;
    protected int numberOfInputs;
    protected double lowerBound;
    protected double upperBound;
    protected List<Individual> population;
    protected double averageFitness;
    protected double totalFitness;

    protected Scatter scatter;
    protected Chart chart;
    protected JPanel chartPanel;
    protected JButton run;
    protected JButton clear;
    protected JComboBox<String> crossCombo;
    protected JComboBox<String> mutationCombo;
    protected JComboBox<String> acceptanceCombo;


    protected AppWindow appWindow;

    public GeneticAlgorithm(AppWindow appWindow, int populationSize, double crossoverRate, double mutationRate, double randomAcceptance, int numberOfInputs, double lowerBound, double upperBound) {

        this.appWindow = appWindow;

        this.populationSize = populationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.randomAcceptance = randomAcceptance;
        this.numberOfInputs = numberOfInputs;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        /*
        * Set the layout clause
        */
        setLayout(new GridBagLayout());

        chartPanel = new JPanel(new BorderLayout());
        GAHandler gaHandler = new GAHandler(appWindow);

        run = new JButton("Run");
        GridBagConstraints searchConstraints = new GridBagConstraints();
        searchConstraints.fill = GridBagConstraints.HORIZONTAL;
        searchConstraints.gridx = 0;
        searchConstraints.gridy = 0;
        searchConstraints.weightx = 0.5;
        run.addActionListener(gaHandler);
        add(run, searchConstraints);

        clear = new JButton("Reset");
        GridBagConstraints stopConstraints = new GridBagConstraints();
        stopConstraints.fill = GridBagConstraints.HORIZONTAL;
        stopConstraints.gridx = 1;
        stopConstraints.gridy = 0;
        stopConstraints.weightx = 0.5;
        clear.addActionListener(gaHandler);
        add(clear, stopConstraints);

        String[] rateCrossover = {"CrossOver Rate: 0.55", "CrossOver Rate: 0.65", "CrossOver Rate: 0.75", "CrossOver Rate: 0.85", "CrossOver Rate: 0.95", "CrossOver Rate: 1"};
        crossCombo = new JComboBox<>(rateCrossover);
        crossCombo.setSelectedIndex(3);
        GridBagConstraints crossConstraints = new GridBagConstraints();
        crossConstraints.fill = GridBagConstraints.HORIZONTAL;
        crossConstraints.gridx = 2;
        crossConstraints.gridy = 0;
        crossConstraints.weightx = 0.5;
        add(crossCombo, crossConstraints);

        String[] rateMutation = {"Mutation Rate: 0.075", "Mutation Rate: 0.085", "Mutation Rate: 0.095", "Mutation Rate: 0.15", "Mutation Rate: 0.25", "Mutation Rate: 0.35"};
        mutationCombo = new JComboBox<>(rateMutation);
        GridBagConstraints mutationConstraints = new GridBagConstraints();
        mutationConstraints.fill = GridBagConstraints.HORIZONTAL;
        mutationConstraints.gridx = 3;
        mutationConstraints.gridy = 0;
        mutationConstraints.weightx = 0.5;
        add(mutationCombo, mutationConstraints);

        String[] acceptanceRate = {"Acceptance Rate: 0.005", "Acceptance Rate: 0.01", "Acceptance Rate: 0.015", "Acceptance Rate: 0.020", "Acceptance Rate: 0.025", "Acceptance Rate: 0.030"};
        acceptanceCombo = new JComboBox<>(acceptanceRate);
        GridBagConstraints acceptanceConstraints = new GridBagConstraints();
        acceptanceConstraints.fill = GridBagConstraints.HORIZONTAL;
        acceptanceConstraints.gridx = 4;
        acceptanceConstraints.gridy = 0;
        acceptanceConstraints.weightx = 0.5;
        add(acceptanceCombo, acceptanceConstraints);

        /*
        * Set up chart
        */

        setUpChart();
        /*
        * Add to JPanel
        */
        GridBagConstraints chartConstraints = new GridBagConstraints();
        chartConstraints.fill = GridBagConstraints.BOTH;
        chartConstraints.gridx = 0;
        chartConstraints.gridy = 1;
        chartConstraints.weightx = 0.5;
        chartConstraints.weighty = 0.5;
        chartConstraints.gridwidth = 5;
        chartConstraints.gridheight = GridBagConstraints.RELATIVE;
        add(chartPanel, chartConstraints);
    }

    public GeneticAlgorithm(AppWindow appWindow, double lowerBound, double upperBound) {
        this(appWindow, 20, 0.85, 0.075, 0.05, 2, lowerBound, upperBound);
    }

    public abstract double fitness(Individual individual);
    public abstract double getZ(double x, double y);
    public abstract String returnName();

    public AppWindow getAppWindow() {
        return appWindow;
    }

    @Override
    public JButton getClear() {
        return clear;
    }

    @Override
    public JButton getSearch() {
        return run;
    }

    @Override
    public void allStatus(boolean flag) {
        clear.setEnabled(flag);
        run.setEnabled(flag);
        chartPanel.setEnabled(flag);
        crossCombo.setEnabled(flag);
        mutationCombo.setEnabled(flag);
        acceptanceCombo.setEnabled(flag);
    }

    @Override
    public void disableExceptClear() {
        clear.setEnabled(true);
        run.setEnabled(false);
        crossCombo.setEnabled(false);
        mutationCombo.setEnabled(false);
        acceptanceCombo.setEnabled(false);
    }

    public void nextGen() {
        new TaskHandler(this).execute();
    }

    public void clearChart(){
        chart.getScene().remove(scatter);
        scatter.clear();
        scatter.setData(generateRandomPoints());
        chart.getScene().add(scatter);
    }

    public void setUpChart(){
        generateInitialPopulation();
        Mapper mapper = new Mapper() {
            public double f(double x, double y) {
                return getZ(x, y);
            }
        };

        //Define range and precision for the function to plot
        Range range = new Range(lowerBound, upperBound);
        int steps = 60;

        //Create the object to represent the function over the given range.
        final Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);

        //Create a chart and add surface
        chart.getScene().getGraph().add(surface);
        chart.getScene().add(getScatter());
        chart.addController(ChartLauncher.configureControllers(chart, "View Controllers", true, false));
        chartPanel.add((Component)chart.getCanvas(), BorderLayout.CENTER);
    }

    public void generateInitialPopulation(){
        population = new LinkedList<>();
        scatter = new Scatter(generateRandomPoints(), new Color(0.0f, 0.0f, 0.0f), 5.0f);
        chart = new Chart(Quality.Nicest, "awt");
    }

    public List<Individual> getPopulation() {
        return population;
    }

    public void setTotalFitness() {
        totalFitness = 0;
        for(Individual individual : population){
            totalFitness += individual.getFitness();
        }
    }

    public void setAverageFitness() {
        averageFitness = totalFitness / population.size();
    }

    public double getAverageFitness() {
        return averageFitness;
    }

    public double getTotalFitness() {
        return totalFitness;
    }

    public Scatter getScatter() {
        return scatter;
    }

    public Chart getChart() {
        return chart;
    }

    public void evolve(){
        population.sort((firstIndividual, secondIndividual) -> firstIndividual.getFitness() <= secondIndividual.getFitness() ? -1 : 1);
        List<Individual> nextGeneration = new LinkedList<>();
        int eliteBoundary = (int) (populationSize * 0.6);
        nextGeneration.add(population.get(0));
        nextGeneration.addAll(doCrossOver(population.get(0), population.get(1)));
        nextGeneration.addAll(doCrossOver(population.get(0), population.get(2)));
        while(nextGeneration.size() < populationSize){
            int i, j;
            do{
                i = ThreadLocalRandom.current().nextInt(0, eliteBoundary);
                j = ThreadLocalRandom.current().nextInt(0, eliteBoundary);
            } while(i == j);
            Individual firstParent = population.get(i);
            Individual secondParent = population.get(j);
            List<Individual> offspring = doCrossOver(firstParent, secondParent);
            doMutate(offspring);
            nextGeneration.addAll(offspring);
            if(nextGeneration.size() <= populationSize && randomAcceptance > Math.random()){
                int k = ThreadLocalRandom.current().nextInt(0, populationSize);
                Individual random = population.get(k);
                mutate(random);
                nextGeneration.add(random);
            }
            removeDuplicates(nextGeneration);
            while(nextGeneration.size() > populationSize){
                nextGeneration.remove(nextGeneration.size() - 1);
            }
        }
        population = nextGeneration;
    }

    private void removeDuplicates(List<Individual> nextGeneration) {
        HashSet<Individual> tempSet = new HashSet<>();
        nextGeneration.stream().filter(individual -> !tempSet.contains(individual)).forEach(tempSet::add);
        nextGeneration.clear();
        nextGeneration.addAll(tempSet);
    }

    private void doMutate(List<Individual> offspring) {
        offspring.stream().filter(individual -> mutationRate > Math.random()).forEach(this::mutate);
    }

    private void mutate(Individual individual){
        int i = ThreadLocalRandom.current().nextInt(0 , numberOfInputs);
        String[] chromosomes = individual.breakUp();
        String mutatedString = "";
        for(char ch : chromosomes[i].toCharArray()){
            if(ch == '0'){
                mutatedString += "1";
            } else {
                mutatedString += "0";
            }
        }
        chromosomes[i] = mutatedString;
        individual.makeUp(chromosomes);
    }

    private List<Individual> doCrossOver(Individual firstParent, Individual secondParent) {
        List<Individual> siblings = new LinkedList<>();
        if(crossoverRate > Math.random()){
            siblings = crossOver(firstParent, secondParent);
        } else {
            siblings.add(firstParent);
            siblings.add(secondParent);
        }
        return siblings;
    }

    private List<Individual> crossOver(Individual firstParent, Individual secondParent) {
        List<Individual> sibling = new LinkedList<>();
        Individual firstSibling = new Individual(this);
        Individual secondSibling = new Individual(this);

        int i = ThreadLocalRandom.current().nextInt(0 , numberOfInputs);
        String[] firstChromosomes = firstParent.breakUp();
        String[] secondChromosomes = secondParent.breakUp();
        String temp = firstChromosomes[i];
        firstChromosomes[i] = secondChromosomes[i];
        secondChromosomes[i] = temp;
        firstSibling.makeUp(firstChromosomes);
        secondSibling.makeUp(secondChromosomes);

        sibling.add(firstSibling);
        sibling.add(secondSibling);
        return sibling;
    }

    private Coord3d[] generateRandomPoints() {
        RandomDataGenerator randomData = new RandomDataGenerator();
        for(int i = 0; i < populationSize; i++){
            List<Double> xValues = new LinkedList<>();
            for(int j = 0; j < numberOfInputs; j++){
                double nextDouble = randomData.nextUniform(lowerBound, upperBound);
                xValues.add(nextDouble);
            }
            Individual nextIndividual = new Individual(this, xValues);
            population.add(nextIndividual);
        }

        Coord3d[] points = new Coord3d[population.size()];
        for(int i = 0; i < population.size(); i++){
            Individual test = population.get(i);
            double z = getZ(test.xDecimal().get(0), test.xDecimal().get(1));
            points[i] = new Coord3d(test.xDecimal().get(0), test.xDecimal().get(1), z);
        }

        return points;
    }

}
