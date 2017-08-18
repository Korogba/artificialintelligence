package ai.kaba.utils;

import java.util.List;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Node;

public class GraphUtils {
	
	public static double euclideanDistance(Node firstNode, Node secondNode) throws NullPointerException {
        double[] firstCoordinates = Toolkit.nodePosition(firstNode);
        double[] secondCoordinates = Toolkit.nodePosition(secondNode);
        double xDifference = Math.abs(firstCoordinates[0] - secondCoordinates[0]);
        double yDifference = Math.abs(firstCoordinates[1] - secondCoordinates[1]);
        return Math.sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2));
    }

    public static double getListCost(List<Node> nodeList){
        double listCost = 0;
        for(int i = 0; i < nodeList.size() ; i++) {
            if(i != nodeList.size() - 1) {
                listCost += euclideanDistance(nodeList.get(i), nodeList.get(i + 1));
            } else{
                listCost += euclideanDistance(nodeList.get(i), nodeList.get(0));
            }
        }
        return listCost;
    }

}
