package version1;

import java.util.ArrayList;
import java.util.Iterator;

public class Plateau {
    public static final int NBCASES=10;
    private boolean finPartie = false;
    private Case cases[][];
    private int coupsRestants;
    public DamierFrame frame;
    private ArrayList<Piece> pieces = new ArrayList<Piece>();

    public Plateau(){
        cases = new Case[10][10];
        initialiserJeu();
    }

    public void initialiserJeu() {
        int i,j;
        int zoneBlancs = 4;
        int zoneNoires = 6;

        for ( i=0; i<NBCASES; i++ ) {
            for ( j=0; j<NBCASES; j++ ) {
                if ( ((i%2 == 0)&&(j%2 == 0)) || ((i%2 == 1)&&(j%2 == 1))) {
                    Case c = new CaseNoire(i,j);
                    setCase(c);
                }
                else if ( ((i%2 == 0)&&(j%2 != 0)) || ((i%2 != 0)&&(j%2 == 0))) {
                    if ( i < zoneBlancs ){
                        Case c = new CaseBlanche(i,j);
                        Piece p = new Pion(c,2);
                        pieces.add(p);
                        ((CaseBlanche) c).setPiece(p);
                        setCase(c);
                    }
                    else if ( i >= zoneNoires ){
                        Case c = new CaseBlanche(i,j);
                        Piece p = new Pion(c,-2);
                        pieces.add(p);
                        ((CaseBlanche) c).setPiece(p);
                        setCase(c);
                    }
                    else{
                        Case c = new CaseBlanche(i,j);
                        setCase(c);
                    }
                }
            }
        }
    }

    public void deplacePiece(Piece p, int iCour, int jCour,int iNew, int jNew){
        ((CaseBlanche)cases[iNew][jNew]).setPiece(((CaseBlanche)cases[iCour][jCour]).getPiece());
        ((CaseBlanche)cases[iCour][jCour]).setPiece(null);
        p.setPosition(cases[iNew][jNew]);
    }

    public void prendsPiece(Piece p, int iEnemi, int jEnemi,int iNew, int jNew){

        ((CaseBlanche)cases[iEnemi][jEnemi]).getPiece().setVivante(false);
        pieces.remove(((CaseBlanche)cases[iEnemi][jEnemi]).getPiece());
        ((CaseBlanche)cases[iEnemi][jEnemi]).setPiece(null);
        ((CaseBlanche)cases[p.getPosition().getLigne()][p.getPosition().getColonne()]).setPiece(null);
        ((CaseBlanche)cases[iNew][jNew]).setPiece(p);
        p.setPosition(cases[iNew][jNew]);
        if (p.getCouleur() == 2){
            frame.changeNbPionJ2();
            frame.changeScoreJ1();
        }else{
            frame.changeNbPionJ1();
            frame.changeScoreJ2();
        }

        verificationDautresCoups(p);
    }

    public void verificationDautresCoups(Piece p){
        int i = p.getPosition().getLigne();
        int j = p.getPosition().getColonne();
        if (p.getCouleur() == 2){
            if (i<NBCASES-2 && j<NBCASES-2 && peutPrendre(p.getCouleur(),i+1,j+1,i+2, j+2)){
                prendsPiece(p,i+1,j+1,i+2, j+2);
            }else if (j>1 && i<NBCASES-2 && peutPrendre(p.getCouleur(),i+1,j-1,i+2, j-2)){
                prendsPiece(p,i+1,j-1,i+2, j-2);
            }
        } else if (p.getCouleur() == -2){
            if (i>1 && j<NBCASES-2 && peutPrendre(p.getCouleur(),i-1, j+1, i-2, j+2)){
                prendsPiece(p,i-1, j+1, i-2, j+2);
            }else if (i>1 && j>1 && peutPrendre(p.getCouleur(),i-1, j-1, i-2, j-2)){
                prendsPiece(p,i-1, j-1, i-2, j-2);
            }
        }


    }

    public boolean peutPrendre(int coul, int iEnemi, int jEnemi,int iNew, int jNew){

        if (!((CaseBlanche)cases[iEnemi][jEnemi]).isLibre() &&
                ((CaseBlanche)cases[iEnemi][jEnemi]).getPiece().getCouleur() != coul &&
                ((CaseBlanche)cases[iNew][jNew]).isLibre()){
            return true;
        }else
            return false;
    }

    public void deplacerAuHazard(){

    }

    public void CoupObligatoire(ArrayList<Piece> piecesPrioritaires, int joueur){
        ArrayList<Piece> piecesVisees = new ArrayList<Piece>();
        Iterator it = pieces.iterator();

        while (it.hasNext() && coupsRestants > 0) {
            Piece p = (Piece) it.next();
            int i = p.getPosition().getLigne();
            int j = p.getPosition().getColonne();

            if (joueur == p.getCouleur()&& p.getCouleur() == 2){
                if (i<NBCASES-2 && j<NBCASES-2 && peutPrendre(p.getCouleur(),i+1,j+1,i+2, j+2)
                        && !piecesVisees.contains((((CaseBlanche)cases[i+1][j+1]).getPiece()))){
                    piecesPrioritaires.add(p);
                    piecesVisees.add(((CaseBlanche)cases[i+1][j+1]).getPiece());
                    coupsRestants--;
                }else if (j>1 && i<NBCASES-2 && peutPrendre(p.getCouleur(),i+1,j-1,i+2, j-2)
                        && !piecesVisees.contains(((CaseBlanche)cases[i+1][j-1]).getPiece())){
                    piecesPrioritaires.add(p);
                    piecesVisees.add(((CaseBlanche)cases[i+1][j-1]).getPiece());
                    coupsRestants--;
                }
            } else if (joueur == p.getCouleur()&& p.getCouleur() == -2){
                if (i>1 && j<NBCASES-2 && peutPrendre(p.getCouleur(),i-1, j+1, i-2, j+2)
                        && !piecesVisees.contains(((CaseBlanche)cases[i-1][j+1]).getPiece())){
                    piecesPrioritaires.add(p);
                    piecesVisees.add(((CaseBlanche)cases[i-1][j+1]).getPiece());
                    coupsRestants--;
                }else if (i>1 && j>1 && peutPrendre(p.getCouleur(),i-1, j-1, i-2, j-2)
                        && !piecesVisees.contains(((CaseBlanche)cases[i-1][j-1]).getPiece())){
                    piecesPrioritaires.add(p);
                    piecesVisees.add(((CaseBlanche)cases[i-1][j-1]).getPiece());
                    coupsRestants--;
                }
            }
        }

        Iterator it1 = piecesPrioritaires.iterator();
        while (it1.hasNext()){
            Piece p = (Piece) it1.next();
            int i = p.getPosition().getLigne();
            int j = p.getPosition().getColonne();

            if (joueur == p.getCouleur()&& p.getCouleur() == 2){
                if (i<NBCASES-2 && j<NBCASES-2 && peutPrendre(p.getCouleur(),i+1,j+1,i+2, j+2)){
                    prendsPiece(p,i+1,j+1,i+2, j+2);
                }else if (j>1 && i<NBCASES-2 && peutPrendre(p.getCouleur(),i+1,j-1,i+2, j-2)){
                    prendsPiece(p,i+1,j-1,i+2, j-2);
                }
            } else if (joueur == p.getCouleur()&& p.getCouleur() == -2){
                if (i>1 && j<NBCASES-2 && peutPrendre(p.getCouleur(),i-1, j+1, i-2, j+2)){
                    prendsPiece(p, i-1, j+1, i-2, j+2);
                }else if (i>1 && j>1 && peutPrendre(p.getCouleur(),i-1, j-1, i-2, j-2)){
                    prendsPiece(p, i-1, j-1, i-2, j-2);
                }
            }
        }
    }

    public void strategieNaive(int joueur, int pionsABouger, DamierFrame frame){

        this.frame = frame;
        ArrayList<Piece> piecesPrioritaires = new ArrayList<Piece>();
        coupsRestants = pionsABouger;

        CoupObligatoire(piecesPrioritaires, joueur);
        Iterator it = pieces.iterator();

        while (it.hasNext() && coupsRestants>0){
            Piece p = (Piece)it.next();
            if (p != null || p.isVivante()){
                int i = p.getPosition().getLigne();
                int j = p.getPosition().getColonne();

                if (!piecesPrioritaires.contains(p)){
                    if (p.getCouleur() == joueur && joueur == 2){
                        if (j<NBCASES-1 && i<NBCASES-1 && ((CaseBlanche)cases[i+1][j+1]).isLibre()){
                            deplacePiece(p, i, j, i+1, j+1);
                            coupsRestants--;
                        }else if(j>0 && i<NBCASES-1 && ((CaseBlanche)cases[i+1][j-1]).isLibre()){
                            deplacePiece(p, i, j, i+1, j-1);
                            coupsRestants--;
                        }
                    }else if (p.getCouleur() == joueur && joueur == -2){
                        if (i>0 && j<NBCASES-1 && ((CaseBlanche)cases[i-1][j+1]).isLibre()){
                            deplacePiece(p, i, j, i-1, j+1);
                            coupsRestants--;
                        }else if(j>0 && i>0 && ((CaseBlanche)cases[i-1][j-1]).isLibre()){
                            deplacePiece(p, i, j, i-1, j-1);
                            coupsRestants--;
                        }
                    }
                }
            }
        }
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

    public void setCase(Case c){
        cases[c.getLigne()][c.getColonne()] = c;
    }

}
