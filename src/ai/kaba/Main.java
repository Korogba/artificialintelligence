package ai.kaba;

import ai.kaba.ui.AppWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        SwingUtilities.invokeLater(() -> {
            AppWindow mainWindow = new AppWindow();
            mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainWindow.setSize(500, 500);
            mainWindow.setVisible(true);
            AppWindow.setGraphPanel("graph.gv", false);
        });
    }
}
