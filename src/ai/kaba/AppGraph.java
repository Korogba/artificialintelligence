package ai.kaba;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;

import java.io.File;
import java.io.IOException;

/**
 * Created by Yusuf on 3/2/2016
 * Graph display
 */
public class AppGraph {

    private Graph graph = new SingleGraph("Graph Traversal");

    /*
    *Initialize graph with edges and nodes
    */
    public Graph init(){

        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");

        initGraph();

        initNodes(graph);

        return graph;
    }

    private void initGraph(){
        FileSource fs = new FileSourceDOT();
        String graph_filename = "graph.gv";
        String absolute_path = System.getProperty("user.home") + File.separator + graph_filename;
        fs.addSink(graph);
        try {
            fs.readAll(absolute_path);
        } catch (IOException | NullPointerException e) {
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
