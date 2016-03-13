package ai.kaba;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Yusuf on 2/28/2016.
 * AppWindow contains the gui components of the application
 */
public class AppWindow extends JFrame{
    /*
    * Initialize gui components
    */
    private JMenuItem[] algorithm;
    private JMenuItem slow;
    private JMenuItem moderate;
    private JMenuItem fast;
    private JButton start;
    private JButton goal;
    private JButton clear;
    private JButton search;
    private JTextField status;
    private static boolean tsp;
    private static boolean firstDraw = true;
    private static JPanel graphPanel = new JPanel(new GridLayout(1,1));
    public static String title = "Artificial Intelligence";
    public static String[] algorithmString = {"Breadth First Search", "Depth First Search", "A*", "Simulated Annealing"};
    public static int searchNumber = -1;
    public static String statusBar = "status";
    public static int speed = 500;

    /*
    * Constructor for displaying application window
    */
    public AppWindow(){
        /*
        * Call superclass constructor
        */
        super(title);
        /*
        * Set the layout clause
        */
        setLayout(new GridBagLayout());
        /*
        * Set up menus
        */
        JMenuBar jMenuBar = new JMenuBar();
        JMenu searchTitle = new JMenu("Search Algorithm");
        searchTitle.setMnemonic('a');
        algorithm = new JMenuItem[algorithmString.length];

        MenuHandler menuHandler = new MenuHandler(this);
        for(int count = 0; count < algorithmString.length; count++){
            algorithm[count] = new JMenuItem(algorithmString[count]);
            searchTitle.add(algorithm[count]);
            algorithm[count].addActionListener(menuHandler);
        }

        JMenu speed = new JMenu("Speed");
        speed.setMnemonic('s');
        slow = new JMenuItem("Slow");
        speed.add(slow);
        slow.addActionListener(menuHandler);
         moderate = new JMenuItem("Moderate (Default)");
        moderate.addActionListener(menuHandler);
        speed.add(moderate);
        fast = new JMenuItem("Fast");
        fast.addActionListener(menuHandler);
        speed.add(fast);

        jMenuBar.add(searchTitle);
        jMenuBar.add(speed);

        setJMenuBar(jMenuBar);

        /*
        * Set up Buttons
        */
        ButtonHandler buttonHandler = new ButtonHandler(this);

        search = new JButton("Search");
        GridBagConstraints searchConstraints = new GridBagConstraints();
        searchConstraints.fill = GridBagConstraints.HORIZONTAL;
        searchConstraints.gridx = 0;
        searchConstraints.gridy = 0;
        searchConstraints.weightx =0.5;
        search.addActionListener(buttonHandler);
        add(search, searchConstraints);

        clear = new JButton("Clear");
        GridBagConstraints stopConstraints = new GridBagConstraints();
        stopConstraints.fill = GridBagConstraints.HORIZONTAL;
        stopConstraints.gridx = 1;
        stopConstraints.gridy = 0;
        stopConstraints.weightx =0.5;
        clear.addActionListener(buttonHandler);
        add(clear, stopConstraints);

        start = new JButton("Set Start");
        GridBagConstraints startConstraints = new GridBagConstraints();
        startConstraints.fill = GridBagConstraints.HORIZONTAL;
        startConstraints.gridx = 2;
        startConstraints.gridy = 0;
        startConstraints.weightx =0.5;
        start.addActionListener(buttonHandler);
        add(start, startConstraints);

        goal = new JButton("Set Goal");
        GridBagConstraints goalConstraints = new GridBagConstraints();
        goalConstraints.fill = GridBagConstraints.HORIZONTAL;
        goalConstraints.gridx = 3;
        goalConstraints.gridy = 0;
        goalConstraints.weightx = 0.5;
        goal.addActionListener(buttonHandler);
        add(goal, goalConstraints);

        /*
        * Set up graph
        */
        GridBagConstraints graphConstraints = new GridBagConstraints();
        graphConstraints.fill = GridBagConstraints.BOTH;
        graphConstraints.gridx = 0;
        graphConstraints.gridy = 1;
        graphConstraints.weightx = 0.5;
        graphConstraints.weighty = 0.5;
        graphConstraints.gridwidth = 4;
        graphConstraints.gridheight = GridBagConstraints.RELATIVE;
        add(graphPanel, graphConstraints);
        /*
        * Set up status bar below
        */
        status = new JTextField(statusBar);
        status.setEditable(false);
        GridBagConstraints statusConstraints = new GridBagConstraints();
        statusConstraints.fill = GridBagConstraints.HORIZONTAL;
        statusConstraints.gridx = 0;
        statusConstraints.gridy = 2;
        statusConstraints.weightx =0.5;
        statusConstraints.gridwidth = 4;
        statusConstraints.gridheight = 1;
        add(status, statusConstraints);
    }

    /*
    * Getters
    */
    public JButton getStart() {
        return start;
    }

    public JButton getGoal() {
        return goal;
    }

    public JButton getClear() {
        return clear;
    }

    public JButton getSearch() {
        return search;
    }

    public JMenuItem[] getAlgorithm() {
        return algorithm;
    }

    public JTextField getStatus() {
        return status;
    }

    public JMenuItem getSlow() {
        return slow;
    }

    public JMenuItem getModerate() {
        return moderate;
    }

    public JMenuItem getFast() {
        return fast;
    }

    public static void selectAppropriateGraph(int number) {
        if(searchNumber == 3 && !tsp){
            searchNumber = number;
            setGraphPanel("dgsGraph.dgs", true);
            tsp = true;
        } else if(tsp) {
            searchNumber = number;
            setGraphPanel("graph.gv", false);
            tsp = false;
        }
    }

    /*
    * Change title to reflect current algorithm
    */
    public void changeTitle(String newTitle) {
        title = newTitle;
        setTitle(newTitle);
    }

    /*
    * Change title to reflect current algorithm
    */
    public void changeStatus(String newStatus) {
        status.setText(newStatus);
    }

    public void allStatus(boolean flag){
        start.setEnabled(flag);
        goal.setEnabled(flag);
        clear.setEnabled(flag);
        search.setEnabled(flag);
        slow.setEnabled(flag);
        moderate.setEnabled(flag);
        fast.setEnabled(flag);
        for (JMenuItem anAlgorithm : algorithm) {
            anAlgorithm.setEnabled(flag);
        }
    }

    public void disableExceptClear(){
        start.setEnabled(false);
        goal.setEnabled(false);
        search.setEnabled(false);
        clear.setEnabled(true);
        slow.setEnabled(false);
        moderate.setEnabled(false);
        fast.setEnabled(false);
        for (JMenuItem anAlgorithm : algorithm) {
            anAlgorithm.setEnabled(false);
        }
    }

    public static void setGraphPanel(String graphName, boolean isDGS){
        if(firstDraw){
            graphPanel.add(AppGraph.init(graphName, isDGS));
            firstDraw = false;
            return;
        }
        AppGraph.getGraph().clear();
        graphPanel.remove(0);
        graphPanel.add(AppGraph.init(graphName, isDGS));
        graphPanel.validate();
        graphPanel.repaint();
    }
}
