package version1;

import java.util.*;

import static java.lang.Math.abs;

public class Plateau {
    public static final int NBCASES=10;
    private boolean finPartie = false;
    private Case cases[][];
    private Case casesTmp[][];
    private Case casestampon[][];
    private Piece pieceTmp1;
    private int nbRisque;
    private int coupsRestants;
    public DamierFrame frame;
    private ArrayList<Piece> pieces = new ArrayList<Piece>();

    public Plateau(){
        cases = new Case[10][10];
        casesTmp = new Case[10][10];
        casestampon = new Case[10][10];
        initialiserJeu();
    }

//a verifier
    public void cloneCases(Case casesTmp[][],Case cases[][]){
        int i,j;

        for ( i=0; i<NBCASES; i++ ) {
            for ( j=0; j<NBCASES; j++ ) {
                casesTmp[i][j].copieCase(cases[i][j]);
            }
        }
    }

    public void initialiserJeu() {
        int i,j;
        int zoneBlancs = 4;
        int zoneNoires = 6;

        for ( i=0; i<NBCASES; i++ ) {
            for ( j=0; j<NBCASES; j++ ) {
                if ( ((i%2 == 0)&&(j%2 == 0)) || ((i%2 == 1)&&(j%2 == 1))) {
                    Case c = new CaseNoire(i,j);
                    setCase(cases, c);

                    Case cTmp = new CaseNoire(i,j);
                    setCase(casesTmp, cTmp);

                    Case cTampon = new CaseNoire(i,j);
                    setCase(casestampon, cTampon);

                }
                else if ( ((i%2 == 0)&&(j%2 != 0)) || ((i%2 != 0)&&(j%2 == 0))) {
                    if ( i < zoneBlancs ){
                        Case c = new CaseBlanche(i,j);
                        Piece p = new Pion(c,2);
                        pieces.add(p);
                        ((CaseBlanche) c).setPiece(p);
                        setCase(cases, c);

                        Case cTmp = new CaseBlanche(i,j);
                        Piece pTmp = new Pion(cTmp,2);
                        ((CaseBlanche) cTmp).setPiece(pTmp);
                        setCase(casesTmp, cTmp);

                        Case cTampon = new CaseBlanche(i,j);
                        Piece pTampon = new Pion(cTampon,2);
                        ((CaseBlanche) cTampon).setPiece(pTampon);
                        setCase(casestampon, cTmp);
                    }
                    else if ( i >= zoneNoires ){
                        Case c = new CaseBlanche(i,j);
                        Piece p = new Pion(c,-2);
                        pieces.add(p);
                        ((CaseBlanche) c).setPiece(p);
                        setCase(cases, c);

                        Case cTmp = new CaseBlanche(i,j);
                        Piece pTmp = new Pion(cTmp,-2);
                        ((CaseBlanche) cTmp).setPiece(pTmp);
                        setCase(casesTmp, cTmp);

                        Case cTampon = new CaseBlanche(i,j);
                        Piece pTampon = new Pion(cTampon,-2);
                        ((CaseBlanche) cTampon).setPiece(pTampon);
                        setCase(casestampon, cTmp);
                    }
                    else{
                        Case c = new CaseBlanche(i,j);
                        setCase(cases, c);

                        Case cTmp = new CaseBlanche(i,j);
                        setCase(casesTmp, cTmp);

                        Case cTampon = new CaseBlanche(i,j);
                        setCase(casestampon, cTampon);
                    }
                }
            }
        }
    }

    public void deplacePiece(Piece p, int iCour, int jCour,int iNew, int jNew){

        frame.miseAJourPanelDebug(cases[iCour][jCour], cases[iNew][jNew], false, null);

       // if (!Jeu.paused.get()){
            ((CaseBlanche)cases[iNew][jNew]).setPiece(((CaseBlanche)cases[iCour][jCour]).getPiece());
            ((CaseBlanche)cases[iCour][jCour]).setPiece(null);
            p.setPosition(cases[iNew][jNew]);

            //crée la dame en si arrivé sur la dernière ligne ou première ligne en fonction du camp du pion
            if(p.getCouleur()==2 && p.getPosition().getLigne()==9 && !p.isDame()){
                p.setDames(true);
            }
            else if(p.getCouleur()==-2 && p.getPosition().getLigne()==0 && !p.isDame()){
                p.setDames(true);
            }
       // }
    }

    public void prendsPiece(Piece p, int iEnemi, int jEnemi,int iNew, int jNew, boolean verification){

        //if (!Jeu.paused.get()){
            if (!verification){
                frame.miseAJourPanelDebug(p.getPosition(), cases[iNew][jNew], true, cases[iEnemi][jEnemi]);

                pieces.remove(((CaseBlanche)cases[iEnemi][jEnemi]).getPiece());
                ((CaseBlanche)cases[iEnemi][jEnemi]).setPiece(null);
                ((CaseBlanche)cases[p.getPosition().getLigne()][p.getPosition().getColonne()]).setPiece(null);
                ((CaseBlanche)cases[iNew][jNew]).setPiece(p);
                p.setPosition(cases[iNew][jNew]);

                if(p.getCouleur()==2 && p.getPosition().getLigne()==9 && !p.isDame()){
                    p.setDames(true);
                }
                else if(p.getCouleur()==-2 && p.getPosition().getLigne()==0 && !p.isDame()){
                    p.setDames(true);
                }

                if (p.getCouleur() == 2){
                    frame.changeNbPionJ2();
                    frame.changeScoreJ1();
                }else{
                    frame.changeNbPionJ1();
                    frame.changeScoreJ2();
                }
            }else{
                ((CaseBlanche)casesTmp[iEnemi][jEnemi]).setPiece(null);
                ((CaseBlanche)casesTmp[pieceTmp1.getPosition().getLigne()][pieceTmp1.getPosition().getColonne()]).setPiece(null);
                pieceTmp1.setPosition(casesTmp[iNew][jNew]);
                ((CaseBlanche)casesTmp[iNew][jNew]).setPiece(pieceTmp1);
            }
            verificationDautresCoups(p, verification);
       // }
    }


    public void verificationDautresCoups(Piece p, boolean verification){
        int i,j;
        if (!verification){
             i = p.getPosition().getLigne();
             j = p.getPosition().getColonne();
        }else{
            i = pieceTmp1.getPosition().getLigne();
            j = pieceTmp1.getPosition().getColonne();
        }
        prise_piece(p, i, j, 1, verification);
    }

    public boolean peutPrendre(int coul, int iEnemi, int jEnemi,int iNew, int jNew, boolean verification){

        if (!verification){
            if (!((CaseBlanche)cases[iEnemi][jEnemi]).isLibre() &&
                    ((CaseBlanche)cases[iEnemi][jEnemi]).getPiece().getCouleur() != coul &&
                    ((CaseBlanche)cases[iNew][jNew]).isLibre()){
                return true;
            }else
                return false;
        }else{
            if (!((CaseBlanche)casesTmp[iEnemi][jEnemi]).isLibre() &&
                    ((CaseBlanche)casesTmp[iEnemi][jEnemi]).getPiece().getCouleur() != coul &&
                    ((CaseBlanche)casesTmp[iNew][jNew]).isLibre()){
                return true;
            }else
                return false;
        }
    }

    public void calculCoupObligatoirePion(ArrayList<Piece> piecesPrioritaires, int i, int j, Piece p){
        ArrayList<Piece> piecesVisees = new ArrayList<Piece>();

        //en haut à droite
        if (posPossible(i,j,1,1,1) && peutPrendre(p.getCouleur(), i + 1, j + 1, i + 2, j + 2, false)
                && !piecesVisees.contains((((CaseBlanche) cases[i + 1][j + 1]).getPiece()))) {
            piecesPrioritaires.add(p);
            piecesVisees.add(((CaseBlanche) cases[i + 1][j + 1]).getPiece());
            coupsRestants--;
        }
        //en haut à gauche
        else if (posPossible(i,j,1,2,1) && peutPrendre(p.getCouleur(), i + 1, j - 1, i + 2, j - 2, false)
                && !piecesVisees.contains(((CaseBlanche) cases[i + 1][j - 1]).getPiece())) {
            piecesPrioritaires.add(p);
            piecesVisees.add(((CaseBlanche) cases[i + 1][j - 1]).getPiece());
            coupsRestants--;
        }
        //en bas à droite
        else if (posPossible(i,j,1,3,1)  && peutPrendre(p.getCouleur(), i - 1, j + 1, i - 2, j + 2, false)
                && !piecesVisees.contains(((CaseBlanche) cases[i - 1][j + 1]).getPiece())) {
            piecesPrioritaires.add(p);
            piecesVisees.add(((CaseBlanche) cases[i - 1][j + 1]).getPiece());
            coupsRestants--;
        }
        //en bas à gauche
        else if (posPossible(i,j,1,4,1) && peutPrendre(p.getCouleur(), i - 1, j - 1, i - 2, j - 2, false)
                && !piecesVisees.contains(((CaseBlanche) cases[i - 1][j - 1]).getPiece())) {
            piecesPrioritaires.add(p);
            piecesVisees.add(((CaseBlanche) cases[i - 1][j - 1]).getPiece());
            coupsRestants--;
        }

        Iterator it = piecesVisees.iterator();

        while (it.hasNext()){
            Piece pion = (Piece)it.next();
        }
    }

    public void calculCoupObligatoireDame(ArrayList<Piece> piecesPrioritaires, int i, int j, Piece p){
        ArrayList<Piece> piecesVisees = new ArrayList<Piece>();

        for (int x=1; x<=3; x++) {
            //le coté haut droite +i et +j
            if (posPossible(i,j,x,1,1) && peutPrendre(p.getCouleur(), i + x, j + x, i + (x+1), j + (x+1), false)
                    && !piecesVisees.contains((((CaseBlanche) cases[i + x][j + x]).getPiece()))) {
                piecesPrioritaires.add(p);
                piecesVisees.add(((CaseBlanche) cases[i + x][j + x]).getPiece());
                coupsRestants--;
                break;
            }
            //le coté haut gauche +i et -j
            else if (posPossible(i,j,x,2,1) && peutPrendre(p.getCouleur(), i + x, j - x, i + (x+1), j - (x+1), false)
                    && !piecesVisees.contains(((CaseBlanche) cases[i + x][j - x]).getPiece())) {
                piecesPrioritaires.add(p);
                piecesVisees.add(((CaseBlanche) cases[i + x][j - x]).getPiece());
                coupsRestants--;
                break;
            }
            //le coté bas droite -i et +j
            else if (posPossible(i,j,x,3,1) && peutPrendre(p.getCouleur(), i - x, j + x, i - (x+1), j + (x+1), false)
                    && !piecesVisees.contains(((CaseBlanche) cases[i - x][j + x]).getPiece())) {
                piecesPrioritaires.add(p);
                piecesVisees.add(((CaseBlanche) cases[i - x][j + x]).getPiece());
                coupsRestants--;
                break;
            }
            //le coté bas gauche -i et -j
            else if (posPossible(i,j,x,4,1) && peutPrendre(p.getCouleur(), i - x, j - x, i - (x+1), j - (x+1), false)
                    && !piecesVisees.contains(((CaseBlanche) cases[i - x][j - x]).getPiece())) {
                piecesPrioritaires.add(p);
                piecesVisees.add(((CaseBlanche) cases[i - x][j - x]).getPiece());
                coupsRestants--;
                break;
            }
        }
    }

    public void coupObligatoire(ArrayList<Piece> piecesPrioritaires, int joueur, int strategie){
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
                }
                else{
                    calculCoupObligatoireDame(piecesPrioritaires, i, j, p);
                }
            }
        }

        Iterator it1 = piecesPrioritaires.iterator();
        //On procede a l'action, on realise les  coups obligatoires
        it1 = piecesPrioritaires.iterator();
        while (it1.hasNext()){
            Piece p = (Piece) it1.next();
            int i = p.getPosition().getLigne();
            int j = p.getPosition().getColonne();

            if (!p.isDame()) {
                if (joueur == p.getCouleur()) {
                    switch (strategie){
                        case 0: prise_piece(p, i, j, 1,false);
                            break;
                        case 1: calculMeilleurCoup(i, j, p,1);
                            break;
                    }
                }
            }
            else{
                for (int x=1; x<=3; x++) {
                    switch (strategie){
                        case 0: prise_piece(p, i, j, x,false);
                            break;
                        case 1: calculMeilleurCoup(i, j, p, x);
                            break;
                    }
                }
            }
        }
    }




    /*System de points
Nb de ennemis pris:
    - si egalite, alors on met en priorite l'avancement vers le territoir de l'enemi que le retour
    ?- si 2 pions amis peuvent prendre le meme ennemi, on prioritise celui qui mange plus ou qui ne se fait pas mange
*/
    public void calculMeilleurCoup(int i, int j, Piece p, int c){
        //la cle est la type de position sur la grille(1,2,3 ou 4) et la valeurs est le nb de pions manges
        HashMap<Integer, Integer> scorePieces = new HashMap<Integer, Integer>();
        pieceTmp1 = new Piece(p);
        cloneCases(casesTmp, cases);
        if (posPossible(i,j,c,1,1) && peutPrendre(p.getCouleur(), i + c, j + c, i + 1 + c, j + 1 + c, false)) {
            verificationDautresCoups(p, true);
            scorePieces.put(1, p.getPionsManges());
            p.resetPionsManges();
            ((CaseBlanche)casesTmp[pieceTmp1.getPosition().getLigne()][pieceTmp1.getPosition().getColonne()]).setPiece(null);
            pieceTmp1.copiePiece(p);
        }
        //le coté haut gauche +i et -j
        if (posPossible(i,j,c,2,1) && peutPrendre(p.getCouleur(), i + c, j - c, i + 1 + c, j - 1 - c, false)) {
            verificationDautresCoups(p,true);
            scorePieces.put(2, p.getPionsManges());
            p.resetPionsManges();
            ((CaseBlanche)casesTmp[pieceTmp1.getPosition().getLigne()][pieceTmp1.getPosition().getColonne()]).setPiece(null);
            pieceTmp1.copiePiece(p);
        }
        //le coté bas droite -i et +j
        if (posPossible(i,j,c,3,1) && peutPrendre(p.getCouleur(), i - c, j + c, i - 1 - c, j + 1 + c, false)) {
            verificationDautresCoups(p, true);
            scorePieces.put(3, p.getPionsManges());
            p.resetPionsManges();
            ((CaseBlanche)casesTmp[pieceTmp1.getPosition().getLigne()][pieceTmp1.getPosition().getColonne()]).setPiece(null);
            pieceTmp1.copiePiece(p);
        }
        //le coté bas gauche -i et -j
        if (posPossible(i,j,c,4,1) && peutPrendre(p.getCouleur(), i - c, j - c, i - 1 - c, j - 1 - c, false)) {
            verificationDautresCoups(p, true);
            scorePieces.put(4, p.getPionsManges());
            p.resetPionsManges();
            ((CaseBlanche)casesTmp[pieceTmp1.getPosition().getLigne()][pieceTmp1.getPosition().getColonne()]).setPiece(null);
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
        
        if (scorePieces.size() > 1){
            //Recherche du coup max dans les scores
            ArrayList<Integer>  lesMaxInd = new ArrayList<Integer>();
            
            for (int key : scorePieces.keySet()) {
                if (coupInit < scorePieces.get(key)){
                    coupInit = scorePieces.get(key);
                    posInit = key;
                }
            }

            lesMaxInd.add(posInit);
            for (int key : scorePieces.keySet()) {
                if ((scorePieces.get(key) == coupInit) && (key != posInit)){
                    lesMaxInd.add(key);
                }
            }

            if (lesMaxInd.size() > 1){
                Random r = new Random();
                int randInd =  r.nextInt((lesMaxInd.size()-1  - 0) + 1) + 0;
                posChoisi = lesMaxInd.get(randInd);
            }else{
                posChoisi = lesMaxInd.get(0);
            }
        }

        switch (posChoisi){
            case 1: prendsPiece(p, i + c, j + c, i + 1 + c, j + 1 + c, false);
                break;
            case 2: prendsPiece(p, i + c, j - c, i + 1 + c, j - 1 - c, false);
                break;
            case 3: prendsPiece(p, i - c, j + c, i - 1 - c, j + 1 + c, false);
                break;
            case 4: prendsPiece(p, i - c, j - c, i - 1 - c ,j - 1 - c, false);
                break;
            default: break;
        }

    }

    private void prise_piece(Piece p, int i, int j, int c, boolean verification) {
        if (posPossible(i,j,c,1,1) && peutPrendre(p.getCouleur(), i + c, j + c, i + 1 + c, j + 1 + c, verification)) {
            p.addPionsManges();
            prendsPiece(p, i + c, j + c, i + 1 + c, j + 1 + c, verification);
        }
        //le coté haut gauche +i et -j
        else if (posPossible(i,j,c,2,1) && peutPrendre(p.getCouleur(), i + c, j - c, i + 1 + c, j - 1 - c, verification)) {
            p.addPionsManges();
            prendsPiece(p, i + c, j - c, i + 1 + c, j - 1 - c, verification);
        }
        //le coté bas droite -i et +j
        else if (posPossible(i,j,c,3,1) && peutPrendre(p.getCouleur(), i - c, j + c, i - 1 - c, j + 1 + c, verification)) {
            p.addPionsManges();
            prendsPiece(p, i - c, j + c, i - 1 - c, j + 1 + c, verification);
        }
        //le coté bas gauche -i et -j
        else if (posPossible(i,j,c,4,1) && peutPrendre(p.getCouleur(), i - c, j - c, i - 1 - c, j - 1 - c, verification)) {
            p.addPionsManges();
            prendsPiece(p, i - c, j - c, i - 1 - c , j - 1 - c, verification);
        }
    }

    /**
     * @param i la ligne i de notre pièce
     * @param j la colone j de notre pièce
     * @param x le nombre de case qu'on veut ce déplacer
     * @param pos si c'est pour en haut à gauche ou en haut à droite...
     * @param prise si c'est pour une prise 1 sinon 0
     * @return true si la position d'arriver de la pièce est dans le plateau ou à l'extérieur
     */
    public boolean posPossible(int i, int j, int x, int pos, int prise){
        if(pos ==1 && i+x+prise<NBCASES && j+x+prise<NBCASES){
            return true;
        } else if(pos ==2 && i+x+prise<NBCASES && j-x-prise>=0){
            return true;
        } else if(pos ==3 && i-x-prise>=0 && j+x+prise<NBCASES){
            return true;
        } else if(pos ==4 && i-x-prise>=0 && j-x-prise>=0){
            return true;
        }
        return false;
    }

    public void change_Pos_Tmp(Piece p, int iNew, int jNew){

        ((CaseBlanche)casesTmp[p.getPosition().getLigne()][p.getPosition().getColonne()]).setPiece(null);
        ((CaseBlanche)casesTmp[iNew][jNew]).setPiece(pieceTmp1);
    }


    public  void choixPieceADeplacer(ArrayList<Piece> piecesPrioritaires, ArrayList<Piece> piecesRestantes, int joueur, boolean cherche_risque, int pionsABouger){
        Iterator it = piecesRestantes.iterator();
        boolean jouer = true;
        if (coupsRestants != pionsABouger && !cherche_risque){
            jouer = false;
        }
        else{
            cloneCases(casesTmp, cases);
            nbRisque = coup_risquer(-1, -1, joueur,-1, true,0,-1,-1);
        }
         while (it.hasNext() && coupsRestants>0 && jouer) {
            Piece p = (Piece) it.next();
            int i = p.getPosition().getLigne();
            int j = p.getPosition().getColonne();

            HashMap<Integer, Integer> possibilites = new HashMap<Integer, Integer>();
            if (p.getCouleur() == joueur){
                cloneCases(casesTmp, cases);
                nbRisque = coup_risquer(-1, -1, joueur,-1, true,0,-1,-1);
                if(!p.isDame() ) {
                    if (p.getCouleur() == joueur && joueur == 2) {
                        //cas 1
                        if (j < NBCASES - 1 && i < NBCASES - 1 && ((CaseBlanche) cases[i + 1][j + 1]).isLibre()) {
                            pieceTmp1 = new Piece(p);
                            cloneCases(casesTmp, cases);
                            change_Pos_Tmp(pieceTmp1, i+1, j+1);
                            if (cherche_risque && nbRisque >= coup_risquer(i+1, j+1, p.getCouleur(),4, true,0,i,j)){
                                possibilites.put(1,1);
                            }
                            else if (!cherche_risque){
                                possibilites.put(1,1);
                            }
                        }
                        //cas 2
                        if (j > 0 && i < NBCASES - 1 && ((CaseBlanche) cases[i + 1][j - 1]).isLibre()) {
                            pieceTmp1 = new Piece(p);
                            cloneCases(casesTmp, cases);
                            change_Pos_Tmp(pieceTmp1, i+1, j-1);
                            if (cherche_risque && nbRisque >= coup_risquer(i+1, j-1, p.getCouleur(),3,true,0,i,j)){
                                possibilites.put(2,1);
                            }
                            else if (!cherche_risque){
                                possibilites.put(2,1);
                            }
                        }
                    } else if (p.getCouleur() == joueur && joueur == -2) {
                        //cas 3
                        if (i > 0 && j < NBCASES - 1 && ((CaseBlanche) cases[i - 1][j + 1]).isLibre()) {
                            pieceTmp1 = new Piece(p);
                            cloneCases(casesTmp, cases);
                            change_Pos_Tmp(pieceTmp1, i-1, j+1);
                            if (cherche_risque && nbRisque >= coup_risquer(i-1, j+1, p.getCouleur(),2, true,0,i,j)){
                                possibilites.put(3,1);
                            }
                            else if (!cherche_risque){
                                possibilites.put(3,1);
                            }
                        }
                            //cas 4
                         if (j > 0 && i > 0 && ((CaseBlanche) cases[i - 1][j - 1]).isLibre()) {
                            pieceTmp1 = new Piece(p);
                            cloneCases(casesTmp, cases);
                            change_Pos_Tmp(pieceTmp1, i-1, j-1);
                            if (cherche_risque && nbRisque >= coup_risquer(i-1, j-1, p.getCouleur(),1, true,0,i,j)){
                                possibilites.put(4,1);
                            }
                            else if (!cherche_risque){
                                possibilites.put(4,1);
                            }
                        }
                    }
                }
                else {
                    for (int x = 1; x <= 3; x++) {
                        //le coté haut droite +i et +j
                        if (posPossible(i, j, x, 1, 0) && ((CaseBlanche) cases[i + x][j + x]).isLibre()) {
                            if (cherche_risque && nbRisque >= coup_risquer(i+x, j+x, p.getCouleur(),4, true,0,i,j)){
                                possibilites.put(1,x);
                            }
                            else if (!cherche_risque){
                                possibilites.put(1,x);
                            }
                        }
                        //le coté haut gauche +i et -j
                        else if (posPossible(i, j, x, 2, 0) && ((CaseBlanche) cases[i + x][j - x]).isLibre()) {
                            if (cherche_risque && nbRisque >= coup_risquer(i+x, j-x, p.getCouleur(),3, true,0,i,j)){
                                possibilites.put(2,x);
                            }
                            else if (!cherche_risque){
                                possibilites.put(2,x);
                            }
                        }
                        //le coté bas droite -i et +j
                        if (posPossible(i, j, x, 3, 0) && ((CaseBlanche) cases[i - x][j + x]).isLibre()) {
                            if (cherche_risque && nbRisque >= coup_risquer(i-x, j+x, p.getCouleur(),2,true,0,i,j)){
                                possibilites.put(3,x);
                            }
                            else if (!cherche_risque){
                                possibilites.put(3,x);
                            }
                        }
                        //le coté bas gauche -i et -j
                        else if (posPossible(i, j, x, 4, 0) && ((CaseBlanche) cases[i - x][j - x]).isLibre()) {
                            if (cherche_risque && nbRisque >= coup_risquer(i-x, j-x, p.getCouleur(),1, true,0,i,j)){
                                possibilites.put(4,x);
                            }
                            else if (!cherche_risque){
                                possibilites.put(4,x);
                            }
                        }
                    }
                }
                //fin pion
                if (possibilites.size() != 0){
                    //on gere les cas quand le pion peut choisir de ne pas rester bloquer
                    Random r = new Random();
                    int randInd = 0;
                    if (possibilites.size() > 1){
                        Iterator itPos = possibilites.entrySet().iterator();
                        while (itPos.hasNext()) {
                            Map.Entry key = (Map.Entry) itPos.next();
                            int k = (int)key.getKey();

                            if (k == 1 || k == 3){
                                if (j + (int)key.getValue() == 9)
                                    possibilites.remove(key);
                            }else if(k == 2 || k == 4){
                                if (j - ((Integer)key.getValue()) == 0)
                                    possibilites.remove(key.getKey());
                            }
                        }
                        randInd =  r.nextInt((possibilites.size()-1  - 0) + 1) + 0;
                    }
                    int xChoisi = 1;
                    int posChoisi = 0;
                    int idx = 0;

                    Iterator itPos = possibilites.entrySet().iterator();
                    while (itPos.hasNext()) {
                        Map.Entry key = (Map.Entry) itPos.next();
                        if (possibilites.size() > 1){
                            if (randInd == idx){
                                posChoisi = (int)key.getKey();
                                xChoisi = possibilites.get(key.getKey());
                            }else{
                                idx++;
                            }
                        }else{
                            xChoisi = (int)key.getValue();
                            posChoisi = (int)key.getKey();
                            break;
                        }
                    }
                    piecesPrioritaires.add(p);
                    actionPiece(p,i,j,xChoisi, posChoisi);
                    //jouer = false;
                    if(!cherche_risque) {
                        jouer = false;
                    }
                }
            }
        }
    }

    /**
     * @param i la position i ou on ce retrouve après avoir jouer le coup
     * @param j la position j ou on ce retrouve après avoir jouer le coup
     * @param coul la couleur de notre pion
     * @param pos la position d'ou l'on vient avec le pion
     * @param verification permet de rappeller la fonction pour savoir si on joue et si on ce fait prendre es ce que l'on peut reprendre la pièce adversaire et donc ne pas être coup risquer
     * @return true si on peut ce faire prendre sans reprendre derrière par l'adversaire et donc évité au maximum de jouer ce coup
     */
    int coup_risquer(int i, int j, int coul, int pos, boolean verification, int nbPrise, int non_i,int non_j){
        int nbRisqueTmp = 0;
        //ArrayList<Boolean> posPossible = new ArrayList<Boolean>();
        Iterator itbis = pieces.iterator();
        cloneCases(casestampon, casesTmp);
        //risquer = pos_risquer(i, j, coul, pos, verification, nbPrise);
        while (itbis.hasNext()) {
            Piece pbis = (Piece) itbis.next();
            int ibis2 = pbis.getPosition().getLigne();
            int jbis2 = pbis.getPosition().getColonne();
            if(ibis2 == non_i && jbis2 == non_j){
                ibis2 = i;
                jbis2 = j;
            }
            if(pbis.getCouleur()==coul) {
                if (verification && pos_risquer(ibis2, jbis2, coul, -1, verification, nbPrise)) {
                    nbRisqueTmp ++;
                }
                cloneCases(casesTmp, casestampon);
            }
        }
        return nbRisqueTmp;
    }

    boolean containePiecetmp(Piece p){
        boolean containe = false;
        if(((CaseBlanche)casesTmp[p.getPosition().getLigne()][p.getPosition().getColonne()]).getPiece() != null){
            containe = true;
        }
        return containe;
    }

    /**
     * @param i la position i ou on ce retrouve après avoir jouer le coup
     * @param j la position j ou on ce retrouve après avoir jouer le coup
     * @param coul la couleur de notre pion
     * @param pos la position d'ou l'on vient avec le pion
     * @param verification permet de rappeller la fonction pour savoir si on joue et si on ce fait prendre es ce que l'on peut reprendre la pièce adversaire et donc ne pas être coup risquer
     * @return true si on peut ce faire prendre sans reprendre derrière par l'adversaire et donc évité au maximum de jouer ce coup
     */
    boolean pos_risquer(int i, int j, int coul, int pos, boolean verification, int nbPrise){
        boolean risquer = false;
        int scorePrise;
        int pl;
        //ArrayList<Boolean> posPossible = new ArrayList<Boolean>();
        Iterator it = pieces.iterator();
        while (it.hasNext() && !risquer) {
            Piece p = (Piece) it.next();
            if(containePiecetmp((p))){
                int ibis = p.getPosition().getLigne();
                int jbis = p.getPosition().getColonne();
                if(abs(i-ibis)==1 && abs(j-jbis)==1 && p.getCouleur()!=coul){
                    pl = 0;
                    pieceTmp1 = new Piece(p);
                    //if (verification){
                    //    pieceTmp1 = new Piece(p);
                    //}
                    //else{
                    //    pieceTmp2 = new Piece(p);
                    //}
                    if(p.isDame()){
                        for(int dep= 1; dep <= 3; dep ++) {
                            if (posPossible(ibis, jbis, dep, 1, 1) && (peutPrendre(p.getCouleur(), i, j, i + 1, j + 1, false) || pos == 1)) {
                                if((i - ibis) == 1 && (j - jbis) == 1 ){
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if(verification){
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise){
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i + 1, j + 1, p.getCouleur(), 1, false,scorePrise) && pos_risquer(i + 2, j + 2, p.getCouleur(), 1,false,scorePrise) && pos_risquer(i + 3, j + 3, p.getCouleur(), 1,false,scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if((i - ibis) == 2 && (j - jbis) == 2 ){
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if(verification){
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise){
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i + 1, j + 1, p.getCouleur(), 1,false,scorePrise) && pos_risquer(i + 2, j + 2, p.getCouleur(), 1,false,scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if((i - ibis) == 3 && (j - jbis) == 3 ){
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if(verification){
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise){
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i + 1, j + 1, p.getCouleur(), 1,false,scorePrise)) {
                                        risquer = false;
                                    }
                                }
                            } else if (posPossible(ibis, jbis, dep, 2, 1) && (peutPrendre(p.getCouleur(), i, j, i + 1, j - 1, false) || pos == 2)) {
                                if((i - ibis) == 1 && (j - jbis) == -1 ){
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if(verification){
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise){
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i + 1, j - 1, p.getCouleur(), 1,false,scorePrise) && pos_risquer(i + 2, j - 2, p.getCouleur(), 1,false,scorePrise) && pos_risquer(i + 3, j - 3, p.getCouleur(), 1,false,scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if((i - ibis) == 2 && (j - jbis) == -2 ){
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if(verification){
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise){
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i + 1, j - 1, p.getCouleur(), 1,false,scorePrise) && pos_risquer(i + 2, j - 2, p.getCouleur(), 1,false,scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if((i - ibis) == 3 && (j - jbis) == -3 ){
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if(verification){
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise){
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i + 1, j - 1, p.getCouleur(), 1,false,scorePrise)) {
                                        risquer = false;
                                    }
                                }
                            } else if (posPossible(ibis, jbis, dep, 3, 1) && (peutPrendre(p.getCouleur(), i, j, i - 1, j + 1, false) || pos == 3)) {
                                if((i - ibis) == -1 && (j - jbis) == 1 ){
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if(verification){
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise){
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i - 1, j + 1, p.getCouleur(), 1,false,scorePrise) && pos_risquer(i - 2, j + 2, p.getCouleur(), 1,false,scorePrise) && pos_risquer(i - 3, j + 3, p.getCouleur(), 1,false,scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if((i - ibis) == -2 && (j - jbis) == 2 ){
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if(verification){
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise){
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i - 1, j + 1, p.getCouleur(), 1,false,scorePrise) && pos_risquer(i - 2, j + 2, p.getCouleur(), 1,false,scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if((i - ibis) == -3 && (j - jbis) == 3 ){
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if(verification){
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise){
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i - 1, j + 1, p.getCouleur(), 1,false,scorePrise)) {
                                        risquer = false;
                                    }
                                }

                            } else if (posPossible(ibis, jbis, dep, 4, 1) && (i - ibis) == -1 && (j - jbis) == -1 && (peutPrendre(p.getCouleur(), i, j, i - 1, j - 1, false) || pos == 4)) {
                                if((i - ibis) == -1 && (j - jbis) == -1 ){
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if(verification){
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise){
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i - 1, j - 1, p.getCouleur(), 1,false,scorePrise) && pos_risquer(i - 2, j - 2, p.getCouleur(), 1,false,scorePrise) && pos_risquer(i - 3, j - 3, p.getCouleur(), 1,false,scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if((i - ibis) == -2 && (j - jbis) == -2 ){
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if(verification){
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise){
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i - 1, j - 1, p.getCouleur(), 1,false,scorePrise) && pos_risquer(i - 2, j - 2, p.getCouleur(), 1,false,scorePrise)) {
                                        risquer = false;
                                    }
                                }
                                if((i - ibis) == -3 && (j - jbis) == -3 ){
                                    verificationDautresCoups(p, true);
                                    scorePrise = p.getPionsManges();
                                    p.resetPionsManges();
                                    if(verification){
                                        risquer = true;
                                    }
                                    if (!verification && scorePrise >= nbPrise){
                                        risquer = true;
                                    }
                                    if (verification && pos_risquer(i - 1, j - 1, p.getCouleur(), 1,false,scorePrise)) {
                                        risquer = false;
                                    }
                                }
                            }
                        }
                    }
                    else{
                        if(posPossible(ibis,jbis,1,1,1)  && (i-ibis)==1 && (j-jbis)==1 && (peutPrendre(p.getCouleur(), i, j, i +1, j +1, true) || pos==1)){
                            verificationDautresCoups(p, true);
                            scorePrise = p.getPionsManges();
                            p.resetPionsManges();
                            if(verification){
                                risquer = true;
                            }
                            if (!verification && scorePrise >= nbPrise){
                                risquer = true;
                            }
                            if(verification && pos_risquer(pieceTmp1.getPosition().getLigne(), pieceTmp1.getPosition().getColonne(), p.getCouleur(), 4,false,scorePrise)) {
                                risquer = false;
                            }
                        }
                        else if (posPossible(ibis,jbis,1,2,1)  && (i-ibis)==1 && (j-jbis)==-1 && (peutPrendre(p.getCouleur(), i, j, i +1, j -1, true) || pos==2)){
                            verificationDautresCoups(p, true);
                            scorePrise = p.getPionsManges();
                            p.resetPionsManges();
                            if(verification){
                                risquer = true;
                            }
                            if (!verification && scorePrise >= nbPrise){
                                risquer = true;
                            }
                            //TODO changer le ibis+2 et jbis-2 par la pos de la piece temp1
                            if(verification && pos_risquer(pieceTmp1.getPosition().getLigne(), pieceTmp1.getPosition().getColonne(), p.getCouleur(), 3,false,scorePrise)) {
                                risquer = false;
                            }
                        }
                        else if (posPossible(ibis,jbis,1,3,1)  && (i-ibis)==-1 && (j-jbis)==1 && (peutPrendre(p.getCouleur(), i, j, i -1, j +1, true) || pos==3)){
                            verificationDautresCoups(p, true);
                            scorePrise = p.getPionsManges();
                            p.resetPionsManges();
                            if(verification){
                                risquer = true;
                            }
                            if (!verification && scorePrise >= nbPrise){
                                risquer = true;
                            }
                            if(verification && pos_risquer(pieceTmp1.getPosition().getLigne(), pieceTmp1.getPosition().getColonne(), p.getCouleur(), 2,false,scorePrise)) {
                                risquer = false;
                            }
                        }
                        else if (posPossible(ibis,jbis,1,4,1)  && (i-ibis)==-1 && (j-jbis)==-1 && (peutPrendre(p.getCouleur(), i, j, i -1, j -1, true) || pos==4)){
                            verificationDautresCoups(p, true);
                            scorePrise = p.getPionsManges();
                            p.resetPionsManges();
                            if(verification){
                                risquer = true;
                            }
                            if (!verification && scorePrise >= nbPrise){
                                risquer = true;
                            }
                            if(verification && pos_risquer(pieceTmp1.getPosition().getLigne(), pieceTmp1.getPosition().getColonne(), p.getCouleur(), 1,false,scorePrise)) {
                                risquer = false;
                            }
                        }
                    }
                }
            }

        }
        return risquer;
    }

    void actionPiece(Piece p, int i, int j, int x, int pos){
        switch (pos){
            case 1: deplacePiece(p, i, j, i + x, j + x);
                break;
            case 2: deplacePiece(p, i, j, i + x, j - x);
                break;
            case 3: deplacePiece(p, i, j, i - x, j + x);
                break;
            case 4: deplacePiece(p, i, j, i - x, j - x);
                break;
            default: break;
        }
        coupsRestants--;
    }

    public void filtrerLesPieces(ArrayList<Piece> piecesPrioritaires, ArrayList<Piece> piecesRestantes){
        piecesRestantes.clear();
        Iterator it = pieces.iterator();
        while (it.hasNext()) {
            Piece p = (Piece) it.next();
            if (!piecesPrioritaires.contains(p))
                piecesRestantes.add(p);
        }
    }

    public void strategieNaive(int joueur, int pionsABouger, DamierFrame frame){

        this.frame = frame;
        this.frame.joueurAct = joueur;
        ArrayList<Piece> piecesRestantes = new ArrayList<Piece>();
        ArrayList<Piece> piecesPrioritaires = new ArrayList<Piece>();
        coupsRestants = pionsABouger;
        //if (!Jeu.paused.get()){
            coupObligatoire(piecesPrioritaires, joueur, 1);
            filtrerLesPieces(piecesPrioritaires, piecesRestantes);
            choixPieceADeplacer(piecesPrioritaires, piecesRestantes, joueur, true, pionsABouger);
            filtrerLesPieces(piecesPrioritaires, piecesRestantes);
            choixPieceADeplacer(piecesPrioritaires, piecesRestantes, joueur, false, pionsABouger);

            if (coupsRestants == pionsABouger)
                finPartie = true;
       // }
    }

    public Case getCase(int i, int j){
        return cases[i][j];
    }

    public void setCoupsRestants(int coupsRestants) {
        this.coupsRestants = coupsRestants;
    }

    public boolean isFinPartie() {
        return finPartie;
    }

    public void setCase(Case cases[][], Case c){
        cases[c.getLigne()][c.getColonne()] = c;
    }

}
