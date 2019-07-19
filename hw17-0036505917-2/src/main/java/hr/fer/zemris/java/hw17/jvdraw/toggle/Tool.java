package hr.fer.zemris.java.hw17.jvdraw.toggle;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Implementacija OO State. Sučelje definira alat za crtanje objekta određene
 * vrste.
 * 
 * @author Ante Miličević
 *
 */
public interface Tool {

	/**
	 * Akcija koja se odvija dok je lijevi klik miša pritisnut.
	 * 
	 * @param e objekt s informacijama o događaju
	 */
	public void mousePressed(MouseEvent e);

	/**
	 * Akcija koja se odvija kada je lijevi klik miša otpušten.
	 * 
	 * @param e objekt s informacijama o događaju
	 */
	public void mouseReleased(MouseEvent e);

	/**
	 * Akcija koja se odvija dok je lijevi klik miša kliknut.
	 * 
	 * @param e objekt s informacijama o događaju
	 */
	public void mouseClicked(MouseEvent e);

	/**
	 * Akcija koja se odvija pri promjeni pozicije miša.
	 * 
	 * @param e objekt s informacijama o događaju
	 */
	public void mouseMoved(MouseEvent e);

	/**
	 * Akcija koja se odvija pri promjeni pozicije miša 
	 * dok je miš stisnut.
	 * 
	 * @param e objekt s informacijama o događaju
	 */
	public void mouseDragged(MouseEvent e);

	/**
	 * Metoda koja se poziva prilikom bojanja komponente
	 * koja sadržava sve objekte. U ovoj metodi alat crta
	 * dosadašnji napredak korisnikovog crtanja.
	 * 
	 * @param g2d objekt za crtanje
	 */
	public void paint(Graphics2D g2d);
}
