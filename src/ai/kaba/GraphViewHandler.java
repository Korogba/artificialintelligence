package ai.kaba;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

/**
 * Created by Yusuf on 3/3/2016
 * Handle clicks on the displayed graph view
 */
public class GraphViewHandler {

    private static Graph graph = new SingleGraph("Graph Traversal");
    private static ViewPanel viewPanel;

    public static void initGraph(){
        /*
        * Initialize graph components
        */
        graph = new AppGraph().init();
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        viewPanel = viewer.addDefaultView(false);
        /*
        * Set up listeners to graph/view events
        */
        ProxyPipe fromViewer = viewer.newThreadProxyOnGraphicGraph();
        fromViewer.addSink(graph);
        fromViewer.pump();
    }

    /*@Override
    public void mouseClicked(MouseEvent mouseEvent) {
        super.mouseClicked(mouseEvent);
        for (Node node : graph) {
            if (mouseEvent.getSource() == node) {
                System.out.println(node.getId());
                break;
            }
        }
        System.out.println(mouseEvent.getSource());
    }*/

    /*@Override
    public void viewClosed(String viewName) {
    }

    @Override
    public void buttonPushed(String id) {
        System.out.println("Button pushed on node "+id);

    }

    @Override
    public void buttonReleased(String id) {
        System.out.println("Button released on node "+id);
    }*/

    /*
    * Getters
    */
    public static Graph getGraph() {
        return graph;
    }

    public static ViewPanel getViewPanel() {
        return viewPanel;
    }
}
