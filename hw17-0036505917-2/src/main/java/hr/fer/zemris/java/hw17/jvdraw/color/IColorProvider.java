package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.Color;

/**
 * Sučelje koje definira objekt za dohvat trenutne boje i omogućava
 * registriranje promatrača na isti za praćenje promjene boje.
 * 
 * @author Ante Miličević
 *
 */
public interface IColorProvider {
	/**
	 * Metoda za dohvaćanje trenutne boje
	 * 
	 * @return trenutna boja
	 */
	public Color getCurrentColor();
	/**
	 * Metoda za dodavanje promatrača
	 * 
	 * @param l promatrač
	 */
	public void addColorChangeListener(ColorChangeListener l);
	/**
	 * Metoda za uklanjanje promatrača
	 * 
	 * @param l promatrač
	 */
	public void removeColorChangeListener(ColorChangeListener l);
}
