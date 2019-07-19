package hr.fer.zemris.java.hw17.jvdraw.geometrical.impls;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectVisitor;

/**
 * Razred koji implementira OO Visitor. Za svaki geometrijski objekt kojeg se
 * prihvaća zapisuju se njegovi podaci u BufferedWriter kojeg se predaje u
 * konstrukoru.
 * 
 * @author Ante Miličević
 *
 */
public class GeometricalObjectSaver implements GeometricalObjectVisitor {

	/**
	 * Objekt za pisanje
	 */
	private BufferedWriter writer;
	
	/**
	 * Konstruktor
	 * 
	 * @param writer objekt za pisanje
	 */
	public GeometricalObjectSaver(BufferedWriter writer) {
		this.writer = Objects.requireNonNull(writer);
	}

	@Override
	public void visit(Line line) {
		try {
			writer.write(String.format("LINE %d %d %d %d %d %d %d%n", line.getStartPoint().x, line.getStartPoint().y,
					line.getEndPoint().x, line.getEndPoint().y, line.getColor().getRed(), line.getColor().getGreen(),
					line.getColor().getBlue()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void visit(Circle circle) {
		try {
			writer.write(String.format("CIRCLE %d %d %d %d %d %d%n", circle.getCenter().x, circle.getCenter().y,
					(int) circle.getRadius(), circle.getColor().getRed(), circle.getColor().getGreen(),
					circle.getColor().getBlue()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		try {
			writer.write(String.format("FCIRCLE %d %d %d %d %d %d %d %d %d%n", filledCircle.getCenter().x,
					filledCircle.getCenter().y, (int) filledCircle.getRadius(), filledCircle.getFgColor().getRed(),
					filledCircle.getFgColor().getGreen(), filledCircle.getFgColor().getBlue(),
					filledCircle.getBgColor().getRed(), filledCircle.getBgColor().getGreen(),
					filledCircle.getBgColor().getBlue()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
