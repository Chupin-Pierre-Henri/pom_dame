package version1;

import java.util.*;

public class Plateau {
    public static final int NBCASES=10;
    private boolean finPartie = false;
    private Case cases[][];
    private Case casesTmp[][];
    private Piece pieceTmp;
    private int coupsRestants;
    public DamierFrame frame;
    private ArrayList<Piece> pieces = new ArrayList<Piece>();

    public Plateau(){
        cases = new Case[10][10];
        casesTmp = new Case[10][10];
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
                    }
                    else{
                        Case c = new CaseBlanche(i,j);
                        setCase(cases, c);

                        Case cTmp = new CaseBlanche(i,j);
                        setCase(casesTmp, cTmp);
                    }
                }
            }
        }
    }

    public void deplacePiece(Piece p, int iCour, int jCour,int iNew, int jNew){

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
    }

    public void prendsPiece(Piece p, int iEnemi, int jEnemi,int iNew, int jNew, boolean verification){

        if (!verification){
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
            ((CaseBlanche)casesTmp[pieceTmp.getPosition().getLigne()][pieceTmp.getPosition().getColonne()]).setPiece(null);
            pieceTmp.setPosition(casesTmp[iNew][jNew]);
            ((CaseBlanche)casesTmp[iNew][jNew]).setPiece(pieceTmp);
        }
        verificationDautresCoups(p, verification);
    }

    public void verificationDautresCoups(Piece p, boolean verification){
        int i,j;
        if (!verification){
             i = p.getPosition().getLigne();
             j = p.getPosition().getColonne();
        }else{
            i = pieceTmp.getPosition().getLigne();
            j = pieceTmp.getPosition().getColonne();
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
    }

    public void calculCoupObligatoireDame(ArrayList<Piece> piecesPrioritaires, int i, int j, Piece p){
        ArrayList<Piece> piecesVisees = new ArrayList<Piece>();

        for (int x=1; x<3; x++) {
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

        //On procede a l'action, on realise les  coups obligatoires
        Iterator it1 = piecesPrioritaires.iterator();
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
                for (int x=1; x<3; x++) {
                    switch (strategie){
                        case 0: prise_piece(p, i, j, x,false);
                            break;
                        case 1: calculMeilleurCoup(i, j, p,x);
                            break;
                    }
                    break;
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
        pieceTmp = new Piece(p);

        cloneCases(casesTmp, cases);
        if (posPossible(i,j,c,1,1) && peutPrendre(p.getCouleur(), i + c, j + c, i + 1 + c, j + 1 + c, false)) {
            verificationDautresCoups(p, true);
            scorePieces.put(1, p.getPionsManges());
            p.resetPionsManges();
            ((CaseBlanche)casesTmp[pieceTmp.getPosition().getLigne()][pieceTmp.getPosition().getColonne()]).setPiece(null);
            pieceTmp.copiePiece(p);
        }
        //le coté haut gauche +i et -j
        if (posPossible(i,j,c,2,1) && peutPrendre(p.getCouleur(), i + c, j - c, i + 1 + c, j - 1 - c, false)) {
            verificationDautresCoups(p,true);
            scorePieces.put(2, p.getPionsManges());
            p.resetPionsManges();
            ((CaseBlanche)casesTmp[pieceTmp.getPosition().getLigne()][pieceTmp.getPosition().getColonne()]).setPiece(null);
            pieceTmp.copiePiece(p);
        }
        //le coté bas droite -i et +j
        if (posPossible(i,j,c,3,1) && peutPrendre(p.getCouleur(), i - c, j + c, i - 1 - c, j + 1 + c, false)) {
            verificationDautresCoups(p, true);
            scorePieces.put(3, p.getPionsManges());
            p.resetPionsManges();
            ((CaseBlanche)casesTmp[pieceTmp.getPosition().getLigne()][pieceTmp.getPosition().getColonne()]).setPiece(null);
            pieceTmp.copiePiece(p);
        }
        //le coté bas gauche -i et -j
        if (posPossible(i,j,c,4,1) && peutPrendre(p.getCouleur(), i - c, j - c, i - 1 - c, j - 1 - c, false)) {
            verificationDautresCoups(p, true);
            scorePieces.put(4, p.getPionsManges());
            p.resetPionsManges();
            ((CaseBlanche)casesTmp[pieceTmp.getPosition().getLigne()][pieceTmp.getPosition().getColonne()]).setPiece(null);
            pieceTmp.copiePiece(p);
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

    public  void choixPieceADeplacer(ArrayList<Piece> piecesRestantes, int joueur){
        Iterator it = piecesRestantes.iterator();
        while (it.hasNext() && coupsRestants>0) {
            Piece p = (Piece) it.next();
            int i = p.getPosition().getLigne();
            int j = p.getPosition().getColonne();

            HashMap<Integer, Integer> possibilites = new HashMap<Integer, Integer>();

            if (p.getCouleur() == joueur){
                if(!p.isDame() ) {
                    if (p.getCouleur() == joueur && joueur == 2) {
                        //cas 1
                        if (j < NBCASES - 1 && i < NBCASES - 1 && ((CaseBlanche) cases[i + 1][j + 1]).isLibre()) {
                            possibilites.put(1,1);
                        }
                        //cas 2
                        if (j > 0 && i < NBCASES - 1 && ((CaseBlanche) cases[i + 1][j - 1]).isLibre()) {
                            possibilites.put(2,1);
                        }
                    } else if (p.getCouleur() == joueur && joueur == -2) {
                        //cas 3
                        if (i > 0 && j < NBCASES - 1 && ((CaseBlanche) cases[i - 1][j + 1]).isLibre()) {
                            possibilites.put(3,1);
                        }
                            //cas 4
                        if (j > 0 && i > 0 && ((CaseBlanche) cases[i - 1][j - 1]).isLibre()) {
                            possibilites.put(4,1);
                        }
                    }
                }
                else {
                    for (int x = 1; x < 3; x++) {
                        //le coté haut droite +i et +j
                        if (posPossible(i, j, x, 1, 0) && ((CaseBlanche) cases[i + x][j + x]).isLibre()) {
                            possibilites.put(1,x);
                        }
                        //le coté haut gauche +i et -j
                        else if (posPossible(i, j, x, 2, 0) && ((CaseBlanche) cases[i + x][j - x]).isLibre()) {
                            possibilites.put(2,x);
                        }
                        //le coté bas droite -i et +j
                        if (posPossible(i, j, x, 3, 0) && ((CaseBlanche) cases[i - x][j + x]).isLibre()) {
                            possibilites.put(3,x);
                        }
                        //le coté bas gauche -i et -j
                        else if (posPossible(i, j, x, 4, 0) && ((CaseBlanche) cases[i - x][j - x]).isLibre()) {
                            possibilites.put(4,x);
                        }
                    }
                }
                //fin pion
                if (possibilites.size() != 0){
                    //on gere les cas quand le pion peut choisir de ne pas rester bloquer
                    Random r = new Random();
                    int randInd = 0;// =  r.nextInt((possibilites.size()-1  - 0) + 1) + 0;
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
                    actionPiece(p,i,j,xChoisi, posChoisi);
                }
            }
        }
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
        Iterator it = pieces.iterator();
        while (it.hasNext()) {
            Piece p = (Piece) it.next();
            if (!piecesPrioritaires.contains(p))
                piecesRestantes.add(p);
        }
    }

    public void strategieNaive(int joueur, int pionsABouger, DamierFrame frame){

        this.frame = frame;
        ArrayList<Piece> piecesRestantes = new ArrayList<Piece>();
        ArrayList<Piece> piecesPrioritaires = new ArrayList<Piece>();
        coupsRestants = pionsABouger;

        coupObligatoire(piecesPrioritaires, joueur, 1);
        filtrerLesPieces(piecesPrioritaires, piecesRestantes);
        choixPieceADeplacer(piecesRestantes, joueur);

        if (coupsRestants == pionsABouger)
            finPartie = true;
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
