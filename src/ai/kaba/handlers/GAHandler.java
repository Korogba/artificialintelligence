package ai.kaba.handlers;

import ai.kaba.abstracts.GeneticAlgorithm;
import ai.kaba.ui.AppWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Yusuf on 4/15/2016
 * Handle Genetic Algorithm events
 */
public class GAHandler implements ActionListener {

    private AppWindow appWindow;
    private boolean toReset = false;

    public GAHandler(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        GeneticAlgorithm current = appWindow.getGaWindow().getCurrent();
        if(actionEvent.getSource() == current.getSearch()){
            current.nextGen();
            toReset = true;
        }
        if(actionEvent.getSource() == current.getClear()){
            if(toReset) {
                current.clearChart();
                toReset = false;
            }
            appWindow.allStatus(true);
        }
    }

}
