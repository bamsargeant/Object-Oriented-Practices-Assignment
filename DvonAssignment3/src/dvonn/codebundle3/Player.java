package dvonn.codebundle3;

import java.awt.event.MouseEvent;

import dvonn.codebundle3.Board.Phase;

public interface Player{
	public abstract void movePlease(Phase phase, ValidMoves vm);
    public abstract void mouseClicked(MouseEvent e);
    public abstract void mousePressed(MouseEvent e);
    public abstract void mouseReleased(MouseEvent e);
}
