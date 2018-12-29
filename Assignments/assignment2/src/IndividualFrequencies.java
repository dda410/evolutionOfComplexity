public class IndividualFrequencies {

    private Individual individualType;
    private long frequency;

    public IndividualFrequencies() {

    }

    public IndividualFrequencies(long frequency, Individual individualType) {
        this.individualType = individualType;
        this.frequency = frequency;
    }

    public Individual getIndividualType() {
        return individualType;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "IndividualFrequencies{" +
                "individualType=" + individualType +
                ", frequency=" + frequency +
                '}';
    }
}
