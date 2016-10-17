package reversi;

/**
 * Repraesentiert algorithmische Vorgaenge des Spiels Reversi.
 * @author Sergej Willmann / Matrikelnummer: s0542972
 */
public class ReversiAlgo {
    
    /** Spielfeld */
    private final int[][] field;
    /** Identifikationsnummer */
    private int id = 1;
    /** Spielfeldgroesse (breite oder hoehe) */
    private final int size;
    /** Position des zuletzt gesetzten Spielsteins */
    private int row, col;
    /** Spielstand */
    private boolean end = false;
    
    /**
     * Erstellt ein Spielfeld (Matrix), wo Laenge und Breite gleich sind.
     * Platziert in der Mitte des Feldes die Spielsteine, sodass diese die
     * Ausgangssituation fuer das Spiel "Reversi" ergeben. Anschliessend 
     * werden moegliche Spielzuege markiert.
     * @param i Wert fuer Breite und Laenge.
     * @throws IllegalArgumentException Fehlermeldung auf fehlerhafte 
     * Parameter.
     */
    public ReversiAlgo(int i) throws IllegalArgumentException {
        if (5 < i && i < 11) {
            size = i;
            field = new int[i][i];
            field[i / 2][i / 2] = 1;
            field[i / 2 - 1][i / 2 - 1] = 1;
            field[i / 2 - 1][i / 2] = 2;
            field[i / 2][i / 2 - 1] = 2;
            setHints();
        } else {
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * Liefert einen booleschen Wert, der die Spielzugabgabe entscheidet. 
     * Wenn ein Spieler keine Zugmoeglichkeiten hat, dann kann er seinen 
     * Spielzug an den anderen Spieler abgeben. Wenn noch keine Spielsteine 
     * gesetzt wurden (Ausgangssituation), dann kann der Spieler seinen 
     * Spielzug abgeben und somit kann der andere Spieler mit seinem 
     * Spielstein das Spiel starten. In allen andere Faellen kann der Spieler 
     * seinen Spielzug nicht abgeben.
     * @return Boolesche Wert, ob Spielzugabgabe erlaubt.
     */
    public boolean getPass() {
        boolean pass = false; 
        int temp = 0;
        int free = 0;
        if (getNumberOfStones(1) == 2 && getNumberOfStones(2) == 2)
            pass = true;
        for (int r = 0; r != size; r++) {
            for (int c = 0; c != size; c++) {
                if (field[r][c] == 3)
                    temp++;
                if (field[r][c] == 0)
                    free++;
            }
        }
        if (temp == 0 && free != 0)
            pass = true;
        return pass;
    }
    
    /**
     * Liefert die Anzahl der moeglichen Spielzuege auf dem Spielfeld.
     * @return Anzahl der moeglichen Spielzuege auf dem Spielfeld.
     */
    public int getNumberOfHints() {
        int numberOfHints = 0;
        for (int r = 0; r != size; r++) {
            for (int c = 0; c != size; c++) {
                if (field[r][c] == 3)
                    numberOfHints++;
            }
        }
        return numberOfHints;
    }
    
    /**
     * Liefert die Anzahl der freien Plaetze auf dem Spielfeld.
     * @return Anzahl der freien Plaetze auf dem Spielfeld.
     */
    public int getNumberOfFreePlaces() {
        int numberOfFreePlaces = 0;
        for (int r = 0; r != size; r++) {
            for (int c = 0; c != size; c++) {
                if (field[r][c] == 0)
                    numberOfFreePlaces++;
            }
        }
        return numberOfFreePlaces;
    }
    
    /**
     * Liefert einen booleschen Wert, welcher den Spielstand repraesentiert.
     * Wenn das Spiel beendet ist, dann wird true geliefert. Wenn das Spiel 
     * noch lauft, dann wird false geliefert.
     * @return Stand des Spiels.
     */
    public boolean getEnd() {
        int free = 0;
        int id1 = 0;
        int id2 = 0;
        int hint = 0;
        for (int r = 0; r != size; r++) {
            for (int c = 0; c != size; c++) {
                if (field[r][c] == 0)
                    free++;
                if (field[r][c] == 1)
                    id1++;
                if (field[r][c] == 2)
                    id2++;
                if (field[r][c] == 3)
                    hint++;
            }
        }
        if ((free == 0 && hint == 0) || id1 == 0 || id2 == 0)
            end = true;
        return end;
    }

    
    /**
     * Liefert die Identifikationsnummer des Spielers, der am Zug ist.
     * @return Identifikationsnummer.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Liefert den Spielfeldinhalt.
     * @return Spielfeldinhalt.
     */
    public int[][] getField() {
        return field;
    }
    
    /**
     * Zaehlt alle Spielsteine, welche der uebergebenen Identifikationsnummer
     * angehoeren und liefert die Anzahl dieser Spielsteine.
     * @param id Uebergebene Identifikationsnummer.
     * @return Anzahl der Spielsteine.
     */
    public int getNumberOfStones(int id) {
        int number = 0;
        for (int r = 0; r != size; r++) {
            for (int c = 0; c != size; c++) {
                if (field[r][c] == id)
                    number++;
            }
        }
        return number;
    }
    
    /**
     * Tauscht die aktuelle Identifikationsnummer durch eine andere.
     * @param id Aktuelle Identifikationsnummer.
     */
    public void setId(int id) {
        if (id == 1 || id == 2) {
            this.id = (3 - id);
            for (int r = 0; r != size; r++) {
                for (int c = 0; c != size; c++)
                    if (field[r][c] == 3)
                        field[r][c] = 0;
            }
            setHints();
        }
    }
    
    /**
     * Setzt einen Spielstein aufs Spielfeld, wenn dieser auf eine erlaubte
     * Position platziert wurde.
     * @param row Spielfeldzeile, in welche der Spielstein gesetzt wurde.
     * @param col Spielfeldspalte, in welche der Spielstein gesetzt wurde.
     */
    public void setStone(int row, int col) {
        if (3 == field[row][col]) {
            field[row][col] = id;
            this.row = row;
            this.col = col;
            turnStones();
            id = (3 - id);
            for (int i = 0; i != size; i++) {
                for (int u = 0; u != size; u++) {
                    if (field[i][u] == 3)
                        field[i][u] = 0;
                }
            }
            setHints();
        }
    }

    /**
     * Fasst alle Methoden, die die moeglichen Spielzuege markieren, 
     * zusammen und ruft diese nacheinander auf.
     */
    private void setHints() {
        setHintsDiagonalRightUpRow();
        setHintsDiagonalRightUpCol();
        setHintsDiagonalLeftDownCol();
        setHintsDiagonalRightDownRow();
        setHintsDiagonalLeftUpRow();
        setHintsDiagonalLeftDownRow();
        setHintsDiagonalRightDownCol();
        setHintsDiagonalLeftUpCol();
        setHintsHorizontalLeft();
        setHintsHorizontalRight();
        setHintsVerticalUp();
        setHintsVerticalDown();
    }
    
    /**
     * Markiert alle Stellen auf dem Spielfeld, wo der Spieler gegnerische
     * Spielsteine horizontal von links nach rechts, durch Auswahl einer 
     * dieser Markierungen, einschliessen kann. 
     */
    private void setHintsHorizontalLeft() {
        int hint;
        for (int r = 0; r != size; r++) {
            for (int c = 0; c < (size - 1); c++) {
                if (field[r][c] == 0 && field[r][c + 1] == (3 - id)) {
                    hint = c;
                    do {
                    } while (c < (size - 1) && field[r][++c] == (3 - id));
                    if (field[r][c] == id)
                        field[r][hint] = 3;
                    else
                        --c;
                }
            }
        } 
    }
    
    /**
     * Markiert alle Stellen auf dem Spielfeld, wo der Spieler gegnerische
     * Spielsteine horizontal von rechts nach links, durch Auswahl einer 
     * dieser Markierungen, einschliessen kann. 
     */
    private void setHintsHorizontalRight() {
        int hint;
        for (int r = 0; r != size; r++) {
            for (int c = (size - 1); c > 0; c--) {
                if (field[r][c] == 0 && field[r][c - 1] == (3 - id)) {
                    hint = c;
                    do {
                    } while (c > 0 && field[r][--c] == (3 - id));
                    if (field[r][c] == id)
                        field[r][hint] = 3;
                    else
                        ++c;
                }
            }
        } 
    }
    
    /**
     * Markiert alle Stellen auf dem Spielfeld, wo der Spieler gegnerische
     * Spielsteine vertikal von unten nach oben, durch Auswahl einer 
     * dieser Markierungen, einschliessen kann. 
     */
    private void setHintsVerticalUp() {
        int hint;
        for (int c = 0; c < size; c++) {
            for (int r = 0; r < (size - 1); r++) {
                if (field[r][c] == 0 && field[r + 1][c] == (3 - id)) {
                    hint = r;
                    do {
                    } while (r < (size - 1) && field[++r][c] == (3 - id));
                    if (field[r][c] == id)
                        field[hint][c] = 3;
                    else
                        --r;
                }
            }
        } 
    }
    
    /**
     * Markiert alle Stellen auf dem Spielfeld, wo der Spieler gegnerische
     * Spielsteine vertikal von oben nach unten, durch Auswahl einer 
     * dieser Markierungen, einschliessen kann. 
     */
    private void setHintsVerticalDown() {
        int hint;
        for (int c = 0; c < size; c++) {
            for (int r = (size - 1); r > 0; r--) {
                if (field[r][c] == 0 && field[r - 1][c] == (3 - id)) {
                    hint = r;
                    do {
                    } while (r > 0 && field[--r][c] == (3 - id));
                    if (field[r][c] == id)
                        field[hint][c] = 3;
                    else
                        ++r;
                }
            }
        }
    }

  
    /**
     * Durchlauft ab der gelieferten Spielfeldspalte das Spielfeld diagonal
     * (nach unten / rechts) durch und markiert die moeglichen Spielzuege, 
     * sodass der Spieler bei seinem Spielzug die gegnerischen Spielsteine 
     * von unten nach oben links einschliessen kann.
     * 
     * marke/geg stein / stein .. von oben links nach unten rechts obere hälfte
     */
    private void setHintsDiagonalLeftUpCol() {
        int hintR, hintC, r, c;
        for (int t = 0; t < (size - 1); t++) {
            r = 0;
            c = t;
            while (r < (size - 1) && c < (size - 1)){
                if (field[r][c] == 0 && field[r + 1][c + 1] == (3 - id)) {
                    hintR = r;
                    hintC = c;
                    do {
                    } while (c < (size - 1) && field[++r][++c] == (3 - id));
                    if (field[r][c] == id)
                        field[hintR][hintC] = 3;
                }
                r++;
                c++;
            }
        }
    }
    
    /**
     * Durchlauft ab der gelieferten Spielfeldspalte das Spielfeld diagonal
     * (nach oben / links) durch und markiert die moeglichen Spielzuege, 
     * sodass der Spieler bei seinem Spielzug die gegnerischen Spielsteine 
     * von oben nach unten rechts einschliessen kann.
     * 
     * marke/geg stein / stein .. von oben rechts nach unten links obere hälfte
     */
    private void setHintsDiagonalRightDownCol() {
        int hintR, hintC, r, c;
        for (int t = 0; t < (size - 1); t++) {
            r = (size - 1) - t;
            c = 0;
            while (r > 0 && c < (size - 1)){
                if (field[r][c] == 0 && field[r - 1][c + 1] == (3 - id)) {
                    hintR = r;
                    hintC = c;
                    do {
                    } while (r > 0 && field[--r][++c] == (3 - id));
                    if (field[r][c] == id)
                        field[hintR][hintC] = 3;
                }
                r--;
                c++;
            }
        }
    }
    
    /**
     * Durchlauft ab der gelieferten Spielfeldzeile das Spielfeld diagonal 
     * (nach unten / rechts) durch und markiert die moeglichen Spielzuege, 
     * sodass der Spieler bei seinem Spielzug die gegnerischen Spielsteine 
     * von unten nach oben links einschliessen kann.
     * 
     * marke/geg stein / stein .. von oben links nach unten rechts untere hälfte
     * 
     */
    private void setHintsDiagonalLeftUpRow() {
        int hintR, hintC, r, c;
        for (int t = 0; t < (size - 1); t++) {
            r = t;
            c = 0;
            while (r < (size - 1) && c < (size - 1)){
                if (field[r][c] == 0 && field[r + 1][c + 1] == (3 - id)) {
                    hintR = r;
                    hintC = c;
                    do {
                    } while (r < (size - 1) && field[++r][++c] == (3 - id));
                    if (field[r][c] == id)
                        field[hintR][hintC] = 3;
                }
                r++;
                c++;
            }
        }
    }

    /**
     * Durchlauft ab der gelieferten Spielfeldzeile das Spielfeld diagonal 
     * (nach oben / links) durch und markiert die moeglichen Spielzuege, 
     * sodass der Spieler bei seinem Spielzug die gegnerischen Spielsteine 
     * von oben nach unten rechts einschliessen kann.
     * 
     * stein/geg stein / marke .. von unten rechts nach oben links obere hälfte
     */
    private void setHintsDiagonalRightDownRow() {
        int hintR, hintC, r, c;
        for (int t = 0; t < (size - 1); t++) {
            r = (size - 1) - t;
            c = (size - 1);
            while (r > 0 && c > 0){
                if (field[r][c] == 0 && field[r - 1][c - 1] == (3 - id)) {
                    hintR = r;
                    hintC = c;
                    do {
                    } while (r > 0 && field[--r][--c] == (3 - id));
                    if (field[r][c] == id)
                        field[hintR][hintC] = 3;
                }
                r--;
                c--;
            }
        }
    }
    
    /**
     * Durchlauft ab der gelieferten Spielfeldspalte das Spielfeld diagonal
     * (nach oben / rechts) durch und markiert die moeglichen Spielzuege, 
     * sodass der Spieler bei seinem Spielzug die gegnerischen Spielsteine 
     * von oben nach unten links einschliessen kann.
     * 

     */
    private void setHintsDiagonalLeftDownCol() {
        int hintR, hintC, r, c;
        for (int t = 0; t < (size - 1); t++) {
            r = (size - 1);
            c = (size - 1) - t;
            while (r > 0 && c > 0){
                if (field[r][c] == 0 && field[r - 1][c - 1] == (3 - id)) {
                    hintR = r;
                    hintC = c;
                    do {
                    } while (c > 0 && field[--r][--c] == (3 - id));
                    if (field[r][c] == id)
                        field[hintR][hintC] = 3;
                }
                r--;
                c--;
            }
        }
    }
    
    /**
     * Durchlauft ab der gelieferten Spielfeldspalte das Spielfeld diagonal
     * (nach unten / links) durch und markiert die moeglichen Spielzuege, 
     * sodass der Spieler bei seinem Spielzug die gegnerischen Spielsteine 
     * von unten nach oben rechts einschliessen kann.
     * 
     * von oben rechts nach unten links obere hälfte
     */
    private void setHintsDiagonalRightUpCol() {
        int hintR, hintC, r, c;
        for (int t = 0; t < (size - 1); t++) {
            r = 0;
            c = (size - 1) - t;
            while (r < (size - 1) && c > 0){
                if (field[r][c] == 0 && field[r + 1][c - 1] == (3 - id)) {
                    hintR = r;
                    hintC = c;
                    do {
                    } while (c > 0 && field[++r][--c] == (3 - id));
                    if (field[r][c] == id)
                        field[hintR][hintC] = 3;
                }
                r++;
                c--;
            }
        }
    }
    
    /**
     * Durchlauft ab der gelieferten Spielfeldzeile das Spielfeld diagonal 
     * (nach oben / rechts) durch und markiert die moeglichen Spielzuege, 
     * sodass der Spieler bei seinem Spielzug die gegnerischen Spielsteine 
     * von oben nach unten links einschliessen kann.
     * 
     * marke/geg stein / stein .. von oben rechts nach unten links untere hälfte
     */
    private void setHintsDiagonalLeftDownRow() {
        int hintR, hintC, r, c;
        for (int t = 0; t < (size - 1); t++) {
            c = t;
            r = (size - 1);
            while (r > 0 && c < (size - 1)){
                if (field[r][c] == 0 && field[r - 1][c + 1] == (3 - id)) {
                    hintR = r;
                    hintC = c;
                    do {
                    } while (c < (size - 1) && field[--r][++c] == (3 - id));
                    if (field[r][c] == id)
                        field[hintR][hintC] = 3;
                }
                r--;
                c++;
            }
        }
    }
    
    /**
     * Durchlauft ab der gelieferten Spielfeldzeile das Spielfeld diagonal 
     * (nach unten / links) durch und markiert die moeglichen Spielzuege, 
     * sodass der Spieler bei seinem Spielzug die gegnerischen Spielsteine 
     * von unten nach oben rechts einschliessen kann.
     * 
     * von oben rechts nach unten links untere hälfte
     */
    private void setHintsDiagonalRightUpRow() {
        int hintR, hintC, r, c;
        for (int t = 0; t < (size - 1); t++) {
            r = (size - 1) - t;
            c = (size - 1);
            while (r < (size - 1) && c > 0){
                if (field[r][c] == 0 && field[r + 1][c - 1] == (3 - id)) {
                    hintR = r;
                    hintC = c;
                    do {
                    } while (r < (size - 1) && field[++r][--c] == (3 - id));
                    if (field[r][c] == id)
                        field[hintR][hintC] = 3;
                }
                r++;
                c--;
            }
        }
    }

    /**
     * Fasst alle Methoden, die die Spielsteinfarbe aendern, zusammen und 
     * ruft diese nacheinander auf.
     */
    private void turnStones() {
        turnStonesHorLeft();
        turnStonesHorRight();
        turnStonesVerUp();
        turnStonesVerDown();
        turnStonesDiagRightDown();
        turnStonesDiagLeftUp();
        turnStonesDiagRightUp();
        turnStonesDiagLeftDown();
    }

    /**
     * Aendert die Farbe aller Spielsteine, die durch den zuletzt gesetzen
     * Spielstein anderer Farbe vertikal von rechts nach links eingeschlossen 
     * wurden.
     */
    private void turnStonesHorLeft() {
        int c = col;
        if (c < (size - 2) && c > -1)
        do {
            if (field[row][++c] == id)
                while (c != col)
                    field[row][--c] = id;
            if (c > (size - 2))
                c = col;
        } while(c != col && field[row][c] == (3 - id));
    }
    
    /**
     * Aendert die Farbe aller Spielsteine, die durch den zuletzt gesetzen
     * Spielstein anderer Farbe vertikal von links nach rechts eingeschlossen 
     * wurden.
     */
    private void turnStonesHorRight() {
        int c = col;
        if (c < size && c > 1)
        do {
            if (field[row][--c] == id)
                while (c != col)
                    field[row][++c] = id;
            if (c < 1)
                c = col;
        } while(c != col && field[row][c] == (3 - id));
    }
    
    /**
     * Aendert die Farbe aller Spielsteine, die durch den zuletzt gesetzen
     * Spielstein anderer Farbe vertikal von oben nach unten eingeschlossen 
     * wurden.
     */
    private void turnStonesVerUp() {
        int r = row;
        if (r < size && r > 1)
        do {
            if (field[--r][col] == id)
                while (r != row)
                    field[++r][col] = id;
            if (r < 1)
                r = row;
        } while(r != row && field[r][col] == (3 - id));
    }
    
    /**
     * Aendert die Farbe aller Spielsteine, die durch den zuletzt gesetzen
     * Spielstein anderer Farbe vertikal von unten nach oben eingeschlossen 
     * wurden.
     */
    private void turnStonesVerDown() {
        int r = row;
        if (r < (size - 2) && r > -1)
        do {
            if (field[++r][col] == id)
                while (r != row)
                    field[--r][col] = id;
            if (r > (size - 2))
                r = row;
        } while(r != row && field[r][col] == (3 - id));
    }
    
    /**
     * Aendert die Farbe aller Spielsteine, die durch den zuletzt gesetzen
     * Spielstein anderer Farbe diagonal von links oben nach unten rechts
     * eingeschlossen wurden.
     */
    private void turnStonesDiagRightDown() {
        int r = row;
        int c = col;
        if (r < (size - 1) && c < (size - 1))
        do {
            if (field[++r][++c] == id)
                while (r != row)
                    field[--r][--c] = id;
            if (c == (size - 1) || r == (size - 1))
                r = row;
        } while(r != row && field[r][c] == (3 - id));
    }
    
    /**
     * Aendert die Farbe aller Spielsteine, die durch den zuletzt gesetzen
     * Spielstein anderer Farbe diagonal von rechts unten nach oben links
     * eingeschlossen wurden.
     */
    private void turnStonesDiagLeftUp() {
        int r = row;
        int c = col;
        if (r > 1 && c > 1)
        do {
            if (field[--r][--c] == id)
                while (r != row)
                    field[++r][++c] = id;
            if (c == 0 || r == 0)
                r = row;
        } while(r != row && field[r][c] == (3 - id));
    }
    
    /**
     * Aendert die Farbe aller Spielsteine, die durch den zuletzt gesetzen
     * Spielstein anderer Farbe diagonal von links unten nach oben rechts
     * eingeschlossen wurden.
     */
    private void turnStonesDiagRightUp() {
        int r = row;
        int c = col;
        int s = (size - 1);
        if (r > 0 && c < s)
        do {
            if (field[--r][++c] == id)
                while (r != row)
                    field[++r][--c] = id;
            if (r == 0 || c == s)
                r = row;
        } while(r != row && field[r][c] == (3 - id));
    }
    
    /**
     * Aendert die Farbe aller Spielsteine, die durch den zuletzt gesetzen
     * Spielstein anderer Farbe diagonal von rechts oben nach unten links
     * eingeschlossen wurden.
     */
    private void turnStonesDiagLeftDown() {
        int r = row;
        int c = col;
        int s = (size - 1);
        if (c > 1 && r < s)
        do {
            if (field[++r][--c] == id)
                while (r != row)
                    field[--r][++c] = id;
            if (c == 0 || r == s)
                r = row;
        } while(r != row && field[r][c] == (3 - id));
    }
}
