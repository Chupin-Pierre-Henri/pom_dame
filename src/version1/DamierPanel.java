package version1;

import javax.swing.*;
import java.awt.*;


public class DamierPanel extends JPanel {
    //tableau de type damier qui permet l'afichage du damier graphique
    public Plateau mat;

    Image noir,blanc,pionN,pionB,pionBS,pionNS,pionBP,dameB,dameN;
    public static final int TAILLEIM=62;

    public DamierPanel(Plateau mat){

        this.mat=mat;
        Toolkit kit=Toolkit.getDefaultToolkit();
        MediaTracker tracker=new MediaTracker(this);
        this.blanc=kit.getImage("src/damier/blanc.jpg");
        this.noir=kit.getImage("src/damier/noir.jpg");
        this.pionB=kit.getImage("src/damier/pionB.jpg");
        this.pionN=kit.getImage("src/damier/pionN.jpg");
        this.pionBS=kit.getImage("src/damier/pionBS.jpg");
        this.pionNS=kit.getImage("src/damier/pionNS.jpg");
        this.pionBP=kit.getImage("src/damier/pionBP.jpg");
        this.dameB=kit.getImage("src/damier/dameB.jpg");
        this.dameN=kit.getImage("src/damier/dameN.jpg");
        tracker.addImage(blanc,0);
        tracker.addImage(noir,0);
        tracker.addImage(pionB,0);
        tracker.addImage(pionN,0);
        tracker.addImage(dameB,0);
        tracker.addImage(dameN,0);
        try {tracker.waitForID(0);}
        catch(InterruptedException e){}
    }

    public void transformPionB(){

    }

    public void paintComponent(Graphics g){

        int i,j;
        //Pour chaque ligne
        for (i=0; i< mat.NBCASES; i++){
            //pour chaque colonne
            for (j=0; j< mat.NBCASES; j++){
                if (mat.getCase(i,j) instanceof CaseNoire)
                    g.drawImage(this.blanc,j*TAILLEIM,i*TAILLEIM,null);
                else if (mat.getCase(i,j) instanceof CaseBlanche && ((CaseBlanche) mat.getCase(i,j)).isLibre())
                    g.drawImage(this.noir,j* TAILLEIM,i* TAILLEIM,null);
                else if (mat.getCase(i,j) instanceof CaseBlanche &&
                        ((CaseBlanche) mat.getCase(i,j)).getPiece().getCouleur() == 2 &&
                        //((CaseBlanche) mat.getCase(i,j)).getPiece().isVivante() &&
                        !((CaseBlanche) mat.getCase(i,j)).getPiece().isDame())

                    g.drawImage(this.pionB,j*TAILLEIM,i*TAILLEIM,null);
                else if (mat.getCase(i,j) instanceof CaseBlanche &&
                        ((CaseBlanche) mat.getCase(i,j)).getPiece().getCouleur() == -2 &&
                        //((CaseBlanche) mat.getCase(i,j)).getPiece().isVivante() &&
                        !((CaseBlanche) mat.getCase(i,j)).getPiece().isDame())

                    g.drawImage(this.pionN,j*TAILLEIM,i*TAILLEIM,null);
                else if (mat.getCase(i,j) instanceof CaseBlanche &&
                        ((CaseBlanche) mat.getCase(i,j)).getPiece().getCouleur() == -2 &&
                        //((CaseBlanche) mat.getCase(i,j)).getPiece().isVivante() &&
                        ((CaseBlanche) mat.getCase(i,j)).getPiece().isDame())

                    g.drawImage(this.dameN,j*TAILLEIM,i*TAILLEIM,null);
                else if (mat.getCase(i,j) instanceof CaseBlanche &&
                    ((CaseBlanche) mat.getCase(i,j)).getPiece().getCouleur() == 2 &&
                    //((CaseBlanche) mat.getCase(i,j)).getPiece().isVivante() &&
                    ((CaseBlanche) mat.getCase(i,j)).getPiece().isDame())

                    g.drawImage(this.dameB,j*TAILLEIM,i*TAILLEIM,null);
            }
        }
        repaint();

    }
    //permet de r�initiliser la variable de damier
    public void rafraichir(Plateau mat){
        this.mat=mat;
    }

}
