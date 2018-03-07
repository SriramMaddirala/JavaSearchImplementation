import java.util.Comparator;

/**
 * Created by Ram on 12/3/17.
 */
public class VertexComparator implements Comparator<Vertex>{
    @Override
    public int compare(Vertex u, Vertex v) {
        if (u.getUpperbound() < v.getUpperbound())
        {
            return -1;
        }
        if (u.getUpperbound() > v.getUpperbound())
        {
            return 1;
        }
        return 0;
    }
}
