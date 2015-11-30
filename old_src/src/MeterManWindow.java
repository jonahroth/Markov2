import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MeterManWindow {
    private JTextField schemeInput;
    private JComboBox meterInput;
    private JButton generatePoemButton;
    private JTextArea poemField;
    private JPanel mmWindow;
    private JButton aboutButton;
    private JButton generateHaikuButton;
    public MeterIndex mind;
    public RhymeIndex rind;
    public Poem poem;
    public String myMeter;
    public String myScheme;

    public MeterManWindow() {
        System.out.println("Loading meter index...");
        mind = new MeterIndex();
        System.out.println("Loading rhyme index...");
        rind = new RhymeIndex();
        System.out.println("Creating presets and action listeners...");
        poem = new Poem(mind, rind);
        myMeter = "0101010101";
        myScheme = "ababcdcdefefgg";

        schemeInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                myScheme = schemeInput.getText();
                poem = new Poem(mind, rind, myScheme, myMeter);
            }
        });
        meterInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(meterInput.getSelectedIndex() == 0) {
                    myMeter = "0101010101";
                } else if (meterInput.getSelectedIndex() == 1) {
                    myMeter = "10010010010010011";
                } else if (meterInput.getSelectedIndex() == 2) {
                    myMeter = "01010101010101";
                } else {
                    myMeter = "1010101";
                }
                poem = new Poem(mind, rind, myScheme, myMeter);
            }
        });
        generatePoemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                poemField.setText(poem.generatePoem());
            }
        });
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                About.makeBox();
            }
        });
        System.out.println("Let's make some randomly-generated art!");
        generateHaikuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                poemField.setText(MeterMan.makeHaiku(mind));
            }
        });
    }

    public static void makeWindow() {
        JFrame frame = new JFrame("MeterMan Random Poem Generator");
        frame.setContentPane(new MeterManWindow().mmWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}