package piece;

import case_plateau.Case;

public class Piece {
    private Case position;
    private boolean dames;
    private int pionsManges;
    //2 pour blanc, -2 pour noir
    private int couleur;

    public Piece(Case pos, int coul) {
        position = pos;
        couleur = coul;
        dames = false;
        pionsManges = 0;

    }

    public Piece(Piece p) {
        position = new Case(p.position.getLigne(), p.position.getColonne());
        couleur = p.couleur;
        dames = p.dames;
        pionsManges = p.pionsManges;
    }

    /**
     * fait une copie de la piece p
     * @param p la piece p à copier
     */
    public void copiePiece(Piece p) {
        position.setLigne(p.getPosition().getLigne());
        position.setColonne(p.getPosition().getColonne());
        couleur = p.couleur;
        dames = p.dames;
        pionsManges = p.pionsManges;
    }

    /**
     * récupère la position de la pièce
     * @return la position
     */
    public Case getPosition() {
        return position;
    }

    /**
     * change la position de la pièce
     * @param position la nouvelle position de la pièce
     */
    public void setPosition(Case position) {
        this.position = position;
    }

    /**
     * récupère la couleur de la pièce
     * @return la couleur de la pièce
     */
    public int getCouleur() {
        return couleur;
    }

    /**
     * incrémante le nombre de pions mangé par la pièce
     */
    public void addPionsManges() {
        this.pionsManges++;
    }

    /**
     * mes le nombre de pions mangé par la pièce à 0
     */
    public void resetPionsManges() {
        this.pionsManges = 0;
    }

    /**
     * récupère le nombre de pions mangé par la pièce
     * @return pionsManges
     */
    public int getPionsManges() {
        return pionsManges;
    }

    /**
     * permet de savoir si la pièce est une dames ou non
     * @return true si c'est un pièce false sinon
     */
    public boolean isDame() {
        return dames;
    }

    /**
     * modifie la situation de la pièce si elle est dame ou non
     * @param dames true si elle devient dames sinon false
     */
    public void setDames(boolean dames) {
        this.dames = dames;
    }
}
