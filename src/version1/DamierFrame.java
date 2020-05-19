package version1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.Integer;

public class DamierFrame extends JFrame{

    //Les deux variables de taille de fenetre
//    private final static int LARGEUR=626;
//    private final static int HAUTEUR=674;50
    private final static int LARGEUR=1300;
    private final static int HAUTEUR=700;

    //damier
    private DamierPanel damPanel;
    private JPanel debug;
    private int phase=0;
    //fenetres des scores
    private JTextField scoreJ1,scoreJ2,nbPionJ1,nbPionJ2;
    //scores
    private int entScoreJ1,entScoreJ2,entNbPionJ1,entNbPionJ2;

    public int joueurAct;

    public DamierFrame(Plateau damier) {
        super();
        setSize(LARGEUR,HAUTEUR);
        setResizable(false);
        JPanel panel=new JPanel();
        panel.setLayout(new BorderLayout());
        //initialisation du damier compris dans la fenetre
        this.damPanel=new DamierPanel(damier);
        //damPanel.repaint();

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

        JTextField text5 = new JTextField("      A                  B                  C                  D                  E                  F                  G                  H                  I                  J");
        text5.setEditable(false);

        Box box1 = Box.createHorizontalBox();
        box1.add(text5);

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(0,1));
        p.add(new JLabel("1"));
        p.add(new JLabel("2"));
        p.add(new JLabel("3"));
        p.add(new JLabel("4"));
        p.add(new JLabel("5"));
        p.add(new JLabel("6"));
        p.add(new JLabel("7"));
        p.add(new JLabel("8"));
        p.add(new JLabel("9"));
        p.add(new JLabel("10"));

        String ACTION_KEY = "theAction";
        JButton pauseButton = new JButton("pause");
        Action actionListener = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                boolean b = Jeu.paused.get();

                Jeu.paused.set(!b);//set it to the opposite of what it was i.e paused to unpaused and vice versa

                if (pauseButton.getText().equals("Pause")) {
                    pauseButton.setText("Un-pause");
                } else {
                    pauseButton.setText("Pause");
                }
            }
        };

        //mis en place du button pause (SPACE)
        KeyStroke space = KeyStroke.getKeyStroke(' ');
        InputMap inputMap = pauseButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(space, ACTION_KEY);
        ActionMap actionMap = pauseButton.getActionMap();
        actionMap.put(ACTION_KEY, actionListener);
        pauseButton.setActionMap(actionMap);
        getContentPane().add(pauseButton);

        //initialisation finale de la fenetre
        panel.add(p,BorderLayout.CENTER);
        panel.add(scoreBox,BorderLayout.SOUTH);
        damPanel.add(box1);
        panel.add(damPanel,BorderLayout.WEST);

        debug = new JPanel();
        debug.setLayout(new GridLayout(0,5));

        panel.add(debug,BorderLayout.EAST);
        getContentPane().add(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public String giveIdx(int i){
        switch (i){
            case 0: return "A";
            case 1: return "B";
            case 2: return "C";
            case 3: return "D";
            case 4: return "E";
            case 5: return "F";
            case 6: return "G";
            case 7: return "H";
            case 8: return "I";
            case 9: return "J";
            default: return "X";
        }
    }

    public void miseAJourPhase(){
        phase++;
    }

    public void miseAJourPanelDebug(Case pred, Case suiv, boolean isPris, Case mange){

        String n = "Ph "+phase+": "+(pred.getLigne()+1)+giveIdx(pred.getColonne())+" -> "+(suiv.getLigne()+1)+giveIdx(suiv.getColonne());
        if (isPris)
            n += "(P "+ (mange.getLigne()+1)+giveIdx(mange.getColonne())+")";
        else
            n += "(D)";
        JTextField newText=new JTextField(n);
        if (joueurAct == 2)
            newText.setForeground(Color.GRAY);
        else
            newText.setForeground(Color.RED);

        newText.setEditable(false);
        debug.add(newText);
    }

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
        //damPanel.repaint();
    }
}