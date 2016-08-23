package dvonn.codebundle3;

import java.util.Vector;

public class PlaceMove implements Move {
    Space to;
    Vector<Piece> from;
    
	public PlaceMove(Space to, Vector<Piece> from) {
		this.to = to;
		this.from = from;
	}

	@Override
	public void perform() {
		to.addPiece(from.remove(0));

	}

}
