import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.regex.PatternSyntaxException;

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

    public void linkOrIncrement(String from, String to) {
        String[] pKey = {from, to};
        StringEdge candidateEdge = this.edges.get(pKey);
        if (candidateEdge == null) {
            this.link(from, to);
        } else {
            candidateEdge.count++;
        }
    }

    public void standardize() {
        Collection<StringNode> vals = nodes.values();
        for (StringNode s : vals) {
            int totalCount = 0;
            for (StringEdge e : s.edgesOut) {
                totalCount += e.count;
            }
            for (StringEdge e : s.edgesOut) {
                e.probability = (double) e.count / (double) totalCount;
            }
            if (s.meter == null || s.rhyme == null) {
                try {
                    String[] values = dictionary.lookup(wordOnly(s.key));
                    s.meter = values[dictionary.METER_INDEX];
                    s.rhyme = values[dictionary.RHYME_INDEX];
                } catch (NoSuchElementException e) {
                    // expected behavior, not all words in literature are real words
                    System.out.println(s.key);
                    continue;
                }
            }
        }
    }

    public void importText(String filePath ) {
        File file = new File(filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] splitArray = line.split("-+|\\s+");
                    for (int i = 0; i < splitArray.length - 1; i++) {
                        this.linkOrIncrement(wordOnly(splitArray[i]), wordOnly(splitArray[i+1]));
                    }
                } catch (PatternSyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.standardize();
    }

    public static String wordOnly(String str) {
        str = str.replaceAll("[^a-zA-Z0-9-']", "");
        str = str.toUpperCase();
        return str;
    }

}
