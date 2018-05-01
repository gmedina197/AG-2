import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.*;

public class Cromossomo {
    private ArrayList<Double> genes;
    private Integer numberGenes;
    private Double fitness;

    Cromossomo(int numberGenes) {
        this.numberGenes = numberGenes;
        this.genes = generatePop();
    }

    public void calcFitness(ArrayList<String> Expressions) {
        ArrayList<Double> results = parse(Expressions);
        Double Z = results.get(0);
        Double req1 = results.get(1);
        Double req2 = results.get(2);
        int violatedReq = 0;

        violatedReq += req1 <= 2 ? 0 : 1;
        violatedReq += req2 <= 10 ? 0 : 1;

        if(violatedReq == 1) Z = 2.0;
        if(violatedReq == 2) Z = 1.0;

        this.fitness = Z;
    }

    private static double getRandomDouble(int minRange, int maxRange) {
        Random randGen = new Random();
        return minRange + (maxRange - minRange) * randGen.nextDouble();
    }

    public ArrayList<Double> getGenes() {
        return genes;
    }

    public Double getFitness() {
        return fitness;
    }

    private ArrayList<Double> generatePop() {
        ArrayList<Double> cromossomo = new ArrayList<>();
        int maxRange = 8;
        int minRange = -8;

        for(int i = 0; i < this.numberGenes; i++) {
            cromossomo.add(getRandomDouble(minRange, maxRange));
        }
        return cromossomo;
    }

    public void mutation() {
        if(getRandomDouble(0, 100) <= 2) {
            int index = (int)getRandomDouble(0,2);
            this.genes.set(index,getRandomDouble(-8, 8));
        }
    }

    private ArrayList <Double> parse(ArrayList<String> Expressions) {
        ArrayList<Double> Results = new ArrayList<>();
        Map<String, Double> vars = new HashMap<>();
        for (Integer i = 0; i < this.genes.size(); i++) {
            vars.put("x".concat((i).toString()), this.genes.get(i));
        }

        for(String expression : Expressions) {
            Expression e = new ExpressionBuilder(expression)
                    .variables("x0", "x1", "x2")
                    .build()
                    .setVariables(vars);

            Results.add(e.evaluate());
        }

        return Results;
    }

    public void setFitness(Double newFitness){
        this.fitness = newFitness;
    }

    public void setGenes(ArrayList<Double> newGenes) {
        this.genes.clear();
        this.genes = newGenes;
    }
}
