package hr.fer.zemris.java.gui.charts;

import java.util.List;
import java.util.Objects;

/**
 * Razred koji sadrži sve podatke stupčastog dijagrama, svaki podatak je
 * modeliran s {@link XYValue}
 */
public class BarChart {

	/**
	 * Vrijednosti stupčastog dijagrama
	 */
	private List<XYValue> values;
	/**
	 * Opis x osi
	 */
	private String xDesc;
	/**
	 * Opis y osi
	 */
	private String yDesc;
	/**
	 * Minimalna vrijednost y osi
	 */
	private int minY;
	/**
	 * Maksimalna vrijednost y osi
	 */
	private int maxY;
	/**
	 * Preciznost pri iscrtavanju
	 */
	private int delta;

	/**
	 * Konstruktor
	 * 
	 * @param values vrijednosti grafa
	 * @param xDesc  opis x osi
	 * @param yDesc  opis y osi
	 * @param minY   minimalna vrijednost y
	 * @param maxY   maksimalna vrijednost y
	 * @param delta  preciznost pri iscrtavanju
	 * 
	 * @throws NullPointerException     ako je xDesc, yDesc ili values jednak null
	 * @throws IllegalArgumentException ako podaci nisu validni
	 */
	public BarChart(List<XYValue> values, String xDesc, String yDesc, int minY, int maxY, int delta) {
		this.xDesc = Objects.requireNonNull(xDesc);
		this.yDesc = Objects.requireNonNull(yDesc);

		this.minY = validateMinY(minY);
		this.maxY = validateMaxY(maxY);
		this.delta = delta;

		if ((maxY - minY) % delta != 0) {
			this.maxY += delta - (maxY - minY) % delta;
		}

		this.values = validateValues(values);
	}

	/**
	 * Metoda koja provjerava:<br>
	 * da li se u listi podataka nešto nalazi<br>
	 * da li postoji y vrijednost manja od minimuma<br>
	 * da li postoje duplikati x vrijednosti
	 * 
	 * @param values vrijednosti grafa
	 * 
	 * @return values
	 * 
	 * @throws IllegalArgumentException ako nisu zadovoljeni uvjeti iz opisa
	 * @throws NullPointerException     ako je values null
	 */
	private List<XYValue> validateValues(List<XYValue> values) throws IllegalArgumentException, NullPointerException {
		Objects.requireNonNull(values);

		if (values.isEmpty()) {
			throw new IllegalArgumentException("There is no informations provided!");
		}

		values.forEach((xy) -> {
			if (xy.getY() < minY) {
				throw new IllegalArgumentException(xy.getY() + " is less than " + minY);
			}
		});

		long n = values.stream().mapToInt((v) -> v.getX()).distinct().count();

		if (n != values.size()) {
			throw new IllegalArgumentException("X values are duplicated!");
		}
		return values;
	}

	/**
	 * Metoda koja provjerava da li je predani maksimum manji ili jednak minimumu
	 * 
	 * @param maxY maksimum
	 * @return maxY
	 * 
	 * @throws IllegalArgumentException ako je maksimum manji od minimuma
	 */
	private int validateMaxY(int maxY) throws IllegalArgumentException {
		if (maxY <= this.minY) {
			throw new IllegalArgumentException("Maximal value of y must be greater than minimal");
		}
		return maxY;
	}

	/**
	 * Metoda koja provjerava da li je predani minimum manji od nula
	 * 
	 * @param minY minimum
	 * @return minY
	 * 
	 * @throws IllegalArgumentException ako je minimum manji od 0
	 */
	private int validateMinY(int minY) throws IllegalArgumentException {
		if (minY < 0) {
			throw new IllegalArgumentException("Minimal value of y can not be negative");
		}
		return minY;
	}

	/**
	 * Getter za values
	 * 
	 * @return values
	 */
	public List<XYValue> getValues() {
		return values;
	}

	/**
	 * Getter za xDesc
	 * 
	 * @return xDesc
	 */
	public String getXDesc() {
		return xDesc;
	}

	/**
	 * Getter za yDesc
	 * 
	 * @return yDesc
	 */
	public String getYDesc() {
		return yDesc;
	}

	/**
	 * Getter za minY
	 * @return minY
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Getter za maxY
	 * @return maxY
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Getter za delta-u
	 * @return delta
	 */
	public int getDelta() {
		return delta;
	}

	/**
	 * Metoda koja računa minimalan x među vrijednostima
	 * 
	 * @return minX
	 */
	public int getMinX() {
		return values.stream().mapToInt((v) -> v.getX()).min().getAsInt();
	}

	/**
	 * Metoda koja računa maksimalan x među vrijednostima
	 * 
	 * @return maxX
	 */
	public int getMaxX() {
		return values.stream().mapToInt((v) -> v.getX()).max().getAsInt();
	}
}
