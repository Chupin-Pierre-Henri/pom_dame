package version1;

public class CaseBlanche extends Case{
    private Piece piece;


    public CaseBlanche(int i,int j) {
        super(i,j);
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean isLibre(){
        if (piece == null)
            return true;
        else
            return false;
    }
}
