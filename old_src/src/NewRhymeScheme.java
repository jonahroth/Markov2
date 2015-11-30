import java.util.HashMap;

public class NewRhymeScheme {
    OldMeterIndex mind;
    RhymeIndex rind;
    String meter;
    String scheme;
    RhymeLine[] poem;
    class RhymeGroup {
        RhymeLine[] lines;
        String meter;
        int numLines;
        String wordToRhyme;
        public RhymeGroup(String s) {
            meter = "0101010101";
            numLines = 2;
            wordToRhyme = s.toUpperCase();
            lines = new RhymeLine[2];
            for (int i = 0; i < lines.length; i++) {
                lines[i] = new RhymeLine(mind ,meter, rind, wordToRhyme);
            }
        }
        public RhymeGroup(String s, int l) {
            meter = "0101010101";
            numLines = l;
            wordToRhyme = s.toUpperCase();
            lines = new RhymeLine[l];
            for (int i = 0; i < lines.length; i++) {
                lines[i] = new RhymeLine(mind ,meter, rind, wordToRhyme);
            }
        }
        public RhymeGroup(String s, int l, String m) {
            meter = m;
            numLines = l;
            wordToRhyme = s.toUpperCase();
            lines = new RhymeLine[l];
            for (int i = 0; i < lines.length; i++) {
                lines[i] = new RhymeLine(mind ,meter, rind, wordToRhyme);
            }
        }
    }

    public NewRhymeScheme(OldMeterIndex m, RhymeIndex r) {
        mind = m;
        rind = r;
        scheme = "ababcdcdefefgg";

    }
    public NewRhymeScheme(OldMeterIndex m, RhymeIndex r, String met) {
        this(m,r);
        meter = met;
    }
    public NewRhymeScheme(OldMeterIndex m, RhymeIndex r, String met, String sch) {
        this(m,r,met);
        scheme = sch;
    }

    public String generate() {
        HashMap<Character,String> rhymeScheme = new HashMap<Character,String>();
        for(int i = 0; i < scheme.length(); i++) {
            Character current = scheme.charAt(i);
            if(rhymeScheme.containsKey(current)) {
                ;
            } else {
                String word = mind.getRandom()[0];
                while (!rind.hasRhyme(word)) {
                    word = mind.getRandom()[0];
                }
                rhymeScheme.put(current, word);
            }
        }
        RhymeGroup[] myLines = new RhymeGroup[rhymeScheme.size()];
        /* TODO fix this */
        for (int i = 0; i < myLines.length; i++) ;
        return "";
    }

}
