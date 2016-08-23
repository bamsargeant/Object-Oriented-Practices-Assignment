package dvonn.codebundle3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;


@SuppressWarnings("serial")
public class Board extends JPanel implements Iterable<Space>, MouseListener, MouseMotionListener, KeyListener {
    
    Space[][] spaces = new Space[11][];
    Point a1 = new Point(150,100);
    Vector<Piece> toPlace;
    public enum Phase {Placement, Play};
	Player white = null;
	Player black = null;
	Player suggester = null;
	Player nextPlayer = null;
	PerhapsDisplay notification = new PerhapsDisplay("", 10, 20);
	PerhapsDisplay suggestion = new PerhapsDisplay("", 10, 50);

    
    int xindex, yindex;
    Point lastDrag = new Point(0,0);
    Stack liftedStack = new NullStack();
    Point lastLiftedLoc = new Point(0,0);

    public Board(){
    	
      white = new SimpletonBot(this);
//      black = new SimpletonBot(this);
//      white = new HumanPlayer(this);
      black = new HumanPlayer(this);      
      
      //suggester = new SimpletonBot(this);
      
      nextPlayer = black;
      
	  for (int i = 0; i < spaces.length; i++){
	  		spaces[i] = new Space[5];
	  }

	  // create spaces with correct locations;
      for (int i =0; i< spaces.length; i++) {
      	for (int j = 0; j< spaces[i].length; j++){
      		if (j == 0 && ( i == 9 || i == 10))
      			spaces[i][j] = new NullSpace();
      		else if (j == 1 && (i == 10))
      			spaces[i][j] = new NullSpace();
      		else if (j == 3 && (i == 0))
      			spaces[i][j] = new NullSpace();
      		else if (j == 4 && (i == 0 || i == 1))
      			spaces[i][j] = new NullSpace();
      		else {
      	  int starty = a1.y;
      	  int startx = a1.x;
      	  spaces[i][j] = new Space(startx + i * Space.xgap*2 - j*(Space.xgap), starty + j*Space.ygap);
      		}
      	}
      }

      // tell the spaces who their neighbours are (works so far for distance 1)
      for (int i =0; i < spaces.length; i++){
    	  for (int j = 0; j < spaces[i].length; j++){
    		  int[][] initVectors = {{-1,-1},{-1,0},{0,1},{1,1},{1,0},{0, -1}};
    		  int[] mults = {1,2,3,4,5,6,7,8,9,10,11};
    		  for (int mult: mults ){
    			int[][] vectors = pointWiseMult(mult, initVectors);
                for (int[] vector: vectors){
            	  int possx = i + vector[0];
            	  int possy = j + vector[1];
            	  if (isInBoard(possx, possy))
            	    spaces[i][j].addNeighbour(mult, spaces[possx][possy]);
                }
    		  }
    	  }
      }
      
      liftedStack = new Stack(new NullSpace());
      toPlace = new Vector<Piece>();
      for(int i = 0; i < 3; i++){toPlace.add(Piece.DVONN);}
      for(int i = 0; i < 23; i++){toPlace.add(Piece.WHITE); toPlace.add(Piece.BLACK);}
      setSize(new Dimension(845,446));
      this.addMouseListener(this);
      this.addKeyListener(this);
      this.addMouseMotionListener(this);
      this.setFocusable(true);
      this.requestFocusInWindow();
	}

    private boolean isInBoard(int possx, int possy){
  	  if (possx >=0 && possx < spaces.length)
  		  if (possy >= 0 && possy < spaces[possx].length)
  			  return true;
  	  return false;
    }
    
    private int[][] pointWiseMult(int m, int[][] vectors){
    	int[][] result = new int[vectors.length][];
    	for (int i = 0; i < result.length; i++){
    		result[i] = new int[vectors[i].length];
    	}
    	for (int i =0; i< vectors.length; i++){
    		for (int j = 0; j < vectors[i].length; j++)
    			result[i][j] = m*vectors[i][j];
    	}
    	return result;
    }

    public Phase phase(){
    	if (toPlace.size() == 0)
    		return Phase.Play;
    	else
    		return Phase.Placement;
    }
    
    void swapPlayer(){
    	if (nextPlayer == white){
    		nextPlayer = black;
    		notification.setVal("black's turn");
    	}else{
    		nextPlayer = white;
    		notification.setVal("white's turn");
    	}
    	if(phase() == Phase.Play)
    		markAndSweep();
        this.repaint();
    }
    
	public void paint(Graphics g){
		ValidMoves vm = new ValidMoves(this);
		
		// check winning conditions
		if (phase() == Phase.Play && vm.allMoves().length == 0){
			swapPlayer();
			if (phase() == Phase.Play && vm.allMoves().length == 0)
				notification.setVal("Game Over");
		}
		g.setColor(Palette.BUFF);
		g.fillRect(0,0, getWidth(), getHeight());

		g.setColor(Color.BLACK);
		for (Space[] row : spaces){
			for (Space s: row){
			  s.paint(g);
			}
		}
		
		notification.paint(g);
		
		liftedStack.paint(g, lastLiftedLoc.x - lastDrag.x, lastLiftedLoc.y - lastDrag.y);
        if (phase() == Phase.Placement || vm.allMoves().length > 0){
        	nextPlayer.movePlease(phase(), vm);
        }
	}

	public void mouseClicked(MouseEvent e) {
	  nextPlayer.mouseClicked(e);
	  notification.mouseClicked(e);
	  this.repaint();
	}
	public void mousePressed(MouseEvent e) {
		nextPlayer.mousePressed(e);
		this.repaint();
	}
	public void mouseReleased(MouseEvent e) {
	  nextPlayer.mouseReleased(e);
	  this.repaint();
    }
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {
		lastDrag = e.getPoint();	
		this.repaint();
	}
	public void mouseMoved(MouseEvent e) {}
	
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == 'f'){
			for (Space[] row : spaces){
				for (Space s: row){
				  if(!s.hasPiece() && s.inPlay())
					s.addPiece(toPlace.remove(0));
				}
			  }
		}
		this.repaint();
	}
	
	public void markAndSweep(){
		for(Space s : this){
			s.setConnected(false);
		}
		
		for(Space s : this){
			if(s.state.contains(Piece.DVONN)){ // if a red piece
				s.setConnected(true);
				markNeighbours(s);
			}
		}
		for(Space s : this){
			if(!s.connected && s.hasPiece()){
				s.state.removeAllElements();
			}
		}
	}
	
	public void markNeighbours(Space neighbourino){
		for(Space n : neighbourino.getNeighbours(1)){
	    		if(n.hasPiece()){
					if(!n.connected){
		    			n.setConnected(true);
		    			markNeighbours(n);
					}
	    		}
    	}
	}

	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	public Iterator<Space> iterator() {
		return new BoardIterator(this);
	}
}