package hr.fer.zemris.java.gui.charts;

/**
 * Razred definira jedan stupac u histogramu modeliranim s {@link BarChart}.
 * 
 * @author Ante Miličević
 *
 */
public class XYValue {

	/**
	 * Pozicija na x osi
	 */
	private int x;
	/**
	 * Vrijednost na y osi
	 */
	private int y;

	/**
	 * Konstruktor
	 * 
	 * @param x
	 * @param y
	 */
	public XYValue(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter za x
	 * 
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter za y
	 * 
	 * @return y
	 */
	public int getY() {
		return y;
	}

}
