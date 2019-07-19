package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * Labela koja ispisuje RGB komponente trenutno izabranih boja.
 * 
 * @author Ante Miličević
 *
 */
public class ColorInfoLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Prednja boja
	 */
	private Color fgColor;
	/**
	 * Stražnja boja
	 */
	private Color bgColor;

	/**
	 * Konstruktor
	 * 
	 * @param fg objekt za odabiranje prednje boje
	 * @param bg objekt za odabiranje stražnje boje
	 */
	public ColorInfoLabel(JColorArea fg, JColorArea bg) {
		addListeners(fg,bg);
		fgColor = fg.getCurrentColor();
		bgColor = bg.getCurrentColor();
		changeText();
	}

	/**
	 * Metoda za dodavanje promatrača na objekte za odabir boje.
	 * 
	 * @param fg objekt za odabir prednje boje
	 * @param bg objekt za odabir stražnje boje
	 */
	private void addListeners(JColorArea fg, JColorArea bg) {
		fg.addColorChangeListener(new ColorChangeListener() {
			@Override
			public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
				fgColor = newColor;
				changeText();
			}
		});
		bg.addColorChangeListener(new ColorChangeListener() {
			@Override
			public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
				bgColor = newColor;
				changeText();
			}
		});
	}
	
	/**
	 * Metoda za ažuriranje teksta prilikom promjene boje
	 */
	private void changeText() {
		setText(String.format("Foreground color: (%d, %d, %d), background color: (%d, %d, %d)", 
				fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue(),
				bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue()));
		repaint();
	}
}
