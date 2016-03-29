package ai.kaba.abstracts;

import ai.kaba.ui.AppWindow;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.LinkedList;

/**
 * Created by Yusuf on 3/8/2016
 * Abstract class to implement common methods of the algorithms
 */
public abstract class AbstractAlgorithm implements Algorithm {

    private AppWindow appWindow;
    protected LinkedList<Node> pathToGoal;

    public AbstractAlgorithm(AppWindow appWindow){
        this.appWindow = appWindow;
    }

    @Override
    public abstract void compute();

    @Override
    public abstract void init(Graph graph);

    public AppWindow getAppWindow() {
        return appWindow;
    }

    public LinkedList<Node> getPathToGoal() {
        return pathToGoal;
    }

}
