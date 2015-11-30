import java.util.ArrayList;

public class DBPoem {
    String scheme;
    String meter;
    ArrayList<String> schemeWords;
    RhymeLine[] lines;
//    RhymeIndex rind;
//    MeterIndex mind;
    MTDatabase db;

    public DBPoem(MTDatabase db) {
        this.db = db;
        scheme = "ababcdcdefefgg";
        meter = "0101010101";
        schemeWords = new ArrayList<String>();
        lines = new RhymeLine[scheme.length()];
    }

    public DBPoem(MTDatabase db, String newScheme) {
        this(db);
        scheme = newScheme;
        meter = "0101010101";
        schemeWords = new ArrayList<String>();
        lines = new RhymeLine[scheme.length()];

    }  public DBPoem(MTDatabase db, String newScheme, String newMeter) {
        this(db);
        scheme = newScheme;
        meter = newMeter;
        schemeWords = new ArrayList<String>();
        lines = new RhymeLine[scheme.length()];

    }

    public String generatePoem() {
        int uc = numUniqueChars(scheme);
        char[] letters = uniqueChars(scheme);
        String[][] rhymeChart = new String[uc][2];
        for (int i = 0; i < uc; i++) {
            String[] randomEntry = db.getRandom();
            rhymeChart[i][0] = "" + letters[i];
            rhymeChart[i][1] = randomEntry[0];
            while (db.hasRhyme(rhymeChart[i][1])) {
                rhymeChart[i][1] = db.getRandom()[0];
            }
            RhymeLine testLine = new RhymeLine(mind, meter, rind, rhymeChart[i][1]);
            while (testLine.lastWord.equals("NRWerror")) {
                rhymeChart[i][1] = mind.getRandom()[0];
                while (!rind.hasRhyme(rhymeChart[i][1])) {
                    rhymeChart[i][1] = mind.getRandom()[0];
                }
                testLine = new RhymeLine(mind, meter, rind, rhymeChart[i][1]);
            }
        }
        String[][] rhymeMap = new String[scheme.length()][2];
        /*
        example: rhymeMap[0] = scheme letter
                 rhymeMap[1] = index of rhyming word in rhymeChart
                 index = line number
        0   a   0
        1   b   1
        2   a   0
        3   b   1
        4   c   2
        5   d   3
        6   c   2
        7   d   3
        8   e   4
        9   f   5
        10  e   4
        11  f   5
        12  g   6
        13  g   6         */

        for (int i = 0; i < rhymeMap.length; i++) {
            rhymeMap[i][0] = "" + scheme.charAt(i);
            int index = -1;
            for (int j = 0; j < letters.length; j++) {
                if ((letters[j] + "").equals(rhymeMap[i][0]))
                    index = j;
            }
            rhymeMap[i][1] = "" + index;
        }
        String finalPoem = "";
        for (int i = 0; i < scheme.length(); i++) {
            lines[i] = new RhymeLine(mind, meter, rind, rhymeChart[Integer.parseInt(rhymeMap[i][1])][1]);
            while (lines[i].lastWord.equals("NRWerror"))
                lines[i] = new RhymeLine(mind, meter, rind, rhymeChart[Integer.parseInt(rhymeMap[i][1])][1]);
            finalPoem += lines[i].text + "\n";
        }
        return finalPoem;
    }

    public static int numUniqueChars(String s) {
        return uniqueChars(s).length;
    }

    public static char[] uniqueChars(String s) {
        ArrayList<Character> chars = new ArrayList<Character>();
        for (int i = 0; i < s.length(); i++) {
            if (!chars.contains(s.charAt(i))) {
                chars.add(s.charAt(i));
            }
        }
        char[] ret = new char[chars.size()];
        for (int i = 0; i < chars.size(); i++) {
            ret[i] = chars.get(i);
        }
        return ret;
    }
}
