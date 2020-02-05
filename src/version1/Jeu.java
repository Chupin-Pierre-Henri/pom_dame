package version1;


public class Jeu {
    public Plateau damier;
    public DamierFrame frame;
    public boolean joueur;

    public Jeu()throws InterruptedException {
        damier = new Plateau();
        frame=new DamierFrame(damier);
        frame.show();
        frame.rafraichir(damier);
        Thread.sleep(2000);
        jouer();
    }

    public void jouer()throws InterruptedException{

        while (!damier.isFinPartie()){

            damier.strategieNaive(2);
            frame.rafraichir(damier);
            Thread.sleep(2000);
            damier.strategieNaive(-2);
            frame.rafraichir(damier);
            Thread.sleep(2000);
            frame.show();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Jeu jeu=new Jeu();
    }

    public void finPartie(){

        FinFrame frame;
        //r?cupere les scores
        int scoreJ=this.frame.getScoreJ();
        int scoreA=this.frame.getScoreA();
        //Creation de la fenetre en fonction des scores
        if(scoreJ<scoreA)
            frame=new FinFrame(-1,scoreJ,scoreA);
        else if(scoreJ==scoreA)
            frame= new FinFrame(0,scoreJ,scoreA);
        else
            frame=new FinFrame(1,scoreJ,scoreA);
    }
}
