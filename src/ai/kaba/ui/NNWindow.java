package ai.kaba.ui;

import ai.kaba.abstracts.interfaces.Runner;
import ai.kaba.handlers.NNHandler;
import org.graphstream.graph.Edge;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Yusuf on 6/15/2016
 */
public class NNWindow extends JPanel implements ViewerListener, Runner {

    private JButton train;
    private JButton run;
    private JButton reset;
    private JButton restore;
    private JTextArea textArea;

    private boolean flag = true;

    private AppGraph appGraph;
    private ViewerPipe fromViewer;

    public NNWindow(AppWindow appWindow){

        super(new GridBagLayout());

        appGraph = new AppGraph();
        NNHandler nnHandler = new NNHandler(this, appWindow);

        textArea = new JTextArea("Results: ", 3, 0);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints textConstraints = new GridBagConstraints();
        textConstraints.fill = GridBagConstraints.HORIZONTAL;
        textConstraints.gridx = 0;
        textConstraints.gridy = 1;
        textConstraints.weighty = 0;
        textConstraints.weightx = 0.5;
        add(textArea, textConstraints);

        JPanel centerPanel = new JPanel(new GridBagLayout());

        train = new JButton("Train");
        GridBagConstraints trainConstraints = new GridBagConstraints();
        trainConstraints.fill = GridBagConstraints.HORIZONTAL;
        trainConstraints.gridx = 0;
        trainConstraints.gridy = 0;
        trainConstraints.weightx = 0.5;
        train.addActionListener(nnHandler);
        centerPanel.add(train, trainConstraints);

        run = new JButton("Run");
        GridBagConstraints runConstraint = new GridBagConstraints();
        runConstraint.fill = GridBagConstraints.HORIZONTAL;
        runConstraint.gridx = 1;
        runConstraint.gridy = 0;
        runConstraint.weightx = 0.5;
        run.addActionListener(nnHandler);
        centerPanel.add(run, runConstraint);

        reset = new JButton("Reset");
        GridBagConstraints resetConstraint = new GridBagConstraints();
        resetConstraint.fill = GridBagConstraints.HORIZONTAL;
        resetConstraint.gridx = 2;
        resetConstraint.gridy = 0;
        resetConstraint.weightx = 0.5;
        reset.addActionListener(nnHandler);
        centerPanel.add(reset, resetConstraint);

        restore = new JButton("Restore");
        GridBagConstraints restoreConstraint = new GridBagConstraints();
        restoreConstraint.fill = GridBagConstraints.HORIZONTAL;
        restoreConstraint.gridx = 3;
        restoreConstraint.gridy = 0;
        restoreConstraint.weightx = 0.5;
        restore.addActionListener(nnHandler);
        centerPanel.add(restore, restoreConstraint);

        GridBagConstraints graphConstraints = new GridBagConstraints();
        graphConstraints.fill = GridBagConstraints.BOTH;
        graphConstraints.gridx = 0;
        graphConstraints.gridy = 1;
        graphConstraints.weightx = 0.5;
        graphConstraints.weighty = 0.5;
        graphConstraints.gridwidth = 4;
        graphConstraints.gridheight = GridBagConstraints.RELATIVE;
        centerPanel.add(appGraph.init("multiLayerPerceptron.dgs", true), graphConstraints);

        GridBagConstraints centerConstraints = new GridBagConstraints();
        centerConstraints.fill = GridBagConstraints.BOTH;
        centerConstraints.gridx = 0;
        centerConstraints.gridy = 0;
        centerConstraints.weightx = 0.5;
        centerConstraints.weighty = 0.5;
        add(centerPanel, centerConstraints);

        Viewer viewer = appGraph.getViewer();

        fromViewer = viewer.newViewerPipe();
        fromViewer.addViewerListener(this);
        fromViewer.addSink(appGraph.getGraph());

    }

    @Override
    public void viewClosed(String viewName) {
        flag = false;
    }

    @Override
    public void buttonPushed(String id) {
        for(Edge edge : appGraph.getGraph().getNode(id).getLeavingEdgeSet()){
            edge.setAttribute("ui.class", "selectedAnn");
        }
    }

    @Override
    public void buttonReleased(String id) {
        for(Edge edge : appGraph.getGraph().getNode(id).getLeavingEdgeSet()){
            edge.setAttribute("ui.class", "ann");
        }
    }

    void callPump() {
        while (flag){
            fromViewer.pump();
        }
    }

    public JButton getTrain() {
        return train;
    }

    public JButton getRun() {
        return run;
    }

    public JButton getReset() {
        return reset;
    }

    public JButton getRestore() {
        return restore;
    }

    protected AppGraph getAppGraph() {
        return appGraph;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    @Override
    public JButton getClear() {
        return reset;
    }

    @Override
    public JButton getSearch() {
        return run;
    }

    @Override
    public void allStatus(boolean flag) {
        reset.setEnabled(flag);
        run.setEnabled(flag);
        train.setEnabled(flag);
        restore.setEnabled(flag);
    }

    @Override
    public void disableExceptClear() {
        reset.setEnabled(true);
        run.setEnabled(false);
        train.setEnabled(false);
        restore.setEnabled(false);
    }
}
