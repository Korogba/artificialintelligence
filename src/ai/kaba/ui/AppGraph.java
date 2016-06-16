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

    private Graph graph = new SingleGraph("Graph Traversal");
    private Viewer viewer;

    /*
    *Initialize graph with edges and nodes
    */
    public ViewPanel init(String graphName, boolean isDGS){

        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");

        initGraph(graphName, isDGS);

        initNodes(graph);

        return attachViewPanel();

    }

    private ViewPanel attachViewPanel() {
        viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        return viewer.addDefaultView(false);
    }

    private void initGraph(String graphName, boolean isDGS){
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

    private void initNodes(Graph graph) {
        for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }
    }

    /*
    * Getters
    */
    public Graph getGraph() {
        return graph;
    }

    public Viewer getViewer() {
        return viewer;
    }

    protected String styleSheet =
            "graph {"+
                    "fill-color: #FFFFFF;" +
                    "}"+
            "edge {"+
                    "size: 2px;"+
                    "fill-mode: dyn-plain;"+
                    "fill-color: #000000, red, green;"+
                    "}"+
            "edge.ann {"+
                    "shape: cubic-curve;"+
                    "text-mode:hidden;"+
                    "text-alignment: along;"+
                    "text-background-mode: rounded-box;"+
                    "}" +
            "edge.selectedAnn {"+
                    "shape: cubic-curve;"+
                    "text-mode:normal;"+
                    "text-alignment: along;"+
                    "text-background-mode: rounded-box;"+
                    "}" +
            "edge.externalEdge {"+
                    "size: 2px;"+
                    "}"+
            "sprite {"+
                    "shape: jcomponent;"+
                    "jcomponent: text-field;"+
                    "}"+
            "node {"+
                    "size: 25px;"+
                    "fill-mode: dyn-plain;"+
                    "fill-color: #EEEEEE, #f2ede4, #95b205;"+
                    "text-size: 16px;"+
                    "}" +
            "node.annOutput {"+
                    "size: 25px;"+
                    "text-mode:hidden;"+
                    "fill-color: #8C2;"+
                    "stroke-mode: plain;"+
                    "stroke-color: #999;"+
                    "shadow-mode: gradient-vertical;"+
                    "shadow-color: #999, white;"+
                    "shadow-offset: 0px;"+
                    "}" +
            "node.annInput {"+
                    "size: 25px;"+
                    "text-mode:hidden;"+
                    "fill-color: #f2f2f2;"+
                    "stroke-mode: plain;"+
                    "stroke-color: #999;"+
                    "shadow-mode: gradient-horizontal;"+
                    "shadow-width: 4px;"+
                    "shadow-offset: 0px;"+
                    "shadow-color: #999, white;"+
                    "shadow-offset: 0px;"+
                    "}" +
            "node.annHidden {"+
                    "size: 25px;"+
                    "text-mode:hidden;"+
                    "fill-color: #808080;"+
                    "stroke-mode: plain;"+
                    "stroke-width: 2px;"+
                    "stroke-color: #CCF;"+
                    "shadow-mode: gradient-radial;"+
                    "shadow-width: 10px;"+
                    "shadow-color: #EEF, #000;"+
                    "shadow-offset: 0px;"+
                    "}" +
            "node.externalNode {"+
                    "size: 10px;"+
                    "text-mode:hidden;"+
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
