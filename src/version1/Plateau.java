package version1;

import java.util.ArrayList;
import java.util.Iterator;

public class Plateau {
    public static final int NBCASES=10;
    private boolean finPartie = false;
    private Case cases[][];
    private int coupsRestants = 2;
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

    public void prendsPiece(Piece p, int iEnemi, int jEnemi,int iNew, int jNew, int iIncrem, int jIncrem){

        do{
            ((CaseBlanche)cases[iEnemi][jEnemi]).getPiece().setVivante(false);
            pieces.remove(((CaseBlanche)cases[iEnemi][jEnemi]).getPiece());
            ((CaseBlanche)cases[iEnemi][jEnemi]).setPiece(null);
            ((CaseBlanche)cases[iEnemi- iIncrem][jEnemi - jIncrem]).setPiece(null);
            ((CaseBlanche)cases[iNew][jNew]).setPiece(p);
        }while(peutPrendre(p.getCouleur(), iNew, jNew,iNew+iIncrem, jNew+jIncrem));
    }

    public boolean peutPrendre(int coul, int iEnemi, int jEnemi,int iNew, int jNew){

        if (!((CaseBlanche)cases[iEnemi][jEnemi]).isLibre() &&
                ((CaseBlanche)cases[iEnemi][jEnemi]).getPiece().getCouleur() != coul &&
                ((CaseBlanche)cases[iNew][jNew]).isLibre()){
            return true;
        }else
            return false;
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
                    //prendsPiece(p,i+1,j+1,i+2, j+2,1,1);
                }else if (j>1 && i<NBCASES-2 && peutPrendre(p.getCouleur(),i+1,j-1,i+2, j-2)
                        && !piecesVisees.contains(((CaseBlanche)cases[i+1][j-1]).getPiece())){
                    piecesPrioritaires.add(p);
                    piecesVisees.add(((CaseBlanche)cases[i+1][j-1]).getPiece());
                    coupsRestants--;
                    //prendsPiece(p,i+1,j-1,i+2, j-2,1,-1);
                }
            } else if (joueur == p.getCouleur()&& p.getCouleur() == -2){
                if (i>1 && j<NBCASES-2 && peutPrendre(p.getCouleur(),i-1, j+1, i-2, j+2)
                        && !piecesVisees.contains(((CaseBlanche)cases[i-1][j+1]).getPiece())){
                    piecesPrioritaires.add(p);
                    piecesVisees.add(((CaseBlanche)cases[i-1][j+1]).getPiece());
                    coupsRestants--;
                    //prendsPiece(p, i-1, j+1, i-2, j+2,-1,1);
                }else if (i>1 && j>1 && peutPrendre(p.getCouleur(),i-1, j-1, i-2, j-2)
                        && !piecesVisees.contains(((CaseBlanche)cases[i-1][j-1]).getPiece())){
                    piecesPrioritaires.add(p);
                    piecesVisees.add(((CaseBlanche)cases[i-1][j-1]).getPiece());
                    coupsRestants--;
                    //prendsPiece(p, i-1, j-1, i-2, j-2,-1,-1);
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
                    prendsPiece(p,i+1,j+1,i+2, j+2,1,1);
                    //return;
                }else if (j>1 && i<NBCASES-2 && peutPrendre(p.getCouleur(),i+1,j-1,i+2, j-2)){
                    prendsPiece(p,i+1,j-1,i+2, j-2,1,-1);
                    //return;
                }
            } else if (i>1 && j<NBCASES-2 && joueur == p.getCouleur()&& p.getCouleur() == -2){
                if (peutPrendre(p.getCouleur(),i-1, j+1, i-2, j+2)){
                    prendsPiece(p, i-1, j+1, i-2, j+2,-1,1);
                    //return;
                }else if (i>1 && j>1 && peutPrendre(p.getCouleur(),i-1, j-1, i-2, j-2)){
                    prendsPiece(p, i-1, j-1, i-2, j-2,-1,-1);
                    //return;
                }
            }
        }
    }

    public void strategieNaive(int joueur){

        ArrayList<Piece> piecesPrioritaires = new ArrayList<Piece>();

        CoupObligatoire(piecesPrioritaires, joueur);
        Iterator it = pieces.iterator();

        while (it.hasNext() && coupsRestants>0){
            Piece p = (Piece)it.next();
            int i = p.getPosition().getLigne();
            int j = p.getPosition().getColonne();

            if (!piecesPrioritaires.contains(p)){
                if (p.getCouleur() == joueur && joueur == 2){

                    if (i < NBCASES-1){

                        if (i<NBCASES-2 && j<NBCASES-2 && peutPrendre(p.getCouleur(), i+1, j+1, i+2, j+2)){
                            prendsPiece(p, i+1, j+1, i+2, j+2,1,1);
                            coupsRestants--;
                        }else if (j<NBCASES-1 && ((CaseBlanche)cases[i+1][j+1]).isLibre()){
                            deplacePiece(p, i, j, i+1, j+1);
                            coupsRestants--;
                        }else if (j>1 && i<NBCASES-2 && peutPrendre(p.getCouleur(), i+1, j-1, i+2, j-2)){
                            prendsPiece(p, i+1, j-1, i+2, j-2,1,-1);
                            coupsRestants--;
                        }else if(j>0 && ((CaseBlanche)cases[i+1][j-1]).isLibre()){
                            deplacePiece(p, i, j, i+1, j-1);
                            coupsRestants--;
                        }
                    }

                }else if (p.getCouleur() == joueur && joueur == -2){
                    if (i>0){

                        if (i>1 && j<NBCASES-2 && peutPrendre(p.getCouleur(), i-1, j+1, i-2, j+2)){
                            prendsPiece(p, i-1, j+1, i-2, j+2,-1,1);
                            coupsRestants--;
                        }else if (j<NBCASES-1 && ((CaseBlanche)cases[i-1][j+1]).isLibre()){
                            deplacePiece(p, i, j, i-1, j+1);
                            coupsRestants--;
                        }else if (i>1 && j>1 && peutPrendre(p.getCouleur(), i-1, j-1, i-2, j-2)){
                            prendsPiece(p, i-1, j-1, i-2, j-2,-1,-1);
                            coupsRestants--;
                        }else if(j>0 && ((CaseBlanche)cases[i-1][j-1]).isLibre()){
                            deplacePiece(p, i, j, i-1, j-1);
                            coupsRestants--;
                        }

                    }
                }
            }
        }
        setCoupsRestants(2);
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
