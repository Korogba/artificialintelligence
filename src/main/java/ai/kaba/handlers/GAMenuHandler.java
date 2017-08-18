package ai.kaba.handlers;

import ai.kaba.abstracts.GeneticAlgorithm;
import ai.kaba.ui.AppWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import ai.kaba.ui.GAWindow;

/**
 * Created by Yusuf on 6/17/2016
 */
public class GAMenuHandler implements ActionListener{

    private AppWindow appWindow;

    public GAMenuHandler(AppWindow appWindow) {
        this.appWindow = appWindow;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Pattern parser = Pattern.compile(Pattern.quote(":"));
        JComboBox<?> comboBox = (JComboBox<?>)actionEvent.getSource();
        String[] selected = parser.split((String)comboBox.getSelectedItem());

        GAWindow defaultWindow = appWindow.getGaWindow();
        GeneticAlgorithm current = defaultWindow.getCurrent();

        if(selected[0].equalsIgnoreCase("CrossOver Rate")){
            current.setCrossoverRate(Double.parseDouble(selected[1]));
            System.out.println("CrossOver rate changed to: " + selected[1]);
        }

        if(selected[0].equalsIgnoreCase("Mutation Rate")){
            current.setMutationRate(Double.parseDouble(selected[1]));
            System.out.println("Mutation rate changed to: " + selected[1]);
        }

        if(selected[0].equalsIgnoreCase("Acceptance Rate")){
            current.setRandomAcceptance(Double.parseDouble(selected[1]));
            System.out.println("Random acceptance rate changed to: " + selected[1]);
        }

    }
}
