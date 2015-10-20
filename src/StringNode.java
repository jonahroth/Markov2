/**
 * Created by jonahrr on 10/19/15.
 */
public class StringNode {
    String key;
    String meter;
    String rhyme;

    public StringNode() {
        super();
    }

    public StringNode(String key) {
        this();
        this.key = key;
    }

    public StringNode(String key, String meter, String rhyme) {
        this();
        this.key = key;
        this.meter = meter;
        this.rhyme = rhyme;
    }

}
