package version1;

import javax.swing.*;
import java.awt.*;
import java.lang.Integer;

public class DamierFrame extends JFrame{

    //Les deux variables de taille de fenetre
    private final static int LARGEUR=626;
    private final static int HAUTEUR=674;
    //damier
    DamierPanel damPanel;
    //fenetres des scores
    JTextField scoreJ1,scoreJ2,nbPionJ1,nbPionJ2;
    //scores
    int entScoreJ1,entScoreJ2,entNbPionJ1,entNbPionJ2;

    public DamierFrame(Plateau damier) {
        super();
        setSize(LARGEUR,HAUTEUR);
        setResizable(false);
        JPanel panel=new JPanel();
        panel.setLayout(new BorderLayout());
        //initialisation du damier compris dans la fenetre
        this.damPanel=new DamierPanel(damier);
        damPanel.repaint();

        Box scoreBox=Box.createHorizontalBox();
        //Initialisation des fenetres de score et des scores
        JTextField text1=new JTextField("Score blancs :");
        text1.setEditable(false);

        scoreJ1=new JTextField("0");
        scoreJ1.setEditable(false);
        entScoreJ1=0;

        JTextField text2=new JTextField("Score noirs :");
        text2.setEditable(false);

        scoreJ2=new JTextField("0");
        scoreJ2.setEditable(false);
        entScoreJ2=0;

        JTextField text3=new JTextField("Pions blancs :");
        text3.setEditable(false);

        nbPionJ1=new JTextField("20");
        nbPionJ1.setEditable(false);
        entNbPionJ1=20;

        JTextField text4=new JTextField("Pions noirs :");
        text4.setEditable(false);

        nbPionJ2=new JTextField("20");
        nbPionJ2.setEditable(false);
        entNbPionJ2=20;

        scoreBox.add(text1);
        scoreBox.add(scoreJ1);
        scoreBox.add(text2);
        scoreBox.add(scoreJ2);
        scoreBox.add(text3);
        scoreBox.add(nbPionJ1);
        scoreBox.add(text4);
        scoreBox.add(nbPionJ2);

        //initialisation finale de la fenetre
        panel.add(scoreBox,BorderLayout.SOUTH);
        panel.add(damPanel,BorderLayout.CENTER);
        getContentPane().add(panel);

    }
    //TODO faudrait pouvoir changer le score en choisissant le score à ajouter ou enlever pour pouvoir à chaque instant évéluer le plateau
    //enleve un point au score du joueur
    public void changeScoreJ1(){
        this.entScoreJ1+=1;
        scoreJ1.setText(Integer.toString(entScoreJ1));
    }

    //enleve un point au score de l'ordi
    public void changeScoreJ2(){
        this.entScoreJ2+=1;
        scoreJ2.setText(Integer.toString(entScoreJ2));
    }

    //enleve un pion au joueur
    public void changeNbPionJ1(){
        this.entNbPionJ1-=1;
        nbPionJ1.setText(Integer.toString(entNbPionJ1));
    }

    //enleve un pion a l'ordi
    public void changeNbPionJ2(){
        this.entNbPionJ2-=1;
        nbPionJ2.setText(Integer.toString(entNbPionJ2));
    }

    //permet de récupere le score du joueur
    public int getScoreJ1(){
        return this.entNbPionJ1+3*this.entScoreJ1;
    }

    //permet de récupérer le score de l'ordi
    public int getScoreJ2(){
        return this.entNbPionJ2+3*this.entScoreJ2;
    }

    //pour rafraichir le damier avec le nouveau tableau
    public void rafraichir(Plateau mat){
        damPanel.rafraichir(mat);
        damPanel.repaint();
    }
}