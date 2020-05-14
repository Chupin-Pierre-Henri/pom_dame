package version1;

import java.util.concurrent.atomic.AtomicBoolean;

public class Jeu {
    public Plateau damier;
    public DamierFrame frame;
    final public int pionsAbouger = 3;
    public static AtomicBoolean paused = new AtomicBoolean(false);


    public Jeu()throws InterruptedException {
        damier = new Plateau();
        frame=new DamierFrame(damier);
        frame.show();
        frame.rafraichir(damier);
        jouer();
    }

    public void jouer()throws InterruptedException{

        while (!damier.isFinPartie()){
            if (!paused.get())
                damier.strategieNaive(2, pionsAbouger, frame);

            if (!damier.isFinPartie()){
                damier.setCoupsRestants(pionsAbouger);
                Thread.sleep(1000);

                if (!paused.get())
                    damier.strategieNaive(-2, pionsAbouger, frame);

                damier.setCoupsRestants(pionsAbouger);
                Thread.sleep(1000);
                frame.show();
            }

        }
        finPartie();
    }

    public static void main(String[] args) throws InterruptedException {
        Jeu jeu=new Jeu();
    }

    public void finPartie(){

        FinFrame frame;
        int scoreJ=this.frame.getScoreJ1();
        int scoreA=this.frame.getScoreJ2();
        //Creation de la fenetre en fonction des scores
        if(scoreJ<scoreA)
            frame=new FinFrame(-1,scoreJ,scoreA);
        else if(scoreJ==scoreA)
            frame= new FinFrame(0,scoreJ,scoreA);
        else
            frame=new FinFrame(1,scoreJ,scoreA);
    }
}
