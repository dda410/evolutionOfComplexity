import java.util.ArrayList;
import java.util.Collections;

public class Main {

    private static final int POPULATION_SIZE = 4000;
    private static final int GENERATIONS = 1000;
    private static final int TIME_IN_GROUP = 4;
    private static final int SMALL_GROUP_SIZE = 4;
    private static final int LARGE_GROUP_SIZE = 40;
    private static final Individual SELFISH_SMALL = new Individual(true, false);
    private static final Individual SEFLISH_LARGE = new Individual(true, true);
    private static final Individual COOPERATIVE_SMALL = new Individual(false, false);
    private static final Individual COOPERATIVE_LARGE = new Individual(false, true);

    private static ArrayList<Individual> generatePopulation() {
        ArrayList<Individual> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE / Individual.POSSIBLE_COMBINATIONS; i++) {
            population.add(new Individual(true, true));
            population.add(new Individual(false, true));
            population.add(new Individual(true, false));
            population.add(new Individual(false, false));
        }
        return population;
    }


    public static void main(String[] args) {
        ArrayList<Individual> population = generatePopulation();
        System.out.println(population);
        Collections.shuffle(population);
        System.out.println(population);
        System.out.println(population.size());
        int occurrences = Collections.frequency(population, SELFISH_SMALL);
        System.out.print(occurrences);
    }
}
