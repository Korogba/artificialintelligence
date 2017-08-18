package ai.kaba.utils;

import java.util.List;

import org.graphstream.graph.Node;

public class StringUtils {
	/**
	 * Return a sting representation of the nodes in the List in the correct order
	 */
	public static String printListString(List<Node> listNodes){
		String printList = "";
        for(int i = 0; i <= listNodes.size() ; i+=2){
            if(i == listNodes.size() - 1 || i == listNodes.size()) {
                printList +=  listNodes.get(0).getId();
            } else {
                printList +=  listNodes.get(i).getId() + " => " + listNodes.get(i + 1).getId() + " => ";
            }
        }
        return printList;
	}
}
