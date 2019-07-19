package hr.fer.zemris.java.hw17.jvdraw.geometrical.impls;

import java.awt.Color;
import java.awt.Point;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectVisitor;

/**
 * Razred koji modelira dužinu koja se iscrtava u komponenti
 * {@link JDrawingCanvas}.
 * 
 * @author Ante Miličević
 *
 */
public class Line extends GeometricalObject {
	/**
	 * Početna točka
	 */
	private Point startPoint;
	/**
	 * Završna točka
	 */
	private Point endPoint;
	/**
	 * Boja dužine
	 */
	private Color color;

	/**
	 * Konstruktor
	 * 
	 * @param startPoint početna točka
	 * @param endPoint   završna točka
	 * @param color      boja dužine
	 */
	public Line(Point startPoint, Point endPoint, Color color) {
		super();
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.color = color;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	/**
	 * Getter za početnu točku
	 * 
	 * @return startPoint
	 */
	public Point getStartPoint() {
		return startPoint;
	}

	/**
	 * Setter za početnu točku
	 * 
	 * @param startPoint početna točka
	 */
	public void setStartPoint(Point startPoint) {
		if (Objects.equals(this.startPoint, startPoint)) {
			return;
		}
		this.startPoint = startPoint;
		fireChanged();
	}

	/**
	 * Getter za završnu točku
	 * 
	 * @return endPoint
	 */
	public Point getEndPoint() {
		return endPoint;
	}

	/**
	 * Setter za završnu točku
	 * 
	 * @param endPoint završna točku
	 */
	public void setEndPoint(Point endPoint) {
		if (Objects.equals(this.endPoint, endPoint)) {
			return;
		}
		this.endPoint = endPoint;
		fireChanged();
	}

	/**
	 * Getter za boju dužine
	 * 
	 * @return color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Setter za boju dužine
	 * 
	 * @param color boja dužine
	 */
	public void setColor(Color color) {
		if (Objects.equals(this.color, color)) {
			return;
		}
		this.color = color;
		fireChanged();
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new LineEditor(this);
	}

}
