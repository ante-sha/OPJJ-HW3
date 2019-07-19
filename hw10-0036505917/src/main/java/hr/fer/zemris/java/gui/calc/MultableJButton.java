package hr.fer.zemris.java.gui.calc;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.util.Objects;

import javax.swing.JButton;

/**
 * Razred koji modelira apstraktni promjenjivi gumb
 * 
 * @author Ante Miličević
 *
 */
public abstract class MultableJButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Naziv prvog stanja
	 */
	private String name1;
	/**
	 * Naziv drugog stanja
	 */
	private String name2;
	/**
	 * Zastavica koja označava koje stanje je trenutno aktivno
	 */
	private boolean oneActive = true;
	
	/**
	 * Konstruktor koji definira nazive stanje
	 * @param name1 naziv prvog stanja
	 * @param name2 naziv drugog stanja
	 * 
	 * @throws NullPointerException ako je jedno od naziva null
	 */
	public MultableJButton(String name1, String name2) {
		super(name1);
		this.name1 = Objects.requireNonNull(name1);
		this.name2 = Objects.requireNonNull(name2);
	}
	
	/**
	 * Metoda koja vraća zastavicu aktivnosti prvog stanja
	 * 
	 * @return oneActive
	 */
	public boolean isOneActive() {
		return oneActive;
	}
	
	/**
	 * Metoda za promjenu zastavice aktivnog stanja
	 */
	public void changeOperator() {
		oneActive = !oneActive;
		setText(oneActive ? name1 : name2);
	}
	
	@Override
	public Dimension getPreferredSize() {
		String name = name1.length() > name2.length() ? name1 : name2;
		FontMetrics fm = this.getFontMetrics(getFont());
		Insets in = getInsets();
		return new Dimension(fm.stringWidth(name) + in.left + in.right, fm.getHeight());
	}
}
