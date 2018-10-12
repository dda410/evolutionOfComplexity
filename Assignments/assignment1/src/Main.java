import java.util.concurrent.ThreadLocalRandom;

public class Main {

    private static final int POPULATION_SIZE = 10;
    private static final String GOAL_STRING = "me thinks it is like a weasel";
    private static final int ASCII_LOWER_LIMIT = 32;
    private static final int ASCII_UPPER_LIMIT = 127;
    private static final int MUTATION_PROBABILITY = 3;

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

    private static void basicHillClimber(Individual x, Individual goalIndividual) {
        int iterations = 0;
        while(x.getFitnessValue() < goalIndividual.getFitnessValue()) {
            Individual guineaPig = x.clone();
            guineaPig.mutateIndividual(MUTATION_PROBABILITY, ASCII_LOWER_LIMIT, ASCII_UPPER_LIMIT, GOAL_STRING);
            if(guineaPig.getFitnessValue() > x.getFitnessValue()) {
                x = guineaPig;
            }
            iterations++;
        }
        System.out.println("Number of iterations with basic hill climber: " + iterations);
    }

    // mutation hill climber with population
    private static void mutationHillClimber(Individual[] population, Individual goalIndividual) {
        int size = population.length;
        int iterations = 0;
        while(goalIndividual.getFitnessValue() > populationMaxFitness(population)) {
//            System.out.println("max val individual is: " + populationMaxFitness(population));
            for (int i = 0; i < size; i++) {
                Individual guineaPig = population[i].clone();
                guineaPig.mutateIndividual(MUTATION_PROBABILITY, ASCII_LOWER_LIMIT, ASCII_UPPER_LIMIT, GOAL_STRING);
                if(guineaPig.getFitnessValue() > population[i].getFitnessValue()) {
                    population[i] = guineaPig;
                }
            }
            iterations++;
        }
        System.out.println("Number of iterations with hill climber: " + iterations);
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

    // GA algorithm with tournament selection and without crossover
    private static void tournamentSelection(Individual[] population, Individual goalIndividual) {
        int iterations = 0;
        while(goalIndividual.getFitnessValue() > populationMaxFitness(population)) {
            // Chose fittest parent between two random individuals
            Individual parentA = popRandomIndividual(population);
            Individual parentB = popRandomIndividual(population);
            Individual parentSelected = parentA.getFitnessValue() > parentB.getFitnessValue() ? parentA.clone() : parentB.clone();
            parentSelected.mutateIndividual(MUTATION_PROBABILITY, ASCII_LOWER_LIMIT, ASCII_UPPER_LIMIT, GOAL_STRING);
            // Select individual to be replaced as less fit among two random individuals
            Individual replaceA = popRandomIndividual(population);
            Individual replaceB = popRandomIndividual(population);
            resetTakenIndividual(parentA, parentB, replaceA, replaceB);
            // Mutate individual and insert it into the population
            Individual replaceSelected = replaceA.getFitnessValue() < replaceB.getFitnessValue() ? replaceA : replaceB;
            replaceSelected.setStringAndFitness(parentSelected.getString(), parentSelected.getFitnessValue());
            iterations++;
        }
        System.out.println("Number of iterations with tournament selection: " + iterations);
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

    private static void crossoverAlgorithm(Individual[] population, Individual goalIndividual) {
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
            child.mutateIndividual(MUTATION_PROBABILITY, ASCII_LOWER_LIMIT, ASCII_UPPER_LIMIT, GOAL_STRING);
            // Select individual to be replaced as less fit among two random individuals
            Individual replaceA = popRandomIndividual(population);
            Individual replaceB = popRandomIndividual(population);
            resetTakenIndividual(parentA, parentB, parentC, parentD, replaceA, replaceB);
            Individual replaceSelected = replaceA.getFitnessValue() < replaceB.getFitnessValue() ? replaceA : replaceB;
            replaceSelected.setStringAndFitness(child.getString(), child.getFitnessValue());
            iterations++;
        }
        System.out.println("Number of iterations with crossover algorithm: " + iterations);
    }

    public static void main(String[] args) {
        // Generating goal individual
        Individual goalIndividual = new Individual(GOAL_STRING, GOAL_STRING);
        System.out.println("GOAL INDIVIDUAL:");
        System.out.println(goalIndividual.toString());
        // Basic Hill climber with just one individual
        System.out.println("\nBASIC HILL CLIMBER ALGORITHM STAGE\n==================================");
        Individual x = new Individual(generateRandomString(GOAL_STRING.length()), GOAL_STRING);
        basicHillClimber(x, goalIndividual);
        // Hill climber algorithm
        System.out.println("\nHILL CLIMBER ALGORITHM STAGE\n==================================");
        Individual[] population = generatePopulation(POPULATION_SIZE);
        mutationHillClimber(population, goalIndividual);
        printPopulation(population);
        // Tournament selection algorithm
        System.out.println("\nTOURNAMENT SELECTION ALGORITHM STAGE\n==================================");
        population = generatePopulation(500);
        tournamentSelection(population, goalIndividual);
        printPopulation(population);
        // Crossover algorithm
        System.out.println("\nCROSSOVER GENETIC ALGORITHM STAGE\n==================================");
        population = generatePopulation(500);
        crossoverAlgorithm(population, goalIndividual);
        printPopulation(population);
    }
}
