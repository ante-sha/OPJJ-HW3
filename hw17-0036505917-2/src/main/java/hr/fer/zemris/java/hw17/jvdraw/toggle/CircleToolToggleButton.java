package hr.fer.zemris.java.hw17.jvdraw.toggle;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.Circle;

/**
 * Razred koji modelira alat za crtanje kružnice u JVDraw aplikaciji.
 * 
 * @author Ante Miličević
 *
 */
public class CircleToolToggleButton extends ToolAdapter {
	private static final long serialVersionUID = 1L;

	/**
	 * Model koji sadržava sve nacrtane objekte
	 */
	private DrawingModel dModel;
	/**
	 * Objekt koji sadrži trenutnu boju iscrtavanja
	 */
	private IColorProvider color;
	/**
	 * Objekt na kojem se iscrtava dužina
	 */
	private JDrawingCanvas canvas;
	
	/**
	 * Točka u kojoj se nalazi centar kružnice
	 */
	private Point center;
	/**
	 * Polumjer kružnice
	 */
	private double radius;
	
	/**
	 * Konstruktor
	 * 
	 * @param dModel model koji sadrži sve objekte
	 * @param color objekt koji nudi trenutnu boju
	 * @param canvas okvir u kojem se crtaju objekti
	 */
	public CircleToolToggleButton(DrawingModel dModel, IColorProvider color, JDrawingCanvas canvas) {
		setText("Circle");
		this.dModel = Objects.requireNonNull(dModel);
		this.color = Objects.requireNonNull(color);
		this.canvas = Objects.requireNonNull(canvas);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(center == null) {
			center = e.getPoint();
			radius = 0;
		} else {
			dModel.add(new Circle(center, radius, color.getCurrentColor()));
			center = null;
		}
		canvas.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(center == null) {
			return;
		}
		Point end = e.getPoint();
		radius = Point.distance(center.getX(), center.getY(), end.getX(), end.getY());
		canvas.repaint();
	}

	@Override
	public void paint(Graphics2D g) {
		if(center == null) {
			return;
		}
		g.setColor(color.getCurrentColor());
		int diameter = (int) (2*radius);
		g.drawOval((int)(center.getX() - radius), (int)(center.getY() - radius), diameter, diameter);
	}
}
