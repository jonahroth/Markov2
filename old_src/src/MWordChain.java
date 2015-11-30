import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class MWordChain {
    int order;
    MWordNode head;
    HashMap<String, MWordNode> nodes;

    public MWordChain(String data, int i_order) {
        // PRECONDITION: "Data" is only composed of words, separated by a single space, and punctuation marks.
        LinkedList<String> words = new LinkedList<String>();
        order = i_order;
        nodes = new HashMap<String, MWordNode>();
        data = data.trim();
        data = data.toLowerCase();
        while (data.indexOf(' ') != -1) {
            String wordCurrent = data.substring(0, data.indexOf(' '));
            words.add(wordCurrent);
            data = data.substring(data.indexOf(' ') + 1);
        }
        words.add(data);
        boolean first = true;
        MWordNode prev = null;
        while (words.size() >= order + 1) {
            String keyCurrent = "";
            for (int i = 0; i < order + 1; i++) {
                keyCurrent += words.get(i) + " ";
            }
            keyCurrent = keyCurrent.trim();
            MWordNode matchCurrent = null;

            if (nodes.containsKey(keyCurrent)) {
                matchCurrent = nodes.get(keyCurrent);
            } else {
                matchCurrent = new MWordNode(keyCurrent);
                nodes.put(keyCurrent, matchCurrent);
            }
            if (first) {
                head = matchCurrent;
                first = false;
            } else {
                prev.addConnection(matchCurrent);
            }

            words.remove();
            prev = matchCurrent;

        }



        /*try {
            //String data = Files.toString(file, StandardCharsets.UTF_8);

            // create first entry in markov chain

            String keyCurrent = data.substring(0, 1 + order);
            MWordNode matchCurrent = null;
            if (nodes.containsKey(keyCurrent)) {
                matchCurrent = nodes.get(keyCurrent);
            } else {
                matchCurrent = new MWordNode(keyCurrent);
                nodes.put(keyCurrent, matchCurrent);
            }
            head = matchCurrent;
            MWordNode matchPrev = matchCurrent;

            // TODO fix parsing code so that it parses words into nodes correctly.
            // it should go word by word not character by character
            for (int i = 1; i < data.length() - order - 1; i++) {
                keyCurrent = data.substring(i, i + 1 + order);
                matchCurrent = null;
                if (nodes.containsKey(keyCurrent)) {
                    matchCurrent = nodes.get(keyCurrent);
                } else {
                    matchCurrent = new MWordNode(keyCurrent);
                    nodes.put(keyCurrent, matchCurrent);
                }
                matchPrev.addConnection(matchCurrent);
                matchPrev = matchCurrent;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }   */


    }

    public String random(int numwords) {
        String ret = head.getKey() + " ";
        MWordNode current = head;

        MWordNode prev = head;
        for (int i = 0; i < numwords - order - 1; i++) {
            prev = current;
            try {
                current = prev.chooseConnection();
            } catch (Exception e) {
                ret += ".";
                break;
            }
            ret += current.getState()[current.getState().length-1] + " ";
        }
        return ret;

    }

    public String random() {
        String ret = head.getKey() + " ";
        MWordNode current = head;
        MWordNode prev = head;
        String finalChar = ret.substring(ret.length() - 1);
        long startTime = System.currentTimeMillis();
        while (!((finalChar.equals(".") || finalChar.equals("!") || finalChar.equals("?"))
                && (int) (Math.random() * 100) < 75) && System.currentTimeMillis() - startTime < 30000) {
            prev = current;
            try {
                current = prev.chooseConnection();
            } catch (Exception e) {
                ret += ".";
                break;
            }
            ret += current.getState()[current.getState().length-1] + " ";
            finalChar = ret.substring(ret.length() - 1);
        }
        return ret;

    }

}
