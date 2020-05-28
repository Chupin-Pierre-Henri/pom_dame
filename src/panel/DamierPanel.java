package panel;

import case_plateau.CaseBlanche;
import case_plateau.CaseNoire;
import plateau.Plateau;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;

public class DamierPanel extends JPanel {
    public static final int TAILLEIM = 62;
    //tableau de type damier qui permet l'afichage du damier graphique
    public Plateau mat;
    Image noir, blanc, pionN, pionB, dameB, dameN;

    public DamierPanel(Plateau mat) {

        this.mat = mat;
        Toolkit kit = Toolkit.getDefaultToolkit();
        MediaTracker tracker = new MediaTracker(this);
        this.blanc = kit.getImage("src/damier/blanc.jpg");
        this.noir = kit.getImage("src/damier/noir.jpg");
        this.pionB = kit.getImage("src/damier/pionB.jpg");
        this.pionN = kit.getImage("src/damier/pionN.jpg");
        this.dameB = kit.getImage("src/damier/dameB.jpg");
        this.dameN = kit.getImage("src/damier/dameN.jpg");
        tracker.addImage(blanc, 0);
        tracker.addImage(noir, 0);
        tracker.addImage(pionB, 0);
        tracker.addImage(pionN, 0);
        tracker.addImage(dameB, 0);
        tracker.addImage(dameN, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
        }
    }

    public void paintComponent(Graphics g) {

        int i, j, n = 25;

        for (i = 0; i < Plateau.NBCASES; i++) {
            for (j = 0; j < Plateau.NBCASES; j++) {
                if (mat.getCase(i, j) instanceof CaseNoire) {
                    g.drawImage(this.blanc, j * TAILLEIM, i * TAILLEIM + n, null);
                } else if (mat.getCase(i, j) instanceof CaseBlanche && ((CaseBlanche) mat.getCase(i, j)).isLibre()) {
                    g.drawImage(this.noir, j * TAILLEIM, i * TAILLEIM + n, null);
                } else if (mat.getCase(i, j) instanceof CaseBlanche &&
                    ((CaseBlanche) mat.getCase(i, j)).getPiece().getCouleur() == 2 &&
                    !((CaseBlanche) mat.getCase(i, j)).getPiece().isDame()) {
                    g.drawImage(this.pionB, j * TAILLEIM, i * TAILLEIM + n, null);
                } else if (mat.getCase(i, j) instanceof CaseBlanche &&
                    ((CaseBlanche) mat.getCase(i, j)).getPiece().getCouleur() == -2 &&
                    !((CaseBlanche) mat.getCase(i, j)).getPiece().isDame()) {
                    g.drawImage(this.pionN, j * TAILLEIM, i * TAILLEIM + n, null);
                } else if (mat.getCase(i, j) instanceof CaseBlanche &&
                    ((CaseBlanche) mat.getCase(i, j)).getPiece().getCouleur() == -2 &&
                    ((CaseBlanche) mat.getCase(i, j)).getPiece().isDame()) {
                    g.drawImage(this.dameN, j * TAILLEIM, i * TAILLEIM + n, null);
                } else if (mat.getCase(i, j) instanceof CaseBlanche &&
                    ((CaseBlanche) mat.getCase(i, j)).getPiece().getCouleur() == 2 &&
                    ((CaseBlanche) mat.getCase(i, j)).getPiece().isDame()) {
                    g.drawImage(this.dameB, j * TAILLEIM, i * TAILLEIM + n, null);
                }

            }
        }
        repaint();

    }

    public void rafraichir(Plateau mat) {
        this.mat = mat;
    }

}
