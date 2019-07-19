package hr.fer.zemris.math;

/**
 * Razred modelira 3D vektor. Primjerak razreda je nepromjenjiv i svakom
 * operacijom nad vektorom kao rezultat se vraća novi primjerak vektora.
 * 
 * @author Ante Miličević
 *
 */
public class Vector3 {
	/**
	 * X komponenta
	 */
	private double x;
	/**
	 * Y komponenta
	 */
	private double y;
	/**
	 * Z komponenta
	 */
	private double z;

	/**
	 * Konstruktor koji inicijalizira sve komponente vektora
	 * 
	 * @param x x komponenta
	 * @param y y komponenta
	 * @param z z komponenta
	 */
	public Vector3(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Metoda vraća duljinu vektora
	 * 
	 * @return duljina vektora
	 */
	public double norm() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Metoda generira jedinični vektor koji ima isti smjer kao trenutni vektor
	 * 
	 * @return jedinični vektor
	 */
	public Vector3 normalized() {
		double norm = norm();
		if (norm == 0) {
			throw new IllegalStateException("Can not normalize 0-vector");
		}
		return new Vector3(x / norm, y / norm, z / norm);
	}

	/**
	 * Metoda zbraja ovaj vektor sa vektorom other.
	 * 
	 * @param other drugi operand
	 * @return rezultat zbrajanja
	 */
	public Vector3 add(Vector3 other) {
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}

	/**
	 * Metoda oduzima vektor other od ovog vektora.
	 * 
	 * @param other drugi operand
	 * @return rezultat oduzimanja
	 */
	public Vector3 sub(Vector3 other) {
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}

	/**
	 * Metoda računa skalarni produkt ovog vektora s vektorom other.
	 * 
	 * @param other drugi operand
	 * @return rezultat skalarnog produkta
	 */
	public double dot(Vector3 other) {
		return x * other.x + y * other.y + z * other.z;
	}

	/**
	 * Metoda računa vektorski produkt ovog vektora s  vektorom other.
	 * 
	 * @param other drugi operand
	 * @return rezultat vektorskog produkta
	 */
	public Vector3 cross(Vector3 other) {
		return new Vector3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
	}

	/**
	 * Metoda računa vrijednost ovog vektora uvečanog za s.
	 * 
	 * @param s faktor uvećanja
	 * @return vektor uvećan za s 
	 */
	public Vector3 scale(double s) {
		return new Vector3(x * s, y * s, z * s);
	}

	/**
	 * Metoda računa kosinus kuta između ovog vektora i vektora other.
	 * 
	 * @param other drugi operand
	 * @return kosinus kuta između vektora
	 */
	public double cosAngle(Vector3 other) {
		return dot(other) / (norm() * other.norm());
	}

	/**
	 * Getter za x komponentu
	 * 
	 * @return x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Getter za y komponentu
	 * 
	 * @return y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Getter za z komponentu
	 * 
	 * @return z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @return komponente vektora u polju
	 */
	public double[] toArray() {
		return new double[] { x, y, z };
	}

	@Override
	public String toString() {
		return String.format("(%f, %f, %f)", x, y, z);
	}

}
