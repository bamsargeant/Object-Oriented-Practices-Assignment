package dvonn.codebundle3;

import java.awt.Polygon;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;


@SuppressWarnings("serial")
public class Space extends Polygon {

	public static int size = 40;
	public static int xjump = (int)(Math.sin(1.04719755)*size);
	public static int yjump = (int)(Math.cos(1.04719755)*size);
	public static int xgap = xjump;
	public static int ygap = size + yjump;
	public int x;
	public int y;
	public boolean connected = false;
	public int value = 0;
	public boolean vunerable = false;
	
	Stack state;
	Map<Integer, Vector<Space>> neighbours;

	public Polygon translated(int x, int y){
		Polygon ret = new Polygon(xpoints, ypoints, npoints);
		ret.translate(-1*x,  -1*y);
		return ret;
	}
	public Space(int x, int y){
		this.x = x;
		this.y = y;
		state = new Stack(this);

		addPoint(x - xjump, y + yjump);
		addPoint(x , y + size);
		addPoint(x + xjump, y + yjump);
		addPoint(x + xjump, y);
		addPoint(x + xjump, y - yjump);
		addPoint(x, y - size);
		addPoint(x - xjump, y - yjump);
		
		neighbours = new HashMap<Integer, Vector<Space>>();
		neighbours.put(0, new Vector<Space>());// no neighbours at distance 0;
	}

	public void addPiece(Piece p){
		state.add(p);
	}
	
	public void addNeighbour(int distance, Space s){
      if (neighbours.containsKey(new Integer(distance))){
	    neighbours.get(new Integer(distance)).add(s);
      } else{
    	  Vector<Space> v = new Vector<Space>();
    	  v.add(s);
          neighbours.put(new Integer(distance),  v);
      }
	}
	
	public Space[] getNeighbours(int distance){
		if (neighbours.containsKey(distance))
          return neighbours.get(new Integer(distance)).toArray(new Space[0]);
		else
			return new Space[0];
	}
	
	public int getHeight(){return state.size();}

	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
		g2.setColor(Palette.BUFF);
		g2.fillPolygon(this);
		g2.setColor(Color.BLACK);
		g2.drawPolygon(this);			
		state.paint(g2);
	}
	public void paintAsSource(Graphics g){
		
	}
	public void paintAsTarget(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
		g2.setColor(Palette.SPECIAL);
		g2.fill(new Ellipse2D.Double(this.x, this.y, 10,10));
	}
	public boolean hasPiece() {
		return state.size() > 0;
	}
	public boolean inPlay() {
		return true;
	}
	
	public Stack liftStack(){
		Stack tmp = state;
		state = new Stack(this);
		return tmp;
	}
	
	public void dropStack(Stack add){
		state.addAll(add);
	}
	
	public String toString(){return String.valueOf("("+ this.getBoundingBox().x) + "," + String.valueOf(this.getBoundingBox().y) +")";}
	
	public Piece getStackOwner(){
		return state.elementAt(getHeight()-1);
	}
	
	public void setConnected(boolean c){
		connected = c;
	}

}