package ai.kaba.ui;

import ai.kaba.abstracts.AbstractGraphWindow;
import ai.kaba.handlers.SearchHandler;
import org.graphstream.ui.swingViewer.ViewPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

/**
 * Created by Yusuf on 4/11/2016
 * Displays graph for Simulated Annealing and Tabu Search
 */
public class OptimizationWindow extends AbstractGraphWindow {
    /*
     * Initialize gui components
     */
    public OptimizationWindow (AppWindow appWindow){
        super(appWindow);
    }

    @Override
    protected void initRadioButtons() {
        algorithms = new LinkedList<>();

        JRadioButton simulatedAnnealing = new JRadioButton("Simulated Annealing");
        simulatedAnnealing.setMnemonic(KeyEvent.VK_S);
        algorithms.add(simulatedAnnealing);

        JRadioButton tabuSearch = new JRadioButton("Tabu Search");
        tabuSearch.setMnemonic(KeyEvent.VK_T);
        algorithms.add(tabuSearch);
    }

    @Override
    protected Component initGraphPanel() {
        ViewPanel viewPanel =  appGraph.init("dgsGraph.dgs", true);
        for(MouseListener toRemove : viewPanel.getMouseListeners()) {
            viewPanel.removeMouseListener(toRemove);
        }
        return viewPanel;
    }

    @Override
    public ActionListener getHandler() {
        return new SearchHandler(this);
    }

    @Override
    public void allStatus(boolean flag) {
        getClear().setEnabled(flag);
        getSearch().setEnabled(flag);
    }

    @Override
    public void disableExceptClear() {
        getSearch().setEnabled(false);
        getClear().setEnabled(true);
    }

}
