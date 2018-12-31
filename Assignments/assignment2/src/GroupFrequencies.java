public class GroupFrequencies {
    private IndividualFrequencies[] groupFrequencies;

    public GroupFrequencies() {

    }

    public GroupFrequencies(IndividualFrequencies ... groupFrequencies) {
        this.groupFrequencies = groupFrequencies;
    }

    public int getSize() {
        int result = 0;
        for (IndividualFrequencies ind : groupFrequencies) {
            result += ind.getFrequency();
        }
        return result;
    }

    public double getSizeOfGenotype(Individual individual) {
        for (IndividualFrequencies ind : groupFrequencies) {
            if (ind.getIndividualType().equals(individual)) {
                return ind.getFrequency();
            }
        }
        return 0;
    }

    public int getNumberOfGenotypes() {
        return this.groupFrequencies.length;
    }

    public IndividualFrequencies getIndividualFrequency(Individual individualType) {
        for (IndividualFrequencies ind:groupFrequencies) {
            if(ind.getIndividualType().equals(individualType)) {
                return ind;
            }
        }
        return new IndividualFrequencies();
    }

    public IndividualFrequencies[] getFrequencies() {
        return groupFrequencies;
    }

    public GroupFrequencies mergeWithGroup(GroupFrequencies other) {
        IndividualFrequencies [] individualFrequencies = new IndividualFrequencies[this.groupFrequencies.length + other.getNumberOfGenotypes()];
        for (int i = 0; i < this.groupFrequencies.length; i++) {
            individualFrequencies[i] = this.groupFrequencies[i].clone();
        }
        IndividualFrequencies[] otherIndividualFrequencies = other.getFrequencies();
        for (int i = 0; i < otherIndividualFrequencies.length; i++) {
            individualFrequencies[i + this.groupFrequencies.length] = otherIndividualFrequencies[i].clone();
        }
        return new GroupFrequencies(individualFrequencies);
    }

    public void rescaleGroup(int populationSize) {
        int totalCurrentSize = this.getSize();
        for (IndividualFrequencies ind : this.groupFrequencies) {
            ind.setFrequency((long) ind.getFrequency() * populationSize / totalCurrentSize);
        }
    }
}
