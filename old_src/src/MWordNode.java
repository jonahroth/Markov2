import java.util.ArrayList;

public class MWordNode {
    private String key;
    private String[] state;
    private ArrayList<MWordNode> nextStates;


    public MWordNode(String s) {
        int numspaces = 0;
        key = s;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') numspaces++;
        }
        state = new String[numspaces + 1];
        for (int i = 0; i < state.length; i++) {
            int id = s.indexOf(' ');
            String word = "";
            if (id != -1) {
                word = s.substring(0, id);
            } else {
                word = s;
            }
            state[i] = word;
            s = s.substring(s.indexOf(' ') + 1);
        }
        nextStates = new ArrayList<MWordNode>();
    }

    public MWordNode(String[] entry) {
        state = new String[entry.length];
        key = "";
        for (int i = 0; i < state.length; i++) {
            state[i] = entry[i];
            key += entry[i] + " ";
        }
        key = key.substring(0,key.length()-1);
        nextStates = new ArrayList<MWordNode>();
    }

    public void addConnection(MWordNode next) {
        nextStates.add(next);
    }

    public MWordNode chooseConnection() {
        int nextIndex = (int) (Math.random() * nextStates.size());
        return nextStates.get(nextIndex);
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getState() {
        return state;
    }

    public void setState(String[] state) {
        this.state = state;
    }

    public ArrayList<MWordNode> getNextStates() {
        return nextStates;
    }

    public void setNextStates(ArrayList<MWordNode> nextStates) {
        this.nextStates = nextStates;
    }
}
