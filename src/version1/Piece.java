package version1;

public class Piece {
    private Case position;
    private boolean dames;
    private int pionsManges;
    //2 pour blanc, -2 pour noir
    private int couleur;

    public Piece(Case pos, int coul){
        position = pos;
        couleur = coul;
        dames = false;
        pionsManges = 0;

    }

    public Piece(Piece p){
        position = new Case(p.position.getLigne(), p.position.getColonne());
        couleur = p.couleur;
        dames = p.dames;
        pionsManges = p.pionsManges;
    }

    public void copiePiece(Piece p){
        position.setLigne(p.getPosition().getLigne());
        position.setColonne(p.getPosition().getColonne());
        couleur = p.couleur;
        dames = p.dames;
        pionsManges = p.pionsManges;
    }

    public Case getPosition() {
        return position;
    }

    public void setPosition(Case position) {
        this.position = position;
    }

    public int getCouleur() {
        return couleur;
    }

    public void setCouleur(int couleur) {
        this.couleur = couleur;
    }

    public void addPionsManges() {
        this.pionsManges++;
    }

    public void resetPionsManges() {
        this.pionsManges = 0;
    }

    public int getPionsManges() {
        return pionsManges;
    }

    public boolean isDame(){
        return dames;
    }

    public void setDames(boolean dames){
        this.dames = dames;
    }

}
