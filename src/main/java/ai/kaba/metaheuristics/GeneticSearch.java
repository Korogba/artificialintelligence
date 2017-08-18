package ai.kaba.metaheuristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.graphstream.graph.Node;

import ai.kaba.abstracts.MetaHeuristics;
import ai.kaba.metaheuristics.ga.Genotype;
import ai.kaba.metaheuristics.ga.Population;
import ai.kaba.ui.AppWindow;

/**
 * Representation of solutions
 * How to generate random solutions?
 * Fitness functions
 * 
 * PseudoCode:
 * 
 * initialize population
 * evaluate population
 * while(!stoppingCondition)
 * 	select fittestIndividuals
 * 	crossover & mutate		
 *  evaluate population
 *  replace parents
 *  
 * @author @kaba_y
 */
public class GeneticSearch extends MetaHeuristics {
	
	private static int POPULATION_SIZE = 20;
	private Population population;
	private double mutationRate = 0.1;

	public GeneticSearch(AppWindow appWindow) {
		super(appWindow);
	}

	public void compute() {
		new SearchTask().execute();
	}
	
	/**
	 * Ordered crossover: First a swath of consecutive nodes/cities is sliced from the first parameter
	 * then consecutive nodes/cities not in the that swath are in the second parameter are added as they
	 * are found
	 * @param oneParent
	 * @param twoParent
	 * @return the sibling of the ordered crossover operator
	 */
	private Genotype orderedCrossover(Genotype oneParent, Genotype twoParent) {
		//Get the number of cities
		int totalNumberofCities = oneParent.getGenes().size();
		List<Node> firstParent = new LinkedList<>(oneParent.getGenes());
		List<Node> secondParent = new LinkedList<>(twoParent.getGenes());
		//Get start point of the swath
        int j = ThreadLocalRandom.current().nextInt(0, totalNumberofCities/2);
		//Get swath
        List<Node> genes;
        genes = (totalNumberofCities % 2  != 0) ? firstParent.subList(j, (totalNumberofCities/2 + j) + 1) :
        	firstParent.subList(j, (totalNumberofCities/2 + j));
        
        //Add cities in from the second parent not in the first 
        for(int i = 0; i < secondParent.size(); i++) {
        	if(!genes.contains(secondParent.get(i))) {
        		genes.add(secondParent.get(i));
        	}
        }
        //Return child
		return new Genotype(genes);
	}
	
	/**
	 * Swap two randomly selected items in the supplied list
	 * @param mutatedList the list to be mutated
	 */
	private Genotype randomSwapMutateList(Genotype mutateGene) {
		//Get two random indices that are not the same
		List<Node> genes = new LinkedList<>(mutateGene.getGenes());
		int i, j;
        do{
            i = ThreadLocalRandom.current().nextInt(0, genes.size());
            j = ThreadLocalRandom.current().nextInt(0, genes.size());
        } while(i == j);
        Collections.swap(genes, i, j);
        return new Genotype(genes);
	}
	
	/**
	 * Select a parent from the provided list using Rank selection
	 * @param parents the list of parents to select from
	 */
	private List<Genotype> selectParentUsingRank(Population currentPopulation) {
		//Get the accumulated rankings for each list
		List<Double> accummulatedRanks = currentPopulation.getAccummulatedRanking();
    	List<Genotype> parents = new ArrayList<Genotype>();
    	//Add the best
    	parents.add(currentPopulation.getBest());
    	int numberOfParents = (POPULATION_SIZE/2) - 2;
    	for(int i = 0; i < numberOfParents; i++) {
    		double randomValue = ThreadLocalRandom.current().nextDouble(0, 1);
    		double selectedParent = binarySearch(randomValue, accummulatedRanks);
    		int parentIndex;
    		if(selectedParent == 1.0) {
    			parentIndex = 0;
    		} else {
    			parentIndex = accummulatedRanks.indexOf(selectedParent);
    		}
    		
    		parents.add(currentPopulation.getSolutions().get(parentIndex));
    	}
		return parents;
	}

	private double binarySearch(double randomValue, List<Double> accummulatedRanks) {
		if(accummulatedRanks.size() == 1){
			return -1;
		}
		if(accummulatedRanks.size() == 2) {
			if(randomValue > accummulatedRanks.get(0)) {
				return randomValue < accummulatedRanks.get(1) ? accummulatedRanks.get(0) : accummulatedRanks.get(1);
			} else {
				return 1;
			}
		}
		int searchIndex = accummulatedRanks.size()/2;
		if(randomValue > accummulatedRanks.get(searchIndex)) {
			if(randomValue < accummulatedRanks.get(searchIndex +1)) {
				return accummulatedRanks.get(searchIndex);
			} else {
				return binarySearch(randomValue, accummulatedRanks.subList(searchIndex, accummulatedRanks.size()));
			}
		} else {
			return binarySearch(randomValue, accummulatedRanks.subList(0, searchIndex));
		}
	}

	/**
	 * Initialize the first generation randomly
	 * @param parents the list of parents to select from
	 */
	private void initializePopulation(){
		population = new Population(POPULATION_SIZE);
		for(int i = 0; i < POPULATION_SIZE; i++) {
			Collections.shuffle(nodeList);
			List<Node> genes = new LinkedList<>(nodeList);
			population.addSolution(new Genotype(genes));
		}
		nodeList = population.getBest().getGenes();
		double initCost = getListCost(nodeList);
		System.out.println("Initial Cost: " + initCost);
	}
	
	private class SearchTask extends MetaHeuristics.SearchTask {

        @Override
        protected List<Node> doInBackground() throws Exception {
        	initializePopulation();
        	int iterationNumber = 50000;
        	while(iterationNumber > 0) {
        		Population newGen = new Population(POPULATION_SIZE);
        		newGen.addSolution(population.getBest());
        		List<Genotype> fitIndividuals = selectParentUsingRank(population);
        		
        		while(newGen.getSolutions().size() < POPULATION_SIZE) {
        			int j, i;
        			 do{
        				 i = ThreadLocalRandom.current().nextInt(0, fitIndividuals.size());
        				 j = ThreadLocalRandom.current().nextInt(0, fitIndividuals.size());
        		     } while(i == j);
        			 Genotype firstChild = orderedCrossover(fitIndividuals.get(i), fitIndividuals.get(j));
        			 if(mutationRate > Math.random()){
        				 firstChild = randomSwapMutateList(firstChild);
        			 }
        			 newGen.addSolution(firstChild);
        		}
        		population = newGen;
        		nodeList = population.getBest().getGenes();
        		publish(nodeList);
        		iterationNumber--;
        	}
        	double finalCost = getListCost(nodeList);
        	System.out.println("Final Cost: " + finalCost);
            return nodeList;
        }
    }
}
