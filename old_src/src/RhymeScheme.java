import java.util.ArrayList;

public class RhymeScheme {
    public String scheme;
    public static final String sonnet = "ababcdcdefefgg";
    OldMeterIndex mind;
    RhymeIndex rind;

    class RhymingPair {
        RhymeLine line1;
        RhymeLine line2;

        public RhymingPair() {
            line1 = new RhymeLine(mind, "0101010101");
           // System.out.println(line1.text);
            line2 = new RhymeLine(mind, "0101010101", rind, line1.lastWord);
            while (line2.text.equals("error") || line1.text.equals("error")) {
                line1 = new RhymeLine(mind, "0101010101");
                line2 = new RhymeLine(mind, "0101010101", rind, line1.lastWord);
            }
            System.out.println(line2.text);
        }
    }

    public String makeSonnet() {
        RhymingPair[] ss = new RhymingPair[7];
        for(int i = 0; i < ss.length; i++) {
            ss[i] = new RhymingPair();
        }
        String sonnet = ss[0].line1.text + "\n";
        sonnet += ss[1].line1.text + "\n";
        sonnet += ss[0].line2.text + "\n";
        sonnet += ss[1].line2.text + "\n";
        sonnet += ss[2].line1.text + "\n";
        sonnet += ss[3].line1.text + "\n";
        sonnet += ss[2].line2.text + "\n";
        sonnet += ss[3].line2.text + "\n";
        sonnet += ss[4].line1.text + "\n";
        sonnet += ss[5].line1.text + "\n";
        sonnet += ss[4].line2.text + "\n";
        sonnet += ss[5].line2.text + "\n";
        sonnet += ss[6].line1.text + "\n";
        sonnet += ss[6].line2.text + "\n";
        return sonnet;

    }


    public RhymeScheme(OldMeterIndex m, RhymeIndex r) {
        scheme = sonnet;
        mind = m;
        rind = r;
    }

    public RhymeScheme(OldMeterIndex m, RhymeIndex r, String s) {
        this(m, r);
        scheme = s;
    }

    public String generateScheme() {
        RhymeLine[] poem = new RhymeLine[scheme.length()];
        String[] poemScheme = new String[scheme.length()];
        ArrayList<String> lines = new ArrayList<String>();
        for (int i = 0; i < scheme.length(); i++) {
            String current = scheme.substring(i, i + 1);
            boolean secondTime = false;
            int firstTime = -1;
            for (int j = 0; j < lines.size(); j++) {
                if (lines.get(j).equals(current)) secondTime = true;
            }
            if (secondTime) {
                for (int j = 0; j < i; j++) {
                    if (scheme.substring(j, j + 1).equals(current)) firstTime = j;
                }
            }
            if (!secondTime) {
                poemScheme[i] = mind.getRandom()[0];
                while (!rind.hasRhyme(poemScheme[i]))
                    poemScheme[i] = mind.getRandom()[0];
            }
            if (secondTime && firstTime != -1) {
                poemScheme[i] = poemScheme[firstTime];
            }
            if (secondTime && firstTime == -1) {
                System.out.println("False secondTime");
            }

        }
        for (int i = 0; i < poem.length; i++) {
            poem[i] = new RhymeLine(mind, "0101010101", rind, poemScheme[i]);
        }
        String ret = "";
        for (RhymeLine i : poem) {
            ret += i.text + "\n";
        }
        return ret;
    }
    /*
    public String oldgenerateScheme() {
        RhymeLine[] poem = new RhymeLine[scheme.length()];
        ArrayList<String> used = new ArrayList<String>();
        for (int i = 0; i < scheme.length(); i++) {
            String thisScheme = "" + scheme.substring(i, i + 1);
            boolean isUsed = false;
            for (int f = 0; f < i; f++) {
                if ()
            }
            if (used.contains(thisScheme)) {
                int j;
                for (j = 0; j < i; j++) {
                    if (thisScheme.equals("" + scheme.substring(j, j + 1))) {
                        break;

                    }
                }
                poem[i] = new RhymeLine(mind, "0101010101", rind, poem[j].lastWord);
                while (poem[i].text.equals("error")) {
                    poem[j] = new RhymeLine(mind, "0101010101");
                    poem[i] = new RhymeLine(mind, "0101010101", rind, poem[j].lastWord);
                }


            } else {
                poem[i] = new RhymeLine(mind, "0101010101");
            }
        }
        String thePoem = "";
        for (RhymeLine r : poem) {
            thePoem += r.text + "\n";
        }
        return thePoem;


    }      */

    public static String lastWord(RhymeLine line) {
        String text = line.text;
        int i;
        for (i = text.length() - 1; i > 0; i--) {
            if (text.charAt(i) == ' ') break;
        }
        return text.substring(i + 1);
    }


}
