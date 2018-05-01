import java.util.Comparator;

public class SortCromossomo implements Comparator<Cromossomo> {
    @Override
    public int compare(Cromossomo c1, Cromossomo c2) {
        return ( c1.getFitness().compareTo(c2.getFitness()) );
    }
}
