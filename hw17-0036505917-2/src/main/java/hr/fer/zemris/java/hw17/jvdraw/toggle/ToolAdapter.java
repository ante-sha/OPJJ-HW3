package hr.fer.zemris.java.hw17.jvdraw.toggle;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import javax.swing.JToggleButton;

/**
 * Adapter Tool sučelja koji je ujedno i toggleButton kojemu su sve metode
 * prazne. Osim praznih metoda ToolAdapter nadjačava metodu getPrefferedSize na
 * odgovarajući način.
 * 
 * @author Ante Miličević
 *
 */
public abstract class ToolAdapter extends JToggleButton implements Tool {
	private static final long serialVersionUID = 1L;

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void paint(Graphics2D g2d) {
	}

	@Override
	public Dimension getPreferredSize() {
		FontMetrics fm = getFontMetrics(getFont());
		return new Dimension(
				fm.stringWidth(getText()) + getMargin().left + getMargin().right + getInsets().left + getInsets().right,
				fm.getHeight() + getMargin().bottom + getMargin().top + getInsets().top + getInsets().right);
	}
}
