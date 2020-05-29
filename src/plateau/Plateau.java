package plateau;


import case_plateau.Case;
import case_plateau.CaseBlanche;
import case_plateau.CaseNoire;
import frame.DamierFrame;
import piece.Piece;
import piece.Pion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static java.lang.Math.abs;

public class Plateau {
    public static final int NBCASES = 10;
    public DamierFrame frame;
    private boolean finPartie = false;
    private Case[][] cases;
    private Case[][] casesTmp;
    private Case[][] casestampon;
    private Piece pieceTmp1;
    private int nbCoupsSansPrise;
    private int nbRisque;
    private int coupsRestants;
    private ArrayList<Piece> pieces = new ArrayList<Piece>();

    public Plateau() {
        cases = new Case[10][10];
        casesTmp = new Case[10][10];
        casestampon = new Case[10][10];
        initialiserJeu();
    }

    /**
     * Permet de copier le plateau actuelle dans un plateau temporaire
     *
     * @param casesTmp plateau temporaire crée
     * @param cases    plateau qui va être copié
     */
    public void cloneCases(Case[][] casesTmp, Case[][] cases) {
        int i, j;

        for (i = 0; i < NBCASES; i++) {
            for (j = 0; j < NBCASES; j++) {
                casesTmp[i][j].copieCase(cases[i][j]);
            }
        }
    }

    /**
     * Initialise le Jeu en plaçant les pions blanc et noir à leurs place initial
     */
    public void initialiserJeu() {
        int i, j;
        int zoneBlancs = 4;
        int zoneNoires = 6;

        for (i = 0; i < NBCASES; i++) {
            for (j = 0; j < NBCASES; j++) {
                if (((i % 2 == 0) && (j % 2 == 0)) || ((i % 2 == 1) && (j % 2 == 1))) {
                    Case c = new CaseNoire(i, j);
                    setCase(cases, c);

                    Case cTmp = new CaseNoire(i, j);
                    setCase(casesTmp, cTmp);

                    Case cTampon = new CaseNoire(i, j);
                    setCase(casestampon, cTampon);

                } else if (((i % 2 == 0) && (j % 2 != 0)) || ((i % 2 != 0) && (j % 2 == 0))) {
                    if (i < zoneBlancs) {
                        Case c = new CaseBlanche(i, j);
                        Piece p = new Pion(c, 2);
                        pieces.add(p);
                        ((CaseBlanche) c).setPiece(p);
                        setCase(cases, c);

                        Case cTmp = new CaseBlanche(i, j);
                        Piece pTmp = new Pion(cTmp, 2);
                        ((CaseBlanche) cTmp).setPiece(pTmp);
                        setCase(casesTmp, cTmp);

                        Case cTampon = new CaseBlanche(i, j);
                        Piece pTampon = new Pion(cTampon, 2);
                        ((CaseBlanche) cTampon).setPiece(pTampon);
                        setCase(casestampon, cTmp);
                    } else if (i >= zoneNoires) {
                        Case c = new CaseBlanche(i, j);
                        Piece p = new Pion(c, -2);
                        pieces.add(p);
                        ((CaseBlanche) c).setPiece(p);
                        setCase(cases, c);

                        Case cTmp = new CaseBlanche(i, j);
                        Piece pTmp = new Pion(cTmp, -2);
                        ((CaseBlanche) cTmp).setPiece(pTmp);
                        setCase(casesTmp, cTmp);

                        Case cTampon = new CaseBlanche(i, j);
                        Piece pTampon = new Pion(cTampon, -2);
                        ((CaseBlanche) cTampon).setPiece(pTampon);
                        setCase(casestampon, cTmp);
                    } else {
                        Case c = new CaseBlanche(i, j);
                        setCase(cases, c);

                        Case cTmp = new CaseBlanche(i, j);
                        setCase(casesTmp, cTmp);

                        Case cTampon = new CaseBlanche(i, j);
                        setCase(casestampon, cTampon);
                    }
                }
            }
        }
    }

    /**
     * Déplace un piece p vers une nouvelle position
     *
     * @param p     la pièce qui va être déplacé
     * @param iCour l'indice i où se trouve la pièce à déplacer
     * @param jCour l'indice j où se trouve la pièce à déplacer
     * @param iNew  l'indice i de la nouvelle position de la pièce à déplacer
     * @param jNew  l'indice j de la nouvelle position de la pièce à déplacer
     */
    public void deplacePiece(Piece p, int iCour, int jCour, int iNew, int jNew) {

        frame.miseAJourPanelDebug(cases[iCour][jCour], cases[iNew][jNew], false, null);

        // if (!Jeu.paused.get()){
        ((CaseBlanche) cases[iNew][jNew]).setPiece(((CaseBlanche) cases[iCour][jCour]).getPiece());
        ((CaseBlanche) cases[iCour][jCour]).setPiece(null);
        p.setPosition(cases[iNew][jNew]);

        //crée la dame en si arrivé sur la dernière ligne ou première ligne en fonction du camp du pion
        if (p.getCouleur() == 2 && p.getPosition().getLigne() == 9 && !p.isDame()) {
            p.setDames(true);
        } else if (p.getCouleur() == -2 && p.getPosition().getLigne() == 0 && !p.isDame()) {
            p.setDames(true);
        }
        // }
    }

    /**
     * Prend un pièce adversaire normalement mais si le paramètre verification est présent alors il le simule grâce à pièceTmp et caseTmp
     *
     * @param p            la pièce qui prend
     * @param iEnemi       l'indice i où se trouve l'adversaire
     * @param jEnemi       l'indice j où se trouve l'adversaire
     * @param iNew         le nouvel indice i de notre pièce p
     * @param jNew         le nouvel indice j de notre pièce p
     * @param verification si il est false alors fait la prise classique sinon simule la prise avec pieceTmp et casTmp
     */
    public void prendsPiece(Piece p, int iEnemi, int jEnemi, int iNew, int jNew, boolean verification) {

        //if (!Jeu.paused.get()){
        if (!verification) {
            frame.miseAJourPanelDebug(p.getPosition(), cases[iNew][jNew], true, cases[iEnemi][jEnemi]);

            pieces.remove(((CaseBlanche) cases[iEnemi][jEnemi]).getPiece());
            ((CaseBlanche) cases[iEnemi][jEnemi]).setPiece(null);
            ((CaseBlanche) cases[p.getPosition().getLigne()][p.getPosition().getColonne()]).setPiece(null);
            ((CaseBlanche) cases[iNew][jNew]).setPiece(p);
            p.setPosition(cases[iNew][jNew]);

            if (p.getCouleur() == 2 && p.getPosition().getLigne() == 9 && !p.isDame()) {
                p.setDames(true);
            } else if (p.getCouleur() == -2 && p.getPosition().getLigne() == 0 && !p.isDame()) {
                p.setDames(true);
            }

            if (p.getCouleur() == 2) {
                frame.changeNbPionJ2();
                frame.changeScoreJ1();
            } else {
                frame.changeNbPionJ1();
                frame.changeScoreJ2();
            }
        } else {
            ((CaseBlanche) casesTmp[iEnemi][jEnemi]).setPiece(null);
            ((CaseBlanche) casesTmp[pieceTmp1.getPosition().getLigne()][pieceTmp1.getPosition().getColonne()]).setPiece(null);
            pieceTmp1.setPosition(casesTmp[iNew][jNew]);
            ((CaseBlanche) casesTmp[iNew][jNew]).setPiece(pieceTmp1);
        }
        verificationDautresCoups(p, verification);
        // }
    }

    /**
     * Permet de vérifier si un pièce peut prendre plusieurs pièces en un coups
     *
     * @param p            la pièce que l'on vérifie
     * @param verification si il est à false alors c'est qu'on fait un vrai vérification avec la pièce p sinon si il est à true alors on utilise pieceTmp pour simuler la vérification
     */
    public void verificationDautresCoups(Piece p, boolean verification) {
        int i, j;
        if (!verification) {
            i = p.getPosition().getLigne();
            j = p.getPosition().getColonne();
        } else {
            i = pieceTmp1.getPosition().getLigne();
            j = pieceTmp1.getPosition().getColonne();
        }
        prise_piece(p, i, j, 1, verification);
    }

    /**
     * vérifie si la prise est possible pour des positions donné soit de façon concraite soit simuler avec pieceTmp
     *
     * @param coul         la couleur de la pièce qui veux prendre
     * @param iEnemi       l'indice i de la pièce adverse
     * @param jEnemi       l'indice j de la pièce adverse
     * @param iNew         l'indice i vers le qu'elle la pièce irait si elle prenait la pièce adverse
     * @param jNew         l'indice j vers le qu'elle la pièce irait si elle prenait la pièce adverse
     * @param verification si false alors on teste avec des pièces concraite sinon on simule avec pieceTmp
     * @return
     */
    public boolean peutPrendre(int coul, int iEnemi, int jEnemi, int iNew, int jNew, boolean verification) {

        if (!verification) {
            return !((CaseBlanche) cases[iEnemi][jEnemi]).isLibre() &&
                ((CaseBlanche) cases[iEnemi][jEnemi]).getPiece().getCouleur() != coul &&
                ((CaseBlanche) cases[iNew][jNew]).isLibre();
        } else {
            return !((CaseBlanche) casesTmp[iEnemi][jEnemi]).isLibre() &&
                ((CaseBlanche) casesTmp[iEnemi][jEnemi]).getPiece().getCouleur() != coul &&
                ((CaseBlanche) casesTmp[iNew][jNew]).isLibre();
        }
    }

    /**
     * liste les coups obligatoires (les prises) pour les pions
     *
     * @param piecesPrioritaires la liste des pièces prioritaires à jouer
     * @param i                  l'indice i de la pièce tester
     * @param j                  l'indice j de la pièce tester
     * @param p                  la pièce tester
     */
    public void calculCoupObligatoirePion(ArrayList<Piece> piecesPrioritaires, int i, int j, Piece p) {
        ArrayList<Piece> piecesVisees = new ArrayList<Piece>();

        //en haut à droite
        if (posPossible(i, j, 1, 1, 1) && peutPrendre(p.getCouleur(), i + 1, j + 1, i + 2, j + 2, false)
            && !piecesVisees.contains((((CaseBlanche) cases[i + 1][j + 1]).getPiece()))) {
            piecesPrioritaires.add(p);
            piecesVisees.add(((CaseBlanche) cases[i + 1][j + 1]).getPiece());
            //coupsRestants--;
        }
        //en haut à gauche
        else if (posPossible(i, j, 1, 2, 1) && peutPrendre(p.getCouleur(), i + 1, j - 1, i + 2, j - 2, false)
            && !piecesVisees.contains(((CaseBlanche) cases[i + 1][j - 1]).getPiece())) {
            piecesPrioritaires.add(p);
            piecesVisees.add(((CaseBlanche) cases[i + 1][j - 1]).getPiece());
            //coupsRestants--;
        }
        //en bas à droite
        else if (posPossible(i, j, 1, 3, 1) && peutPrendre(p.getCouleur(), i - 1, j + 1, i - 2, j + 2, false)
            && !piecesVisees.contains(((CaseBlanche) cases[i - 1][j + 1]).getPiece())) {
            piecesPrioritaires.add(p);
            piecesVisees.add(((CaseBlanche) cases[i - 1][j + 1]).getPiece());
            //coupsRestants--;
        }
        //en bas à gauche
        else if (posPossible(i, j, 1, 4, 1) && peutPrendre(p.getCouleur(), i - 1, j - 1, i - 2, j - 2, false)
            && !piecesVisees.contains(((CaseBlanche) cases[i - 1][j - 1]).getPiece())) {
            piecesPrioritaires.add(p);
            piecesVisees.add(((CaseBlanche) cases[i - 1][j - 1]).getPiece());
            //coupsRestants--;
        }

        Iterator it = piecesVisees.iterator();

        while (it.hasNext()) {
            Piece pion = (Piece) it.next();
        }
    }

    /**
     * liste les coups obligatoires (les prises) pour les dames
     *
     * @param piecesPrioritaires la liste des pièces prioritaires à jouer
     * @param i                  l'indice i de la pièce tester
     * @param j                  l'indice j de la pièce tester
     * @param p                  la pièce tester
     */
    public void calculCoupObligatoireDame(ArrayList<Piece> piecesPrioritaires, int i, int j, Piece p) {
        ArrayList<Piece> piecesVisees = new ArrayList<Piece>();

        for (int x = 1; x <= 3; x++) {
            //le coté haut droite +i et +j
            if (posPossible(i, j, x, 1, 1) && peutPrendre(p.getCouleur(), i + x, j + x, i + (x + 1), j + (x + 1), false)
                && !piecesVisees.contains((((CaseBlanche) cases[i + x][j + x]).getPiece()))) {
                piecesPrioritaires.add(p);
                piecesVisees.add(((CaseBlanche) cases[i + x][j + x]).getPiece());
                //coupsRestants--;
                break;
            }
            //le coté haut gauche +i et -j
            else if (posPossible(i, j, x, 2, 1) && peutPrendre(p.getCouleur(), i + x, j - x, i + (x + 1), j - (x + 1), false)
                && !piecesVisees.contains(((CaseBlanche) cases[i + x][j - x]).getPiece())) {
                piecesPrioritaires.add(p);
                piecesVisees.add(((CaseBlanche) cases[i + x][j - x]).getPiece());
                //coupsRestants--;
                break;
            }
            //le coté bas droite -i et +j
            else if (posPossible(i, j, x, 3, 1) && peutPrendre(p.getCouleur(), i - x, j + x, i - (x + 1), j + (x + 1), false)
                && !piecesVisees.contains(((CaseBlanche) cases[i - x][j + x]).getPiece())) {
                piecesPrioritaires.add(p);
                piecesVisees.add(((CaseBlanche) cases[i - x][j + x]).getPiece());
                //coupsRestants--;
                break;
            }
            //le coté bas gauche -i et -j
            else if (posPossible(i, j, x, 4, 1) && peutPrendre(p.getCouleur(), i - x, j - x, i - (x + 1), j - (x + 1), false)
                && !piecesVisees.contains(((CaseBlanche) cases[i - x][j - x]).getPiece())) {
                piecesPrioritaires.add(p);
                piecesVisees.add(((CaseBlanche) cases[i - x][j - x]).getPiece());
                //coupsRestants--;
                break;
            }
        }
    }

    /**
     * effectue les coups obligatoires à faire à chaque début de tour
     *
     * @param piecesPrioritaires la liste des pièces prioritaire à jouer
     * @param joueur             si 2 alors c'est el joueur des blancs si -2 alors c'est le joueur des noirs
     * @param strategie          la stratégie utilisé ici nous utilison la 1
     */
    public void coupObligatoire(ArrayList<Piece> piecesPrioritaires, int joueur, int strategie) {
        Iterator it = pieces.iterator();

        while (it.hasNext() && coupsRestants > 0) {
            Piece p = (Piece) it.next();
            int i = p.getPosition().getLigne();
            int j = p.getPosition().getColonne();

            //on fait la différence entre les pièces classique et les dames
            //calcul des coups obligatoires
            if (joueur == p.getCouleur()) {
                if (!p.isDame()) {
                    calculCoupObligatoirePion(piecesPrioritaires, i, j, p);
                } else {
                    calculCoupObligatoireDame(piecesPrioritaires, i, j, p);
                }
            }
        }

        Iterator it1 = piecesPrioritaires.iterator();
        //On procede a l'action, on realise les  coups obligatoires
        it1 = piecesPrioritaires.iterator();
        while (it1.hasNext()) {
            Piece p = (Piece) it1.next();
            int i = p.getPosition().getLigne();
            int j = p.getPosition().getColonne();

            if (!p.isDame()) {
                if (joueur == p.getCouleur()) {
                    if ((posPossible(i, j, 1, 1, 1) && peutPrendre(p.getCouleur(), i + 1, j + 1, i + 1 + 1, j + 1 + 1, false)) || (posPossible(i, j, 1, 2, 1) && peutPrendre(p.getCouleur(), i + 1, j - 1, i + 1 + 1, j - 1 - 1, false)) || (posPossible(i, j, 1, 3, 1) && peutPrendre(p.getCouleur(), i - 1, j + 1, i - 1 - 1, j + 1 + 1, false)) || (posPossible(i, j, 1, 4, 1) && peutPrendre(p.getCouleur(), i - 1, j - 1, i - 1 - 1, j - 1 - 1, false))) {
                        coupsRestants--;
                        switch (strategie) {
                            case 0:
                                prise_piece(p, i, j, 1, false);
                                break;
                            case 1:
                                calculMeilleurCoup(i, j, p, 1);
                                break;
                        }
                    }
                }
            } else {
                for (int x = 1; x <= 3; x++) {
                    if (joueur == p.getCouleur()) {
                        if ((posPossible(i, j, x, 1, 1) && peutPrendre(p.getCouleur(), i + x, j + x, i + 1 + x, j + 1 + x, false)) || (posPossible(i, j, x, 2, 1) && peutPrendre(p.getCouleur(), i + x, j - x, i + 1 + x, j - 1 - x, false)) || (posPossible(i, j, x, 3, 1) && peutPrendre(p.getCouleur(), i - x, j + x, i - 1 - x, j + 1 + x, false)) || (posPossible(i, j, x, 4, 1) && peutPrendre(p.getCouleur(), i - x, j - x, i - 1 - x, j - 1 - x, false))) {
                            coupsRestants--;
                            switch (strategie) {
                                case 0:
                                    prise_piece(p, i, j, x, false);
                                    break;
                                case 1:
                                    calculMeilleurCoup(i, j, p, x);
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * on calcul le meilleur coup possible à jouer, le system de points est
     * Nb de ennemis pris:
     * - si egalite, alors on met en priorite l'avancement vers le territoir de l'enemi que le retour
     * - si 2 pions amis peuvent prendre le meme ennemi, on prioritise celui qui mange plus ou qui ne se fait pas mange
     *
     * @param i l'indice i de la pièce p
     * @param j l'indice j de la pièce p
     * @param p la pièce p pour la qu'elle on calcul son meilleur coup
     * @param c la distance à parcouru pour la prise, pour les pions c'est toujours 1 et pour les dames c'est de 1 à 3
     */
    public void calculMeilleurCoup(int i, int j, Piece p, int c) {
        //la cle est la type de position sur la grille(1,2,3 ou 4) et la valeurs est le nb de pions manges
        HashMap<Integer, Integer> scorePieces = new HashMap<Integer, Integer>();
        pieceTmp1 = new Piece(p);
        cloneCases(casesTmp, cases);
        if (posPossible(i, j, c, 1, 1) && peutPrendre(p.getCouleur(), i + c, j + c, i + 1 + c, j + 1 + c, false)) {
            verificationDautresCoups(p, true);
            scorePieces.put(1, p.getPionsManges());
            p.resetPionsManges();
            ((CaseBlanche) casesTmp[pieceTmp1.getPosition().getLigne()][pieceTmp1.getPosition().getColonne()]).setPiece(null);
            pieceTmp1.copiePiece(p);
        }
        //le coté haut gauche +i et -j
        if (posPossible(i, j, c, 2, 1) && peutPrendre(p.getCouleur(), i + c, j - c, i + 1 + c, j - 1 - c, false)) {
            verificationDautresCoups(p, true);
            scorePieces.put(2, p.getPionsManges());
            p.resetPionsManges();
            ((CaseBlanche) casesTmp[pieceTmp1.getPosition().getLigne()][pieceTmp1.getPosition().getColonne()]).setPiece(null);
            pieceTmp1.copiePiece(p);
        }
        //le coté bas droite -i et +j
        if (posPossible(i, j, c, 3, 1) && peutPrendre(p.getCouleur(), i - c, j + c, i - 1 - c, j + 1 + c, false)) {
            verificationDautresCoups(p, true);
            scorePieces.put(3, p.getPionsManges());
            p.resetPionsManges();
            ((CaseBlanche) casesTmp[pieceTmp1.getPosition().getLigne()][pieceTmp1.getPosition().getColonne()]).setPiece(null);
            pieceTmp1.copiePiece(p);
        }
        //le coté bas gauche -i et -j
        if (posPossible(i, j, c, 4, 1) && peutPrendre(p.getCouleur(), i - c, j - c, i - 1 - c, j - 1 - c, false)) {
            verificationDautresCoups(p, true);
            scorePieces.put(4, p.getPionsManges());
            p.resetPionsManges();
            ((CaseBlanche) casesTmp[pieceTmp1.getPosition().getLigne()][pieceTmp1.getPosition().getColonne()]).setPiece(null);
            pieceTmp1.copiePiece(p);
        }
        cloneCases(casesTmp, cases);

        int posChoisi;
        int coupInit = 0;
        int posInit = 0;

        for (int firstVal : scorePieces.keySet()) {
            posInit = firstVal;
            coupInit = scorePieces.get(posInit);
            break;
        }
        posChoisi = posInit;

        if (scorePieces.size() > 1) {
            //Recherche du coup max dans les scores
            ArrayList<Integer> lesMaxInd = new ArrayList<Integer>();

            for (int key : scorePieces.keySet()) {
                if (coupInit < scorePieces.get(key)) {
                    coupInit = scorePieces.get(key);
                    posInit = key;
                }
            }

            lesMaxInd.add(posInit);
            for (int key : scorePieces.keySet()) {
                if ((scorePieces.get(key) == coupInit) && (key != posInit)) {
                    lesMaxInd.add(key);
                }
            }

            if (lesMaxInd.size() > 1) {
                Random r = new Random();
                int randInd = r.nextInt((lesMaxInd.size() - 1 - 0) + 1) + 0;
                posChoisi = lesMaxInd.get(randInd);
            } else {
                posChoisi = lesMaxInd.get(0);
            }
        }

        switch (posChoisi) {
            case 1:
                prendsPiece(p, i + c, j + c, i + 1 + c, j + 1 + c, false);
                break;
            case 2:
                prendsPiece(p, i + c, j - c, i + 1 + c, j - 1 - c, false);
                break;
            case 3:
                prendsPiece(p, i - c, j + c, i - 1 - c, j + 1 + c, false);
                break;
            case 4:
                prendsPiece(p, i - c, j - c, i - 1 - c, j - 1 - c, false);
                break;
            default:
                break;
        }

    }

    /**
     * permet de faire un prise de pièce et de compter le nombre de pièce manger par la pièce p
     *
     * @param p            la pièce p qui mange
     * @param i            l'indice i de la pièce p
     * @param j            l'indice j de la pièce p
     * @param c            la distance à parcouru pour la prise, pour les pions c'est toujours 1 et pour les dames c'est de 1 à 3
     * @param verification si false alors on est sur un vrai prise et si true alors on simule avec pieceTmp afin de compter combien de pions un pièce peu prendre pour le comparer aux autres pièces
     */
    private void prise_piece(Piece p, int i, int j, int c, boolean verification) {
        if (posPossible(i, j, c, 1, 1) && peutPrendre(p.getCouleur(), i + c, j + c, i + 1 + c, j + 1 + c, verification)) {
            p.addPionsManges();
            prendsPiece(p, i + c, j + c, i + 1 + c, j + 1 + c, verification);
        }
        //le coté haut gauche +i et -j
        else if (posPossible(i, j, c, 2, 1) && peutPrendre(p.getCouleur(), i + c, j - c, i + 1 + c, j - 1 - c, verification)) {
            p.addPionsManges();
            prendsPiece(p, i + c, j - c, i + 1 + c, j - 1 - c, verification);
        }
        //le coté bas droite -i et +j
        else if (posPossible(i, j, c, 3, 1) && peutPrendre(p.getCouleur(), i - c, j + c, i - 1 - c, j + 1 + c, verification)) {
            p.addPionsManges();
            prendsPiece(p, i - c, j + c, i - 1 - c, j + 1 + c, verification);
        }
        //le coté bas gauche -i et -j
        else if (posPossible(i, j, c, 4, 1) && peutPrendre(p.getCouleur(), i - c, j - c, i - 1 - c, j - 1 - c, verification)) {
            p.addPionsManges();
            prendsPiece(p, i - c, j - c, i - 1 - c, j - 1 - c, verification);
        }
    }

    /**
     * @param i     la ligne i de notre pièce
     * @param j     la colone j de notre pièce
     * @param x     le nombre de case qu'on veut ce déplacer
     * @param pos   si c'est pour en haut à gauche ou en haut à droite...
     * @param prise si c'est pour une prise 1 sinon 0
     * @return true si la position d'arriver de la pièce est dans le plateau ou à l'extérieur
     */
    public boolean posPossible(int i, int j, int x, int pos, int prise) {
        if (pos == 1 && i + x + prise < NBCASES && j + x + prise < NBCASES) {
            return true;
        } else if (pos == 2 && i + x + prise < NBCASES && j - x - prise >= 0) {
            return true;
        } else if (pos == 3 && i - x - prise >= 0 && j + x + prise < NBCASES) {
            return true;
        } else {
            return pos == 4 && i - x - prise >= 0 && j - x - prise >= 0;
        }
    }

    /**
     * Change la position de façon temporaire d'une pièce
     *
     * @param p    la pièce qu'on change de position dans le plateau temporaire
     * @param iNew le nouvelle indice i de la pièce déplacé
     * @param jNew le nouvelle indice j de la pièce déplacé
     */
    public void change_Pos_Tmp(Piece p, int iNew, int jNew) {

        ((CaseBlanche) casesTmp[p.getPosition().getLigne()][p.getPosition().getColonne()]).setPiece(null);
        ((CaseBlanche) casesTmp[iNew][jNew]).setPiece(pieceTmp1);
    }


    /**
     * Une fois les coups obligatoires effectué si l'on peut encore effectuer des déplacements alors on regarde les qu'elle faire et on vérifie si ils sont risqué ou non avant de les jouer
     *
     * @param piecesPrioritaires liste des pièces prioritaires à déplacer
     * @param piecesRestantes    liste des pièces qui ne sont pas prioritaires
     * @param joueur             le joueur qui joue 2 pour les blancs et -2 pour les noirs
     * @param cherche_risque     es ce que l'on regarde si le coup est risqué ou non si à false c'est qu'on à encore rien jouer et que tous les coups sont risqués donc qu'il faut jouer un coup risqué au minimum
     * @param pionsABouger       nombre de pions que l'on peut déplacer en tout à chaque coups
     */
    public void choixPieceADeplacer(ArrayList<Piece> piecesPrioritaires, ArrayList<Piece> piecesRestantes, int joueur, boolean cherche_risque, int pionsABouger) {
        Iterator it = piecesRestantes.iterator();
        boolean jouer = true;
        if (coupsRestants != pionsABouger && !cherche_risque) {
            jouer = false;
        } else {
            cloneCases(casesTmp, cases);
            nbRisque = coup_risquer(-1, -1, joueur, -1, true, 0, -1, -1);
        }
        while (it.hasNext() && coupsRestants > 0 && jouer) {
            Piece p = (Piece) it.next();
            int i = p.getPosition().getLigne();
            int j = p.getPosition().getColonne();

            HashMap<Integer, Integer> possibilites = new HashMap<Integer, Integer>();
            if (p.getCouleur() == joueur) {
                cloneCases(casesTmp, cases);
                nbRisque = coup_risquer(-1, -1, joueur, -1, true, 0, -1, -1);
                if (!p.isDame()) {
                    if (p.getCouleur() == joueur && joueur == 2) {
                        //cas 1
                        if (j < NBCASES - 1 && i < NBCASES - 1 && ((CaseBlanche) cases[i + 1][j + 1]).isLibre()) {
                            pieceTmp1 = new Piece(p);
                            cloneCases(casesTmp, cases);
                            change_Pos_Tmp(pieceTmp1, i + 1, j + 1);
                            if (cherche_risque && nbRisque >= coup_risquer(i + 1, j + 1, p.getCouleur(), 4, true, 0, i, j)) {
                                possibilites.put(1, 1);
                            } else if (!cherche_risque) {
                                possibilites.put(1, 1);
                            }
                        }
                        //cas 2
                        if (j > 0 && i < NBCASES - 1 && ((CaseBlanche) cases[i + 1][j - 1]).isLibre()) {
                            pieceTmp1 = new Piece(p);
                            cloneCases(casesTmp, cases);
                            change_Pos_Tmp(pieceTmp1, i + 1, j - 1);
                            if (cherche_risque && nbRisque >= coup_risquer(i + 1, j - 1, p.getCouleur(), 3, true, 0, i, j)) {
                                possibilites.put(2, 1);
                            } else if (!cherche_risque) {
                                possibilites.put(2, 1);
                            }
                        }
                    } else if (p.getCouleur() == joueur && joueur == -2) {
                        //cas 3
                        if (i > 0 && j < NBCASES - 1 && ((CaseBlanche) cases[i - 1][j + 1]).isLibre()) {
                            pieceTmp1 = new Piece(p);
                            cloneCases(casesTmp, cases);
                            change_Pos_Tmp(pieceTmp1, i - 1, j + 1);
                            if (cherche_risque && nbRisque >= coup_risquer(i - 1, j + 1, p.getCouleur(), 2, true, 0, i, j)) {
                                possibilites.put(3, 1);
                            } else if (!cherche_risque) {
                                possibilites.put(3, 1);
                            }
                        }
                        //cas 4
                        if (j > 0 && i > 0 && ((CaseBlanche) cases[i - 1][j - 1]).isLibre()) {
                            pieceTmp1 = new Piece(p);
                            cloneCases(casesTmp, cases);
                            change_Pos_Tmp(pieceTmp1, i - 1, j - 1);
                            if (cherche_risque && nbRisque >= coup_risquer(i - 1, j - 1, p.getCouleur(), 1, true, 0, i, j)) {
                                possibilites.put(4, 1);
                            } else if (!cherche_risque) {
                                possibilites.put(4, 1);
                            }
                        }
                    }
                } else {
                    for (int x = 1; x <= 3; x++) {
                        //le coté haut droite +i et +j
                        if (posPossible(i, j, x, 1, 0) && ((CaseBlanche) cases[i + x][j + x]).isLibre()) {
                            if (cherche_risque && nbRisque >= coup_risquer(i + x, j + x, p.getCouleur(), 4, true, 0, i, j)) {
                                possibilites.put(1, x);
                            } else if (!cherche_risque) {
                                possibilites.put(1, x);
                            }
                        }
                        //le coté haut gauche +i et -j
                        else if (posPossible(i, j, x, 2, 0) && ((CaseBlanche) cases[i + x][j - x]).isLibre()) {
                            if (cherche_risque && nbRisque >= coup_risquer(i + x, j - x, p.getCouleur(), 3, true, 0, i, j)) {
                                possibilites.put(2, x);
                            } else if (!cherche_risque) {
                                possibilites.put(2, x);
                            }
                        }
                        //le coté bas droite -i et +j
                        if (posPossible(i, j, x, 3, 0) && ((CaseBlanche) cases[i - x][j + x]).isLibre()) {
                            if (cherche_risque && nbRisque >= coup_risquer(i - x, j + x, p.getCouleur(), 2, true, 0, i, j)) {
                                possibilites.put(3, x);
                            } else if (!cherche_risque) {
                                possibilites.put(3, x);
                            }
                        }
                        //le coté bas gauche -i et -j
                        else if (posPossible(i, j, x, 4, 0) && ((CaseBlanche) cases[i - x][j - x]).isLibre()) {
                            if (cherche_risque && nbRisque >= coup_risquer(i - x, j - x, p.getCouleur(), 1, true, 0, i, j)) {
                                possibilites.put(4, x);
                            } else if (!cherche_risque) {
                                possibilites.put(4, x);
                            }
                        }
                    }
                }
                //fin pion
                if (possibilites.size() != 0) {
                    //on gere les cas quand le pion peut choisir de ne pas rester bloquer
                    Random r = new Random();
                    int randInd = 0;
                    if (possibilites.size() > 1) {
                        Iterator itPos = possibilites.entrySet().iterator();
                        while (itPos.hasNext()) {
                            Map.Entry key = (Map.Entry) itPos.next();
                            int k = (int) key.getKey();

                            if (k == 1 || k == 3) {
                                if (j + (int) key.getValue() == 9) {
                                    possibilites.remove(key);
                                }
                            } else if (k == 2 || k == 4) {
                                if (j - ((Integer) key.getValue()) == 0) {
                                    possibilites.remove(key.getKey());
                                }
                            }
                        }
                        randInd = r.nextInt((possibilites.size() - 1 - 0) + 1) + 0;
                    }
                    int xChoisi = 1;
                    int posChoisi = 0;
                    int idx = 0;

                    Iterator itPos = possibilites.entrySet().iterator();
                    while (itPos.hasNext()) {
                        Map.Entry key = (Map.Entry) itPos.next();
                        if (possibilites.size() > 1) {
                            if (randInd == idx) {
                                posChoisi = (int) key.getKey();
                                xChoisi = possibilites.get(key.getKey());
                            } else {
                                idx++;
                            }
                        } else {
                            xChoisi = (int) key.getValue();
                            posChoisi = (int) key.getKey();
                            break;
                        }
                    }
                    piecesPrioritaires.add(p);
                    actionPiece(p, i, j, xChoisi, posChoisi);
                    //jouer = false;
                    if (!cherche_risque) {
                        jouer = false;
                    }
                }
            }
        }
    }

    /**
     * on regarde combien de pièce sont en position risqué pour un position de plateau donnée, si on simule un déplacement de pièce alors on utilise sa nouvelle position dans les paremètres
     *
     * @param i            la position i ou on ce retrouve après avoir jouer le coup
     * @param j            la position j ou on ce retrouve après avoir jouer le coup
     * @param coul         la couleur de notre pion
     * @param pos          la position d'où l'on vient avec le pion
     * @param verification permet de rappeller la fonction pour savoir si on joue et si on ce fait prendre es ce que l'on peut reprendre la pièce adversaire et donc ne pas être coup risquer
     * @return Le nombre de pièce en position risqué
     */
    int coup_risquer(int i, int j, int coul, int pos, boolean verification, int nbPrise, int non_i, int non_j) {
        int nbRisqueTmp = 0;
        //ArrayList<Boolean> posPossible = new ArrayList<Boolean>();
        Iterator itbis = pieces.iterator();
        cloneCases(casestampon, casesTmp);
        //risquer = pos_risquer(i, j, coul, pos, verification, nbPrise);
        while (itbis.hasNext()) {
            Piece pbis = (Piece) itbis.next();
            int ibis2 = pbis.getPosition().getLigne();
            int jbis2 = pbis.getPosition().getColonne();
            if (ibis2 == non_i && jbis2 == non_j) {
                ibis2 = i;
                jbis2 = j;
            }
            if (pbis.getCouleur() == coul) {
                if (verification && pos_risquer(ibis2, jbis2, coul, -1, verification, nbPrise)) {
                    nbRisqueTmp++;
                }
                cloneCases(casesTmp, casestampon);
            }
        }
        return nbRisqueTmp;
    }

    /**
     * on regarde si la pièce p est contenue dans casesTmp
     *
     * @param p la pièces à tester
     * @return true si la pièces et contenue dans casesTmp
     */
    boolean containePiecetmp(Piece p) {
        boolean containe = false;
        if (((CaseBlanche) casesTmp[p.getPosition().getLigne()][p.getPosition().getColonne()]).getPiece() != null) {
            containe = true;
        }
        return containe;
    }

    /**
     * calcul pour une pièce si elle est en position de risque où non cet fonction est récursive car on teste si une fois que l'adversaire nous à pris si on peut le reprendre et donc annulé le risque du coup
     *
     * @param i            la position i de la pièce
     * @param j            la position j de la pièce
     * @param coul         la couleur de notre pion
     * @param pos          la position d'ou l'on vient avec le pion
     * @param verification permet de rappeller la fonction pour savoir si on joue et si on ce fait prendre es ce que l'on peut reprendre la pièce adversaire et donc ne pas être coup risquer
     * @param nbPrise      permet de savoir combien de pièce l'adversaire peut nous prendre et permet de le comparer au nombre de pièce que l'on peut lui prendre en retour
     * @return true si on peut ce faire prendre sans reprendre derrière par l'adversaire et donc évité au maximum de jouer ce coup
     */
    boolean pos_risquer(int i, int j, int coul, int pos, boolean verification, int nbPrise) {
        boolean risquer = false;
        int scorePrise;
        int pl;
        //ArrayList<Boolean> posPossible = new ArrayList<Boolean>();
        Iterator it = pieces.iterator();
        while (it.hasNext() && !risquer) {
            Piece p = (Piece) it.next();
            if (containePiecetmp((p))) {
                int ibis = p.getPosition().getLigne();
                int jbis = p.getPosition().getColonne();
                if (abs(i - ibis) == 1 && abs(j - jbis) == 1 && p.getCouleur() != coul) {
                    pl = 0;
                    pieceTmp1 = new Piece(p);
                    //if (verification){
                    //    pieceTmp1 = new Piece(p);
                    //}
                    //else{
                    //    pieceTmp2 = new Piece(p);
                    //}
                    if (p.isDame()) {
                        for (int dep = 1; dep <= 3; dep++) {
                            if (posPossible(ibis, jbis, dep, 1, 1) && (peutPrendre(p.getCouleur(), i, j, i + 1, j + 1, false) || pos == 1)) {
                                if ((i - ibis) == 1 && (j - jbis) == 1) {
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if (verification) {
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise) {
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i + 1, j + 1, p.getCouleur(), 1, false, scorePrise) && pos_risquer(i + 2, j + 2, p.getCouleur(), 1, false, scorePrise) && pos_risquer(i + 3, j + 3, p.getCouleur(), 1, false, scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if ((i - ibis) == 2 && (j - jbis) == 2) {
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if (verification) {
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise) {
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i + 1, j + 1, p.getCouleur(), 1, false, scorePrise) && pos_risquer(i + 2, j + 2, p.getCouleur(), 1, false, scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if ((i - ibis) == 3 && (j - jbis) == 3) {
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if (verification) {
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise) {
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i + 1, j + 1, p.getCouleur(), 1, false, scorePrise)) {
                                        risquer = false;
                                    }
                                }
                            } else if (posPossible(ibis, jbis, dep, 2, 1) && (peutPrendre(p.getCouleur(), i, j, i + 1, j - 1, false) || pos == 2)) {
                                if ((i - ibis) == 1 && (j - jbis) == -1) {
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if (verification) {
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise) {
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i + 1, j - 1, p.getCouleur(), 1, false, scorePrise) && pos_risquer(i + 2, j - 2, p.getCouleur(), 1, false, scorePrise) && pos_risquer(i + 3, j - 3, p.getCouleur(), 1, false, scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if ((i - ibis) == 2 && (j - jbis) == -2) {
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if (verification) {
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise) {
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i + 1, j - 1, p.getCouleur(), 1, false, scorePrise) && pos_risquer(i + 2, j - 2, p.getCouleur(), 1, false, scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if ((i - ibis) == 3 && (j - jbis) == -3) {
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if (verification) {
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise) {
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i + 1, j - 1, p.getCouleur(), 1, false, scorePrise)) {
                                        risquer = false;
                                    }
                                }
                            } else if (posPossible(ibis, jbis, dep, 3, 1) && (peutPrendre(p.getCouleur(), i, j, i - 1, j + 1, false) || pos == 3)) {
                                if ((i - ibis) == -1 && (j - jbis) == 1) {
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if (verification) {
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise) {
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i - 1, j + 1, p.getCouleur(), 1, false, scorePrise) && pos_risquer(i - 2, j + 2, p.getCouleur(), 1, false, scorePrise) && pos_risquer(i - 3, j + 3, p.getCouleur(), 1, false, scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if ((i - ibis) == -2 && (j - jbis) == 2) {
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if (verification) {
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise) {
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i - 1, j + 1, p.getCouleur(), 1, false, scorePrise) && pos_risquer(i - 2, j + 2, p.getCouleur(), 1, false, scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if ((i - ibis) == -3 && (j - jbis) == 3) {
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if (verification) {
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise) {
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i - 1, j + 1, p.getCouleur(), 1, false, scorePrise)) {
                                        risquer = false;
                                    }
                                }

                            } else if (posPossible(ibis, jbis, dep, 4, 1) && (i - ibis) == -1 && (j - jbis) == -1 && (peutPrendre(p.getCouleur(), i, j, i - 1, j - 1, false) || pos == 4)) {
                                if ((i - ibis) == -1 && (j - jbis) == -1) {
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if (verification) {
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise) {
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i - 1, j - 1, p.getCouleur(), 1, false, scorePrise) && pos_risquer(i - 2, j - 2, p.getCouleur(), 1, false, scorePrise) && pos_risquer(i - 3, j - 3, p.getCouleur(), 1, false, scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if ((i - ibis) == -2 && (j - jbis) == -2) {
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if (verification) {
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise) {
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i - 1, j - 1, p.getCouleur(), 1, false, scorePrise) && pos_risquer(i - 2, j - 2, p.getCouleur(), 1, false, scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if ((i - ibis) == -3 && (j - jbis) == -3) {
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if (verification) {
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise) {
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i - 1, j - 1, p.getCouleur(), 1, false, scorePrise)) {
                                        risquer = false;
                                    }
                                }
                            }
                        }
                    } else {
                        if (posPossible(ibis, jbis, 1, 1, 1) && (i - ibis) == 1 && (j - jbis) == 1 && (peutPrendre(p.getCouleur(), i, j, i + 1, j + 1, true) || pos == 1)) {
                            verificationDautresCoups(p, true);
                            scorePrise = p.getPionsManges();
                            p.resetPionsManges();
                            if (verification) {
                                risquer = true;
                            }
                            if (!verification && scorePrise >= nbPrise) {
                                risquer = true;
                            }
                            if (verification && pos_risquer(pieceTmp1.getPosition().getLigne(), pieceTmp1.getPosition().getColonne(), p.getCouleur(), 4, false, scorePrise)) {
                                risquer = false;
                            }
                        } else if (posPossible(ibis, jbis, 1, 2, 1) && (i - ibis) == 1 && (j - jbis) == -1 && (peutPrendre(p.getCouleur(), i, j, i + 1, j - 1, true) || pos == 2)) {
                            verificationDautresCoups(p, true);
                            scorePrise = p.getPionsManges();
                            p.resetPionsManges();
                            if (verification) {
                                risquer = true;
                            }
                            if (!verification && scorePrise >= nbPrise) {
                                risquer = true;
                            }
                            if (verification && pos_risquer(pieceTmp1.getPosition().getLigne(), pieceTmp1.getPosition().getColonne(), p.getCouleur(), 3, false, scorePrise)) {
                                risquer = false;
                            }
                        } else if (posPossible(ibis, jbis, 1, 3, 1) && (i - ibis) == -1 && (j - jbis) == 1 && (peutPrendre(p.getCouleur(), i, j, i - 1, j + 1, true) || pos == 3)) {
                            verificationDautresCoups(p, true);
                            scorePrise = p.getPionsManges();
                            p.resetPionsManges();
                            if (verification) {
                                risquer = true;
                            }
                            if (!verification && scorePrise >= nbPrise) {
                                risquer = true;
                            }
                            if (verification && pos_risquer(pieceTmp1.getPosition().getLigne(), pieceTmp1.getPosition().getColonne(), p.getCouleur(), 2, false, scorePrise)) {
                                risquer = false;
                            }
                        } else if (posPossible(ibis, jbis, 1, 4, 1) && (i - ibis) == -1 && (j - jbis) == -1 && (peutPrendre(p.getCouleur(), i, j, i - 1, j - 1, true) || pos == 4)) {
                            verificationDautresCoups(p, true);
                            scorePrise = p.getPionsManges();
                            p.resetPionsManges();
                            if (verification) {
                                risquer = true;
                            }
                            if (!verification && scorePrise >= nbPrise) {
                                risquer = true;
                            }
                            if (verification && pos_risquer(pieceTmp1.getPosition().getLigne(), pieceTmp1.getPosition().getColonne(), p.getCouleur(), 1, false, scorePrise)) {
                                risquer = false;
                            }
                        }
                    }
                }
            }

        }
        return risquer;
    }

    /**
     * lance l'action de déplacer une pièce p
     *
     * @param p   la pièce à déplacer
     * @param i   l'indice i de la pièce
     * @param j   l'indice j de la pièce
     * @param x   la distance de déplacement 1 pour les pions et 1 à 3 pour les dames
     * @param pos 1,2,3 ou 4 en fonction de la diagonale de déplacement
     */
    void actionPiece(Piece p, int i, int j, int x, int pos) {
        switch (pos) {
            case 1:
                deplacePiece(p, i, j, i + x, j + x);
                break;
            case 2:
                deplacePiece(p, i, j, i + x, j - x);
                break;
            case 3:
                deplacePiece(p, i, j, i - x, j + x);
                break;
            case 4:
                deplacePiece(p, i, j, i - x, j - x);
                break;
            default:
                break;
        }
        coupsRestants--;
    }

    /**
     * filtres les pièces en retirant de pièces Prioritaires les pièces restantes
     *
     * @param piecesPrioritaires la liste des pièces prioritaires
     * @param piecesRestantes    la liste des pièces restantes
     */
    public void filtrerLesPieces(ArrayList<Piece> piecesPrioritaires, ArrayList<Piece> piecesRestantes) {
        piecesRestantes.clear();
        Iterator it = pieces.iterator();
        while (it.hasNext()) {
            Piece p = (Piece) it.next();
            if (!piecesPrioritaires.contains(p)) {
                piecesRestantes.add(p);
            }
        }
    }

    /**
     * Création de la stratégie à jouer
     *
     * @param joueur       le joueur qui joue avec cette stratégie
     * @param pionsABouger le nombre de pions à bouger par coups
     * @param frame        le damier dans le qu'elle on évolue
     */
    public void strategieNaive(int joueur, int pionsABouger, DamierFrame frame) {

        this.frame = frame;
        this.frame.joueurAct = joueur;
        ArrayList<Piece> piecesRestantes = new ArrayList<Piece>();
        ArrayList<Piece> piecesPrioritaires = new ArrayList<Piece>();
        coupsRestants = pionsABouger;
        //if (!Jeu.paused.get()){
        coupObligatoire(piecesPrioritaires, joueur, 1);
        filtrerLesPieces(piecesPrioritaires, piecesRestantes);
        if(coupsRestants == pionsABouger){
            nbCoupsSansPrise++;
        }
        else{
            nbCoupsSansPrise = 0;
        }
        choixPieceADeplacer(piecesPrioritaires, piecesRestantes, joueur, true, pionsABouger);
        filtrerLesPieces(piecesPrioritaires, piecesRestantes);
        choixPieceADeplacer(piecesPrioritaires, piecesRestantes, joueur, false, pionsABouger);

        if (coupsRestants == pionsABouger || nbCoupsSansPrise == 30) {
            finPartie = true;
        }
        // }
    }

    /**
     * récupère la case au indice i et j
     *
     * @param i l'indice i de la case
     * @param j l'indice j de la case
     * @return
     */
    public Case getCase(int i, int j) {
        return cases[i][j];
    }

    /**
     * change le nombre de coups restant à jouer
     *
     * @param coupsRestants le nouveau nombre de coups restant à jouer
     */
    public void setCoupsRestants(int coupsRestants) {
        this.coupsRestants = coupsRestants;
    }

    /**
     * permet de savoir si on a fini la partie ou non
     *
     * @return true si la partie est fini sinon retourne false
     */
    public boolean isFinPartie() {
        return finPartie;
    }

    /**
     * remplace la cases par une nouvelle cases c
     *
     * @param cases la case qui est remplacer
     * @param c     la nouvelle case
     */
    public void setCase(Case[][] cases, Case c) {
        cases[c.getLigne()][c.getColonne()] = c;
    }

}
