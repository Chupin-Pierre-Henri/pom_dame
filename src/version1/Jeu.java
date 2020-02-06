package version1;


public class Jeu {
    public Plateau damier;
    public DamierFrame frame;
    final public int pionsAbouger = 2;


    public Jeu()throws InterruptedException {
        damier = new Plateau();
        frame=new DamierFrame(damier);
        frame.show();
        frame.rafraichir(damier);
        jouer();
    }

    public void jouer()throws InterruptedException{

        while (!damier.isFinPartie()){

            damier.strategieNaive(2, pionsAbouger, frame);
            if (!damier.isFinPartie()){
                damier.setCoupsRestants(3);
                Thread.sleep(500);

                damier.strategieNaive(-2, pionsAbouger, frame);
                damier.setCoupsRestants(3);
                Thread.sleep(500);
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
        //r?cupere les scores
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
