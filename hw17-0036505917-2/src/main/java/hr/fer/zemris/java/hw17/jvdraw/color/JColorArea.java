package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

/**
 * Komponenta za odabir boje. Na svojoj površini prikazuje boju koju čuva pod
 * imenom selectedColor.
 * 
 * @author Ante Miličević
 *
 */
public class JColorArea extends JComponent implements IColorProvider {
	private static final long serialVersionUID = 1L;
	/**
	 * Promatrači
	 */
	private List<ColorChangeListener> listeners = new CopyOnWriteArrayList<>();
	/**
	 * Odabrana boja
	 */
	private Color selectedColor;

	/**
	 * Konstrukor
	 * 
	 * @param initColor inicijalna boja
	 */
	public JColorArea(Color initColor) {
		selectedColor = Objects.requireNonNull(initColor);
		setMouseListener();
	}

	/**
	 * Metoda za postavljanje promatrača na miš tako da prilikom klika se pojavljuje
	 * JColorChooser za odabir boje.
	 */
	private void setMouseListener() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color oldColor = selectedColor;
				Color newColor = JColorChooser.showDialog(JColorArea.this.getParent(), "Choose color", selectedColor);
				if (newColor == null) {
					return;
				}
				selectedColor = newColor;
				changeColor(oldColor);
			}
		});
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(15, 15);
	}

	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		if (l != null && !listeners.contains(l)) {
			listeners.add(l);
		}
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(selectedColor);
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.dispose();
	}

	public void changeColor(Color oldColor) {
		repaint();
		for (ColorChangeListener l : listeners) {
			l.newColorSelected(this, oldColor, selectedColor);
		}
	}
}
