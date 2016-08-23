package dvonn.codebundle3;

public class PlayMove implements Move {
	Space from;
	Space to;
	
	public PlayMove(Space in, Space out){from = in; to = out;}
	public String toString(){return from.toString() + " -> "+ to.toString();}
	public void perform(){to.dropStack(from.liftStack());}

}
