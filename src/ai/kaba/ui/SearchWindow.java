package ai.kaba.ui;

import ai.kaba.abstracts.AbstractGraphWindow;
import ai.kaba.handlers.SearchHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

/**
 * Created by Yusuf on 4/11/2016
 * Displays graph for Breadth-First, Depth-First and A* searches
 */
public class SearchWindow extends AbstractGraphWindow {

    protected JButton start;
    protected JButton goal;
    /*
     * Initialize gui components
     */
    public SearchWindow(AppWindow appWindow){
        super(appWindow);

        start = new JButton("Set Start");
        GridBagConstraints startConstraints = new GridBagConstraints();
        startConstraints.fill = GridBagConstraints.HORIZONTAL;
        startConstraints.gridx = 2;
        startConstraints.gridy = 0;
        startConstraints.weightx = 0.5;
        start.addActionListener(getHandler());
        add(start, startConstraints);

        goal = new JButton("Set Goal");
        GridBagConstraints goalConstraints = new GridBagConstraints();
        goalConstraints.fill = GridBagConstraints.HORIZONTAL;
        goalConstraints.gridx = 3;
        goalConstraints.gridy = 0;
        goalConstraints.weightx = 0.5;
        goal.addActionListener(getHandler());
        add(goal, goalConstraints);
    }

    @Override
    protected void initRadioButtons() {
        algorithms = new LinkedList<>();

        JRadioButton breadthFirstSearch = new JRadioButton("Breadth First Search");
        breadthFirstSearch.setMnemonic(KeyEvent.VK_B);
        algorithms.add(breadthFirstSearch);

        JRadioButton depthFirstSearch = new JRadioButton("Depth First Search");
        depthFirstSearch.setMnemonic(KeyEvent.VK_D);
        algorithms.add(depthFirstSearch);

        JRadioButton aStar = new JRadioButton("A* Search");
        aStar.setMnemonic(KeyEvent.VK_A);
        algorithms.add(aStar);
    }

    @Override
    protected Component initGraphPanel() {
        return appGraph.init("graph.gv", false);
    }

    @Override
    public ActionListener getHandler() {
        return new SearchHandler(this);
    }

    @Override
    public void allStatus(boolean flag) {
        getStart().setEnabled(flag);
        getGoal().setEnabled(flag);
        getClear().setEnabled(flag);
        getSearch().setEnabled(flag);
    }

    @Override
    public void disableExceptClear() {
        getStart().setEnabled(false);
        getGoal().setEnabled(false);
        getSearch().setEnabled(false);
        getClear().setEnabled(true);
    }

    public JButton getStart() {
        return start;
    }

    public JButton getGoal() {
        return goal;
    }
}
