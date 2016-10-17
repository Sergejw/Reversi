package reversi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Repraesentiert grafische Vorgaenge des Spiels Reversi.
 * @author Sergej Willmann / Matrikelnummer: s0542972
 */
public class ReversiFrame extends JFrame {
    
    /** Teile des Fensters*/
    JPanel menuBar, field, mBLSCont, mBLNCont;
    /** Text */
    JLabel t0, t1;
    /** Positionen auf dem Spielfeld */
    JPanel[][] fPos;
    /** Farbe */
    final private Color lGray = new Color(210, 210, 210);
    /** Spielfeldgroesse */
    private int number = 8;
    /** Spielfeldinhalt */
    private int[][] m;
    /** Wem die Spielsteine gehoeren */
    private int figureId;
    /** Ob passen erlaubt ist */
    JButton pass;
    /** Programm gerade gestartet */
    private boolean newRun = true;
    /** Logik vom Spiel */
    ReversiAlgo k;

    /**
     * Erstellt ein Fenster, welches ein Spiel "Reversi" in Ausgangsposition
     * beinhaltet. Ausgangsposition bedeutet, dass der/die Benutzer erst 
     * Einstellungen vornehmen muessen.
     */
    public ReversiFrame() {
        setTitle("Reversi");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(250, 360));
        setSize(470, 620);
        setLocationRelativeTo(null);
        setField();
        mBLSCont = new JPanel(new BorderLayout());
        t1 = new JLabel("Wählen Sie eine der Spielfeldgrößen aus.", JLabel.CENTER);
        t1.setPreferredSize(new Dimension(100, 28));
        mBLSCont.add(t1, BorderLayout.SOUTH);
        add(mBLSCont, BorderLayout.SOUTH);
        mBLNCont = new JPanel(new BorderLayout());
        t0 = new JLabel("  Spielfeldgröße  /  Spielfeld für neues Spiel zurücksetzen"
                      + " (Auswahl nochmal anklicken.)", JLabel.LEFT);
        t0.setPreferredSize(new Dimension(100, 28));
        setButtons();
        mBLNCont.add(t0, BorderLayout.NORTH);
        add(mBLNCont, BorderLayout.NORTH);
        setVisible(true); 
    }
    
    /**
     * Erstellt Buttons fuer die Auswahl der Spielfeldgroesse und einen 
     * Button fuers Passen.
     */
    private void setButtons() {
        JPanel g = new JPanel(new GridLayout(0,5));
        g.setPreferredSize(new Dimension(100, 33));
        ButtonGroup group = new ButtonGroup();
        JRadioButton jRB;
        for (int i = 0; i != 5; i++) {
            jRB = new JRadioButton (" " + (i+6) + "x" + (i+6), false);
            jRB.addActionListener(new Action(i));
            group.add(jRB);
            g.add(jRB);
        }        
        mBLNCont.add(g, BorderLayout.SOUTH);
        pass = new JButton("Passen");
        pass.addActionListener(new Action(5));
        pass.setEnabled(false);
        pass.setPreferredSize(new Dimension(100, 33));
        mBLSCont.add(pass, BorderLayout.NORTH);
    }

    /**
     * Zeigt auf dem Spielfeld alle bereits platzierten Spielsteine und 
     * moegliche Spielzuege des Spielers, der am Zug ist.
     */
    private void setField() {
        if (newRun) {
            m = new int[number][number];
        } else {
            m = k.getField();
            remove(field);
            pass.setEnabled(k.getPass());
        }
        field = new JPanel(new GridLayout(number, number));
        fPos = new JPanel[number][number];
        for (int i = 0; i != number; i++) {
            for (int u = 0; u != number; u++) {
                fPos[i][u] = new JPanel(new GridLayout(0,1));
                fPos[i][u].setBorder(BorderFactory.createLineBorder(lGray));
                if (!newRun)
                fPos[i][u].addMouseListener(new MouseAdapter(i, u));
                figureId = m[i][u];
                if (figureId == 1 || figureId == 2)
                    fPos[i][u].add(new FigureComponent());
                if (figureId == 3) {
                    fPos[i][u].add(new JLabel("<html><font color='gray'>+"
                                            + "</font></html>", JLabel.CENTER));
                }
                field.add(fPos[i][u]);
            }
        }
        add(field, BorderLayout.CENTER);
        validate();
        newRun = false;
    }

    /**
     * Veraendert den Textinhalt der Zeile, die den Benutzern des Spiels die 
     * Informationen zum Spielverlauf anzeigt.
     */
    private void setInfo() {
        String s, ss;
        if (k.getNumberOfHints() == 0 && k.getNumberOfFreePlaces() != 0)
            t1.setText("Keine moeglichen Spielzuege. Sie muessen passen.");
        else {
            if (k.getId() == 1)
                s = "#3ec6df";
            else
                s = "#e84e5d";
            if (k.getEnd()) {
                ss = "Gewinner:";
                if (k.getNumberOfStones(1) > k.getNumberOfStones(2))
                    s = "#3ec6df";
                else
                    s = "#e84e5d";
            } else
                ss = "Spielzug:";
            t1.setText("<html><font color='#3ec6df' size='5'>&#x95;</font>"
                     + "&nbsp;&nbsp;&nbsp;" + k.getNumberOfStones(1) 
                     + "&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;"
                     + "<font color='#e84e5d' size='5'>&#x95;</font>"
                     + "&nbsp;&nbsp;&nbsp;" + k.getNumberOfStones(2)
                     + "&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;" + ss + " "
                     + "<font color='" + s + "' size='5'>&#x95;</font>"
                     + "</html>");
        }
    }

    /**
     * Verarbeitet die Position des Mauszeigers auf dem Spielfeld.
     */
    class MouseAdapter implements MouseListener {
        int row, col;
        String s;
        MouseAdapter(int row, int col) {
            this.row = row;
            this.col = col;
        }
        
        @Override
        public void mousePressed(MouseEvent me) {
                pass.setEnabled(k.getPass());
                k.setStone(row, col);
                m = k.getField();
                setInfo();
                setField();
        }

        @Override
        public void mouseClicked(MouseEvent me) {}

        @Override
        public void mouseReleased(MouseEvent me) {}

        @Override
        public void mouseEntered(MouseEvent me) {
            fPos[row][col].setBackground(new Color(225, 225, 225));
        }

        @Override
        public void mouseExited(MouseEvent me) {
            fPos[row][col].setBackground(null); 
        }
    }
        
    /**
     * Liefert ein grafisches Objekt "Oval".
     */
    class FigureComponent extends JComponent {
        int color = figureId;
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            switch(color) {
                case 1:
                    g.setColor(new Color(62, 198, 223));
                    break;
                case 2:
                    g.setColor(new Color(232, 78, 93));
                    break;
                default:
                    break;
            }
            g.fillOval(10, 10, (fPos[0][0].getWidth() - 22), 
                      (fPos[0][0].getHeight() - 22));          
        }      
    }
    
    /**
     * Verarbeitet die angeklickten Buttons.
     */
    class Action implements ActionListener {
        int i;
        Action (int n){
            i = n;
        }
      
        public void actionPerformed(ActionEvent e){ 
            switch(i) {
                case 0:
                    number = 6;
                    k = new ReversiAlgo(6);
                    break;
                case 1:
                    number = 7;
                    k = new ReversiAlgo(7);
                    break;
                case 2:
                    number = 8;
                    k = new ReversiAlgo(8);
                    break;
                case 3:
                    number = 9;
                    k = new ReversiAlgo(9);
                    break;
                case 4:
                    number = 10;
                    k = new ReversiAlgo(10);
                    break;
                case 5:
                    k.setId(k.getId());
                    setInfo();
                    break;
                default:
                    break;
            }
            setField();
            if (i != 5)
                t1.setText("<html>Spielzug: <font color='#3ec6df' size='5'>&#x95;</font>"
                        + " Sie koennen den ersten Spielzug passen."
                        + " Mögliche Spielzüge sind(+).");
        } 
    }
    
}