package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.math.Vector2D;

/**
 * Razred modelira stanje "kornjače" tj. elementa pomoću kojeg izvodimo
 * operacije tipa {@link Command}. Parametri koji definiraju stanje su pozicija,
 * kut, boja i "jedinična" duljina kod koračnih naredbi. Kut mora biti definiran
 * jediničnim vektorom; null vrijednosti parametara nisu dozvoljene.
 * 
 * @author Ante Miličević
 *
 */
public class TurtleState {
	/**
	 * Pozicija kornjače na ekranu
	 */
	private Vector2D position;
	/**
	 * Orijentacija kornače
	 */
	private Vector2D angle;
	/**
	 * Boja kojom kornjača piše
	 */
	private Color color;
	/**
	 * Duljina koraka pri koračnim naredbama (draw, step)
	 */
	private double unitLength;

	/**
	 * Konstruktor koji definira sve parametre objekta. Null vrijednosti nisu
	 * dozvoljene.
	 * 
	 * @param position   pozicija kornjače
	 * @param angle      orijentacija kornjače
	 * @param color      boja ispisivanja
	 * @param unitLength duljina koraka
	 * 
	 * @throws NullPointerException     ako je neki od argumenata null
	 * @throws IllegalArgumentException ako kut nije jedinične duljine
	 */
	public TurtleState(Vector2D position, Vector2D angle, Color color, double unitLength) {
		super();
		this.position = Objects.requireNonNull(position);
		this.angle = validateAngle(angle);
		this.color = Objects.requireNonNull(color);
		this.unitLength = unitLength;
	}

	/**
	 * Metoda koja provjerava je li orijentacija jedinične duljine
	 * 
	 * @param angle orijentacija
	 * @return angle
	 * 
	 * @throws NullPointerException     ako je angle = null
	 * @throws IllegalArgumentException ako angle nije jedinične duljine
	 */
	private Vector2D validateAngle(Vector2D angle) {
		Objects.requireNonNull(angle);
		if (Math.abs(angle.getAmplitude() - 1) > 1E-6) {
			throw new IllegalArgumentException("Angle must be vector with amplitude 1");
		}
		return angle;
	}

	/**
	 * Metoda koja šalje kopiju ovog stanja.<br>
	 * Stvaranje kopija svakog vektora i boje se stvara da ne bi došlo do promjene
	 * zbog vanjskih modifikacija.
	 * 
	 * @return kopija stanja
	 */
	public TurtleState copy() {
		return new TurtleState(position.copy(), angle.copy(), new Color(color.getRGB()), unitLength);
	}

	/**
	 * Getter za poziciju
	 * @return position
	 */
	public Vector2D getPosition() {
		return position;
	}

	/**
	 * Getter za orijentaciju
	 * @return angle
	 */
	public Vector2D getAngle() {
		return angle;
	}

	/**
	 * Getter za boju
	 * @return color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Setter za boju
	 * 
	 * @param color boja
	 * 
	 * @throws NullPointerException ako je boja null
	 */
	public void setColor(Color color) {
		this.color = Objects.requireNonNull(color);
	}

	/**
	 * Getter za duljinu koraka
	 * @return unitLenght
	 */
	public double getUnitLength() {
		return unitLength;
	}

	/**
	 * Setter za duljinu koraka
	 * 
	 * @param unitLength
	 */
	public void setUnitLength(double unitLength) {
		this.unitLength = unitLength;
	}
}
