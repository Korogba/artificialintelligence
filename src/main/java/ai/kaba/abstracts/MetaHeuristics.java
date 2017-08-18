package ai.kaba.abstracts;

import ai.kaba.ui.AppWindow;
import ai.kaba.utils.GraphUtils;
import ai.kaba.utils.StringUtils;

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
 * Created by Yusuf on 3/15/2015
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
        getAppWindow().changeStatus("Running " + AppWindow.getAlgorithmString() + "...");
    }

    private void updateEdges(List<Node> nodeList, double color){
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

    protected double getListCost(List<Node> nodeList){
        return GraphUtils.getListCost(nodeList);
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
            	solutionTimer.stop();
                List<Node> finalList = get();
                updateEdges(finalList, 1);
                String printList = StringUtils.printListString(finalList);
                System.out.println("Final Travel Path: " + printList);
                getAppWindow().changeStatus("Done running " + AppWindow.getAlgorithmString()  + ".");
                getAppWindow().disableExceptClear();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
