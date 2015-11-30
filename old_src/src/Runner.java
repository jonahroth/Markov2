import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Runner {

    public static void main(String[] args) {
                  /*
        File file = new File("HawaiiPart2Lyrics.txt");
        String data = null;
        try {
            data = Files.toString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MarkovChain test1 = new MarkovChain(data, 6);
        MWordChain test2  = new MWordChain(data, 2);
        String output = test1.random();
        String duoput = test2.random();
        System.out.println(output + "\n\n\n\n\n\n\n\n\n\n\n\n\n" + duoput);
        try {
            PrintStream out = new PrintStream("output.txt");
            out.println(output);
            out.println("\n\n\n\n\n\n\n");
           // out.println(duoput);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }     */

        MTDatabase db = new MTDatabase();
        db.populateDatabase();
        String[] test = db.getRandom();
        for(String s : test) {
            System.out.println(s);
        }
        System.out.println(db.hasRhyme("TEST"));
    }
}
