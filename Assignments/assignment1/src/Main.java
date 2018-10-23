import java.util.concurrent.ThreadLocalRandom;

public class Main {

    private static final String GOAL_STRING = "me thinks it is like a weasel";
    private static final int ASCII_LOWER_LIMIT = 32;
    private static final int ASCII_UPPER_LIMIT = 127;
    private static final int[] MUTATION_PROBABILITIES = {3, 6, 12};
    private static final int[] POPULATION_SIZES = {10, 20, 50, 100, 200, 500, 1000};
    private static final int TESTING_TIMES = 10;

    private static String generateRandomString(int size) {
        String result = "";
        for (int i = 0; i < size; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(ASCII_LOWER_LIMIT, ASCII_UPPER_LIMIT + 1);
            result += (char) randomNum;
        }
        return result;
    }

    private static Individual[] generatePopulation(int size) {
        int individualSize = GOAL_STRING.length();
        Individual[] population = new Individual[size];
        for (int i = 0; i < size; i++) {
            population[i] = new Individual(generateRandomString(individualSize), GOAL_STRING);
        }
        return population;
    }

    // Debugging utility to print all the population individuals
    private static void printPopulation(Individual[] pop) {
        for (Individual individual: pop) {
            System.out.println(individual.toString());
        }
    }

    private static int populationMaxFitness(Individual[] pop) {
        int maxVal = 0;
        for (int i = 0; i < pop.length; i++) {
            maxVal = pop[i].getFitnessValue() > maxVal ? pop[i].getFitnessValue() : maxVal;
        }
        return maxVal;
    }

    private static int basicHillClimber(Individual x, Individual goalIndividual, int mutationProbability) {
        int iterations = 0;
        while(x.getFitnessValue() < goalIndividual.getFitnessValue()) {
            Individual guineaPig = x.clone();
            guineaPig.mutateIndividual(mutationProbability, ASCII_LOWER_LIMIT, ASCII_UPPER_LIMIT, GOAL_STRING);
            if(guineaPig.getFitnessValue() > x.getFitnessValue()) {
                x = guineaPig;
            }
            iterations++;
        }
        return iterations;
    }

    // mutation hill climber with population
    private static int mutationHillClimber(Individual[] population, Individual goalIndividual, int mutationProbability) {
        int size = population.length;
        int iterations = 0;
        while(goalIndividual.getFitnessValue() > populationMaxFitness(population)) {
            for (int i = 0; i < size; i++) {
                Individual guineaPig = population[i].clone();
                guineaPig.mutateIndividual(mutationProbability, ASCII_LOWER_LIMIT, ASCII_UPPER_LIMIT, GOAL_STRING);
                if(guineaPig.getFitnessValue() > population[i].getFitnessValue()) {
                    population[i] = guineaPig;
                }
                iterations++;
            }
        }
        return iterations;
    }

    private static Individual popRandomIndividual(Individual[] population) {
        while(true) {
            int rand = ThreadLocalRandom.current().nextInt(0, population.length);
            if (!population[rand].isTaken()) {
                population[rand].setTaken(true);
                return population[rand];
            }
        }
    }

    private static void resetTakenIndividual(Individual... pop) {
        for (Individual ind : pop) {
            ind.setTaken(false);
        }
    }

    /* SYNOPSIS (GA algorithm with tournament selection and without crossover):
     * 2 random individuals -> fitter one will be the parent
     * Mutation stage -> mutate the generated child
     * 2 random individuals -> substitute less fit with the generated child. */
    private static int tournamentSelection(Individual[] population, Individual goalIndividual, int mutationProbability) {
        int iterations = 0;
        while(goalIndividual.getFitnessValue() > populationMaxFitness(population)) {
            // Chose fittest parent between two random individuals
            Individual parentA = popRandomIndividual(population);
            Individual parentB = popRandomIndividual(population);
            Individual parentSelected = parentA.getFitnessValue() > parentB.getFitnessValue() ? parentA.clone() : parentB.clone();
            parentSelected.mutateIndividual(mutationProbability, ASCII_LOWER_LIMIT, ASCII_UPPER_LIMIT, GOAL_STRING);
            // Select individual to be replaced as less fit among two random individuals
            Individual replaceA = popRandomIndividual(population);
            Individual replaceB = popRandomIndividual(population);
            resetTakenIndividual(parentA, parentB, replaceA, replaceB);
            // Mutate individual and insert it into the population
            Individual replaceSelected = replaceA.getFitnessValue() < replaceB.getFitnessValue() ? replaceA : replaceB;
            replaceSelected.setStringAndFitness(parentSelected.getString(), parentSelected.getFitnessValue());
            iterations++;
        }
        return iterations;
    }

    private static Individual crossover(String mother, String father) {
        int size = mother.length() < father.length() ? mother.length() : father.length();
        String childString = "";
        for (int i = 0; i < size; i++) {
            int rand = ThreadLocalRandom.current().nextInt(0, 100);
            childString += rand < 50 ? mother.charAt(i) : father.charAt(i);
        }
        return new Individual(childString, GOAL_STRING);
    }

    /* SYNOPSIS (Genetic algorithm with crossover):
     * 2 Random individuals -> fitter one will be the mother
     * 2 Random individuals -> fitter one will be the father
     * Reproduction stage -> generate child individual from the genes of the parents (uniform crossover)
     * Mutation stage -> mutate the generated child
     * 2 Random individuals -> substitute less fit wiht the generated child. */
    private static int crossoverAlgorithm(Individual[] population, Individual goalIndividual, int mutationProbability) {
        int iterations = 0;
        while(goalIndividual.getFitnessValue() > populationMaxFitness(population)) {
            // Select mother as fittest between two random individuals
            Individual parentA = popRandomIndividual(population);
            Individual parentB = popRandomIndividual(population);
            Individual mother = parentA.getFitnessValue() > parentB.getFitnessValue() ? parentA : parentB;
            // Select father as fittest between two random individuals
            Individual parentC = popRandomIndividual(population);
            Individual parentD = popRandomIndividual(population);
            Individual father = parentC.getFitnessValue() > parentD.getFitnessValue() ? parentC : parentD;
            // Generate child as crossover of two parents and mutate it
            Individual child = crossover(mother.getString(), father.getString());
            child.mutateIndividual(mutationProbability, ASCII_LOWER_LIMIT, ASCII_UPPER_LIMIT, GOAL_STRING);
            // Select individual to be replaced as less fit among two random individuals
            Individual replaceA = popRandomIndividual(population);
            Individual replaceB = popRandomIndividual(population);
            resetTakenIndividual(parentA, parentB, parentC, parentD, replaceA, replaceB);
            Individual replaceSelected = replaceA.getFitnessValue() < replaceB.getFitnessValue() ? replaceA : replaceB;
            replaceSelected.setStringAndFitness(child.getString(), child.getFitnessValue());
            iterations++;
        }
        return iterations;
    }

    private static void printAlgorithmStats(String name, int iterations, int popSize, int mutationProb) {
        System.out.format("%s,%d,%d,%d\n", name, mutationProb, popSize, iterations);
    }

    public static void main(String[] args) {
        // Generating goal individual
        Individual goalIndividual = new Individual(GOAL_STRING, GOAL_STRING);
        System.out.println("ALGORITHM NAME, MUTATION PROBABILITY, POPULATION SIZE, ITERATIONS");
        int iterations = 0;
        for(int mutationProbability : MUTATION_PROBABILITIES) {
            // Basic Hill climber with just one individual
            iterations = 0;
            for (int i = 0; i < TESTING_TIMES; i++) {
                Individual x = new Individual(generateRandomString(GOAL_STRING.length()), GOAL_STRING);
                iterations += basicHillClimber(x, goalIndividual, mutationProbability);
            }
            printAlgorithmStats("Basic Hill Climber", (int) Math.round((double) iterations / TESTING_TIMES), 1, mutationProbability);
            for(int popSize : POPULATION_SIZES) {
                // Hill climber algorithm
                iterations = 0;
                Individual[] population;
                for (int i = 0; i < TESTING_TIMES; i++) {
                    population = generatePopulation(popSize);
                    iterations += mutationHillClimber(population, goalIndividual, mutationProbability);
                }
                printAlgorithmStats("Hill Climber", (int) Math.round((double) iterations / TESTING_TIMES), popSize, mutationProbability);
                // Tournament selection algorithm
                iterations = 0;
                for (int i = 0; i < TESTING_TIMES; i++) {
                    population = generatePopulation(popSize);
                    iterations += tournamentSelection(population, goalIndividual, mutationProbability);
                }
                printAlgorithmStats("Tournament Selection", (int) Math.round((double) iterations / TESTING_TIMES), popSize, mutationProbability);
                // Crossover algorithm
                iterations = 0;
                for (int i = 0; i < TESTING_TIMES; i++) {
                    population = generatePopulation(popSize);
                    iterations += crossoverAlgorithm(population, goalIndividual, mutationProbability);
                }
                printAlgorithmStats("Crossover Genetic Alg", (int) Math.round((double) iterations / TESTING_TIMES), popSize, mutationProbability);
            }
        }
    }
}
