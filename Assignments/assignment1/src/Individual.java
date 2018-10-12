import java.util.concurrent.ThreadLocalRandom;

public class Individual {

    private String s;
    private int fitnessValue;
    private boolean taken;

    public Individual() {

    }

    public Individual(String s, String goalString) {
        this.s = s;
        calculateFitness(goalString);
        taken = false;
    }

    public Individual(String s, int fitnessValue) {
        this.s = s;
        this.fitnessValue = fitnessValue;
        taken = false;
    }

    private void calculateFitness(String goal) {
        int size = s.length() < goal.length() ? s.length() : goal.length();
        int matchingChars = 0;
        for (int i = 0; i < size; i++) {
            if (s.charAt(i) == goal.charAt(i)) {
                matchingChars++;
            }
        }
        this.fitnessValue = matchingChars;
    }

    public int getFitnessValue() {
        return fitnessValue;
    }

    public String getString() {
        return s;
    }

    public void setString(String s, String goalString) {
        this.s = s;
        calculateFitness(goalString);
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public void setStringAndFitness(String s, int fitnessValue) {
        this.s = s;
        this.fitnessValue = fitnessValue;
    }

    public void mutateIndividual(int probability, int asciiLow, int asciiUp, String goalString) {
        int size = s.length();
        char[] chars = s.toCharArray();
        for (int i = 0; i < size; i++) {
            if (ThreadLocalRandom.current().nextInt(0, 100) < probability) {
                chars[i] = (char) ThreadLocalRandom.current().nextInt(asciiLow, asciiUp + 1);
            }
        }
        s = String.valueOf(chars);
        calculateFitness(goalString);
    }

    @Override
    public String toString() {
        return "Individual{" + "s='" + s + '\'' + ", fitnessValue=" + fitnessValue + '}';
    }

    public Individual clone() {
        return new Individual(s, fitnessValue);
    }

}
