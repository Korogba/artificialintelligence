package ai.kaba;

import org.apache.commons.lang3.text.WordUtils;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * Created by Yusuf on 3/3/2016
 * Handle Start, Goal, Search, Stop
 */
public class ButtonHandler implements ActionListener {

    private AppWindow appWindow;

    public ButtonHandler(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        /* Set up appropriate boolean */
        if (actionEvent.getSource() == appWindow.getStart()) {
            String type = "start";
            showDialog(type);
        }
        if (actionEvent.getSource() == appWindow.getGoal()) {
            String type = "goal";
            showDialog(type);
        }
        /* Call appropriate algorithm */
        if (actionEvent.getSource() == appWindow.getSearch()) {
            switch (AppWindow.searchNumber) {
                case 0:
                    BFSAlgorithm bfsAlgorithm = new BFSAlgorithm(appWindow);
                    bfsAlgorithm.init(AppGraph.getGraph());
                    bfsAlgorithm.compute();
                    break;
                case 1:
                    DFSAlgorithm dfsAlgorithm = new DFSAlgorithm(appWindow);
                    dfsAlgorithm.init(AppGraph.getGraph());
                    dfsAlgorithm.compute();
                    break;
                case 2:
                    AStarAlgorithm aStarAlgorithm = new AStarAlgorithm(appWindow);
                    aStarAlgorithm.init(AppGraph.getGraph());
                    aStarAlgorithm.compute();
                    break;
                case 3:
                    SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(appWindow);
                    simulatedAnnealing.init(AppGraph.getGraph());
                    simulatedAnnealing.compute();
                    break;
                case 4:
                    TabuSearch tabuSearch = new TabuSearch(appWindow);
                    tabuSearch.init(AppGraph.getGraph());
                    tabuSearch.compute();
                    break;
                default:
                    JOptionPane.showMessageDialog(appWindow, "Oga, please ensure an appropriate search algorithm is selected from the menu",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
        if (actionEvent.getSource() == appWindow.getClear()) {
            int choice = JOptionPane.showConfirmDialog(appWindow, "Are you sure you want to clear graph?", "Clear Graph",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == 0) {
                clearGraph();
                appWindow.allStatus(true);
            }
        }
    }

    private void clearGraph() {
        Graph graph = AppGraph.getGraph();
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
        if(AppWindow.isTsp()){
            while (graph.getEdgeCount() > 0) {
                graph.removeEdge(0);
            }
        }
        appWindow.getStatus().setText("Graph cleared!");
    }

    /*
    * Show dialog box to display Nodes to select
    */
    private void showDialog(String type) {
        Graph graph = AppGraph.getGraph();
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
        Object selected = JOptionPane.showInputDialog(appWindow, "Select a node from the list", WordUtils.capitalize(type) +
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
            appWindow.getStatus().setText(WordUtils.capitalize(type) + " node set to: " + graph.getNode(selected).getId());
        } catch (NullPointerException e){
            appWindow.getStatus().setText(WordUtils.capitalize(type) + " node set to: Non existent goal");
        }
    }
}
