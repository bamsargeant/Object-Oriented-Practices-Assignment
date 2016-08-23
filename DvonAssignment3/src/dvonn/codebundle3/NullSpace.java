package dvonn.codebundle3;


import java.awt.Graphics;

@SuppressWarnings("serial")
public class NullSpace extends Space {
	public NullSpace(){super(0,0);}
	public void paint(Graphics g){
		// don't paint
	}
	public boolean inPlay(){return false;}//never in play
}
