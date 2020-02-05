package version1;

import java.util.Random;

public class Piece {
    private Case position;
    private boolean vivante;
    //2 pour blanc, -2 pour noir
    private int couleur;

    //-1 pour gauche, 1 pour droite
    private int direction;

    public Piece(Case pos, int coul){
        position = pos;
        couleur = coul;
        vivante = true;

        Random rand = new Random();
        int n = rand.nextInt(2);
        if(n == 0)
            direction = -1;
        else
            direction = n;
    }

    public boolean isVivante() {
        return vivante;
    }

    public void setVivante(boolean vivante) {
        this.vivante = vivante;
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

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

}
