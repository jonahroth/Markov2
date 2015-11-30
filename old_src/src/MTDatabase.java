import java.io.File;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MTDatabase {

    Connection c;

    public MTDatabase() {
        c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:word_database.db");

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully.");
    }

    public void populateDatabase() {
        try {
            Statement stmt = c.createStatement();

            try {

                String sql = "CREATE TABLE Words (" +
                        "word   VARCHAR(50) PRIMARY KEY," +
                        "meter  VARCHAR(20)," +
                        "rhyme  VARCHAR(50));";
                // System.out.println(stmt.executeUpdate(sql));
                stmt.close();
            } catch (Exception e) {
                // System.err.println("Tried to create a table that already exists.");
            }
            // System.out.println("Loading meter index...");
            MeterIndex mind = new MeterIndex();
            //  System.out.println("Loading rhyme index...");
            RhymeIndex rind = new RhymeIndex();
            File file = new File("database.txt");
            c.setAutoCommit(false);
            stmt = c.createStatement();
            try {
                InputStream is = getClass().getResourceAsStream("database.txt");
                Scanner scanner = new Scanner(is);
                int lineNum = 0;
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    lineNum++;
                    line = line.replace("\"", "");   // preventing SQL injection
                    line = line.replace("\'", "");
                    if (line.contains(" ")) {
                        String word = line.substring(0, line.indexOf(" "));
                        //    System.out.println(word);
                        String meter = mind.getMeterOfWord(word);
                        String rhyme = rind.rhymeRoot(word);
                        try {
                            stmt.executeUpdate("INSERT INTO WORDS (word, meter, rhyme) VALUES (\"" + word + "\", \"" + meter + "\", \"" + rhyme + "\");");
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
                ResultSet rs = stmt.executeQuery("SELECT * FROM Words");
                while (rs.next()) {
                    String wrd = rs.getString("word");
                    String mtr = rs.getString("meter");
                    String rhm = rs.getString("rhyme");
                    //  System.out.println(wrd + "\t" + mtr + "\t" + rhm);
                }
                rs.close();
                stmt.close();
                //c.close();
            } catch (Exception e) {
                //handle this
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
            stmt = c.createStatement();
            System.out.println("Populated database successfully.");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void sendStatement(String sql) {
        try {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);

        }
    }

    public boolean hasRhyme(String target) { // returns TRUE if the rhyme stem associated with target has two associated words or more
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT rhyme FROM Words WHERE word = \"" + target.toUpperCase() + "\";");
            rs.next();
            String stem = rs.getString("rhyme");
            ResultSet rs2 = stmt.executeQuery("SELECT word FROM Words WHERE rhyme = \"" + stem + "\";");
            ArrayList<String> words = new ArrayList<String>();
            while (rs2.next()) {
                words.add(rs2.getString("word"));
            }
            System.out.println(words.toString());
            return (words.size() > 1);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return false;
    }

    public String[] getRandom() {
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Words ORDER BY RANDOM() LIMIT 1;");
            String[] ret = new String[3];
            try {

                if (rs.next()) {
                    ret[0] = rs.getString("word");
                    ret[1] = rs.getString("meter");
                    ret[2] = rs.getString("rhyme");
                }
            } catch (Exception e) {
                ret[0] = "Error";
            }
            return ret;

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        String[] ret = {"ERROR"};
        return ret;
    }

    public String rhymeStem(String target) {
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Words WHERE word = \"" + target.toUpperCase() + "\";");
            if (rs.next()) {
                return rs.getString("rhyme");
            } else return "";


        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);

        }
        return "";
    }

    public String getRhymingWord(String target) {
        try {
            String stem = rhymeStem(target);
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Words WHERE rhyme = \""+stem+"\";");
            ArrayList<String> words = new ArrayList<String>();
            while(rs.next()) {
                words.add(rs.getString("word"));
            }
            String match;
            if (words.size() > 2) {
                return "NRWerror";
            }
            do {
                int rindex = (int)(Math.random()*words.size());
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public String[] get(String target) {
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Words WHERE word = \"" + target.toUpperCase() + "\";");
            String[] ret = new String[3];
            rs.next();
            ret[0] = rs.getString("word");
            ret[1] = rs.getString("meter");
            ret[2] = rs.getString("rhyme");
            return ret;

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);

        }
    }

}
