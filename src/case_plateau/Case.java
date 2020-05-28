package case_plateau;

public class Case {
    private int ligne;
    private int colonne;

    public Case() {
        setLigne(-1);
        setColonne(-1);
    }

    public Case(int i, int j) {
        setLigne(i);
        setColonne(j);
    }

    /**
     * récupère la ligne
     * @return la ligne
     */
    public int getLigne() {
        return ligne;
    }

    /**
     * change la ligne de la case
     * @param ligne la nouvelle ligne
     */
    public void setLigne(int ligne) {
        this.ligne = ligne;
    }

    /**
     * récupère la colonne
     * @return la colonne
     */
    public int getColonne() {
        return colonne;
    }

    /**
     * change la colonne de la case
     * @param colonne la nouvelle colonne
     */
    public void setColonne(int colonne) {
        this.colonne = colonne;
    }

    /**
     * modifie la la case
     * @param i l'indice de la ligne
     * @param j l'indice de la colonne
     */
    public void setCase(int i, int j) {
        ligne = i;
        colonne = j;
    }

    /**
     * fait une copie de la case
     * @param c la case copié
     */
    public void copieCase(Case c) {
        ligne = c.ligne;
        colonne = c.colonne;
    }
}
