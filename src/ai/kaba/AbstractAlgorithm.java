package ai.kaba;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by Yusuf on 3/8/2016
 * Abstract class to implement common methods of the algorithms
 */
public abstract class AbstractAlgorithm implements Algorithm {

    private Node startNode;
    private Node goalNode;
    private AppWindow appWindow;
    protected LinkedList<Node> pathToGoal;
    private boolean executeTask;
    protected Timer timer;

    public AbstractAlgorithm(AppWindow appWindow){
        this.appWindow = appWindow;
    }

    @Override
    public void init(Graph graph) {
        boolean startExists = false;
        boolean goalExists = false;
        for (Node node : graph) {
            if (Objects.equals(node.getAttribute("ui.class", String.class), "start")) {
                startNode = node;
                startExists = true;
            }
            if (Objects.equals(node.getAttribute("ui.class", String.class), "goal")) {
                goalNode = node;
                goalExists = true;
            }
        }
        if(!goalExists){
            goalNode = graph.nodeFactory().newInstance("-1", null);
        }

        initializeTimer();

        executeTask = (startExists);
    }

    @Override
    public abstract void compute();

    public abstract void initializeTimer();

    public Node getStartNode() {
        return startNode;
    }

    public Node getGoalNode() {
        return goalNode;
    }

    public AppWindow getAppWindow() {
        return appWindow;
    }

    public LinkedList<Node> getPathToGoal() {
        return pathToGoal;
    }

    public boolean isExecuteTask() {
        return executeTask;
    }

    public Timer getTimer() {
        return timer;
    }
}
