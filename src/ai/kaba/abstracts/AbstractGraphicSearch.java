package ai.kaba.abstracts;

import ai.kaba.ui.AppWindow;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by Yusuf on 3/8/2016.
 * Superclass of Breadth First and Depth First searches
 */
public abstract class AbstractGraphicSearch extends AbstractAlgorithm implements ActionListener {

    private Node startNode;
    private Node goalNode;
    private boolean executeTask;
    private Timer timer;

    public AbstractGraphicSearch(AppWindow appWindow) {
        super(appWindow);
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
    public void compute() {
        if(isExecuteTask()){
            getAppWindow().allStatus(false);
            getAppWindow().changeStatus("Running " + AppWindow.getAlgorithmString() + "...");
            getSearchTask().execute();
        } else {
            JOptionPane.showMessageDialog(getAppWindow(), "Oga Select Start AND Goal nodes", "Error", JOptionPane.ERROR_MESSAGE);
            getAppWindow().allStatus(true);
            getAppWindow().changeStatus("Failed to run: " + AppWindow.getAlgorithmString());
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Node nextNode = getPathToGoal().removeLast();
        Edge visitedEdge = nextNode.getEdgeBetween(nextNode.getAttribute("parent", Node.class));
        visitedEdge.setAttribute("ui.color", 1);
        if(nextNode == getStartNode() || getPathToGoal().size() == 0){
            getTimer().stop();
            getAppWindow().changeStatus("Done running " + AppWindow.getAlgorithmString()  + ".");
            getAppWindow().disableExceptClear();
        }
    }

    protected abstract class SearchTask extends SwingWorker<LinkedList<Node>, Node> {
        private ArrayList<Node> visitedList;
        private int visitedIndex = 0;
        private boolean traversalDone = false;
        private Timer traversal = new Timer(AppWindow.speed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Node lastVisited = visitedList.get(visitedIndex);
                Edge visitedEdge = lastVisited.getEdgeBetween(lastVisited.getAttribute("parent", Node.class));
                visitedEdge.setAttribute("ui.color", 0.5);
                visitedIndex++;
                if(visitedIndex >= visitedList.size()){
                    traversal.stop();
                    traversalDone = true;
                    if(AbstractGraphicSearch.this.getPathToGoal() != null){
                        startTimer();
                    }
                }
            }
        });

        @Override
        protected LinkedList<Node> doInBackground() throws Exception {
            Node found = publishNode(getStartNode(), getGoalNode());
            if (found != null) {
                return getGoalPath(found);
            } else{
                return null;
            }
        }

        @Override
        protected void process(List<Node> list) {
            visitedList = (ArrayList<Node>) list;
            traversal.start();
        }

        @Override
        protected void done() {
            try {
                AbstractGraphicSearch.this.pathToGoal = get();
                if(traversalDone && AbstractGraphicSearch.this.getPathToGoal() != null){
                    startTimer();
                }
                if(AbstractGraphicSearch.this.getPathToGoal() == null){
                    throw new NullPointerException("Goal Not Found.");
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                JOptionPane.showMessageDialog(getAppWindow(), "Goal Node Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
                getAppWindow().disableExceptClear();
                getAppWindow().changeStatus("Goal node not found");
            }
        }

        private LinkedList<Node> getGoalPath(Node found) {
            LinkedList<Node> path = new LinkedList<>();
            Node parent = found.getAttribute("parent");
            path.addLast(found);
            while (parent != getStartNode()){
                path.addLast(parent);
                parent = parent.getAttribute("parent");
            }
            return path;
        }

        protected abstract Node publishNode(Node start, Node goal);
    }

    private void initializeTimer() {
        timer = new Timer(AppWindow.speed, this);
    }

    private void startTimer(){
        getTimer().start();
    }

    public abstract SearchTask getSearchTask();

    public abstract void sortEdges(List<Edge> edges, Node reference);

    private boolean isExecuteTask() {
        return executeTask;
    }

    private Timer getTimer() {
        return timer;
    }

    protected Node getStartNode() {
        return startNode;
    }

    protected Node getGoalNode() {
        return goalNode;
    }
}
