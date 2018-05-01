import java.util.*;

public class GeneticAlgorithm {

    private static Integer MAX_GENES = 3;
    private static Integer MAX_POP = 30;
    private static Integer MAX_GENERATION = 500;

    private static Cromossomo Crossover(Cromossomo c1, Cromossomo c2) {
        ArrayList<Double> P1 = c1.getGenes();
        ArrayList<Double> P2 = c2.getGenes();
        ArrayList<Double> cross = new ArrayList<>();

        for(int i = 0 ; i < MAX_GENES; i++) {
            cross.add( ( P1.get(i) + P2.get(i) ) / 2);
        }
        Cromossomo son = new Cromossomo(MAX_GENES);
        son.setGenes(cross);
        son.mutation();
        return son;
    }

    private static ArrayList<Cromossomo> generatePopulation(ArrayList<String> Expressions){
        Cromossomo crom;
        ArrayList<Cromossomo> Population = new ArrayList<>();
        double minFitness = Double.MAX_VALUE;

        for (int j = 0; j < MAX_POP; j++) {
            crom = new Cromossomo(MAX_GENES);
            crom.calcFitness(Expressions);
            Population.add(crom);
            if(crom.getFitness() < minFitness)
                minFitness = crom.getFitness();
        }
        Population = normalize(Population, minFitness);

        //Population.sort(new SortCromossomo());

        return Population;
    }

    private static ArrayList<Cromossomo> normalize(ArrayList<Cromossomo> Population, double minFitness){
        for(Cromossomo c: Population) {
            if(c.getFitness() < 0) {
                c.setFitness((Math.abs(minFitness) + c.getFitness()) + 1);
            }
        }
        return Population;
    }


    private static Cromossomo roulette(ArrayList<Cromossomo> Population) {
        Cromossomo chosen = null;
        Random randGen = new Random();
        double totalFitness = 0;
        double accumulatedFitness = 0.0;

        for(Cromossomo c : Population) {
            totalFitness += c.getFitness();
        }

        double drawn = totalFitness * randGen.nextDouble();

        for (Cromossomo c : Population) {
            accumulatedFitness += c.getFitness();
            if(drawn <= accumulatedFitness) {
                chosen = c;
                break;
            }
        }

        return chosen;
    }

    private static Cromossomo getGreater(ArrayList<Cromossomo> generation) {
        Double maxValue = Double.NEGATIVE_INFINITY;
        Integer index = -1;
        for (Integer i = 0; i < MAX_POP; i++) {
            Cromossomo cromo = generation.get(i);
            Double value = cromo.getFitness();
            if (value > maxValue) {
                maxValue = value;
                index = i;
            }
        }
        return generation.get(index);
    }

    public static void main(String[] args) {

        ArrayList<String> Expressions = new ArrayList<>();

        Expressions.add("(x0 * x1) + (x1 * x2)");
        Expressions.add("(x0 * x0) - (x1 * x1) + (x2 * x2)");
        Expressions.add("(x0 * x0) + (x1 * x1) + (x2 * x2)");

        ArrayList<Cromossomo> Population = generatePopulation(Expressions);

        for(int i = 0; i < MAX_GENERATION; i++) {
            ArrayList<Cromossomo> newPopulation = new ArrayList<>();
            Cromossomo elite = getGreater(Population);
            newPopulation.add(elite);
            for(int j = 0; j < MAX_POP - 1; j++) {
                Cromossomo P1 = roulette(Population);
                Cromossomo P2 = roulette(Population);
                Cromossomo son = Crossover(P1, P2);
                son.calcFitness(Expressions);
                newPopulation.add(son);
            }
            Population = newPopulation;
        }

        System.out.println("Z = " +getGreater(Population).getFitness());
    }
}
