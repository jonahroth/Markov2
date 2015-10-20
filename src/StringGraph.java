import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.regex.PatternSyntaxException;

/**
 * Created by jonahrr on 10/19/15.
 */
public class StringGraph {

    // TODO this implements a markov chain by word of order 1; generalize to any order

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
            String[] arr = {from, to};
            edges.put(arr, candidateEdge);
            tailNode.edgesOut.add(candidateEdge);
            headNode.edgesIn.add(candidateEdge);
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
        Iterator vals = nodes.entrySet().iterator();
        while (vals.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)vals.next();
            StringNode s = (StringNode)pair.getValue();
            int totalCount = 0;
            for (StringEdge e : s.edgesOut) {
                totalCount += e.count;
            }
            Iterator<StringEdge> edgeIterator = s.edgesOut.iterator();
            while(edgeIterator.hasNext()) {
                StringEdge e = edgeIterator.next();
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

    public StringNode highCountWord() { // TODO this method sucks, we need to find words that begin sentences
        return this.exists("THE");
        /*
        Object[] edgeCol = edges.values().toArray();
        int index;
        do {
            index = (int) (Math.random() * edgeCol.length);
        } while (!(edgeCol[index] instanceof StringEdge && ((StringEdge) edgeCol[index]).count < 5));
        return ((StringEdge) edgeCol[index]).head;
        */
    }

    public StringNode nextWord(StringNode word) { // TODO rewrite so we don't need probability at all?
                                                    // is it faster this way?
        double p = Math.random();
        double acc = 0.0;
        for (StringEdge e : word.edgesOut) {
            acc += e.probability;
            if (acc >= p) {
                return e.head;
            }
        }
        return null;
    }

    public String buildChain(int length) {
        String ret = "";
        StringNode thisWord = this.highCountWord();
        while (length > 0) {
            ret += thisWord.key + " ";
            thisWord = nextWord(thisWord);
            if (thisWord == null) {
                thisWord = this.highCountWord();
            }
            length--;
        }

        return ret;
    }

}
