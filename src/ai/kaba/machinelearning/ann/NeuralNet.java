package ai.kaba.machinelearning.ann;

import ai.kaba.ui.AppWindow;
import ai.kaba.ui.NNWindow;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.graphstream.graph.Graph;

/**
 * Created by Yusuf on 4/25/2016
 * Forward propagate
 *      initialize weights for input and hidden layers
 *      for each neuron in the hidden layer
 *			value = summation(weight * input) for every incoming input
 *			output = activationFunction(value)
 *		for each neuron in the output layer
 *			value = summation(weight * input) for every incoming input
 *			output = activationFunction(value)
 * Backward propagate
 *		do
 *		    for every input set in training data
 *			    Forward propagate(input set)
 *			    calculate cost = 1/2(actual - expected)2
 *			    calculate djDw1 for weights from input to hidden layer
 *			    calculate djDw2 for weights from hidden layer to output layer
 *			    update weights as
 *		while (cost > 0.0001)
 */
public class NeuralNet extends NNWindow {

    private RealMatrix w1;
    private RealMatrix w2;

    private RealMatrix z2;
    private RealMatrix z3;

    private RealMatrix a2;

    private RealMatrix yHat;

    private RealMatrix djDw1;
    private RealMatrix djDw2;

    private RealMatrix y = new Array2DRowRealMatrix(new double[][]{{0},{1},{1},{0}});

    public NeuralNet(AppWindow appWindow){
        super(appWindow);

        createNeuralNet();
    }

    private void createNeuralNet() {
        int hiddenLayerSize = 3;
        int inputLayerSize = 2;
        int outputLayerSize = 1;
        double[][] weight1 = new double[inputLayerSize][hiddenLayerSize];
        double[][] weight2 = new double[hiddenLayerSize][outputLayerSize];
        populateWithRandom(weight1);
        populateWithRandom(weight2);

        w1 = MatrixUtils.createRealMatrix(weight1);
        w2 = MatrixUtils.createRealMatrix(weight2);

        updateWeights();
    }

    private void updateWeights() {
        Graph ourGraph = getAppGraph().getGraph();

        double[] x1 = w1.getRow(0);
        String labelOne = x1[0] + "";
        ourGraph.getEdge("x1y1").setAttribute("ui.label", labelOne);
        ourGraph.getEdge("x1y2").setAttribute("ui.label", x1[1]);
        ourGraph.getEdge("x1y3").setAttribute("ui.label", x1[2]);

        double[] x2 = w1.getRow(1);
        ourGraph.getEdge("x2y1").setAttribute("ui.label", x2[0]);
        ourGraph.getEdge("x2y2").setAttribute("ui.label", x2[1]);
        ourGraph.getEdge("x2y3").setAttribute("ui.label", x2[2]);

        double[] y1 = w2.getRow(0);
        ourGraph.getEdge("y1z").setAttribute("ui.label", y1[0]);

        double[] y2 = w2.getRow(0);
        ourGraph.getEdge("y2z").setAttribute("ui.label", y2[0]);

        double[] y3 = w2.getRow(0);
        ourGraph.getEdge("y3z").setAttribute("ui.label", y3[0]);
    }

/*    public RealMatrix getW1() {
        return w1;
    }

    public RealMatrix getW2() {
        return w2;
    }*/

    public RealMatrix getyHat() {
        return yHat;
    }

/*    public RealMatrix getDjDw1() {
        return djDw1;
    }

    public RealMatrix getDjDw2() {
        return djDw2;
    }

    public RealMatrix getA2() {
        return a2;
    }

    public RealMatrix getZ2() {
        return z2;
    }

    public RealMatrix getZ3() {
        return z3;
    }*/

    public RealMatrix forward(RealMatrix xInput){
        z2 = xInput.multiply(w1);
        a2 = sigmoid(z2);
        z3 = a2.multiply(w2);
        yHat = sigmoid(z3);
        return yHat;
    }

    public double costFunction(RealMatrix xInput, RealMatrix yOutput){
        double cost = 0;
        yHat = forward(xInput);
        for(int i = 0; i < yHat.getRowDimension(); i++){
            for(int j = 0; j < yHat.getColumnDimension(); j++){
                cost += (0.5 * Math.pow((yOutput.getEntry(i, j) - yHat.getEntry(i, j)), 2));
            }
        }
        return cost;
    }

    public RealMatrix[] costFunctionPrime(RealMatrix xInput){
        yHat = forward(xInput);

        RealMatrix delta3 = (y.subtract(yHat)).scalarMultiply(-1);
        RealMatrix sigPrimeZ3 = sigmoidPrime(z3);
        elementMultiply(delta3, sigPrimeZ3);

        djDw2 = a2.transpose().multiply(delta3);

        RealMatrix delta2 = delta3.multiply(w2.transpose());
        RealMatrix sigZ2 = sigmoidPrime(z2);
        elementMultiply(delta2, sigZ2);

        djDw1 = xInput.transpose().multiply(delta2);
        return new RealMatrix[]{djDw1, djDw2};
    }

    private RealMatrix sigmoidPrime(RealMatrix z) {
        RealMatrix a = new Array2DRowRealMatrix(z.getRowDimension(), z.getColumnDimension());
        for(int i = 0; i < z.getRowDimension(); i++){
            for(int j = 0; j < z.getColumnDimension(); j++){
                a.setEntry(i, j, prime(z.getEntry(i, j)));
            }
        }
        return a;
    }

    private double prime(double entry) {
        return Math.exp(-entry)/(Math.pow(1 + Math.exp(-entry), 2));
    }

    private void elementMultiply(RealMatrix delta3, RealMatrix sigZ) {
        for(int i = 0; i < delta3.getRowDimension(); i++){
            for(int j = 0; j < delta3.getColumnDimension(); j++){
                delta3.setEntry(i, j, ( delta3.getEntry(i, j) * sigZ.getEntry(i, j) ));
            }
        }
    }

    private RealMatrix sigmoid(RealMatrix z) {
        RealMatrix a = new Array2DRowRealMatrix(z.getRowDimension(), z.getColumnDimension());
        for(int i = 0; i < z.getRowDimension(); i++){
            for(int j = 0; j < z.getColumnDimension(); j++){
                a.setEntry(i, j, sigmoidFunction(z.getEntry(i, j)));
            }
        }
        return a;
    }

    private double sigmoidFunction(double entry) {
        return (1 / (1 + Math.exp(-entry)));
    }

    private void populateWithRandom(double[][] weight) {
        RandomDataGenerator randomData = new RandomDataGenerator();
        for(int i = 0; i < weight.length; i++){
            for(int j = 0; j < weight[i].length; j++){
                weight[i][j] = randomData.nextLong(1 , 2);
            }
        }
    }

    public void improve(){
        RealMatrix innerDjDw1 = MatrixUtils.createRealMatrix(djDw1.getData());
        RealMatrix innerDjDw2 = MatrixUtils.createRealMatrix(djDw2.getData());
        elementMultiply(innerDjDw1, a2);
        elementMultiply(innerDjDw2, yHat);
        w1 = w1.add(innerDjDw1.scalarMultiply(-5.75));
        w2 = w2.add(innerDjDw2.scalarMultiply(-5.75));

        updateWeights();
    }

    public void reset() {
        createNeuralNet();
    }
}
