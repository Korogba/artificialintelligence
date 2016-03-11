package ai.kaba;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Yusuf on 3/3/2016
 * Handler Search Algorithm menu
 */
public class MenuHandler implements ActionListener {

    private AppWindow appWindow;

    public MenuHandler(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent){

        if(actionEvent.getSource() == appWindow.getSlow()){
            AppWindow.speed = 1000;
            appWindow.changeStatus("Search speed changed to: slow");
            return;
        }

        if(actionEvent.getSource() == appWindow.getModerate()){
            AppWindow.speed = 500;
            appWindow.changeStatus("Search speed changed to: moderate");
            return;
        }

        if(actionEvent.getSource() == appWindow.getFast()){
            AppWindow.speed = 200;
            appWindow.changeStatus("Search speed changed to: fast");
            return;
        }

        JMenuItem[] algorithms = appWindow.getAlgorithm();
        int count = 0;
        for (JMenuItem algorithm : algorithms) {
            if (actionEvent.getSource() == algorithm) {
                appWindow.changeTitle("Artificial Intelligence: " + algorithm.getText());
                appWindow.changeStatus("Algorithm change: " + algorithm.getText());
                AppWindow.searchNumber = count;
                break;
            }
            count++;
        }
    }
}
