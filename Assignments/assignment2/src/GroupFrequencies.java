import java.util.Arrays;

public class GroupFrequencies {
    private IndividualFrequencies[] groupFrequencies;

    public GroupFrequencies() {

    }

    public GroupFrequencies(IndividualFrequencies ... groupFrequencies) {
        this.groupFrequencies = groupFrequencies;
    }

    public IndividualFrequencies[] getGroupFrequencies() {
        return groupFrequencies;
    }

    public int getSize() {
        int result = 0;
        for (IndividualFrequencies ind : groupFrequencies) {
            result += ind.getFrequency();
        }
        return result;
    }

    public long getSizeOfGenotype(Individual individual) {
        for (IndividualFrequencies ind : groupFrequencies) {
            if (ind.getIndividualType().equals(individual)) {
                return ind.getFrequency();
            }
        }
        return 0;
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

    @Override
    public String toString() {
        return "GroupFrequencies{" +
                "groupFrequencies=" + Arrays.toString(groupFrequencies) +
                '}';
    }
}
