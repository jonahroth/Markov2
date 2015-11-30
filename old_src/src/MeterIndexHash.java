import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/* Copyright 2014 Jonah Raider-Roth
* This program can generate lines of random text in an arbitrary meter. Meters are specified as strings of zeros and onces
* (0 = unstressed, 1 = stressed). It uses CMUDict as a resource for both metrical and rhyming databases. Rhyming database
* currently in alpha, using the last stressed syllable (or unstressed, if no stress is available) as the rhyming part of
* the word.
*
* */

public class MeterIndexHash {
    HashMap<String, String> index;
    ArrayList<String> keys;

    public MeterIndexHash() {
        index = new HashMap<String, String>();
        keys = new ArrayList<String>();
    }


    public MeterIndexHash(int i) {
        this();
        if (i == 1) {
            String s = readFileAsString("database.txt");
            while (s.indexOf("\n") != -1 && s.indexOf(" ") != -1) {
                String word = s.substring(0,s.indexOf(" "));
                add(word, s.substring(s.indexOf(" ") + 1, s.indexOf("\n")));
                keys.add(word);
                System.out.println(word);
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
        int randInd = (int) (Math.random() * index.size());
        String[] ret = {keys.get(randInd), index.get(keys.get(randInd))};
        return ret;
    }

    public String getTargetMeter(String target) {
        return index.get(target);
    }

    public void add(String word, String meterPattern) {
        index.put(word, meterPattern);
    }


}
