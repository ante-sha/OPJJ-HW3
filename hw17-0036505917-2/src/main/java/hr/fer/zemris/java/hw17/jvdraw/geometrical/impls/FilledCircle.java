package hr.fer.zemris.java.hw17.jvdraw.geometrical.impls;

import java.awt.Color;
import java.awt.Point;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectVisitor;

/**
 * Razred koji modelira krug kojeg se iscrtava u komponenti
 * {@link JDrawingCanvas}.
 * 
 * @author Ante Miličević
 *
 */
public class FilledCircle extends GeometricalObject {
	/**
	 * Točka središta
	 */
	private Point center;
	/**
	 * Polumjer
	 */
	private double radius;
	/**
	 * Boja kružnice
	 */
	private Color fgColor;
	/**
	 * Boja kruga
	 */
	private Color bgColor;

	/**
	 * Konstruktor
	 * 
	 * @param center točka središta
	 * @param radius polumjer kruga
	 * @param fgColor boja kružnice
	 * @param bgColor boja kruga
	 */
	public FilledCircle(Point center, double radius, Color fgColor, Color bgColor) {
		super();
		this.center = center;
		this.radius = radius;
		this.fgColor = fgColor;
		this.bgColor = bgColor;
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
	 * Getter za polumjer
	 * 
	 * @return radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Setter za polumjer
	 * 
	 * @param radius polumjer
	 */
	public void setRadius(double radius) {
		if(Math.abs(this.radius - radius) < 1E-6) {
			return;
		}
		this.radius = radius;
		fireChanged();
	}

	/**
	 * Getter za boju kružnice
	 * 
	 * @return fgColor
	 */
	public Color getFgColor() {
		return fgColor;
	}

	/**
	 * Setter za boju kružnice
	 * 
	 * @param fgColor boja kružnice
	 */
	public void setFgColor(Color fgColor) {
		if(Objects.equals(this.fgColor, fgColor)) {
			return;
		}
		this.fgColor = fgColor;
		fireChanged();
	}

	/**
	 * Getter za boju kruga
	 * 
	 * @return bgColor
	 */
	public Color getBgColor() {
		return bgColor;
	}

	/**
	 * Setter za boju kruga
	 * 
	 * @param bgColor boja kruga
	 */
	public void setBgColor(Color bgColor) {
		if(Objects.equals(this.bgColor, bgColor)) {
			return;
		}
		this.bgColor = bgColor;
		fireChanged();
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledCircleEditor(this);
	}

}
