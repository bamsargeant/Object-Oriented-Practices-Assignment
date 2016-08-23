package dvonn.codebundle3;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

import dvonn.codebundle3.Board.Phase;

public class SimpletonBot implements Player, Runnable{
    private Board board;
    Thread botThread = null;
    Phase phase = null;
    ValidMoves vm = null;
    Vector<Space> Stacks;
    
    private volatile boolean mRunning;
    
	public SimpletonBot(Board b){
		mRunning = false;
		board = b;
		start();
	}

	public void movePlease(Phase p, ValidMoves v) {
		start();
	}

	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	public void start(){
		if (mRunning) {
			return;
		}
		else{
			botThread = new Thread(this);
			botThread.start();
			mRunning = true;
		}
	}
	
	public void stop(){
		if(!mRunning)
			return;
		else{
			mRunning = false;
			botThread.interrupt();
			try {
				botThread.join();
			} catch (InterruptedException eExn) {
				eExn.printStackTrace();
			}
		}
	}
	
	public boolean isRunning(){
		return mRunning;
	}
	
	public void setPhase(){
		phase = board.phase();
	}
	
	public void setValidMoves(){
		vm = new ValidMoves(board);
		
	}
	

	@SuppressWarnings("null")
	public void calculateMove()
	{
	
		Piece myColour;
		Piece enemyColour;
		if(board.nextPlayer == board.white)
		{
			myColour = Piece.WHITE;
			enemyColour = Piece.BLACK;
		}
		else
		{
			myColour = Piece.BLACK;
			enemyColour = Piece.WHITE;
		}
		
		Stacks = new Vector<Space>();
		for(Space s:board)
		{
			if(s.hasPiece() == false)
				continue;
			if(s.state.elementAt(s.state.size()-1) == myColour)
			{
				s.value = s.state.size()-1;		// get tower value
				if(s.state.contains(Piece.DVONN))	// if DVONN present, add on to tower value
					s.value +=2;
				for(Space n: s.getNeighbours(1))	// iterate through all immediate neighbours
				{
					if(n.hasPiece() == false)
						continue;
					if(n.state.elementAt(n.getHeight()-1) == enemyColour)
					{
						s.vunerable = true;	
						break;
					}
				}
				Stacks.add(s);
			}
		}
		Space temp = Stacks.firstElement();
		temp.value = 0;
		for(Space s: Stacks)
		{
			if(s.vunerable)
			{
				if(s.value > temp.value)
					if(vm.movesFor(s).length >0 )
						temp=s;
			}
		}
		
		try{vm.movesFor(temp)[0].perform();}
		catch(Exception e){}
	}
	
//	public void placePieces(){
//	// Make this class set the pieces on the board. Start with DVONN pieces as
//	// away from each other as possible, then try to set pieces near enemy pieces
//	// using getNeighbours
//
//	for(Space s : board){
//		if(!s.hasPiece()){
//			if(s)){
//				
//			}
//		}
//		
//	}
//}

	@Override
	public void run() {
		while(mRunning){
			if(board.nextPlayer == this){
				System.out.println("Hello from a thread!");
				
				try{Thread.sleep(30);}catch(Exception e){}
				setPhase();
				setValidMoves();
				if (phase == Phase.Placement){
					vm.allPlacements()[0].perform();
					board.swapPlayer();
					mRunning = false;
				} else {
					try{Thread.sleep(30);}catch(Exception e){}
					try{calculateMove();}catch(Exception e){}
					board.swapPlayer();
					mRunning = false;
				}
			}
		}
		stop();
	}

}
