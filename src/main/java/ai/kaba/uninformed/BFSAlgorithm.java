package ai.kaba.uninformed;

import ai.kaba.ui.AppWindow;
import ai.kaba.abstracts.AbstractGraphicSearch;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Yusuf on 3/4/2016
 *
 * Breadth First Search Algorithm
 * parameters: Node startNode, Node GoalNode(Nullable)
 * Start:
 *      startNode.parent = null
 *      startNode.level = 0
 *      startNode: mark as visited
 *      Queue frontier
 *      int i = 1
 *      frontier.add(startNode)
 *      while(!empty(frontier))
 *          Queue next
 *          for(Node current in frontier)
 *              Node current = dequeue(frontier)
 *              //At this point, do manipulations with current: Print to screen, add to search tree, add to search path or whatever
 *              if(current == goal)
 *                  return;
 *              for(Node child OF current)
 *                  if(child not visited)
 *                      child.level = i
 *                      addChildToLevel(child)
 *                      child.parent = current
 *                      next.add(child)
 *                      i++
 *                  endif
 *              endfor
 *          frontier = next
 *          endfor
 *      endwhile
 *      return null //Node not found
 */
public class BFSAlgorithm extends AbstractGraphicSearch {

    public BFSAlgorithm(AppWindow appWindow) {
        super(appWindow);
    }

    @Override
    public void sortEdges(List<Edge> edges, Node reference) {
        Collections.sort(edges, (first, second) -> first.getOpposite(reference).getId().compareTo(second.getOpposite(reference).getId()));
    }

    @Override
    public AbstractGraphicSearch.SearchTask getSearchTask() {
        return  new SearchTask();
    }

    private class SearchTask extends AbstractGraphicSearch.SearchTask{
        @Override
        protected Node publishNode(Node start, Node goal){
            start.setAttribute("parent", "null");
            start.setAttribute("level", 0);
            start.setAttribute("visited?");
            LinkedList<Node> queueFrontier = new LinkedList<>();
            int level = 1;
            queueFrontier.addLast(start);
            while (!queueFrontier.isEmpty()) {
                System.out.println("Level " + (level - 1)+": "+ queueFrontier.toString());
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
                    LinkedList<Edge> edges = new LinkedList<>(node.getEdgeSet());
                    sortEdges(edges, node);
                    for (Edge edge : edges) {
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
