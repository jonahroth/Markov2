import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class MarkovChain {

    int order;
    MarkovNode head;
    HashMap<String, MarkovNode> nodes;

    public MarkovChain(String data, int i_order) {
        order = i_order;
        nodes = new HashMap<String, MarkovNode>();
        try {
            //String data = Files.toString(file, StandardCharsets.UTF_8);

            // create first entry in markov chain

            String keyCurrent = data.substring(0, 1 + order);
            MarkovNode matchCurrent = null;
            if (nodes.containsKey(keyCurrent)) {
                matchCurrent = nodes.get(keyCurrent);
            } else {
                matchCurrent = new MarkovNode(keyCurrent);
                nodes.put(keyCurrent, matchCurrent);
            }
            head = matchCurrent;
            MarkovNode matchPrev = matchCurrent;

            for (int i = 1; i < data.length() - order - 1; i++) {
                keyCurrent = data.substring(i, i + 1 + order);
                matchCurrent = null;
                if (nodes.containsKey(keyCurrent)) {
                    matchCurrent = nodes.get(keyCurrent);
                } else {
                    matchCurrent = new MarkovNode(keyCurrent);
                    nodes.put(keyCurrent, matchCurrent);
                }
                matchPrev.addConnection(matchCurrent);
                matchPrev = matchCurrent;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String random(int chars) {
        String ret = head.getKey();
        MarkovNode current = head;
        MarkovNode prev = head;
        for (int i = 0; i < chars - order - 1; i++) {
            prev = current;
            current = prev.chooseConnection();
            ret += current.getKey().substring(current.getKey().length()-1);
        }
        return ret;

    }

    public String random() {
        String ret = head.getKey();
        MarkovNode current = head;
        MarkovNode prev = head;
        String finalChar = ret.substring(ret.length()-1);
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
            ret += current.getKey().substring(current.getKey().length()-1);
            finalChar = ret.substring(ret.length()-1);
        }
        return ret;

    }


}
