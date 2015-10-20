import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by jonahrr on 10/19/15.
 */
public class Dictionary {
    HashMap<String, String[]> entries;

    final int METER_INDEX = 0,
              RHYME_INDEX = 1,
              ATTR_COUNT = 2;


    public Dictionary(String filePath, boolean newFlag) {
        if (newFlag) {
            entries = new HashMap<>();

            File file = new File(filePath);

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] entry = process(line);

                    String[] value = new String[ATTR_COUNT];
                    value[METER_INDEX] = entry[1];
                    value[RHYME_INDEX] = entry[2];

                    entries.put(entry[0], value);
                    System.out.println(Arrays.toString(entry));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileInputStream fileIn = new FileInputStream(filePath);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                entries = (HashMap<String, String[]>) in.readObject();
                in.close();
                fileIn.close();
            } catch(IOException i) {
                i.printStackTrace();
            } catch(ClassNotFoundException c) {
                System.out.println("Employee class not found");
                c.printStackTrace();
            }
            System.out.println("Successfully deserialized " + filePath);
        }

    }

    public void export(String filePath) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(entries);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in " + filePath);
        }catch(IOException i)
        {
            i.printStackTrace();
        }

    }

    private static String[] process(String str) { // formatted for cmudict
        String[] arr = new String[3];
        int firstSpace = str.indexOf(" ");

        if (firstSpace == -1) {
            arr[0] = str;
            return arr;
        }

        // get word component
        arr[0] = str.substring(0, firstSpace); // word component

        String stem = str.substring(firstSpace+1); // two spaces in between

        // get meter component
        arr[1] = stem.replaceAll("[^0-2]", "");

        // get rhyme stem component
        int stringStart = 0;
        if (stem.contains("2")) {
            stringStart = stem.lastIndexOf(" ", stem.lastIndexOf("2"));
        } else if (stem.contains("1")) {
            stringStart = stem.lastIndexOf(" ", stem.lastIndexOf("1"));
        }
        arr[2] = stem.substring(stringStart+1);

        return arr;

    }


}
