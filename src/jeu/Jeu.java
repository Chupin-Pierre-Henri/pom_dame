package jeu;

import frame.DamierFrame;
import frame.FinFrame;
import plateau.Plateau;

import java.util.concurrent.atomic.AtomicBoolean;

public class Jeu {
    public static AtomicBoolean paused = new AtomicBoolean(true);
    final public int pionsAbouger = 3;
    public Plateau damier;
    public DamierFrame frame;


    public Jeu() throws InterruptedException {
        damier = new Plateau();
        frame = new DamierFrame(damier);
        frame.show();
        frame.rafraichir(damier);
        jouer();
    }

    public static void main(String[] args) throws InterruptedException {
        Jeu jeu = new Jeu();
    }

    /**
     * lance le jeu de dames
     * @throws InterruptedException
     */
    public void jouer() throws InterruptedException {
        int turn = -2;

        while (!damier.isFinPartie()) {
            if (!paused.get()) {
                if (turn == -2) {
                    damier.strategieNaive(2, pionsAbouger, frame);
                    turn = 2;

                    if (!damier.isFinPartie()) {
                        damier.setCoupsRestants(pionsAbouger);
                        Thread.sleep(1000);
                    }
                }
                if (turn == 2) {
                    damier.strategieNaive(-2, pionsAbouger, frame);
                    turn = -2;
                    frame.miseAJourPhase();

                    damier.setCoupsRestants(pionsAbouger);
                    Thread.sleep(1000);
                }
            }
            frame.show();
        }
        finPartie();
    }

    /**
     * affiche la fin de la partie
     */
    public void finPartie() {

        FinFrame frame;
        int scoreJ = this.frame.getScoreJ1();
        int scoreA = this.frame.getScoreJ2();
        //Creation de la fenetre en fonction des scores
        if (scoreJ < scoreA) {
            frame = new FinFrame(-1, scoreJ, scoreA);
        } else if (scoreJ == scoreA) {
            frame = new FinFrame(0, scoreJ, scoreA);
        } else {
            frame = new FinFrame(1, scoreJ, scoreA);
        }
    }
}
