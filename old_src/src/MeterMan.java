import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MeterMan {
    public static void main(String[] args) {
        //MeterManWindow.makeWindow();
        MTDatabase db = new MTDatabase();
        db.populateDatabase();
        try {
            Statement stmt = db.c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Words WHERE meter = \"101\";");
            while (rs.next()) {
                String wrd = rs.getString("word");
                String mtr = rs.getString("meter");
                String rhm = rs.getString("rhyme");
                System.out.println(wrd + "\t" + mtr + "\t" + rhm);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void iambicPentameter(int k, OldMeterIndex ind) {
        for (int i = 0; i < k; i++) {
            int reverseBeginning = (int) (Math.random() * 50);
            int feminineEnding = (int) (Math.random() * 50);
            if (reverseBeginning > 47 && feminineEnding > 47) {
                System.out.println(ipTest(ind, "10010101010"));
            } else if (reverseBeginning > 45) {
                System.out.println(ipTest(ind, "1001010101"));
            } else if (feminineEnding > 45) {
                System.out.println(ipTest(ind, "01010101010"));
            } else {
                System.out.println(ipTest(ind, "0101010101"));
            }
        }
    }

    public static String makeHaiku(MeterIndex mind) {
        String m1 = "", m2 = "", m3 = "";
        for (int i = 0; i < 5; i++) {
            m1 += chooseRandom("0", "1");
            m2 += chooseRandom("0", "1");
            m3 += chooseRandom("0", "1");
        }
        for (int i = 5; i < 7; i++) {
            m2 += chooseRandom("0", "1");
        }
        RhymeLine l1 = new RhymeLine(mind, m1);
        RhymeLine l2 = new RhymeLine(mind, m2);
        RhymeLine l3 = new RhymeLine(mind, m3);
        return l1.text + "\n" + l2.text + "\n" + l3.text;
    }

    public static String chooseRandom(String s1, String s2) {
        double d = Math.random();
        if (d < .5) return s1;
        else return s2;
    }

    public static String ipTest(OldMeterIndex ind, String str) {
        String iambx5 = str;
        String line = "";
        while (!iambx5.equals("")) {
            String[] test = ind.getRandom();
            if (test[1].length() <= iambx5.length() && test[1].equals(iambx5.substring(0, test[1].length()))) {
                line += test[0] + " ";
                iambx5 = iambx5.substring(test[1].length());
            }
        }
        return line;
    }


}
