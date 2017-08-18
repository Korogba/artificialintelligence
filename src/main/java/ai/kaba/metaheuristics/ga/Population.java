package ai.kaba.metaheuristics.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.graphstream.graph.Node;

public class Population {
	
	private List<Genotype> solutions;
	private int populationSize;

	public Population(int populationSize) {
		solutions = new ArrayList<>();
		this.populationSize = populationSize;
	}
	
	public List<Genotype> getSolutions() {
		Collections.sort(solutions);
		return solutions;
	}

	public void setSolutions(List<Genotype> solutions) {
		Collections.sort(solutions);
		this.solutions = solutions;
	}
	
	public void addSolution(Genotype solution) {
		if(solutions.size() == populationSize) {
			throw new IllegalStateException("Population size exceeded");
		}
		solutions.add(solution);
		Collections.sort(solutions);
		for(Genotype anySolution: solutions){
			assignRank(anySolution);
		}
	}
	
	private void assignRank(Genotype solution) {
		int solutionPosition = solutions.indexOf(solution);
		solution.setRank(populationSize - solutionPosition);
	}

	public List<Node> getFromIndex(int i) {
		return solutions.get(i).getGenes();
	}

	public Genotype getBest() {
		Collections.sort(solutions);
		return solutions.get(0);
	}

	public List<Double> getAccummulatedRanking() {
		List<Genotype> reverseSolutions = new LinkedList<Genotype>(solutions);
		Collections.reverse(reverseSolutions);
		
		double total = totalRank();
		List<Double> accummulatedRanking = new LinkedList<Double>();
		double[] normalizedRank = new double[solutions.size()];
		
		for(int i = 0; i < reverseSolutions.size(); i++) {
			double rank = reverseSolutions.get(i).getRank();
			normalizedRank[i] = rank/total;
			double sum = normalizedRank[i];
			for(int j = 0; j < i; j++) {
				sum += normalizedRank[j];
			}
			accummulatedRanking.add(i, sum);
		}
		//Add?
//		Collections.reverse(accummulatedRanking);
		return accummulatedRanking;
	}

	private int totalRank() {
		int aggregateRank = 0;
		for(Genotype solution: solutions) {
			aggregateRank += solution.getRank();
		}
		return aggregateRank;
	}
}
