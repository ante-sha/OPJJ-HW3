package hr.fer.zemris.java.hw17.jvdraw.toggle;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.FilledCircle;

/**
 * Razred koji modelira alata za crtanje krugova u JVDraw aplikaciji.
 * 
 * @author Ante Miličević
 *
 */
public class FilledCircleToggleButton extends ToolAdapter {
	private static final long serialVersionUID = 1L;

	/**
	 * Model koji sadržava sve nacrtane objekte
	 */
	private DrawingModel dModel;
	/**
	 * Objekt koji sadrži trenutnu boju za iscrtavanje ruba kruga
	 */
	private IColorProvider fgColor;
	/**
	 * Objekt koji sadrži trenutnu boju za iscrtavanje unutrašnost kruga
	 */
	private IColorProvider bgColor;
	/**
	 * Objekt na kojem se iscrtava dužina
	 */
	private JDrawingCanvas canvas;
	
	/**
	 * Središte kruga
	 */
	private Point center;
	/**
	 * Polumjer
	 */
	private double radius;
	
	/**
	 * Konstruktor
	 * 
	 * @param dModel model koji sadrži sve objekte
	 * @param fgColor objekt koji sadrži boju kružnice
	 * @param bgColor objekt koji sadrži boju unutrašnosti kruga
	 * @param canvas okvir u kojem se iscrtavaju objekti
	 */
	public FilledCircleToggleButton(DrawingModel dModel, IColorProvider fgColor, IColorProvider bgColor, JDrawingCanvas canvas) {
		setText("Filled Circle");
		this.dModel = Objects.requireNonNull(dModel);
		this.fgColor = Objects.requireNonNull(fgColor);
		this.bgColor = Objects.requireNonNull(bgColor);
		this.canvas = Objects.requireNonNull(canvas);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(center == null) {
			center = e.getPoint();
			radius = 0;
		} else {
			dModel.add(new FilledCircle(center, radius, fgColor.getCurrentColor(), bgColor.getCurrentColor()));
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
		g.setColor(fgColor.getCurrentColor());
		int diameter = (int)(2*radius);
		g.drawOval((int)(center.getX() - radius), (int)(center.getY() - radius), diameter, diameter);
		g.setColor(bgColor.getCurrentColor());
		g.fillOval((int)(center.getX() - radius), (int)(center.getY() - radius), diameter, diameter);
	}
}
