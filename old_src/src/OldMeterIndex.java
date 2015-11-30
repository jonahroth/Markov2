import java.io.*;

/* Copyright 2014 Jonah Raider-Roth
* This program can generate lines of random text in an arbitrary meter. Meters are specified as strings of zeros and onces
* (0 = unstressed, 1 = stressed). It uses CMUDict as a resource for both metrical and rhyming databases. Rhyming database
* currently in alpha, using the last stressed syllable (or unstressed, if no stress is available) as the rhyming part of
* the word.
*
* */

public class OldMeterIndex {
    String[][] index;
    int currentIndex;
    int lastFilledIndex;

    public OldMeterIndex() {
        index = new String[16][2];
        currentIndex = 0;
        lastFilledIndex = -1;
    }

    public OldMeterIndex(int i) {
        this();
        if (i == 1) {
            String s = readFileAsString("resources/database.txt");
            while (s.indexOf("\n") != -1 && s.indexOf(" ") != -1) {
                add(s.substring(0, s.indexOf(" ")), s.substring(s.indexOf(" ") + 1, s.indexOf("\n")));
                s = s.substring(s.indexOf("\n") + 1);
            }
        }
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

    public void createGoodDatabase() {
        String currentFirstLetter = "a";
        String s = readFileAsString("src/cmudict.txt");
        String output = "";
        while (!s.equals("") && s.indexOf("\n") != -1) {
            String thisLine = s.substring(0, s.indexOf("\n"));
            s = s.substring(s.indexOf("\n") + 1);
            String thisWord = thisLine.substring(0, thisLine.indexOf(" "));
            String thisMeter = "";
            for (int k = 0; k < thisLine.length(); k++) {
                if (thisLine.substring(k, k + 1).equals("0")) thisMeter += "0";
                else if (thisLine.substring(k, k + 1).equals("1") || thisLine.substring(k, k + 1).equals("2"))
                    thisMeter += "1";
            }
            output += thisWord + " " + thisMeter + "\n";
            if (!thisWord.substring(0, 1).equals(currentFirstLetter)) {
                currentFirstLetter = thisWord.substring(0, 1);
                System.out.println(currentFirstLetter);
            }
        }
        try {
            writeStringAsFile(output, "database.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void writeStringAsFile(String input, String fileName)
            throws FileNotFoundException {
        PrintWriter out = new PrintWriter(fileName);
        out.println(input);
        out.close();
    }


    public String[] getRandom() {
        int randInd = (int) (Math.random() * lastFilledIndex);
        return index[randInd];
    }

    public String[] getNext() {

        if (lastFilledIndex != -1) {
            currentIndex++;
            return index[currentIndex - 1];
        } else {
            String[] error = {"ERROR", ""};
            return error;
        }
    }

    public String[] getTarget(String target) {
        boolean foundTarget = false;
        int low = 0;
        int high = index.length;
        int mid = (high - low) / 2 + low;
        while (!foundTarget && Math.abs(low - mid) > 1) {
           // System.out.println("hi:" + high + " lo:" + low + " mid:" + mid);
            if (index[mid][0].equals(target)) {
                foundTarget = true;
                //System.out.println("found it");
            } else if (index[mid][0].compareTo(target) < 0) {
                low = mid;
                mid = (high - low) / 2 + low;
            } else {
                high = mid;
                mid = (high - low) / 2 + low;
            }
        }
        return index[mid];
    }

    public void add(String word, String meterPattern) {
        if (lastFilledIndex >= index.length - 1) {
            doubleSize();
        }
        index[lastFilledIndex + 1][0] = word;
        index[lastFilledIndex + 1][1] = meterPattern;
        System.out.println(word);
        lastFilledIndex++;
    }

    public void doubleSize() {
        String[][] newIndex = new String[index.length * 2][2];
        for (int i = 0; i < index.length; i++) {
            for (int j = 0; j < index[i].length; j++) {
                newIndex[i][j] = index[i][j];
            }
        }
        index = newIndex;
    }


}
