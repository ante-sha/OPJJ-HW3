package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Razred modelira 2D vektor s komponentama x i y, translacija vektora u ovom
 * razredu označava zbrajanje dva vektora. Ponuđene su metode koje direktno
 * mijenjaju trenutni vektor i one koje trenutni vektor ostavljaju kakav je bio,
 * ali vraćaju novi kao rezultat djelovanja metode na trenutni vektor.
 * 
 * @author Ante Miličević
 *
 */
public class Vector2D {
	/** Komponenta vektora u smjeru x-osi */
	private double x;
	/** Komponenta vektora u smjeru y-osi */
	private double y;

	/**
	 * Konstruktor koji definira 2D vektor po njegovim komponenetama.
	 * 
	 * @param x
	 * @param y
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter za x.
	 * 
	 * @return x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Getter za y.
	 * 
	 * @return y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Zbrajanje vektora
	 * 
	 * @param offset vektor koji se pridodaje trenutnom
	 * @throws NullPointerException ako je offset == null
	 */
	public void translate(Vector2D offset) {
		Objects.requireNonNull(offset);
		x += offset.x;
		y += offset.y;
	}

	/**
	 * Metoda koja vraća rezultat operacije zbrajanja {@code this} i {@code offset}
	 * vektora i pri tom oba ostaju ne promjenjena.
	 * 
	 * @param offset vektor koji se pridodaje trenutnom
	 * @return zbroj {@code this+offset}
	 * @throws NullPointerException ako je offset == null
	 */
	public Vector2D translated(Vector2D offset) {
		Objects.requireNonNull(offset);
		return new Vector2D(x + offset.x, y + offset.y);
	}

	/**
	 * Metoda koja rotira vektor za kut {@code angle}
	 * 
	 * @param angle kut za koji se vektor rotira
	 */
	public void rotate(double angle) {
		upisiVrijednostiIzAmplitudeIKuta(amplituda(), kut() + angle);
	}

	/**
	 * Metoda koja vraća novi vektor koji je rezultat operacije rotiranja vektora za
	 * kut {@code angle}
	 * 
	 * @param angle kut za koji se rotira vektor
	 * @return rezultat rotacije vektora
	 */
	public Vector2D rotated(double angle) {
		return vektorIzAmplitudeIKuta(amplituda(), kut() + angle);
	}

	/**
	 * Metoda koja vektor skalira s argumentom {@code scaler}
	 * 
	 * @param scaler faktor skaliranja vektora
	 */
	public void scale(double scaler) {
		x *= scaler;
		y *= scaler;
	}

	/**
	 * Metoda koja vraća novi vektor koji je rezultat skaliranja trenutnog vektora s
	 * faktorom {@code scaler}
	 * 
	 * @param scaler faktor skaliranja
	 * @return skalirani vektor
	 */
	public Vector2D scaled(double scaler) {
		return new Vector2D(x * scaler, y * scaler);
	}

	/**
	 * Metoda koja generira kopiju trenutnog vektora
	 * 
	 * @return kopija vektora
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}

	/**
	 * Metoda koja računa i upisuje komponente iz amplitude i kuta
	 * 
	 * @param amplituda
	 * @param kut
	 */
	private void upisiVrijednostiIzAmplitudeIKuta(double amplituda, double kut) {
		x = amplituda * Math.cos(kut);
		y = amplituda * Math.sin(kut);
	}

	/**
	 * Metoda koja generira novi vektor iz amplitude i kuta
	 * 
	 * @param amplituda
	 * @param kut
	 * @return vektor generiran iz amplitude i kuta iz argumenata
	 */
	private Vector2D vektorIzAmplitudeIKuta(double amplituda, double kut) {
		return new Vector2D(amplituda * Math.cos(kut), amplituda * Math.sin(kut));
	}

	/**
	 * Metoda koja računa kut vektora iz članskih varijabli
	 * 
	 * @return kut koji vektor zatvara s x osi
	 */
	private double kut() {
		return Math.atan2(y, x);
	}

	/**
	 * Metoda koja računa amplitudu vektora iz članskih varijabli
	 * 
	 * @return amplituda vektora
	 */
	private double amplituda() {
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Vector2D)) {
			return false;		
		}
		Vector2D other = (Vector2D) obj;
		return Math.abs(x - other.x) < 1E-6 && Math.abs(y - other.y) < 1E-6;
	}

}
