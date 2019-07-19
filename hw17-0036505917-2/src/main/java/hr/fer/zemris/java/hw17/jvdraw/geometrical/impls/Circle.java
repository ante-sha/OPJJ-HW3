package hr.fer.zemris.java.hw17.jvdraw.geometrical.impls;

import java.awt.Color;
import java.awt.Point;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectVisitor;

/**
 * Razred koji modelira kružnicu kojeg se iscrtava u komponenti
 * {@link JDrawingCanvas}.
 * 
 * @author Ante Miličević
 *
 */
public class Circle extends GeometricalObject {
	/**
	 * Točka središta
	 */
	private Point center;
	/**
	 * Polumjer kružnice
	 */
	private double radius;
	/**
	 * Boja kružnice
	 */
	private Color color;
	
	/**
	 * Konstrukor
	 * 
	 * @param center točka središta
	 * @param radius polumjer kružnice
	 * @param color boja kružnice
	 */
	public Circle(Point center, double radius, Color color) {
		super();
		this.center = center;
		this.radius = radius;
		this.color = color;
	}

	/**
	 * Getter za točku središta
	 * 
	 * @return center
	 */
	public Point getCenter() {
		return center;
	}

	/**
	 * Getter za polumjer
	 * 
	 * @return radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Setter za točku središta
	 * 
	 * @param center točka središta
	 */
	public void setCenter(Point center) {
		if(Objects.equals(this.center, center)) {
			return;
		}
		this.center = center;
		fireChanged();
	}

	/**
	 * Setter za polumjer kružnice
	 * 
	 * @param radius polumjer kružnice
	 */
	public void setRadius(double radius) {
		if(Math.abs(this.radius - radius) < 1E-6) {
			return;
		}
		this.radius = radius;
		fireChanged();
	}

	/**
	 * Setter za boju kružnice
	 * 
	 * @param color boja kružnice
	 */
	public void setColor(Color color) {
		if(Objects.equals(this.color, color)) {
			return;
		}
		this.color = color;
		fireChanged();
	}

	/**
	 * Getter za boju
	 * 
	 * @return color
	 */
	public Color getColor() {
		return color;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new CircleEditor(this);
	}

}
