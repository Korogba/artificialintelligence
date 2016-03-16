package ai.kaba;

import org.graphstream.graph.Node;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Yusuf on 3/11/2016
 * Simulated Annealing Algorithm
 * parameters: none
 * input: set of nodes with coordinates specified
 * start:
 *      generate random solution by randomly attaching edges to each node to form a round trip: nodeList
 *      get cost of random solution: finalCost
 *      temperature = 100
 *      monteCarlo = 50
 *      while(temperature < 0.0001)
 *          for 1 till monteCarlo
 *              tempList = perturb(nodeList) //randomly swap any two edges
 *              tempCost = get cost of tempList
 *              delta = tempCost - finalCost
 *              //if tempCost is lesser than finalCost swap current list with initial solution
 *              //else probabilistically swap current list with initial solution
 *              if(delta < 0 or exp(-delta/temperature) > Random(0,1)
 *                  nodeList = tempList
 *                  finalCost = tempCost
 *               endif
 *           endfor
 *           temperature = temperature * 0.999
 *       endwhile
 *       return nodeList
 */
public class SimulatedAnnealing extends MetaHeuristics {

    public SimulatedAnnealing(AppWindow appWindow) {
        super(appWindow);
    }

    @Override
    public void compute() {
        new SearchTask().execute();
    }

    private List<Node> perturbList(List<Node> nodeList){
        List<Node> returnList = new LinkedList<>(nodeList);
        int i, j;
        do{
            i = ThreadLocalRandom.current().nextInt(0, returnList.size());
            j = ThreadLocalRandom.current().nextInt(0, returnList.size());
        } while(i == j);
        Collections.swap(returnList, i, j);
        return returnList;
    }

    protected class SearchTask extends MetaHeuristics.SearchTask {

        @Override
        protected List<Node> doInBackground() throws Exception {
            double finalCost = getListCost(nodeList);
            System.out.println("Initial Cost: " + finalCost);
            double temperature = 100;
            int monteCarlo = 200;
            while(temperature > 0.0001){
                for(int i = 0; i < monteCarlo ; i++){
                    List<Node> tempList = perturbList(nodeList);
                    double tempCost = getListCost(tempList);
                    double delta = tempCost - finalCost;
                    if(delta < 0 || Math.exp((-delta/temperature)) > Math.random()){
                        finalCost = tempCost;
                        nodeList = tempList;
                        publish(nodeList);
                    }
                }
                temperature *= 0.999;
            }
            System.out.println("Final Cost: " + finalCost);
            return nodeList;
        }
    }
}
