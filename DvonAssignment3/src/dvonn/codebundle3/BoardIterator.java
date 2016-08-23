package dvonn.codebundle3;

import java.util.Iterator;

public class BoardIterator implements Iterator<Space> {

	Board board;
	int xindex, yindex;
	
	public BoardIterator(Board b){
		this.board = b;
		xindex = -1; yindex = 0;
	}
	public boolean hasNext() {
		return !(xindex == 10 && yindex == 4) ;
	}

	public Space next() {
		if ((yindex == 0 && xindex < 8) || (yindex == 1 && xindex < 9) || (yindex >1 && xindex < 10)){
			xindex++;
		} else
			if (yindex < 2){
			  xindex = 0;
		      yindex++;
			} else if (yindex == 2){
				  xindex = 1;
			      yindex++;				
			} else if (yindex == 3){
				  xindex = 2;
			      yindex++;
			} else {
				//chuck a spacca
				return null;
			}
	    return board.spaces[xindex][yindex];
	}

	public void remove() {
		// no-op becuase you can't remove board spaces
	}

}
