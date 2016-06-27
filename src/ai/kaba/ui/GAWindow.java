package ai.kaba.ui;

import ai.kaba.abstracts.GeneticAlgorithm;
import ai.kaba.machinelearning.ga.Ackley;
import ai.kaba.machinelearning.ga.Griewank;
import ai.kaba.machinelearning.ga.Rastrigin;
import ai.kaba.machinelearning.ga.Rosenbrock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Yusuf on 4/14/2016
 * Display TabbedPane for Genetic Algorithm
 */
public class GAWindow extends JPanel {

    private JTabbedPane chartPane;

    GAWindow(AppWindow appWindow){
        super(new GridLayout(0, 1));
        chartPane = new JTabbedPane(JTabbedPane.LEFT);

        Ackley ackley = new Ackley(appWindow, -34.0254, 34.0254);
        chartPane.addTab("Ackley Function", ackley);
        chartPane.setMnemonicAt(0, KeyEvent.VK_1);

        Griewank griewank = new Griewank(appWindow, -600, 600);
        chartPane.addTab("Griewank Function", griewank);
        chartPane.setMnemonicAt(1, KeyEvent.VK_2);

        Rastrigin rastrigin = new Rastrigin(appWindow, -5.12, 5.12);
        chartPane.addTab("Rastrigin Function", rastrigin);
        chartPane.setMnemonicAt(2, KeyEvent.VK_3);

        Rosenbrock rosenbrock = new Rosenbrock(appWindow, -30, 30);
        chartPane.addTab("Rosenbrock Function", rosenbrock);
        chartPane.setMnemonicAt(3, KeyEvent.VK_4);

        chartPane.addChangeListener(changeEvent -> appWindow.setAppropriateTitleAndStatus(chartPane, "Genetic Algorithm"));

        add(chartPane);
    }

    public GeneticAlgorithm getCurrent(){
        return (GeneticAlgorithm) chartPane.getSelectedComponent();
    }

    JTabbedPane getChartPane() {
        return chartPane;
    }
}
