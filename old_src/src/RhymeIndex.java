import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RhymeIndex {
    HashMap<String, ArrayList<String>> rhymeToWord;
    HashMap<String, String> wordToRhyme;

    public RhymeIndex() {
        rhymeToWord = new HashMap<String, ArrayList<String>>();
        wordToRhyme = new HashMap<String, String>();
        File file = new File("src/resources/rhymebase.txt");

        try {
            InputStream is = getClass().getResourceAsStream("rhymebase.txt");
            Scanner scanner = new Scanner(is);
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                if(line.contains(" ")){
                String word = line.substring(0,line.indexOf(" "));
                String rhyme = line.substring(line.indexOf(" "));
                wordToRhyme.put(word, rhyme);
                if (rhymeToWord.containsKey(rhyme)) {
                    rhymeToWord.get(rhyme).add(0, word);
                } else {
                    rhymeToWord.put(rhyme, new ArrayList<String>());
                    rhymeToWord.get(rhyme).add(0, word);
                }
             //   System.out.println(word);
                }
            }
        } catch (Exception e) {
            //handle this
            System.out.println("ERROR!!!");
        }

    }
    public boolean hasRhyme (String target) {
        return (wordToRhyme.containsKey(target));
    }
    public String rhymeRoot (String target) {
        if (wordToRhyme.containsKey(target)) {
            return wordToRhyme.get(target);
        } else {
            return "";
        }
    }
    public String getRhymingWord(String target) {
        if (!wordToRhyme.containsKey(target)) {
            return "WNFerror";     // word not found
        }
        String rhymingWord = target;
        String requirements = wordToRhyme.get(target);
        ArrayList<String> possibleWords = rhymeToWord.get(requirements);
        if (possibleWords.size() == 1 && possibleWords.get(0).equals(target)) {
            return "NRWerror";     // no rhyming words
        }
        while (rhymingWord.equals(target)) {
            rhymingWord = possibleWords.get((int)(Math.random() * possibleWords.size()));
        }
        return rhymingWord;
    }

    public static String readFileAsString(String fileName) {
        try {
            int currentChar;
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            StringBuilder fileAsString = new StringBuilder();
            while ((currentChar = reader.read()) != -1) {
                fileAsString.append((char) currentChar);
            }
            reader.close();
            return fileAsString.toString();
        } catch (IOException exception) {
            return null;
        }
    }


    public static void writeStringAsFile(String input, String fileName)
            throws FileNotFoundException {
        PrintWriter out = new PrintWriter(fileName);
        out.println(input);
        out.close();
    }

    public static int lastIndexOf(String s, String target) {
        int i = 0;
        for (int k = 0; k < s.length() - target.length() + 1; k++) {
            if (s.substring(k, k + target.length()).equals(target)) i = k;
        }
        return i;

    }


    public void createGoodDatabase() {
        String currentFirstLetter = "a";
        String s = RhymeIndex.readFileAsString("src/cmudict.txt");
        String output = "";
        while (!s.equals("") && s.indexOf("\n") != -1) {
            String thisLine = s.substring(0, s.indexOf("\n"));
            s = s.substring(s.indexOf("\n") + 1);
            String thisWord = thisLine.substring(0, thisLine.indexOf(" "));
            String thisRhyme = "";
            if ((thisLine.contains("2") && thisLine.contains("1") && lastIndexOf(thisLine, "2") < lastIndexOf(thisLine, "1")) || (thisLine.contains("1")) && !thisLine.contains("2")) {
                //  for (int k = 0; k < thisLine.length(); k++) {
                int last1ind = thisLine.length() - 1;
                boolean found1 = false;
                while (!found1) {
                    if (thisLine.charAt(last1ind) != '1') {
                        last1ind--;
                    } else found1 = true;
                }
                boolean found2 = false;
                while (!found2) {
                    if (thisLine.charAt(last1ind) != ' ') {
                        last1ind--;
                    } else found2 = true;
                }
                thisRhyme = thisLine.substring(last1ind);

                //    }
            } else if (thisLine.contains("2") && thisLine.contains("1")) {
                for (int k = 0; k < thisLine.length(); k++) {
                    int last1ind = thisLine.length() - 1;
                    boolean found1 = false;
                    while (!found1) {
                        if (thisLine.charAt(last1ind) != '2') {
                            last1ind--;
                        } else found1 = true;
                    }
                    boolean found2 = false;
                    while (!found2) {
                        if (thisLine.charAt(last1ind) != ' ') {
                            last1ind--;
                        } else found2 = true;
                    }
                    thisRhyme = thisLine.substring(last1ind);

                }
            } else if (thisLine.contains("0")) {
                //   for (int k = 0; k < thisLine.length(); k++) {
                int last1ind = thisLine.length() - 1;
                boolean found1 = false;
                while (!found1) {
                    if (thisLine.charAt(last1ind) != '0') {
                        last1ind--;
                    } else found1 = true;
                }
                boolean found2 = false;
                while (!found2) {
                    if (thisLine.charAt(last1ind) != ' ') {
                        last1ind--;
                    } else found2 = true;
                }
                thisRhyme = thisLine.substring(last1ind);

                //     }


            }
            output += thisWord + " " + thisRhyme + "\n";
            System.out.println(thisWord + " " + thisRhyme);

            if (!thisWord.substring(0, 1).equals(currentFirstLetter)) {
                currentFirstLetter = thisWord.substring(0, 1);
                System.out.println(currentFirstLetter);
            }
            try {
                RhymeIndex.writeStringAsFile(output, "rhymebase.txt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }
}
