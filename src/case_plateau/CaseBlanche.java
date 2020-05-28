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

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        if (piece != null) {
            piece.setPosition(this);
        }
    }

    public boolean isLibre() {
        return piece == null;
    }
}
