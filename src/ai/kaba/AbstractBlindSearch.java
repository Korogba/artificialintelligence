package ai.kaba;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Yusuf on 3/8/2016.
 * Superclass of Breadth First and Depth First searches
 */
public abstract class AbstractBlindSearch extends AbstractAlgorithm implements ActionListener {

    public AbstractBlindSearch(AppWindow appWindow) {
        super(appWindow);
    }

    @Override
    public void compute() {
        if(isExecuteTask()){
            getSearchTask().execute();
        } else {
            JOptionPane.showMessageDialog(getAppWindow(), "Oga Select Start AND Goal nodes", "Error", JOptionPane.ERROR_MESSAGE);
            getAppWindow().allStatus(true);
            getAppWindow().changeStatus("Failed to run: " + AppWindow.algorithmString[AppWindow.searchNumber]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Node nextNode = getPathToGoal().removeLast();
        Edge visitedEdge = nextNode.getEdgeBetween(nextNode.getAttribute("parent", Node.class));
        visitedEdge.setAttribute("ui.color", 1);
        if(nextNode == getStartNode() || getPathToGoal().size() == 0){
            getTimer().stop();
            getAppWindow().changeStatus("Done running " + AppWindow.algorithmString[AppWindow.searchNumber]  + ".");
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
                    if(AbstractBlindSearch.this.getPathToGoal() != null){
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
                AbstractBlindSearch.this.pathToGoal = get();
                if(traversalDone && AbstractBlindSearch.this.getPathToGoal() != null){
                    startTimer();
                }
                if(AbstractBlindSearch.this.getPathToGoal() == null){
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

    private void startTimer(){
        getTimer().start();
    }

    public abstract SearchTask getSearchTask();

    public abstract void sortEdges(List<Edge> edges, Node reference);
}
