package ai.kaba.abstracts;

import ai.kaba.abstracts.interfaces.Runner;
import ai.kaba.ui.AppGraph;
import ai.kaba.ui.AppWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Yusuf on 4/11/2016
 * Combines functionality of SearchWindow and OptimizationWindow
 */
public abstract class AbstractGraphWindow extends JPanel implements Runner {
    /*
    * Initialize gui components
    */
    private JButton clear;
    protected JButton search;
    protected AppGraph appGraph;
    protected AppWindow appWindow;
    private int algorithmIndex = -1;
    protected List<JRadioButton> algorithms;

    public AbstractGraphWindow (AppWindow appWindow) {
        /*
        * Set the layout clause
        */
        setLayout(new GridBagLayout());
        /*
        * Set up Buttons
        */
        this.appWindow = appWindow;

        search = new JButton("Search");
        GridBagConstraints searchConstraints = new GridBagConstraints();
        searchConstraints.fill = GridBagConstraints.HORIZONTAL;
        searchConstraints.gridx = 0;
        searchConstraints.gridy = 0;
        searchConstraints.weightx = 0.5;
        search.addActionListener(getHandler());
        add(search, searchConstraints);

        clear = new JButton("Clear");
        GridBagConstraints stopConstraints = new GridBagConstraints();
        stopConstraints.fill = GridBagConstraints.HORIZONTAL;
        stopConstraints.gridx = 1;
        stopConstraints.gridy = 0;
        stopConstraints.weightx = 0.5;
        clear.addActionListener(getHandler());
        add(clear, stopConstraints);

        JPanel sideBarAndGraph = new JPanel(new GridBagLayout());

        /*
        * Set up SideBar
        */
        JPanel sideBar = new JPanel();
        sideBar.setLayout(new GridLayout(0, 1));
        sideBar.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 10, 10), BorderFactory.createTitledBorder("Algorithms")));
        addButtons(sideBar);
        GridBagConstraints toolBarConstraints = new GridBagConstraints();
        toolBarConstraints.anchor = GridBagConstraints.PAGE_START;
        toolBarConstraints.gridx = 0;
        toolBarConstraints.gridy = 0;
        sideBarAndGraph.add(sideBar, toolBarConstraints);

        /*
        * Set up graph
        */
        appGraph = new AppGraph();
        GridBagConstraints graphConstraints = new GridBagConstraints();
        graphConstraints.fill = GridBagConstraints.BOTH;
        graphConstraints.gridx = 1;
        graphConstraints.gridy = 0;
        graphConstraints.weightx = 0.5;
        graphConstraints.weighty = 0.5;
        graphConstraints.gridwidth = GridBagConstraints.REMAINDER;
        graphConstraints.gridheight = GridBagConstraints.RELATIVE;
        JPanel graphPanel = new JPanel(new GridLayout(1, 1));
        graphPanel.add(initGraphPanel());
        sideBarAndGraph.add(graphPanel, graphConstraints);

        GridBagConstraints panelConstraints = new GridBagConstraints();
        panelConstraints.fill = GridBagConstraints.BOTH;
        panelConstraints.gridx = 0;
        panelConstraints.gridy = 1;
        panelConstraints.weightx = 0.5;
        panelConstraints.weighty = 0.5;
        panelConstraints.gridwidth = 4;
        panelConstraints.gridheight = GridBagConstraints.RELATIVE;
        add(sideBarAndGraph, panelConstraints);

    }

    private void addButtons(JPanel sideBar){
        initRadioButtons();
        ButtonGroup algorithmGroup = new ButtonGroup();
        RadioHandler radioHandler = new RadioHandler();

        for(JRadioButton radioButton: algorithms){
            sideBar.add(radioButton);
            algorithmGroup.add(radioButton);
            radioButton.addActionListener(radioHandler);
        }
    }

    protected abstract void initRadioButtons();
    protected abstract Component initGraphPanel();
    protected abstract ActionListener getHandler();
    public abstract void disableExceptClear();

    public void allStatus(boolean flag){
        for(JRadioButton radioButton: algorithms){
            radioButton.setEnabled(flag);
        }
    }

    /*
     * Getters
     */
    public JButton getClear() {
        return clear;
    }

    public JButton getSearch() {
        return search;
    }

    public AppGraph getAppGraph() {
        return appGraph;
    }

    public AppWindow getAppWindow() {
        return appWindow;
    }

    public int getAlgorithmIndex() {
        return algorithmIndex;
    }

    public List<JRadioButton> getAlgorithms() {
        return algorithms;
    }

    private class RadioHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            algorithms.stream().filter(radioButton -> radioButton == actionEvent.getSource()).forEach(radioButton -> {
                algorithmIndex = algorithms.indexOf(radioButton);
                AppWindow.setAlgorithmString(radioButton.getText());
                appWindow.changeTitle("Artificial Intelligence: " + radioButton.getText());
                appWindow.changeStatus(radioButton.getText() + " selected");
            });
        }
    }
}
