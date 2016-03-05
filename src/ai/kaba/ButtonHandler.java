package ai.kaba;

import org.apache.commons.lang3.text.WordUtils;
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
        if(actionEvent.getSource() == appWindow.getStart()){
            String type = "start";
            showDialog(type);
        }
        if(actionEvent.getSource() == appWindow.getGoal()){
            String type = "goal";
            showDialog(type);
        }
        /* Call appropriate algorithm */
        if(actionEvent.getSource() == appWindow.getSearch()){
            switch (AppWindow.searchNumber) {
                case 0:
                    BFSAlgorithm bfsAlgorithm = new BFSAlgorithm(appWindow);
                    bfsAlgorithm.init(GraphViewHandler.getGraph());
                    bfsAlgorithm.compute();
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        }
        if(actionEvent.getSource() == appWindow.getStop()){
            try{
                JOptionPane.showMessageDialog(appWindow, "You stopped search Algorithm: " + AppWindow.algorithmString[AppWindow.searchNumber] );
            } catch (IndexOutOfBoundsException exception){
                JOptionPane.showMessageDialog(appWindow, "Oga Why you tryna stop something you never started" );
            }
        }
    }

    /*
    * Show dialog box to display Nodes to select
    */
    private void showDialog(String type) {
        Graph graph =  GraphViewHandler.getGraph();
        String[] nodeLabels = new String[graph.getNodeCount()];
        int count = 0;
        for (Node node : graph) {
            nodeLabels[count] = node.getId();
            count++;
        }
        Object selected = JOptionPane.showInputDialog(appWindow, "Select a node from the list", WordUtils.capitalize(type) + " Node", JOptionPane.PLAIN_MESSAGE, null, nodeLabels, nodeLabels[0]);
        if(selected != null){
            setNode((String) selected, graph, type);
        }
    }

    /*
    * Set the appropriate attributes on the selected nodes, clear from the other nodes
    */
    private void setNode(String selected, Graph graph, String type) {
        for (Node node : graph) {
            if(Objects.equals(node.getAttribute("ui.class", String.class), type)){
                node.removeAttribute("ui.class");
            }
        }
        graph.getNode(selected).addAttribute("ui.class", type);
        appWindow.getStatus().setText(WordUtils.capitalize(type) + " node set to: " +graph.getNode(selected).getId());
    }
}
