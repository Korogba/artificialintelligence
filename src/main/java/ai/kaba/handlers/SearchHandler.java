package ai.kaba.handlers;

import ai.kaba.abstracts.AbstractGraphWindow;
import ai.kaba.informed.AStarAlgorithm;
import ai.kaba.metaheuristics.GeneticSearch;
import ai.kaba.metaheuristics.SimulatedAnnealing;
import ai.kaba.metaheuristics.TabuSearch;
import ai.kaba.ui.OptimizationWindow;
import ai.kaba.ui.SearchWindow;
import ai.kaba.uninformed.BFSAlgorithm;
import ai.kaba.uninformed.DFSAlgorithm;
import org.apache.commons.lang3.text.WordUtils;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * Created by Yusuf on 4/11/2016
 * Handle Start, Goal, Search, Stop for SearchWindow
 */
public class SearchHandler implements ActionListener {

    private AbstractGraphWindow abstractGraphWindow;

    public SearchHandler(AbstractGraphWindow abstractGraphWindow) {
        this.abstractGraphWindow = abstractGraphWindow;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if(abstractGraphWindow.getClass().equals(SearchWindow.class)){
            SearchWindow searchWindow = (SearchWindow) abstractGraphWindow;
            /* Set up appropriate boolean */
            if (actionEvent.getSource() == searchWindow.getStart()) {
                String type = "start";
                showDialog(type);
            }
            if (actionEvent.getSource() == searchWindow.getGoal()) {
                String type = "goal";
                showDialog(type);
            }
        }
         /* Call appropriate algorithm */
        if (actionEvent.getSource() == abstractGraphWindow.getSearch() && abstractGraphWindow.getClass().equals(SearchWindow.class)) {
            switch (abstractGraphWindow.getAlgorithmIndex()) {
                case 0:
                    BFSAlgorithm bfsAlgorithm = new BFSAlgorithm(abstractGraphWindow.getAppWindow());
                    bfsAlgorithm.init(abstractGraphWindow.getAppGraph().getGraph());
                    bfsAlgorithm.compute();
                    break;
                case 1:
                    DFSAlgorithm dfsAlgorithm = new DFSAlgorithm(abstractGraphWindow.getAppWindow());
                    dfsAlgorithm.init(abstractGraphWindow.getAppGraph().getGraph());
                    dfsAlgorithm.compute();
                    break;
                case 2:
                    AStarAlgorithm aStarAlgorithm = new AStarAlgorithm(abstractGraphWindow.getAppWindow());
                    aStarAlgorithm.init(abstractGraphWindow.getAppGraph().getGraph());
                    aStarAlgorithm.compute();
                    break;
                default:
                    JOptionPane.showMessageDialog(abstractGraphWindow.getAppWindow(), "Oga, please ensure an appropriate search algorithm is selected from the menu",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
        /* Call appropriate algorithm */
        if (actionEvent.getSource() == abstractGraphWindow.getSearch() && abstractGraphWindow.getClass().equals(OptimizationWindow.class)) {
            switch (abstractGraphWindow.getAlgorithmIndex()) {
                case 0:
                    SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(abstractGraphWindow.getAppWindow());
                    simulatedAnnealing.init(abstractGraphWindow.getAppGraph().getGraph());
                    simulatedAnnealing.compute();
                    break;
                case 1:
                    TabuSearch tabuSearch = new TabuSearch(abstractGraphWindow.getAppWindow());
                    tabuSearch.init(abstractGraphWindow.getAppGraph().getGraph());
                    tabuSearch.compute();
                    break;
                case 2:
                    GeneticSearch geneticSearch = new GeneticSearch(abstractGraphWindow.getAppWindow());
                    geneticSearch.init(abstractGraphWindow.getAppGraph().getGraph());
                    geneticSearch.compute();
                    break;
                default:
                    JOptionPane.showMessageDialog(abstractGraphWindow.getAppWindow(), "Oga, please ensure an appropriate search algorithm is selected from the menu",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }

        if (actionEvent.getSource() == abstractGraphWindow.getClear()) {
            int choice = JOptionPane.showConfirmDialog(abstractGraphWindow.getAppWindow(), "Are you sure you want to clear graph?", "Clear Graph",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == 0) {
                clearGraph();
                abstractGraphWindow.getAppWindow().allStatus(true);
            }
        }
    }

    /*
    * Show dialog box to display Nodes to select
    */
    private void showDialog(String type) {
        Graph graph = abstractGraphWindow.getAppGraph().getGraph();
        int defaultGoal = 0;
        if(Objects.equals("goal", type)){
            defaultGoal = 1;
        }
        String[] nodeLabels = new String[graph.getNodeCount() + defaultGoal];
        int count = defaultGoal;
        for (Node node : graph) {
            nodeLabels[count] = node.getId();
            count++;
        }
        if(Objects.equals("goal", type)){
            nodeLabels[0] = "Goal not in graph";
        }
        Object selected = JOptionPane.showInputDialog(abstractGraphWindow, "Select a node from the list", WordUtils.capitalize(type) +
                " Node", JOptionPane.PLAIN_MESSAGE, null, nodeLabels, nodeLabels[0]);
        if (selected != null) {
            setNode((String) selected, graph, type);
        }
    }

    /*
    * Set the appropriate attributes on the selected nodes, clear from the other nodes
    */
    private void setNode(String selected, Graph graph, String type) {
        for (Node node : graph) {
            if (Objects.equals(node.getAttribute("ui.class", String.class), type)) {
                node.removeAttribute("ui.class");
            }
        }
        try {
            graph.getNode(selected).addAttribute("ui.class", type);
            abstractGraphWindow.getAppWindow().getStatus().setText(WordUtils.capitalize(type) + " node set to: " + graph.getNode(selected).getId());
        } catch (NullPointerException e){
            abstractGraphWindow.getAppWindow().getStatus().setText(WordUtils.capitalize(type) + " node set to: Non existent goal");
        }
    }

    private void clearGraph() {
        Graph graph = abstractGraphWindow.getAppGraph().getGraph();
        for (Node node : graph) {
            node.removeAttribute("ui.class");
            node.removeAttribute("visited?");
            node.removeAttribute("level");
            node.removeAttribute("parent");
        }
        for (Edge edge : graph.getEdgeSet()) {
            edge.removeAttribute("ui.color");
            edge.removeAttribute("ui.class");
        }
        if(abstractGraphWindow.getClass().equals(OptimizationWindow.class)){
            while (graph.getEdgeCount() > 0) {
                graph.removeEdge(0);
            }
        }
        abstractGraphWindow.getAppWindow().getStatus().setText("Graph cleared!");
    }
}
