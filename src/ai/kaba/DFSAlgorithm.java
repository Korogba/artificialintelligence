package ai.kaba;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.Nullable;

import javax.swing.Timer;
import java.util.*;

/**
 * Created by Yusuf on 3/4/2016
 * Depth First Search Algorithm: Same as Breadth First Search except a stack data structure is used as the frontier node
 * <p>
 * parameters: Node startNode, Node GoalNode(Nullable)
 * Start:
 * startNode.parent = null
 * startNode.level = 0
 * startNode: mark as visited
 * Stack frontier
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
public class DFSAlgorithm extends AbstractBlindSearch {

    public DFSAlgorithm(AppWindow appWindow) {
        super(appWindow);
    }

    @Override
    public AbstractBlindSearch.SearchTask getSearchTask() {
        return  new SearchTask();
    }

    @Override
    public void sortEdges(List<Edge> edges, Node reference) {
        Collections.sort(edges, (first, second) -> second.getOpposite(reference).getId().compareTo(first.getOpposite(reference).getId()));
    }

    @Override
    public void initializeTimer() {
        timer = new Timer(AppWindow.speed, this);
    }

    private class SearchTask extends AbstractBlindSearch.SearchTask{
        @Override
        @Nullable
        protected Node publishNode(Node start, Node goal) {
            start.setAttribute("parent", "null");
            start.setAttribute("level", 0);
            start.setAttribute("visited?");
            Stack<Node> stackFrontier = new Stack<>();
            stackFrontier.push(start);
            while (!stackFrontier.isEmpty()) {
                System.out.println("Stack Content: "+stackFrontier.toString());
                Node node = stackFrontier.pop();
                if (node == goal) {
                    System.out.println(node.getId() + ": Found Found Found!!!");
                    publish(node);
                    return node;
                }
                System.out.print(node.getId() + " visited \t");
                if (node != start) {
                    publish(node);
                }
                LinkedList<Edge> edges = new LinkedList<>(node.getEdgeSet());
                sortEdges(edges, node);
                for (Edge edge : edges) {
                    Node opposite = edge.getOpposite(node);
                    if (!opposite.hasAttribute("visited?")) {
                        System.out.print(opposite.getId() + " pushed onto stack \t");
                        opposite.setAttribute("parent", node);
                        opposite.setAttribute("visited?");
                        stackFrontier.push(opposite);
                    }
                }
                System.out.print("\n");
            }
            return null;
        }
    }
}
