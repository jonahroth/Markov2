import java.util.LinkedList;

/**
 * Created by jonahrr on 10/19/15.
 */
public class StringNode {
    String key;
    String meter;
    String rhyme;
    String[] data;

    LinkedList<StringEdge> edgesIn;
    LinkedList<StringEdge> edgesOut;

    public StringNode() {
        super();
        edgesIn = new LinkedList<>();
        edgesOut = new LinkedList<>();
    }

    public StringNode(String key) {
        this();
        this.key = key;
        // obtain meter and rhyme somehow
    }

    public StringNode(String key, String meter, String rhyme) {
        this();
        this.key = key;
        this.meter = meter;
        this.rhyme = rhyme;
    }

}
