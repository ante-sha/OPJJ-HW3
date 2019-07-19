package coloring.algorithms;

import java.util.Objects;

/**
 * Razred modelira jedan piksel s koordinatama x i y
 * 
 * @author Ante Miličević
 *
 */
public class Pixel {

	/**
	 * x koordinata piksela
	 */
	private int x;
	/**
	 * y koordinata piksela
	 */
	private int y;

	/**
	 * Konstruktor koji inicijalizira koordinate piksela
	 * 
	 * @param x
	 * @param y
	 */
	public Pixel(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter za x koordinatu
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter za y koordinatu
	 * 
	 * @return y
	 */
	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return String.format("(%d,%d)", x, y);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Pixel))
			return false;
		Pixel other = (Pixel) obj;
		return x == other.x && y == other.y;
	}
}
