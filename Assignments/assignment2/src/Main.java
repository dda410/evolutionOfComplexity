import java.lang.reflect.Array;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

    private static final int POPULATION_SIZE = 4000;
    private static final int GENERATIONS = 1000;
    private static final int TIME_IN_GROUP = 4;
    private static final int SMALL_GROUP_SIZE = 4;
    private static final int LARGE_GROUP_SIZE = 40;
    private static final double SMALL_GROUP_RESOURCE = 4;
    private static final double LARGE_GROUP_RESOURCE = 50;
    private static final double DEATH_RATE = 0.1;
    private static final Individual SELFISH_SMALL = new Individual(true, false);
    private static final Individual SELFISH_LARGE = new Individual(true, true);
    private static final Individual COOPERATIVE_SMALL = new Individual(false, false);
    private static final Individual COOPERATIVE_LARGE = new Individual(false, true);
    private static final Individual [] ALL_INDIVIDUALS = new Individual[] {SELFISH_LARGE, COOPERATIVE_LARGE, SELFISH_SMALL, COOPERATIVE_SMALL};

    private static ArrayList<Individual> generatePopulation() {
        ArrayList<Individual> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE / ALL_INDIVIDUALS.length; i++) {
            for (Individual individual : ALL_INDIVIDUALS) {
                population.add(individual.clone());
            }
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

    private static double computeNextGeneration(double thisGeneSize, double thisGeneGrowth, double thisGeneConsumption,
                                             double thatGeneSize, double thatGeneGrowth, double thatGeneConsumption, double resources) {
        double resourceShare = ( (thisGeneSize * thisGeneGrowth * thisGeneConsumption) /
                (thatGeneSize * thatGeneGrowth * thatGeneConsumption + thisGeneSize * thisGeneGrowth * thisGeneConsumption) ) * resources;
        return thisGeneSize + (resourceShare / thisGeneConsumption) - DEATH_RATE * thisGeneSize;
    }

    private static ArrayList<GroupFrequencies> translateGroupsToFrequencies(ArrayList<ArrayList> groups, Individual cheater, Individual cooperator) {
        ArrayList<GroupFrequencies> result = new ArrayList<>();
        for (ArrayList<Individual> group : groups) {
            result.add(new GroupFrequencies(new IndividualFrequencies(Collections.frequency(group, cheater), cheater), new IndividualFrequencies(Collections.frequency(group, cooperator), cooperator)));
        }
        return result;
    }

    private static ArrayList<Individual> generatePopulationGivenFrequencies(IndividualFrequencies ... individualFrequencies) {
        ArrayList<Individual> result = new ArrayList<>();
        for (IndividualFrequencies ind : individualFrequencies) {
            for (int i = 0; i < ind.getFrequency(); i++) {
                result.add(ind.getIndividualType().clone());
            }
        }
        return result;
    }

    private static GroupFrequencies reproductionStep(ArrayList<ArrayList> groups, boolean largeGroup) {
        Individual cheater = largeGroup ? SELFISH_LARGE : SELFISH_SMALL;
        Individual cooperator = largeGroup ? COOPERATIVE_LARGE : COOPERATIVE_SMALL;
        ArrayList<GroupFrequencies> groupsWithFrequencies = translateGroupsToFrequencies(groups, cheater, cooperator);
        double groupResources = largeGroup ? LARGE_GROUP_RESOURCE : SMALL_GROUP_RESOURCE;
        double totalNumberOfCheaters = 0;
        double totalNumberOfCooperators = 0;
        for (int i = 0; i < TIME_IN_GROUP; i++) {
            totalNumberOfCheaters = 0;
            totalNumberOfCooperators = 0;
            int index = 0;
            for (GroupFrequencies group : groupsWithFrequencies) {
                double numberOfCheaters = group.getSizeOfGenotype(cheater);
                double numberOfCooperators = group.getSizeOfGenotype(cooperator);
                double nextNumberOfCheaters = computeNextGeneration(numberOfCheaters, Individual.GROWTH_SELFISH, Individual.CONSUMPTION_SELFISH,
                        numberOfCooperators, Individual.GROWTH_COOPERATIVE, Individual.CONSUMPTION_COOPERATIVE, groupResources);
                double nextNumberOfCooperators = computeNextGeneration(numberOfCooperators, Individual.GROWTH_COOPERATIVE, Individual.CONSUMPTION_COOPERATIVE,
                        numberOfCheaters, Individual.GROWTH_SELFISH, Individual.CONSUMPTION_SELFISH, groupResources);
                groupsWithFrequencies.set(index, new GroupFrequencies(new IndividualFrequencies(nextNumberOfCheaters, cheater), new IndividualFrequencies(nextNumberOfCooperators, cooperator)));
                index++;
                totalNumberOfCheaters += nextNumberOfCheaters;
                totalNumberOfCooperators += nextNumberOfCooperators;
            }
        }
        return new GroupFrequencies(new IndividualFrequencies(totalNumberOfCheaters, cheater), new IndividualFrequencies(totalNumberOfCooperators, cooperator));
    }

    private static void printGenerations(ArrayList<GroupFrequencies> generations) {
        for (GroupFrequencies group : generations) {
            for (Individual ind : ALL_INDIVIDUALS) {
                System.out.printf("%.0f ", group.getIndividualFrequency(ind).getFrequency());
            }
        }
    }

    public static void main(String[] args) {
        // 1: Generate population of N individuals. Equiprobable distribution of genotypes.
        ArrayList<Individual> population = generatePopulation();
        ArrayList<GroupFrequencies> allGenerationsFrequencies = new ArrayList<>();
        for (int i = 0; i < GENERATIONS / TIME_IN_GROUP; i++) {
            Collections.shuffle(population);
            // 2: Divide into groups
            ArrayList<ArrayList> smallGroups = computeGroups(population, SMALL_GROUP_SIZE, false);
            ArrayList<ArrayList> largeGroups = computeGroups(population, LARGE_GROUP_SIZE, true);
            // 3: Reproduction step
            GroupFrequencies largeGroupsFrequencies = reproductionStep(largeGroups, true);
            GroupFrequencies smallGroupFrequencies = reproductionStep(smallGroups, false);
            // 4: Return progeny to the migrant pool
            GroupFrequencies currentGeneration = smallGroupFrequencies.mergeWithGroup(largeGroupsFrequencies);
            currentGeneration.rescaleGroup(POPULATION_SIZE);
            allGenerationsFrequencies.add(currentGeneration);
            // 6: Repeat from step 2 for N generations.
            population = generatePopulationGivenFrequencies(currentGeneration.getFrequencies());
        }
        printGenerations(allGenerationsFrequencies);
    }
}
