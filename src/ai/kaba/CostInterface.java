package ai.kaba;

import org.graphstream.graph.Node;

/**
 * Created by Yusuf on 3/10/2016
 * Implemented by informed algorithms
 */
public interface CostInterface {
    int heuristic(Node currentNode, Node goalNode);
    int cost(Node currentNode, Node startNode);
}
