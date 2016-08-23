package dvonn.codebundle3;

import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import dvonn.codebundle3.Board.Phase;

public class HumanPlayer implements Player{
  Board b = null;
  PlayMove[] movesInPlay;
  Space lastVacatedSpace = new NullSpace();
  boolean validMove = false;

  public HumanPlayer(Board b){
	  this.b = b;
  }
  
  public void movePlease(Phase phase, ValidMoves vm){}
  
  public void mouseClicked(MouseEvent e) {
	  if (b.phase() == Phase.Placement){
			for (Space s: b){
			  if (s.contains(e.getPoint()))
				  if(!s.hasPiece()){
				    s.addPiece(b.toPlace.remove(0));
			        b.swapPlayer();
				  }
				  else
					java.awt.Toolkit.getDefaultToolkit().beep();
			}
		  }
	}
  
  public void mousePressed(MouseEvent e) {
		// get valid moves for whole board
		ValidMoves vm = new ValidMoves(b);
		if (b.phase() == Phase.Play){
				for (Space s: b){
					if(b.liftedStack.isEmpty()){
					  if (s.contains(e.getPoint())){
						  if(s.hasPiece()){
							  if((b.white == b.nextPlayer && s.getStackOwner() == Piece.WHITE) || (b.black == b.nextPlayer && s.getStackOwner() == Piece.BLACK)){
								    movesInPlay = vm.movesFor(s);
								    b.liftedStack = s.liftStack();
								    b.lastLiftedLoc = e.getPoint();
								    lastVacatedSpace = s;
							  }
						  }
					  }
					}
				}
		  }
  }
  
  public void mouseReleased(MouseEvent e){
	  if (b.phase() == Phase.Play){
		  for (Space s : b){
			  if (s.contains(e.getPoint())){
				  if(s.hasPiece()){
					  if(!b.liftedStack.isEmpty()){
						  // was this move in play?
						  validMove = false;
						  for (PlayMove m: movesInPlay){
							  if (m.to.equals(s))
								  validMove = true;
						  }
						  if (validMove){
					        s.dropStack(b.liftedStack);
					        b.liftedStack = new NullStack();
					        b.swapPlayer();
						  } 
					  }
				  }
			  }
			}
			  if(!validMove){
				  lastVacatedSpace.dropStack(b.liftedStack); 
				  b.liftedStack = new NullStack();
			  }
		  }
	  }
  }
