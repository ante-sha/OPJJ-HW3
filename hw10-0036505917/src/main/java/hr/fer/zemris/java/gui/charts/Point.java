package hr.fer.zemris.java.gui.charts;

/**
 * Pomoćna struktura za zapis točke
 * 
 * @author Ante Miličević
 *
 */
public class Point {
	/**
	 * Vrijednost na x osi
	 */
	public int x;
	/**
	 * Vrijednost na y osi
	 */
	public int y;
	
	/**
	 * Konstruktor
	 * 
	 * @param x
	 * @param y
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Metoda za kloniranje točke
	 */
	public Point clone() {
		return new Point(x,y);
	}
}