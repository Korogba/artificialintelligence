package ai.kaba.ui;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDGS;
import org.graphstream.stream.file.FileSourceDOT;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import java.io.File;
import java.io.IOException;

/**
 * Created by Yusuf on 3/2/2016
 * Graph display
 */
public class AppGraph {

    private static Graph graph = new SingleGraph("Graph Traversal");

    /*
    *Initialize graph with edges and nodes
    */
    public static ViewPanel init(String graphName, boolean isDGS){

        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");

        initGraph(graphName, isDGS);

        initNodes(graph);

        return attachViewPanel();

    }

    private static ViewPanel attachViewPanel() {
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        return viewer.addDefaultView(false);
    }

    private static void initGraph(String graphName, boolean isDGS){
        FileSource fs;
        if(isDGS){
            fs = new FileSourceDGS();
        } else {
            fs = new FileSourceDOT();
        }
        String absolute_path = System.getProperty("user.home") + File.separator + graphName;
        fs.addSink(graph);
        try {
            fs.readAll(absolute_path);
        } catch (IOException | NullPointerException e ) {
            e.printStackTrace();
        } finally {
            fs.removeSink(graph);
        }
    }

    private static void initNodes(Graph graph) {
        for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }
    }

    /*
    * Getters
    */
    public static Graph getGraph() {
        return graph;
    }

    protected static String styleSheet =
            "graph {"+
                    "fill-color: #eee;" +
                    "}"+
            "edge {"+
                    "size: 2px;"+
                    "fill-mode: dyn-plain;"+
                    "fill-color: #000000, red, green;"+
                    "}"+
            "node {"+
                    "size: 25px;"+
                    "fill-mode: dyn-plain;"+
                    "fill-color: #FFFFFF, #f2ede4, #95b205;"+
                    "text-size: 16px;"+
                    "}" +
            "node.start {"+
                    "fill-color: #e1f9f2;"+
                    "stroke-mode: plain;"+
                    "stroke-color: #555;"+
                    "stroke-width: 3px;"+
                    "}" +
            "node.goal {"+
                    "fill-color: #e1f9f2;"+
                    "stroke-mode: plain;"+
                    "stroke-color: #855;"+
                    "stroke-width: 3px;"+
                    "shape: rounded-box;"+
                    "}" +
            "node:clicked {" +
                    "fill-color: #c7e475;"+
                    "}";

}
