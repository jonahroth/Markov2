import java.util.ArrayList;

public class MarkovNode {

    private String key;
    private char[] state;
    private ArrayList<MarkovNode> nextStates;


    public MarkovNode(String s) {
        state = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            state[i] = s.charAt(i);
        }
        key = s;
        nextStates = new ArrayList<MarkovNode>();
    }

    public MarkovNode(char[] entry) {
        state = new char[entry.length];
        key = "";
        for (int i = 0; i < state.length; i++) {
            state[i] = entry[i];
            key += entry[i] + " ";
        }
        key = key.substring(0, key.length()-1);
        nextStates = new ArrayList<MarkovNode>();
    }

    public void addConnection(MarkovNode next) {
        nextStates.add(next);
    }

    public MarkovNode chooseConnection() {
        int nextIndex = (int) (Math.random() * nextStates.size());
        return nextStates.get(nextIndex);
    }








    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public char[] getState() {
        return state;
    }

    public void setState(char[] state) {
        this.state = state;
    }

    public ArrayList<MarkovNode> getNextStates() {
        return nextStates;
    }

    public void setNextStates(ArrayList<MarkovNode> nextStates) {
        this.nextStates = nextStates;
    }
}