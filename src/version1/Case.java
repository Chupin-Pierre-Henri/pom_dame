package version1;

public class Case {
    private int ligne;
    private int colonne;

    public Case(){
        setLigne(-1);
        setColonne(-1);
    }

    public Case(int i, int j){
        setLigne(i);
        setColonne(j);
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }

    public void setLigne(int ligne) {
        this.ligne = ligne;
    }

    public void setColonne(int colonne) {
        this.colonne = colonne;
    }

    public void setCase(int i, int j){
        ligne = i;
        colonne = j;
    }

    public void copieCase(Case c){
        ligne = c.ligne;
        colonne = c.colonne;
    }
}
