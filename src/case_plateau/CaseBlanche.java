package case_plateau;

import piece.Piece;

public class CaseBlanche extends Case {
    private Piece piece;

    public CaseBlanche(int i, int j) {
        super(i, j);
    }

    @Override
    public void copieCase(Case c) {
        if (!((CaseBlanche) c).isLibre()) {
            piece = new Piece(((CaseBlanche) c).getPiece());
            piece.setPosition(this);
        } else {
            piece = null;
            setLigne(c.getLigne());
            setColonne(c.getColonne());
        }

    }

    /**
     * permet de récupérer la piece sur la case blanche
     * @return la pièce présente sur la case
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * change la pièce sur la case
     * @param piece la nouvelle pièce de la case
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
        if (piece != null) {
            piece.setPosition(this);
        }
    }

    /**
     * permet de savoir si la case est vide de pièce ou non
     * @return true si la case est libre et false si elle est occupé par une pièce
     */
    public boolean isLibre() {
        return piece == null;
    }
}
