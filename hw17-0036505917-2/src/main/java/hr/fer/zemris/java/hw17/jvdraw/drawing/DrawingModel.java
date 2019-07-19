package hr.fer.zemris.java.hw17.jvdraw.drawing;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;

/**
 * Model koji omogućuje operacije nad geometrijskim objektima i pruža sve podatke
 * o izmjeni modela i geometrijskih objekata.
 * 
 * @author Ante Miličević
 *
 */
public interface DrawingModel {
	/**
	 * Dohvat količine geometrijskih objekata
	 * 
	 * @return broj geometrijskih objekata
	 */
	public int getSize();
	/**
	 * Metoda za dohvat geometrijskog objekta s pozicije index
	 * 
	 * @param index indeks s kojeg se dohvaća objekt
	 * @return geometrijski objekt na poziciji index
	 */
	public GeometricalObject getObject(int index);
	/**
	 * Metoda za dodavanje geometrijskog objekta
	 * 
	 * @param object geometrijski objekt
	 */
	public void add(GeometricalObject object);
	/**
	 * Metoda za uklanjanje geometrijskog objekta
	 * 
	 * @param object geometrijski objekt
	 */
	public void remove(GeometricalObject object);
	/**
	 * Metoda za promjenu pozicije geometrijskog objekta
	 * 
	 * @param object geometrijski objekt
	 */
	public void changeOrder(GeometricalObject object, int offset);
	/**
	 * Metoda za dohvat indeksa geometrijskog objekta
	 * 
	 * @param object geometrijski objekt
	 */
	public int indexOf(GeometricalObject object);
	/**
	 * Metoda za čišćenje modela
	 */
	public void clear();
	/**
	 * Metoda za postavljanje zastavice modified na false
	 */
	public void clearModifiedFlag();
	/**
	 * Dohvat modified zastavice
	 * 
	 * @return modified
	 */
	public boolean isModified();
	/**
	 * Metoda za dodavanje promatrača na model
	 * 
	 * @param l promatrač
	 */
	public void addDrawingModelListener(DrawingModelListener l);
	/**
	 * Metoda za uklanjanje promatrača na modelu
	 * 
	 * @param l promatrač
	 */
	public void removeDrawingModelListener(DrawingModelListener l);
}
