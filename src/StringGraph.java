import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jonahrr on 10/19/15.
 */
public class StringGraph {

    HashMap<String[], StringEdge> edges;
    HashMap<String,   StringNode> nodes;
    Dictionary dictionary;

    public StringGraph() {
        this.edges = new HashMap<>();
        this.nodes = new HashMap<>();
        this.dictionary = new Dictionary("dicts/cmudict-0.7b.txt", true);
    }

    public StringNode exists(String key) {
        if (this.nodes.get(key) != null) {
            return this.nodes.get(key);
        } else {
            StringNode candidateNode = new StringNode(key);
            this.nodes.put(key, candidateNode);
            return candidateNode;
        }
    }

    public StringEdge link(String from, String to) {
        String[] pKey = {from, to};
        StringEdge candidateEdge = this.edges.get(pKey);
        if (candidateEdge == null) {
            StringNode tailNode = exists(from);
            StringNode headNode = exists(to);
            candidateEdge = new StringEdge(tailNode, headNode);
        }
        return candidateEdge;
    }

}
