package ai.kaba;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
public class SimulatedAnnealing extends AbstractAlgorithm {

    private List<Node> nodeList;
    private Graph graph;

    public SimulatedAnnealing(AppWindow appWindow) {
        super(appWindow);
    }

    @Override
    public void init(Graph graph) {
        this.graph = graph;
        nodeList = new LinkedList<>(graph.getNodeSet());
        Collections.shuffle(nodeList);
        for (Node node : nodeList) {
            node.addAttribute("layout.frozen");
        }
        updateEdges(nodeList, 0.5);
    }

    @Override
    public void compute() {
        new AnnealingTask().execute();
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

    private double getListCost(List<Node> nodeList){
        double listCost = 0;
        for(int i = 0; i < nodeList.size() ; i++){
            if(i != nodeList.size() - 1) {
                listCost += euclideanDistance(nodeList.get(i), nodeList.get(i + 1));
            } else{
                listCost += euclideanDistance(nodeList.get(i), nodeList.get(0));
            }
        }
        return listCost;
    }

    private double euclideanDistance(Node firstNode, Node secondNode) throws NullPointerException {
        double[] firstCoordinates = Toolkit.nodePosition(firstNode);
        double[] secondCoordinates = Toolkit.nodePosition(secondNode);
        double xDifference = Math.abs(firstCoordinates[0] - secondCoordinates[0]);
        double yDifference = Math.abs(firstCoordinates[1] - secondCoordinates[1]);
        return Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2));
    }

    private void updateEdges(List<Node> nodeList, double color){
        for(Edge edge : graph.getEdgeSet()){
            graph.removeEdge(edge);
        }
        for(int i = 0; i < nodeList.size() ; i++){
            if(i != nodeList.size() - 1) {
                graph.addEdge(nodeList.get(i).getId() + nodeList.get(i + 1).getId(), nodeList.get(i), nodeList.get(i + 1));
            } else{
                graph.addEdge(nodeList.get(i).getId() + nodeList.get(0).getId(), nodeList.get(i), nodeList.get(0));
            }
        }
        for(Edge edge : graph.getEdgeSet()){
            edge.addAttribute("ui.color", color);
        }
    }

    private class AnnealingTask extends SwingWorker<List<Node>, List<Node>> {

        private int listIndex = 0;
        private ArrayList<List<Node>> allCurrentBest;
        private Timer solutionTimer = new Timer(AppWindow.speed/4, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<Node> current = allCurrentBest.remove(listIndex);
                updateEdges(current, 0.5);
                if(listIndex >= allCurrentBest.size()){
                    solutionTimer.stop();
                }
            }
        });

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

        @Override
        protected void process(List<List<Node>> list) {
            allCurrentBest = (ArrayList<List<Node>>) list;
            solutionTimer.start();
        }

        @Override
        protected void done() {
            try {
                List<Node> finalList = get();
                updateEdges(finalList, 1);
                String printList = "";
                for(int i = 0; i <= finalList.size() ; i+=2){
                    if(i == finalList.size() - 1 || i == finalList.size()) {
                        printList +=  finalList.get(0).getId();
                    } else {
                        printList +=  finalList.get(i).getId() + " => " + finalList.get(i + 1).getId() + " => ";
                    }
                }
                System.out.println("Final Travel Path: " + printList);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }
    }
}
