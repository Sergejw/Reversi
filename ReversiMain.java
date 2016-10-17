package reversi;
import javax.swing.UIManager;

/**
 * Repraesentiert das Spiel Reversi.
 * @author Sergej Willmann / Matrikelnummer: s0542972
 */
public class ReversiMain {

    /**
     * Setzt alle grafische Elemente auf Design von Windows und fuehr das 
     * Spiel Reversi aus.
     * @param args Wird nicht benutzt.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ReversiFrame();
    }
}
