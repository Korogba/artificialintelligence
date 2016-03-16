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

/**
 * Created by Yusuf on 3/15/2016
 * SubClasses: SimulatedAnnealing and TabuSearch
 */
public abstract class MetaHeuristics extends AbstractAlgorithm {

    protected List<Node> nodeList;
    protected Graph graph;

    public MetaHeuristics(AppWindow appWindow) {
        super(appWindow);
    }
    /*
    * Getters
    */
    public Graph getGraph() {
        return graph;
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
        getAppWindow().allStatus(false);
        getAppWindow().changeStatus("Running " + AppWindow.algorithmString[AppWindow.searchNumber] + "...");
    }

    protected void updateEdges(List<Node> nodeList, double color){
        while (graph.getEdgeCount() > 0) {
            graph.removeEdge(0);
        }
        for(int i = 0; i < nodeList.size(); i++){
            if(i != nodeList.size() - 1) {
                graph.addEdge(nodeList.get(i).getId() + nodeList.get(i + 1).getId(), nodeList.get(i), nodeList.get(i + 1));
            } else {
                graph.addEdge(nodeList.get(i).getId() + nodeList.get(0).getId(), nodeList.get(i), nodeList.get(0));
            }
        }
        for(Edge edge : graph.getEdgeSet()){
            edge.addAttribute("ui.color", color);
        }
    }

    protected double euclideanDistance(Node firstNode, Node secondNode) throws NullPointerException {
        double[] firstCoordinates = Toolkit.nodePosition(firstNode);
        double[] secondCoordinates = Toolkit.nodePosition(secondNode);
        double xDifference = Math.abs(firstCoordinates[0] - secondCoordinates[0]);
        double yDifference = Math.abs(firstCoordinates[1] - secondCoordinates[1]);
        return Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2));
    }

    protected double getListCost(List<Node> nodeList){
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

    protected abstract class SearchTask extends SwingWorker<List<Node>, List<Node>> {

        private int listIndex = 0;
        private ArrayList<List<Node>> publishedList;
        private Timer solutionTimer = new Timer(AppWindow.speed/4, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<Node> current = publishedList.remove(listIndex);
                updateEdges(current, 0.5);
                if(listIndex >= publishedList.size()){
                    solutionTimer.stop();
                }
            }
        });

        @Override
        protected void process(List<List<Node>> list) {
            publishedList = (ArrayList<List<Node>>) list;
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
                getAppWindow().changeStatus("Done running " + AppWindow.algorithmString[AppWindow.searchNumber]  + ".");
                getAppWindow().disableExceptClear();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
