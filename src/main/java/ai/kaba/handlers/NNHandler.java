package ai.kaba.handlers;

import ai.kaba.machinelearning.ann.NeuralNet;
import ai.kaba.ui.AppWindow;
import ai.kaba.ui.NNWindow;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomDataGenerator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Yusuf on 6/15/2016
 */
public class NNHandler implements ActionListener {

    private NeuralNet nnWindow;
    private AppWindow appWindow;
    private boolean isTrained = false;

    public NNHandler(NNWindow nnWindow, AppWindow appWindow){
        this.nnWindow = (NeuralNet) nnWindow;
        this.appWindow = appWindow;
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == nnWindow.getTrain()){
            appWindow.allStatus(false);
            appWindow.changeStatus("Training Perceptron...");
            nnWindow.getTextArea().setText("Running...");
            trainNetwork();
            isTrained = true;
        }
        if(actionEvent.getSource() == nnWindow.getRun()){
            RandomDataGenerator randomData = new RandomDataGenerator();
            RealMatrix input = new Array2DRowRealMatrix(new double[][]{{randomData.nextUniform(0, 1, true), randomData.nextUniform(0, 1, true)}});
            RealMatrix yHat = nnWindow.forward(input);
            appWindow.changeStatus("Ran Test");
            nnWindow.getTextArea().setText("Input: " + input + "\nOutput: " + yHat);
        }
        if(actionEvent.getSource() == nnWindow.getReset()){
            appWindow.allStatus(true);
            appWindow.changeStatus("Reset");
            nnWindow.getTextArea().setText("Results: ");
            if(isTrained) {
                nnWindow.getTrain().setEnabled(false);
            }
        }
        if(actionEvent.getSource() == nnWindow.getRestore()){
            nnWindow.reset();
            appWindow.allStatus(true);
            isTrained = false;
            nnWindow.getTextArea().setText("Results: ");
            appWindow.changeStatus("Perceptron restored to default untrained weights.");
        }
    }

    private void trainNetwork() {
        new AsyncClass().execute();
    }

    private class AsyncClass extends SwingWorker<LinkedList<Double>, Void> {

        RealMatrix xInput = new Array2DRowRealMatrix(new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}});
        RealMatrix yOutput = new Array2DRowRealMatrix(new double[][]{{0}, {1}, {1}, {0}});

        double i = 0;

        @Override
        protected LinkedList<Double> doInBackground() throws Exception {
            nnWindow.forward(xInput);
            nnWindow.costFunctionPrime(xInput);
            LinkedList<Double> results = new LinkedList<>();
            while (nnWindow.costFunction(xInput, yOutput) > 0.0001) {
                nnWindow.improve();
                nnWindow.forward(xInput);
                nnWindow.costFunctionPrime(xInput);
                results.add(0, nnWindow.costFunction(xInput, yOutput));
                results.add(1, i);
                i++;
            }
            return results;
        }

        @Override
        protected void done() {
            try {
                LinkedList<Double> results = get();
                nnWindow.getTextArea().setText("Output (Passing default input in): " + nnWindow.getyHat() + "\nCost function: " + results.get(0) + "\nNumber of iterations: " + results.get(1));
                appWindow.disableExceptClear();
                appWindow.changeStatus("Done Training Perceptron.");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
