package ai.kaba.machinelearning.ga.helper;

import ai.kaba.abstracts.interfaces.Fitness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Yusuf on 4/5/2016
 */
public class Individual {
    private String binaryCode;
    private double fitness;
    private Fitness fitnessCalculator;

    public Individual(Fitness type){
        fitnessCalculator = type;
    }

/*    public Individual(Fitness type, String binaryCode) {
        this.binaryCode = binaryCode;
        fitnessCalculator = type;
        fitness = type.fitness(this);
    }*/

    public Individual(Fitness type, List<Double> xValues) {
        fitnessCalculator = type;
        binaryCode = "";
        for(double xValue : xValues){
            String bitRep = Long.toBinaryString(Double.doubleToRawLongBits(xValue));
            binaryCode += (bitRep + "|");
        }
        fitness = fitnessCalculator.fitness(this);
    }

/*    public Individual(Fitness type, double...xValues){
        fitnessCalculator = type;
        binaryCode = "";
        for(double xValue : xValues){
            String bitRep = Long.toBinaryString(Double.doubleToRawLongBits(xValue));
            binaryCode += (bitRep + "|");
        }
        fitness = fitnessCalculator.fitness(this);
    }*/

    public double getFitness() {
        return fitness;
    }

    /*    public String getBinaryCode() {
        return binaryCode;
    }*/

    private List<String> xValues(){
        String[] chromosomes = breakUp();
        return Arrays.asList(chromosomes);
    }

    public List<Double> xDecimal() throws NumberFormatException {
        if(binaryCode.equals("")){
            throw new NumberFormatException("This object does not have a binary code defined");
        }
        List<Double> doubleChromosomes = new ArrayList<>();
        List<String> stringChromosomes = xValues();
        for (String chromosome : stringChromosomes){
            long signBit = 0;
            if(chromosome.length() >= 64){
                signBit = Long.parseLong(chromosome.substring(0, 1));
                chromosome = chromosome.substring(1);
            }
            int sign = (signBit == 0) ?  1 : -1;
            long intermediate = Long.parseLong(chromosome, 2);
            double doubleChromosome = (Double.longBitsToDouble(intermediate) * sign);
            doubleChromosomes.add(doubleChromosome);
        }
        return doubleChromosomes;
    }

    public double[] xArray() throws NumberFormatException {
        if(binaryCode.equals("")){
            throw new NumberFormatException("This object does not have a binary code defined");
        }
        List<String> stringChromosomes = xValues();
        double[] doubleChromosomes = new double[stringChromosomes.size()];
        for(int i = 0; i < stringChromosomes.size(); i++){
            long signBit = 0;
            String chromosome = stringChromosomes.get(i);
            if(chromosome.length() >= 64){
                signBit = Long.parseLong(chromosome.substring(0, 1));
                chromosome = chromosome.substring(1);
            }
            int sign = (signBit == 0) ?  1 : -1;
            long intermediate = Long.parseLong(chromosome, 2);
            double doubleChromosome = (Double.longBitsToDouble(intermediate) * sign);
            doubleChromosomes[i] = doubleChromosome;
        }

        return doubleChromosomes;
    }

    @Override
    public String toString() {
        String formattedForOutput = "";
        String[] chromosomes = breakUp();
        for(String chromosome : chromosomes){
            formattedForOutput += (chromosome + "\t");
        }
        return "Individual{ " +
                "binaryCode = '" + formattedForOutput + '\'' +
                '}';
    }

    public String[] breakUp(){
        Pattern parser = Pattern.compile(Pattern.quote("|"));
        return parser.split(binaryCode);
    }

    public void makeUp(String[] chromosomes) {
        binaryCode = "";
        for(String chromosome : chromosomes){
            binaryCode += (chromosome + "|");
        }
        fitness = fitnessCalculator.fitness(this);
    }
}
