package ai.kaba.metaheuristics.ga;

import java.util.List;

import org.graphstream.graph.Node;

import ai.kaba.utils.GraphUtils;

public class Genotype implements Comparable<Genotype>{
	
	private List<Node> genes;
	private int rank;
	private double fitness;

	public Genotype(List<Node> genes) {
		this.genes = genes;
		this.fitness = GraphUtils.getListCost(genes);
	}

	public List<Node> getGenes() {
		return genes;
	}

	public void setGenes(List<Node> genes) {
		this.genes = genes;
	}
	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	private double getFitness() {
		return fitness;
	}

	@Override
	public int compareTo(Genotype competitor) {
		if(this.getFitness() == competitor.getFitness()){
			return 0;
		}
		return (this.getFitness() > competitor.getFitness()) ? 1 : -1;
	}
}
