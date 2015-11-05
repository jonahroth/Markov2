import java.io.*;
import java.util.*;
import java.util.regex.PatternSyntaxException;

/**
 * Created by jonahrr on 10/19/15.
 */
public class StringGraph {

    int order;
    HashMap<String[], StringEdge> edges;
    HashMap<String, StringNode> nodes;
    Dictionary dictionary;

    public StringGraph() {
        this.edges = new HashMap<>();
        this.nodes = new HashMap<>();
        this.dictionary = new Dictionary("dicts/cmudict-0.7b.txt", true);
        this.order = 1;
    }

    public StringGraph(int n) {
        this();
        this.order = n;
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

    /*
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
    */
    public void importText(String filePath, boolean punctuation) {
        File file = new File(filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] splitArray = line.split("[\\s-]+");
                    if (punctuation) {
                        LinkedList<String> splitList = new LinkedList<>(Arrays.asList(splitArray));
                        System.out.println(splitList.toString());
                        if (splitList.size() >= 1) {
                            // This loop is horribly inefficient. TODO write an iterator that isn't fail-fast
                            for (int i = 0; i < splitList.size(); i++) {
                                String current = splitList.get(i);
                                if (current.length() < 1) continue;
                                System.out.println(current);
                                char last = current.charAt(current.length() - 1);
                                if (isAcceptablePunctuation(last)) {
                                    splitList.add(i + 1, "" + last);
                                    i++; // skip the next element
                                    System.out.println(splitList.get(i));
                                }
                            }
                            for (int i = 0; i < splitList.size() - this.order - 1; i++) {
                                String[] thisWords = new String[this.order];
                                String[] nextWords = new String[this.order];
                                for (int j = 0; j < this.order; j++) {
                                    thisWords[j] = wordOnly(splitList.get(i + j));
                                    nextWords[j] = wordOnly(splitList.get(i + j + 1));
                                }
                                this.linkOrIncrement(String.join(" ", thisWords), String.join(" ", nextWords));

                            }

                        }
                    } else {
                        System.out.println(Arrays.toString(splitArray));
                        if (splitArray.length < this.order) continue;
                        for (int i = 0; i < splitArray.length - this.order - 1; i++) {
                            String[] thisWords = new String[this.order];
                            String[] nextWords = new String[this.order];
                            for (int j = 0; j < this.order; j++) {
                                thisWords[j] = wordOnly(splitArray[i + j]);
                                nextWords[j] = wordOnly(splitArray[i + j + 1]);
                            }
                            this.linkOrIncrement(String.join(" ", thisWords), String.join(" ", nextWords));
                        }
                    }
                } catch (PatternSyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // this.standardize();
    }

    public String wordOnly(String str) {
        if (!(str.length() == 1 && isAcceptablePunctuation(str.charAt(0)))) {
            str = str.replaceAll("[^a-zA-Z0-9-']", "");
            str = str.toUpperCase();
        }
        return str;

    }

    public StringNode highCountWord() { // TODO this method sucks, we need to find words that begin sentences

        Object[] edgeCol = edges.values().toArray();
        int index;
        do {
            index = (int) (Math.random() * edgeCol.length);
        } while (!(edgeCol[index] instanceof StringEdge && ((StringEdge) edgeCol[index]).count < 5));
        return ((StringEdge) edgeCol[index]).head;

    }


    public StringNode nextWord(StringNode word) { // TODO rewrite so we don't need probability at all?
        // is it faster this way?

        int count = 0;

        for (StringEdge e : word.edgesOut) {
            count += e.count;
        }

        int p = (int) (Math.random() * count);
        int acc = 0;

        for (StringEdge e : word.edgesOut) {
            acc += e.count;
            if (acc >= p) {
                return e.head;
            }
        }
        return null;
    }

    public String buildChain(int length) {
        String ret = "";
        StringNode thisWord = this.highCountWord();
        while (length > 1) {
            ret += thisWord.key.substring(0, thisWord.key.indexOf(" ")) + " ";
            thisWord = nextWord(thisWord);
            if (thisWord == null) {
                thisWord = this.highCountWord();
            }
            length--;
        }
        ret += thisWord.key;

        return ret;
    }

    public StringGraph importPath(String filePath) {
        StringGraph ret = null;
        try {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object[] imports = (Object[]) in.readObject();
            in.close();
            fileIn.close();
            int order = (int) imports[0];
            ret = new StringGraph(order);
            ret.edges = (HashMap<String[], StringEdge>) imports[1];
            ret.nodes = (HashMap<String, StringNode>) imports[2];

        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Employee class not found");
            c.printStackTrace();
        }
        System.out.println("Successfully deserialized " + filePath);
        return ret;
    }

    public void export(String filePath) {
        try {
            Object[] exports = {order, edges, nodes};
            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(exports);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in " + filePath);
        } catch (IOException i) {
            i.printStackTrace();
        }


    }

    public boolean isAcceptablePunctuation(char c) {
        char[] acceptable = {'.', ',', '?', '!', '—', '–', '/'};
        for (char p : acceptable) {
            if (p == c) return true;
        }
        return false;
    }

}
