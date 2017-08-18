package ai.kaba.metaheuristics;

import ai.kaba.ui.AppWindow;
import ai.kaba.utils.GraphUtils;
import ai.kaba.abstracts.MetaHeuristics;
import org.graphstream.graph.Node;

import java.util.*;

/**
 * Created by Yusuf on 3/15/2016
 * Tabu Search Algorithm
 * Start:
 *      generate random starting solution, currentTour
 *      generateHeuristicList of currentTour
 *      make cost of currentTour: bestCost
 *      loop: for total number of predefined iterations
 *          if: after every predefined number of steps
 *              shuffle currentTour
 *              generateHeuristicList of currentTour
 *              clear tabuList
 *          endif
 *          generateBestNeighbor of currentTour
 *          update tabuList
 *          if(cost of currentTour < cost of bestTour)
 *              bestTour = currentTour
 *              bestCost = cost of currentTour
 *              publish bestTour
 *          endif
 *      endLoop
 *      return bestTour
 *
 */
public class TabuSearch extends MetaHeuristics {

    private double bestCost;
    private List<Node> bestTour;
    private List<Node> currentTour;
    private Set<Node> currentSwap;
    private List<Set<Node>> tabuList = new LinkedList<>();

    public TabuSearch(AppWindow appWindow) {
        super(appWindow);
    }

    @Override
    public void compute() {
        currentTour = nodeList;
        bestTour = nodeList;
        generateHeuristicList();
        bestCost = getListCost(currentTour);
        System.out.println("Initial Cost: " + bestCost);
        new SearchTask().execute();
    }

    private class SearchTask extends MetaHeuristics.SearchTask {

        @Override
        protected List<Node> doInBackground() throws Exception {
            for(int i = 0; i < 60000; i++){
                if(i % 30 == 0){
                    Collections.shuffle(currentTour);
                    generateHeuristicList();
                    tabuList.clear();
                }
                generateBestNeighbor(currentTour);
                updateTabu();
                if(getListCost(currentTour) < bestCost){
                    bestTour = currentTour;
                    bestCost = getListCost(currentTour);
                    publish(currentTour);
                }
            }
            System.out.println("Final Cost: " + bestCost);
            return bestTour;
        }
    }

    private void updateTabu() {
        if(tabuList.size() > 25) {
            tabuList.remove(tabuList.size()-1);
            tabuList.add(currentSwap);
            //System.out.println("Tabu" + tabuList.toString());
        } else {
            tabuList.add(currentSwap);
            //System.out.println("Tabu" + tabuList.toString());
        }
    }

    private void generateBestNeighbor(List<Node> nodeList) {
        double bestInnerCost = 0;
        Set<Node> swap = new HashSet<>(2);
        List<Node> bestInnerList = new LinkedList<>();
        Node firstSwapNode = null;
        Node secondSwapNode = null;
        int first;
        int second;
        for(first = 0; first < nodeList.size()-1; first++) {
            List<Node> swapList = new LinkedList<>(nodeList);
            second = first + 1;
            Collections.swap(swapList, first, second);
            //System.out.println("Swap List: " + swapList);
            if(getListCost(swapList) < bestInnerCost || bestInnerCost == 0 ){
                if(tabu(swapList.get(first), swapList.get(second))){
                    if(aspirationCriteria(swapList)){
                        bestInnerList = swapList;
                        bestInnerCost = getListCost(swapList);
                        firstSwapNode = swapList.get(first);
                        secondSwapNode = swapList.get(second);
                        //System.out.println("Aspiration MET!");
                    }
                } else {
                    bestInnerList = swapList;
                    bestInnerCost = getListCost(swapList);
                    firstSwapNode = swapList.get(first);
                    secondSwapNode = swapList.get(second);
                    //System.out.println("2nd inner");
                }
            }
        }
        swap.add(firstSwapNode);
        swap.add(secondSwapNode);
        currentTour = bestInnerList;
        currentSwap = swap;
    }

    /*private class TabuNode{
        Node tabuNode;
        int position;

        public TabuNode() {
        }

        public TabuNode(Node tabuNode, int position) {
            this.tabuNode = tabuNode;
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        public void init(Node tabuNode, int position) {
            this.tabuNode = tabuNode;
            this.position = position;
        }

    }*/

    private boolean aspirationCriteria(List<Node> nodeList) {
        return getListCost(nodeList) < bestCost;
    }

    private void generateHeuristicList(){
        double smallestDistance = 0;
        for(int i = 0; i < currentTour.size()-1; i+=2){
            for(int j = 1; j < currentTour.size(); j++) {
                int nextToCurrent = i + 1;
                double currentDistance = GraphUtils.euclideanDistance(currentTour.get(i), currentTour.get(j));
                if(currentDistance < smallestDistance || smallestDistance == 0){
                    if(smallestDistance != 0) {
                        Collections.swap(currentTour, nextToCurrent, j);
                    }
                    smallestDistance = currentDistance;
                }
            }
        }
    }

    private boolean tabu(Node firstNode, Node secondNode) {
        Set<Node> testTabu = new HashSet<>(2);
        testTabu.add(firstNode);
        testTabu.add(secondNode);
        return tabuList.contains(testTabu);
//        for(Set<TabuNode> setTabuNode : tabuList){
//            if(setTabuNode.contains(firstNode) || setTabuNode.contains(secondNode)){
//                if(firstNode.getPosition() == first || secondNode.getPosition() == second){
//                    taboo = true;
//                }
//            }
//        }
    }
}
