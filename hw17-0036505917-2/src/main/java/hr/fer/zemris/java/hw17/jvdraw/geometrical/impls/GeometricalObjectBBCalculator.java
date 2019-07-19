package hr.fer.zemris.java.hw17.jvdraw.geometrical.impls;

import java.awt.Point;
import java.awt.Rectangle;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectVisitor;

/**
 * Razred koji implementira OO Visitor. Primjerak ovog razreda računa bounding
 * box svih geometrijskih objekata.
 * 
 * @author Ante Miličević
 *
 */
public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {

	/**
	 * Točka gornje lijevog kuta
	 */
	private Point min;
	/**
	 * Točka doljnje desnog kuta
	 */
	private Point max;

	/**
	 * Getter za minimum
	 * 
	 * @return min
	 */
	public Point getMin() {
		return min;
	}

	/**
	 * Getter za maksimum
	 * 
	 * @return max
	 */
	public Point getMax() {
		return max;
	}

	/**
	 * Metoda koja računa i vraća bounding box za posjećene geometrijske objekte
	 * 
	 * @return bounding box
	 */
	public Rectangle getBoundingBox() {
		if (min == null || max == null) {
			return new Rectangle(0, 0, 0, 0);
		}

		return new Rectangle(min.x, min.y, max.x - min.x, max.y - min.y);
	}

	@Override
	public void visit(Line line) {
		Point start = line.getStartPoint();
		checkForMin(start);
		checkForMax(start);

		Point end = line.getEndPoint();
		checkForMin(end);
		checkForMax(end);
	}

	/**
	 * Metoda koja uspoređuje trenutnu točku maksimuma s točkom point i na osnovu
	 * toga mijenja podatke točke maksimuma ako je bilo koja komponenta u točki
	 * point veća
	 * 
	 * @param point točka
	 */
	private void checkForMax(Point point) {
		if (max == null) {
			max = new Point(point.x, point.y);
		}
		if (point.getX() > max.getX()) {
			max.x = point.x;
		}
		if (point.getY() > max.getY()) {
			max.y = point.y;
		}
	}

	/**
	 * Metoda koja uspoređuje trenutnu točku minimuma s točkom point i na osnovu
	 * toga mijenja podatke točke minimuma ako je bilo koja komponenta u točki
	 * point veća
	 * 
	 * @param point točka
	 */
	private void checkForMin(Point point) {
		if (min == null) {
			min = new Point(point.x, point.y);
		}
		if (point.getX() < min.getX()) {
			min.x = point.x;
		}
		if (point.getY() < min.getY()) {
			min.y = point.y;
		}
	}

	@Override
	public void visit(Circle circle) {
		visitCircle(circle.getCenter(), (int) circle.getRadius());
	}

	/**
	 * Metoda za posjećivanje kružnice
	 * 
	 * @param center točka središta
	 * @param radius polumjer kružnice
	 */
	private void visitCircle(Point center, int radius) {
		Point min = new Point(center.x - radius, center.y - radius);
		checkForMin(min);
		Point max = new Point(center.x + radius, center.y + radius);
		checkForMax(max);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		visitCircle(filledCircle.getCenter(), (int) filledCircle.getRadius());
	}

}
