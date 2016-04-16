package ai.kaba.informed;

import ai.kaba.ui.AppWindow;
import ai.kaba.abstracts.AbstractGraphicSearch;
import ai.kaba.abstracts.interfaces.CostInterface;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Yusuf on 3/9/2016
 *
 * A* Search
 * parameters: startNode, goalNode (means of estimating heuristics)
 *      initialize openList frontier (PriorityQueue)
 *      initialize closedList visited
 *      add startNode to frontier
 *      while (frontier != empty)
 *          get node n with minimal cost from frontier
 *          if n == goalNode
 *              return
 *          add n to visited
 *          for(node child of n)
 *              if child in visited
 *                  discard child
 *                  continue
 *              else if child in frontier
 *                  if(current.child.cost better than frontier.child.cost)
 *                      discard frontier.child
 *                      calculate new cost value for current.child
 *                      set parent of child to n
 *                      add child to frontier
 *                      continue
 *              else
 *                  calculate cost value for child
 *                  set child parent to n
 *                  add child to frontier
 *                  continue
 *           endForLoop
 *       endWhileLoop
 *       return null
 *
 */
public class AStarAlgorithm extends AbstractGraphicSearch implements CostInterface {

    public AStarAlgorithm(AppWindow appWindow) {
        super(appWindow);
    }

    @Override
    public AbstractGraphicSearch.SearchTask getSearchTask() {
        return  new SearchTask();
    }

    @Override
    public void sortEdges(List<Edge> edges, Node reference) {
        //Do nothing
    }

    @Override
    /* Get Proper Admissible, Monotonic Heuristic: This is not optimal!
       With a non-admissible heuristic, the A* algorithm could overlook
       the optimal solution to a search problem due to an overestimation in f(n)
       Which is the case when start node is A and goal node Q
       At the other extreme, if h(n) is very high relative to g(n),
       then only h(n) plays a role, and A* turns into Greedy Best-First-Search (Likely scenario here)
       At one extreme, if h(n) is 0, then only g(n) plays a role, and A* turns into Dijkstra’s algorithm, which is guaranteed to find a shortest path.
       If h(n) is always lower than (or equal to) the cost of moving from n to the goal,
       then A* is guaranteed to find a shortest path. The lower h(n) is, the more node A* expands, making it slower. (What we want)
       Reference: http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html
   */
    public int heuristic(Node currentNode, Node goalNode) {
        if(currentNode == goalNode) {
            return 0;
        }
        Character currentChar = currentNode.toString().charAt(0);
        Character goalChar = goalNode.toString().charAt(0);
        return Math.abs(Character.compare(currentChar, goalChar));
    }

    @Override
    public int cost(Node currentNode, Node startNode) {
        if(currentNode == startNode) {
            return 0;
        }
        int edgeCount = 1;
        Node parent = currentNode.getAttribute("parent");
        while(parent != startNode){
            edgeCount++;
            parent = parent.getAttribute("parent");
        }
        return edgeCount;
    }

    public int costFunction(Node currentNode){
        return (heuristic(currentNode, getGoalNode()) + cost(currentNode, getStartNode()));
    }

    private class SearchTask extends AbstractGraphicSearch.SearchTask{
        @Override
        @Nullable
        protected Node publishNode(Node startNode, Node goalNode) {
            PriorityQueue<Node> frontier = new PriorityQueue<>(5, (Comparator<Node>) (firstEdge, secondEdge) -> (costFunction(firstEdge) - costFunction(secondEdge)));
            List<Node> visited = new LinkedList<>();
            frontier.add(startNode);
            while(!frontier.isEmpty()){
                Node node = frontier.poll();
                if(node == goalNode){
                    System.out.println(node.getId()+ ": Found Found Found");
                    publish(node);
                    return node;
                }
                System.out.print(node.getId() + " visited- \t");
                visited.add(node);
                if (node != startNode) {
                    publish(node);
                }
                for(Edge edge: node.getEdgeSet()){
                    Node adjacent = edge.getOpposite(node);
                    if (visited.contains(adjacent)){
                        continue;
                    }
                    if(frontier.contains(adjacent)){
                        if((cost(node, startNode)+1) < cost(adjacent, startNode)){
                            frontier.remove(adjacent);
                            adjacent.setAttribute("parent", node);
                            frontier.add(adjacent);
                            System.out.print("Node: " + adjacent.toString() +" replaced in the frontier \t");
                        }
                    }
                    else {
                        adjacent.setAttribute("parent", node);
                        frontier.add(adjacent);
                        System.out.print("Node: " + adjacent.toString() +" added to frontier \t");
                    }
                }
                System.out.print("\nFrontier: " + frontier.toString() +"\n");
            }
            return null;
        }
    }
}
