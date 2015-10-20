/**
 * Created by jonahrr on 10/19/15.
 */
public class StringEdge {

    StringNode tail;
    StringNode head;
    double probability;
    int count;

    public StringEdge(StringNode tail, StringNode head) {
        this.tail = tail;
        this.head = head;
        this.probability = 0; // probability equalizing to be implemented in StringChain class for efficiency
        this.count = 1;
    }

}
