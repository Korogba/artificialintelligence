package ai.kaba;

import ai.kaba.ui.AppWindow;
import javax.swing.UIManager.*;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            //Use default look and feel
        }

        try {
            SwingUtilities.invokeAndWait(() -> {
                AppWindow mainWindow = new AppWindow();
                mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainWindow.setSize(800, 600);
                mainWindow.setVisible(true);
                //AppWindow.setGraphPanel("graph.gv", false);
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            AppWindow.callListeners();
        }

    }
}
