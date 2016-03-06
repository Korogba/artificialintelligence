package ai.kaba;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by Yusuf on 3/4/2016
 * Breadth First Search Algorithm
 * <p>
 * parameters: Node startNode, Node GoalNode(Nullable)
 * Start:
 * startNode.parent = null
 * startNode.level = 0
 * startNode: mark as visited
 * Queue frontier
 * int i = 1
 * frontier.add(startNode)
 * while(!empty(frontier))
 * Queue next
 * for(Node current in frontier)
 * Node current = dequeue(frontier)
 * //At this point, do manipulations with current: Print to screen, add to search tree, add to search path or whatever
 * if(current == goal)
 * return;
 * for(Node siblings OF current)
 * if(sibling not visited)
 * sibling.level = i
 * addSiblingToLevel(sibling)
 * sibling.parent = current
 * next.add(sibling)
 * i++
 * frontier = next
 */
public class BFSAlgorithm implements Algorithm, ActionListener{

    private Node startNode;
    private Node goalNode;
    private AppWindow appWindow;
    private LinkedList<Node> pathToGoal;
    private boolean executeTask;
    private Timer timer;

    public BFSAlgorithm(AppWindow appWindow) {
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

        timer = new Timer(AppWindow.speed, this);

        executeTask = (goalExists && startExists);
    }

    @Override
    public void compute() {
        if(executeTask){
            new BFSTask().execute();
        } else {
            JOptionPane.showMessageDialog(appWindow, "Oga Select Start AND Goal nodes", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Node nextNode = pathToGoal.removeLast();
        Edge visitedEdge = nextNode.getEdgeBetween(nextNode.getAttribute("parent", Node.class));
        visitedEdge.setAttribute("ui.color", 1);
        if(nextNode == startNode || pathToGoal.size() == 0){
            timer.stop();
            appWindow.getStatus().setText("Done running " + AppWindow.algorithmString[AppWindow.searchNumber]  + ".");
            appWindow.disableExceptClear();
        }
    }

    private void startTimer(){
        timer.start();
    }

    private class BFSTask extends SwingWorker<LinkedList<Node>, Node>{
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
                    startTimer();
                }
            }
        });

        @Override
        protected LinkedList<Node> doInBackground() throws Exception {
            Node found = publishNodeBreadthFirst(startNode, goalNode);
            if (found != null) {
                return getPathToGoal(found);
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
                pathToGoal = get();
                if(traversalDone){
                    startTimer();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        private LinkedList<Node> getPathToGoal(Node found) {
            LinkedList<Node> path = new LinkedList<>();
            Node parent = found.getAttribute("parent");
            path.addLast(found);
            while (parent != startNode){
                path.addLast(parent);
                parent = parent.getAttribute("parent");
            }
            return path;
        }

        @Nullable
        private Node publishNodeBreadthFirst(Node start, Node goal){
            start.setAttribute("parent", "null");
            start.setAttribute("level", 0);
            start.setAttribute("visited?");
            LinkedList<Node> queueFrontier = new LinkedList<>();
            int level = 1;
            queueFrontier.addLast(start);
            while (!queueFrontier.isEmpty()) {
                System.out.println("Level: " + (level - 1));
                LinkedList<Node> next = new LinkedList<>();
                for (Node node : queueFrontier) {
                    if (node == goal) {
                        System.out.println(node.getId() + ": Found Found Found!!!");
                        publish(node);
                        return node;
                    }
                    System.out.print(node.getId() + " visited \t");
                    if (node != start){
                        publish(node);
                    }
                    for (Edge edge : node.getEdgeSet()) {
                        Node opposite = edge.getOpposite(node);
                        if (!opposite.hasAttribute("visited?")) {
                            System.out.print(opposite.getId() + " enqueued \t");
                            opposite.setAttribute("level", level);
                            opposite.setAttribute("parent", node);
                            opposite.setAttribute("visited?");
                            next.addLast(opposite);
                        }
                    }
                    System.out.print("\n");
                }
                level++;
                queueFrontier = next;
            }
            return null;
        }

    }

}
