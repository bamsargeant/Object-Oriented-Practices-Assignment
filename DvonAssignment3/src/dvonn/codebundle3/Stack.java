package dvonn.codebundle3;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

@SuppressWarnings("serial")
public class Stack extends java.util.Vector<Piece> {
	  /**
	 * 
	 */
	private final Space space;

	public Stack(Space space){
		  super();
		this.space = space;
	  }
	  
	  public void paint(Graphics g, int offx, int offy){
		  Graphics2D g2 = (Graphics2D)g;
		  for (int i = 0; i < this.size(); i++){
				switch(this.get(i)){
				  case DVONN: g2.setColor(Palette.RED); break;
				  case WHITE: g2.setColor(Palette.WHITE); break;
				  case BLACK: g2.setColor(Palette.BLACK); break;
				  default : g2.setColor(Palette.BUFF);
				}
				Polygon ip = this.space.translated(i*5 + offx,i*5 + offy);
				g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
				g2.fillPolygon(ip);
				g2.setColor(Color.BLACK);
				g2.drawPolygon(ip);
				g2.setColor(Palette.VISIBLE);
				g2.drawString(String.valueOf(i+1), this.space.x - i*5 - offx, this.space.y - i*5 - offy);
			}	  
	  }
	  
	  public void paint(Graphics g){paint(g,0,0);}
	}