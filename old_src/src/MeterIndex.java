import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MeterIndex extends OldMeterIndex {
    HashMap<String, ArrayList<String>> meterToWord;
    HashMap<String, String> wordToMeter;

    public MeterIndex() {
        meterToWord = new HashMap<String, ArrayList<String>>();
        wordToMeter = new HashMap<String, String>();
        File file = new File("database.txt");

        try {
            InputStream is = getClass().getResourceAsStream("database.txt");
            Scanner scanner = new Scanner(is);
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                if (line.contains(" ")) {
                    String word = line.substring(0, line.indexOf(" "));
                    if (line.indexOf(" ") >= line.length() - 1 || line.indexOf(" ") == -1) {
                        continue;
                    }
                    String meter = line.substring(line.indexOf(" ") + 1);
                    wordToMeter.put(word, meter);
                    if (meterToWord.containsKey(meter)) {
                        meterToWord.get(meter).add(0, word);
                    } else {
                        meterToWord.put(meter, new ArrayList<String>());
                        meterToWord.get(meter).add(0, word);
                    }
                    //System.out.println(word);
                }
            }
        } catch (Exception e) {
            //handle this
            System.out.println("ERROR!!!");
        }
        /*try {
            String content = Files.toString(new File("src/resources/database.txt"), Charsets.UTF_8);
            while(!content.equals("")) {
                String line;
                if (content.contains("\n")) {
                    line = content.substring(0,content.indexOf("\n"));
                    content = content.substring(content.indexOf("\n")+1);
                }
                else {
                    line = content;
                }
                if (line.contains(" ")) {
                    String word = line.substring(0, line.indexOf(" "));
                    if (line.indexOf(" ") >= line.length() - 1 || !line.contains(" ")) {
                        continue;
                    }
                    String meter = line.substring(line.indexOf(" ") + 1);
                    wordToMeter.put(word, meter);
                    if (meterToWord.containsKey(meter)) {
                        meterToWord.get(meter).add(0, word);
                    } else {
                        meterToWord.put(meter, new ArrayList<String>());
                        meterToWord.get(meter).add(0, word);
                    }
                    System.out.println(word);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } */


    }

    public String getRandom(String remainingMeter) {
        int rnd = (int) (Math.random() * remainingMeter.length());
        String myMeter = remainingMeter.substring(0, rnd);
        String ret = getWordWithMeter(myMeter);
        while (ret.equals("NSWError")) {
            rnd = (int) (Math.random() * remainingMeter.length());
            myMeter = remainingMeter.substring(0, rnd);
            ret = getWordWithMeter(myMeter);
        }
        return ret;
    }

    public String[] getRandom() {
        int upperLimit = (int) (Math.random() * 5);
        String rndMeter = "";
        for (int i = 0; i < upperLimit; i++) {
            double d = Math.random();
            if (d < .5) {
                rndMeter += "0";
            } else {
                rndMeter += "1";
            }
        }
        ArrayList<String> options = meterToWord.get(rndMeter);
        while (options == null || options.size() == 0) {
            upperLimit = (int) (Math.random() * 5);
            rndMeter = "";
            for (int i = 0; i < upperLimit; i++) {
                double d = Math.random();
                if (d < .5) {
                    rndMeter += "0";
                } else {
                    rndMeter += "1";
                }
            }
            options = meterToWord.get(rndMeter);
        }
        String retMe = options.get((int) (Math.random() * options.size()));
        String[] ret = {retMe, rndMeter};
        return ret;
    }

    public String[] getTarget(String target) {
        target = target.toUpperCase();
        String[] ret = {target, wordToMeter.get(target)};
        return ret;
    }

    public String getWordWithMeter(String targetMeter) {
        ArrayList<String> options = meterToWord.get(targetMeter);
        if (options == null) {
            return "NSWError";
        }
        int rnd = (int) (Math.random() * options.size());
        return options.get(rnd);
    }

    public String getMeterOfWord(String target) {
        return wordToMeter.get(target);
    }


}
