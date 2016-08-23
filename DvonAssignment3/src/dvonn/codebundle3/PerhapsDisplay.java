package dvonn.codebundle3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class PerhapsDisplay{
	private String val = "";
	private boolean active = false;
	Point pos;

	public PerhapsDisplay(String s, int x, int y){val = s; pos = new Point(x,y);}
	public void toggle(){active = !active;}
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g.setColor(Color.BLACK);
		g2.drawString("[-]", pos.x, pos.y);
		if (active){
		    g2.drawString(val, pos.x+20, pos.y);
		}
	}
	public void setVal(String s){val = s;}
	public void mouseClicked(MouseEvent e){
		if (pos.distance(e.getPoint()) < 20)
			toggle();
	}
}
