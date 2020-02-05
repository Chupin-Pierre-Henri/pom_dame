package version1;

import javax.swing.*;


public class FinFrame extends JFrame{

    public FinFrame(int joueur,int score1,int score2) {
        super();
        setSize(540,200);
        setResizable(false);
        FinPanel panel=new FinPanel(joueur,score1,score2);
        getContentPane().add(panel);
        show();
        panel.repaint();
    }
}
