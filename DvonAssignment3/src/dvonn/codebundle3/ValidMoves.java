package dvonn.codebundle3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ValidMoves {
	Map<Space, PlayMove[]> plays;
	Vector<PlaceMove> placements;
	
	// generates all the valid moves from a board
	public ValidMoves(Board b){
		plays = new HashMap<Space, PlayMove[]>();
		placements = new Vector<PlaceMove>();
		for (Space s: b){
			
			if (s.getHeight() == 0){
				//System.out.println("height 0");
				placements.add(new PlaceMove(s,b.toPlace));
			} else {
				// find out how many non-empty neighbours this space has
				// only proceed if there are less than 6
				Space[] rightNextTo = s.getNeighbours(1);

				boolean validMove = true;
				if(b.nextPlayer == b.white){
					if(s.getStackOwner() != Piece.WHITE){
						validMove = false;
					}
				}
				else{
					if(s.getStackOwner() != Piece.BLACK){
						validMove = false;
					}
				}
				
				int countNeighbours = 0;
				for (int i =0; i < rightNextTo.length; i++)
					if (rightNextTo[i].getHeight() > 0)
						countNeighbours++;
				if (countNeighbours < 6){

					Space[] spaceSet = s.getNeighbours(s.getHeight());
					Vector<Space> tmpSpaceSet = new Vector<Space>();
					for (int i =0; i < spaceSet.length; i++){
						if (spaceSet[i].getHeight() > 0)
							tmpSpaceSet.add(spaceSet[i]);
					}
					PlayMove[] moveSet = new PlayMove[tmpSpaceSet.size()];
					for (int i = 0; i < tmpSpaceSet.size(); i++){
						moveSet[i] = new PlayMove(s, tmpSpaceSet.get(i));
					}
					if(validMove)
						plays.put(s, moveSet);
				} else {
					plays.put(s, new PlayMove[0]);
				}
			}
		}
	}

	public PlayMove[] allMoves(){
		Vector<PlayMove> ret = new Vector<PlayMove>();
		for (Map.Entry<Space, PlayMove[]> entry: plays.entrySet()){
			ret.addAll(Arrays.asList(entry.getValue()));
		}

		return ret.toArray(new PlayMove[0]);
	}
	public PlayMove[] movesFor(Space s){return plays.get(s);}
	public PlaceMove[] allPlacements(){return  placements.toArray(new PlaceMove[0]);}
}
