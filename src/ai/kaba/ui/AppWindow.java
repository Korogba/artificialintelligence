package ai.kaba.ui;

import ai.kaba.abstracts.AbstractGraphWindow;
import ai.kaba.abstracts.GeneticAlgorithm;
import ai.kaba.abstracts.interfaces.Runner;
import ai.kaba.handlers.MenuHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Created by Yusuf on 2/28/2016.
 * AppWindow contains the gui components of the application
 */
public class AppWindow extends JFrame {
    /*
    * Initialize gui components
    */
    private JMenuItem slow;
    private JMenuItem moderate;
    private JMenuItem fast;
    private JTextField status;
    private JTabbedPane tabbedPane;
    public static String title = "Artificial Intelligence";
    public static String algorithmString;
    public static String statusBar = "status";
    public static int speed = 500;
    private SearchWindow searchWindow;
    private OptimizationWindow optimizationWindow;
    private Runner selectedComponent;
    private GAWindow gaWindow;

    //TODO Tabbed views: Add logic for menuitem clicking switching to appropriate tab
    //TODO Properly set up tabbed: shortcut mnemonic n all
    /*
    * Constructor for displaying application window
    */
    public AppWindow(){
        /*
        * Call superclass constructor
        */
        super(title);
        /*
        * Set the layout
        */
        setLayout(new GridBagLayout());
        /*
        * Set up menus
        */
        JMenuBar jMenuBar = new JMenuBar();
        JMenu file = new JMenu("File");

        MenuHandler menuHandler = new MenuHandler(this);

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

        jMenuBar.add(file);
        jMenuBar.add(speed);

        setJMenuBar(jMenuBar);

        /*
        * Set up Tabs
        */
        GridBagConstraints tabConstraints = new GridBagConstraints();
        tabConstraints.fill = GridBagConstraints.BOTH;
        tabConstraints.gridx = 0;
        tabConstraints.gridy = 0;
        tabConstraints.weightx = 0.5;
        tabConstraints.weighty = 0.5;
        tabConstraints.gridwidth = GridBagConstraints.REMAINDER;

        tabbedPane = new JTabbedPane();
        searchWindow = new SearchWindow(this);
        tabbedPane.addTab("Graph Traversal", searchWindow);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        optimizationWindow = new OptimizationWindow(this);
        tabbedPane.addTab("Minimization Graph", optimizationWindow);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        gaWindow = new GAWindow(this);
        tabbedPane.addTab("Machine Learning", gaWindow);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        tabbedPane.addChangeListener(changeEvent -> {
            if(tabbedPane.getSelectedComponent().getClass().equals(SearchWindow.class)){
                setAppropriateTitleAndStatus(searchWindow.getAlgorithms(), "Graph Search");
                selectedComponent = (AbstractGraphWindow) tabbedPane.getSelectedComponent();
            }
            if(tabbedPane.getSelectedComponent().getClass().equals(OptimizationWindow.class)){
                setAppropriateTitleAndStatus(optimizationWindow.getAlgorithms(), "Minimization Problem");
                selectedComponent = (AbstractGraphWindow) tabbedPane.getSelectedComponent();
            }
            if(tabbedPane.getSelectedComponent().getClass().equals(GAWindow.class)){
                setAppropriateTitleAndStatus(gaWindow.getChartPane(), "Machine Learning");
                selectedComponent = gaWindow.getCurrent();
            }
        });

        selectedComponent = (AbstractGraphWindow) tabbedPane.getSelectedComponent();

        add(tabbedPane, tabConstraints);

        /*
        * Set up status bar below
        */
        status = new JTextField(statusBar);
        status.setEditable(false);
        GridBagConstraints statusConstraints = new GridBagConstraints();
        statusConstraints.fill = GridBagConstraints.HORIZONTAL;
        statusConstraints.gridx = 0;
        statusConstraints.gridy = 1;
        statusConstraints.weightx =0.5;
        statusConstraints.gridwidth = GridBagConstraints.REMAINDER;
        statusConstraints.gridheight = 1;
        add(status, statusConstraints);
    }

    public void setAppropriateTitleAndStatus(List<JRadioButton> algorithms, String defaultTitle) {
        boolean flag = true;
        for(JRadioButton radioButton : algorithms){
            if(radioButton.isSelected()){
                setAlgorithmString(radioButton.getText());
                changeTitle("Artificial Intelligence: " + radioButton.getText());
                changeStatus(radioButton.getText() + " selected");
                flag = false;
            }
        }
        if(flag){
            changeTitle("Artificial Intelligence: " + defaultTitle);
            changeStatus(defaultTitle + " selected");
        }
    }

    public void setAppropriateTitleAndStatus(JTabbedPane tabbedPane, String defaultTitle) {
        GeneticAlgorithm selectedPane = (GeneticAlgorithm) tabbedPane.getSelectedComponent();
        String functionName = selectedPane.returnName();
        changeTitle(defaultTitle + ": " + functionName);
        changeStatus(functionName + " selected");
    }

    /*
    * Getters
    */
    public static void setAlgorithmString(String algorithmName) {
        algorithmString = algorithmName;
    }

    public static String getAlgorithmString() {
        return algorithmString;
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

    public GAWindow getGaWindow() {
        return gaWindow;
    }

    /*
        * Change title to reflect current algorithm
        */
    public void changeTitle(String newTitle) {
        title = newTitle;
        setTitle(newTitle);
    }

    /*
    * Change status bar to reflect current/last operation
    */
    public void changeStatus(String newStatus) {
        status.setText(newStatus);
    }

    public void allStatus(boolean flag){
        selectedComponent.allStatus(flag);
        slow.setEnabled(flag);
        moderate.setEnabled(flag);
        fast.setEnabled(flag);
        tabbedPane.setEnabled(flag);
        gaWindow.getChartPane().setEnabled(flag);
    }

    public void disableExceptClear(){
        selectedComponent.disableExceptClear();
        slow.setEnabled(false);
        moderate.setEnabled(false);
        fast.setEnabled(false);
        tabbedPane.setEnabled(true);
        gaWindow.getChartPane().setEnabled(true);
    }
}
