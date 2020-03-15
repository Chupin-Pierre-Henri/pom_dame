package version1;


import javax.swing.*;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

public class Jeu {
    public Plateau damier;
    public DamierFrame frame;
    final public int pionsAbouger = 3;


    public Jeu()throws InterruptedException {
        damier = new Plateau();
        frame=new DamierFrame(damier);
        frame.show();
        frame.rafraichir(damier);
        jouer();
    }

    public void jouer()throws InterruptedException{
        /*InputMap im = BasicPopupMenuUI.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        Timer timer = new Timer(500, this);;

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "space");
        am.put("space", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                if (timer.isRunning()) {
                    timer.stop();
                } else {
                    timer.start();
                }
            }
        });*/


        while (!damier.isFinPartie()){

            damier.strategieNaive(2, pionsAbouger, frame);
            if (!damier.isFinPartie()){
                damier.setCoupsRestants(pionsAbouger);
                Thread.sleep(1000);

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
