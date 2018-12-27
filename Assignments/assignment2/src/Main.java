import java.lang.reflect.Array;
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

    private static ArrayList<ArrayList> computeGroups(ArrayList<Individual> population, int groupSize, boolean largeGroup) {
        ArrayList<ArrayList> allGroups = new ArrayList<>();
        ArrayList<Individual> group = new ArrayList<>();
        for (Individual individual : population) {
            if(individual.isLargeGroupGene() == largeGroup) {
                group.add(individual.clone());
            }
            if(group.size() == groupSize) {
                allGroups.add(group);
                group = new ArrayList<>();
            }
        }
        return allGroups;
    }

    private static void printGroups(ArrayList<ArrayList> groups) {
        int i = 1;
        for (ArrayList<Individual> group : groups) {
            System.out.println("Printing group number " + i);
            for (Individual ind : group) {
                System.out.println(ind);
            }
            i++;
        }
    }


    public static void main(String[] args) {
        // 1: Generate population of N individuals. Equiprobable distribution of genotypes.
        ArrayList<Individual> population = generatePopulation();
        System.out.println(population);
        Collections.shuffle(population);
        System.out.println(population);
        System.out.println(population.size());
        // 2: Divide into groups
        ArrayList<ArrayList> smallGroups = computeGroups(population, SMALL_GROUP_SIZE, false);
        System.out.println("Printing small groups with size: " + smallGroups.size());
        printGroups(smallGroups);
        ArrayList<ArrayList> largeGroups = computeGroups(population, LARGE_GROUP_SIZE, true);
        System.out.println("Printing large groups with size: " + largeGroups.size());
        printGroups(largeGroups);


//        int occurrences = Collections.frequency(population, SELFISH_SMALL);
//        System.out.print(occurrences);
    }
}
