package hr.fer.zemris.java.hw17.jvdraw.geometrical.impls;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectVisitor;

/**
 * Razred koji implementira OO Visitor. Za svaki geometrijski objekt kojeg se
 * prihvaća crta se njegov oblik u Graphics2D predan u konstruktoru.
 * 
 * @author Ante Miličević
 *
 */
public class GeometricalObjectPainter implements GeometricalObjectVisitor {

	/**
	 * Objekt za crtanje
	 */
	private Graphics2D g;
	
	/**
	 * Konstruktor
	 * 
	 * @param g objekt za crtanje
	 */
	public GeometricalObjectPainter(Graphics2D g) {
		this.g = Objects.requireNonNull(g);
	}
	
	@Override
	public void visit(Line line) {
		g.setColor(line.getColor());
		g.drawLine((int)line.getStartPoint().getX(), (int)line.getStartPoint().getY(),
				(int)line.getEndPoint().getX(), (int)line.getEndPoint().getY());
	}

	@Override
	public void visit(Circle circle) {
		g.setColor(circle.getColor());
		
		Point center = circle.getCenter();
		int radius = (int)circle.getRadius();
		int diameter = (int) (2*circle.getRadius());
		
		g.drawOval((int)(center.getX() - radius), (int)(center.getY() - radius), diameter, diameter);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		Point center = filledCircle.getCenter();
		int radius = (int)filledCircle.getRadius();
		int diameter = (int) (2*filledCircle.getRadius());
		
		g.setColor(filledCircle.getFgColor());
		g.drawOval((int)(center.getX() - radius), (int)(center.getY() - radius), diameter, diameter);
		
		g.setColor(filledCircle.getBgColor());
		g.fillOval((int)(center.getX() - radius), (int)(center.getY() - radius), diameter, diameter);
	}

}
