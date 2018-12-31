import java.util.Objects;

public class Individual {

    public static final double GROWTH_COOPERATIVE = 0.018;
    public static final double GROWTH_SELFISH = 0.02;
    public static final double CONSUMPTION_COOPERATIVE = 0.1;
    public static final double CONSUMPTION_SELFISH = 0.2;
    public static final int POSSIBLE_COMBINATIONS = 4;
    private boolean selfishGene;
    private boolean largeGroupGene;

    public Individual() {

    }

    public Individual(boolean selfishGene, boolean largeGroupGene) {
        this.selfishGene = selfishGene;
        this.largeGroupGene = largeGroupGene;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "selfishGene=" + selfishGene +
                ", largeGroupGene=" + largeGroupGene +
                '}';
    }

    public boolean isSelfishGene() {
        return selfishGene;
    }

    public boolean isLargeGroupGene() {
        return largeGroupGene;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Individual that = (Individual) o;
        return selfishGene == that.isSelfishGene() &&
                largeGroupGene == that.isLargeGroupGene();
    }

    @Override
    public int hashCode() {
        return Objects.hash(selfishGene, largeGroupGene);
    }

    public Individual clone() {
        return new Individual(this.isSelfishGene(), this.isLargeGroupGene());
    }

}
