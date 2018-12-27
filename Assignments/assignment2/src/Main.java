import java.util.ArrayList;
import java.util.Collections;

public class Main {

    private static final int POPULATION_SIZE = 4000;
    private static final int GENERATIONS = 1000;
    private static final int TIME_IN_GROUP = 4;
    private static final int SMALL_GROUP_SIZE = 4;
    private static final int LARGE_GROUP_SIZE = 40;
    private static final double BONUS_RESOURCE = 0.05;
    private static final double SMALL_GROUP_RESOURCE = 1 * SMALL_GROUP_SIZE;
    private static final double LARGE_GROUP_RESOURCE = (1 + LARGE_GROUP_SIZE / SMALL_GROUP_SIZE * BONUS_RESOURCE) * LARGE_GROUP_SIZE;
    private static final double DEATH_RATE = 0.1;
    private static final Individual SELFISH_SMALL = new Individual(true, false);
    private static final Individual SELFISH_LARGE = new Individual(true, true);
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

    private static double computeNextGeneration(int thisGeneSize, double thisGeneGrowth, double thisGeneConsumption,
                                             int thatGeneSize, double thatGeneGrowth, double thatGeneConsumption, double resources) {
        double resourceShare = ( (thisGeneSize * thisGeneGrowth * thisGeneConsumption) /
                (thatGeneSize * thatGeneGrowth * thatGeneConsumption) ) * resources;
        return thisGeneSize * (resourceShare / thisGeneConsumption) - DEATH_RATE * thisGeneSize;
    }

    private static void reproductionStep(ArrayList<ArrayList> groups, boolean largeGroup) {
        double resources = largeGroup ? LARGE_GROUP_RESOURCE : SMALL_GROUP_RESOURCE;
        Individual cheater = largeGroup ? SELFISH_LARGE : SELFISH_SMALL;
        Individual cooperator = largeGroup ? COOPERATIVE_LARGE : COOPERATIVE_SMALL;
        int groupSize = largeGroup ? LARGE_GROUP_SIZE : SMALL_GROUP_SIZE;
        for (int i = 0; i < TIME_IN_GROUP; i++) {
            for (ArrayList<Individual> group : groups) {
                double groupResources = (1 + group.size() / SMALL_GROUP_SIZE * BONUS_RESOURCE) * group.size();
                int numberOfCheaters = Collections.frequency(group, cheater);
                int numberOfCooperators = Collections.frequency(group, cooperator);
                double nextNumberOfCheaters = computeNextGeneration(numberOfCheaters, Individual.GROWTH_SELFISH, Individual.CONSUMPTION_SELFISH,
                        numberOfCooperators, Individual.GROWTH_COOPERATIVE, Individual.CONSUMPTION_COOPERATIVE, groupResources);
                double nextNumberOfCooperators = computeNextGeneration(numberOfCooperators, Individual.GROWTH_COOPERATIVE, Individual.CONSUMPTION_COOPERATIVE,
                        numberOfCheaters, Individual.GROWTH_SELFISH, Individual.CONSUMPTION_SELFISH, groupResources);
                System.out.println("Cheaters now: " + numberOfCheaters + " Future: " + nextNumberOfCheaters);
                System.out.println("Cooperators: " + numberOfCooperators + " Future: " + nextNumberOfCooperators);
            }
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
        System.out.println(LARGE_GROUP_RESOURCE);
        System.out.println(SMALL_GROUP_RESOURCE);
        reproductionStep(largeGroups, true);
        reproductionStep(smallGroups, false);

//        int occurrences = Collections.frequency(population, SELFISH_SMALL);
//        System.out.print(occurrences);
    }
}
