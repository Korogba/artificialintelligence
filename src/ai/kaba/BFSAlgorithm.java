package ai.kaba;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Objects;

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
public class BFSAlgorithm implements Algorithm {

    private Graph searchGraph;
    private Node startNode;
    private Node goalNode;
    private Node current;
    private AppWindow appWindow;

    public BFSAlgorithm(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    @Override
    public void init(Graph graph) {
        searchGraph = graph;
    }

    @Override
    public void compute() {
        boolean startExists = false;
        boolean goalExists = false;
        for (Node node : searchGraph) {
            if (Objects.equals(node.getAttribute("ui.class", String.class), "start")) {
                startNode = node;
                startExists = true;
            }
            if (Objects.equals(node.getAttribute("ui.class", String.class), "goal")) {
                goalNode = node;
                goalExists = true;
            }
        }
        if (goalExists && startExists) {
            callBFS(startNode, goalNode);
        } else {
            JOptionPane.showMessageDialog(appWindow, "Oga Select Start AND Goal nodes", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void callBFS(Node startNode, Node goalNode) {
        startNode.setAttribute("parent", "null");
        startNode.setAttribute("level", 0);
        startNode.setAttribute("visited?");
        LinkedList<Node> queueFrontier = new LinkedList<>();
        int level = 1;
        queueFrontier.addLast(startNode);
        while (!queueFrontier.isEmpty()) {
            System.out.println("Level: " + (level - 1));
            LinkedList<Node> next = new LinkedList<>();
            for (Node node : queueFrontier) {
                current = node;
                if (node == goalNode) {
                    System.out.println(node.getId() + ": Found Found Found!!!");
                    if (node != startNode) {
                        colorEdge(current);
                    }
                    return;
                }
                System.out.print(node.getId() + " visited \t");
                if (node != startNode) {
                    colorEdge(current);
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
            sleep();
        }
    }

    private void colorEdge(Node node) {
        Edge visitedEdge = node.getEdgeBetween(node.getAttribute("parent", Node.class));
        visitedEdge.setAttribute("ui.color", 0.5);
        sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(AppWindow.speed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
