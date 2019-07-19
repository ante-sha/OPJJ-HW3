package hr.fer.zemris.java.hw17.jvdraw.toggle;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.Line;

/**
 * Razred koji modelira alata za crtanje dužina u JVDraw aplikaciji.
 * 
 * @author Ante Miličević
 *
 */
public class LineToolToggleButton extends ToolAdapter {
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
	 * Početna točka
	 */
	private Point startPoint;
	/**
	 * Završna točka
	 */
	private Point endPoint;
	
	/**
	 * Konstruktor
	 * 
	 * @param dModel model koji sadrži sve objekte
	 * @param color objekt koji nudi trenutnu boju
	 * @param canvas okvir u kojem se crtaju objekti
	 */
	public LineToolToggleButton(DrawingModel dModel, IColorProvider color, JDrawingCanvas canvas) {
		setText("Line");
		this.dModel = Objects.requireNonNull(dModel);
		this.color = Objects.requireNonNull(color);
		this.canvas = Objects.requireNonNull(canvas);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(startPoint == null) {
			startPoint = e.getPoint();
			endPoint = e.getPoint();
		} else {
			dModel.add(new Line(startPoint, e.getPoint(), color.getCurrentColor()));
			startPoint = null;
		}
		canvas.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(startPoint == null) {
			return;
		}
		endPoint = e.getPoint();
		canvas.repaint();
	}

	@Override
	public void paint(Graphics2D g) {
		if(startPoint == null) {
			return;
		}
		g.setColor(color.getCurrentColor());
		g.drawLine((int)startPoint.getX(), (int)startPoint.getY(), (int)endPoint.getX(), (int)endPoint.getY());
	}
}
