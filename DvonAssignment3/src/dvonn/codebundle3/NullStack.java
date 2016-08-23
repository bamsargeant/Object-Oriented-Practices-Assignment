package dvonn.codebundle3;

import java.awt.Graphics;

public class NullStack extends Stack {

	public NullStack() {
		super(new NullSpace());
	}
	
	public void paint(Graphics g, int offx, int offy){}

}
