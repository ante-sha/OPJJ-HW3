package hr.fer.zemris.java.gui.charts;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.util.Objects;

/**
 * Razred koji izračunava sve potrebne mjere za iscrtavanje
 * {@link BarChartComponent}. Nakon inicijalizacije bitno je pozvati metodu
 * {@link #updateDimension(Dimension)}.
 * 
 * @author Ante Miličević
 *
 */
public class BarChartMeasures {

	/**
	 * Graf za kojeg se uzimaju mjere
	 */
	private BarChart chart;
	/**
	 * Unutanje zabranjene granice
	 */
	private Insets insets;
	/**
	 * Dimenzije prozora
	 */
	private Dimension dim;
	/**
	 * Metrika fonta za ispis teksta
	 */
	private FontMetrics fmText;
	/**
	 * Metrika fonta za ispis brojeva
	 */
	private FontMetrics fmNum;

	/**
	 * Vertikalni razmak ispisnih komponenti
	 */
	public static final int verticalPadding = 6;
	/**
	 * Horizontalni razmak ispisnih komponenti
	 */
	public static final int horizontalPadding = 6;
	/**
	 * Unutarnja gornja granica
	 */
	public static final int topInset = 20;
	/**
	 * Unutarnja desna granica
	 */
	public static final int rightInset = 20;
	/**
	 * Dužina oznake na koordinatnim osima
	 */
	public static final int mark = 5;

	/**
	 * Točka u kojoj počinje ispis opisa y osi
	 */
	private Point yDescStart;
	/**
	 * Točka u kojoj počinje ispis opisa x osi
	 */
	private Point xDescStart;

	/**
	 * Visina teksta
	 */
	private int textHeight;
	/**
	 * Visina brojeva
	 */
	private int numHeight;

	/**
	 * Ishodište koordinatnog sustava
	 */
	private Point origin = new Point(0, 0);

	/**
	 * Maksimalna širina ispisa broja na y osi
	 */
	private int yAxisNumWidth;

	/**
	 * Udaljenost točaka y osi od ishodišta
	 */
	private int[] yAxisDots;
	/**
	 * Udaljenost točaka x osi od ishodišta
	 */
	private int[] xAxisDots;

	/**
	 * Konstruktor
	 * 
	 * @param chart
	 * @param insets
	 * @param fmText
	 * @param fmNum
	 * 
	 * @throws NullPointerException ako je chart, insets, fmText ili fmNum null
	 */
	public BarChartMeasures(BarChart chart, Insets insets, FontMetrics fmText, FontMetrics fmNum) {
		this.chart = Objects.requireNonNull(chart);
		this.insets = Objects.requireNonNull(insets);
		this.fmText = Objects.requireNonNull(fmText);
		this.fmNum = Objects.requireNonNull(fmNum);

		textHeight = fmText.getHeight();
		numHeight = fmNum.getHeight();
		yAxisNumWidth = fmNum.stringWidth(Integer.toString(chart.getMaxY()));
	}

	/**
	 * Metoda koja poziva računanje metrike za graf
	 * 
	 * @param dim dimenzije prozora
	 * 
	 * @throws NullPointerException ako je dim null
	 */
	public void updateDimension(Dimension dim) {
		this.dim = Objects.requireNonNull(dim);

		measure();
	}

	/**
	 * Metoda za izračun vrijednosti
	 */
	private void measure() {
		calculateXDescStart();
		calculateYDescStart();

		yAxisDots = axisDots(false);
		xAxisDots = axisDots(true);
	}

	/**
	 * Metoda za izračun početne točke za ispis broja na y osi, ako se broj ne
	 * nalazi na preciznosti delta umjesto njega rezultat će biti izračunat za
	 * najbliži cjeli broj na preciznosti delta
	 * 
	 * @param num broj za ispis
	 * @return točka početka ispisa
	 * 
	 *         throws IllegalArgumentException ako se broj ne nalazi u dozvoljenom
	 *         intervalu
	 */
	public Point startForYNumber(int num) {
		if (num < chart.getMinY() || num > chart.getMaxY()) {
			throw new IllegalArgumentException("Number do not exist in this chart");
		}

		int width = fmNum.stringWidth(Integer.toString(num));
		int xEnd = origin.x - mark - horizontalPadding;

		int y = origin.y - yAxisDots[(num - chart.getMinY()) / chart.getDelta()] + fmNum.getAscent() / 2;
		int x = xEnd - width;

		return new Point(x, y);
	}

	/**
	 * Metoda za izračun početne točke za ispis broja na x osi
	 * 
	 * @param num traženi broj
	 * @return točka početka ispisa
	 * 
	 * @throws IllegalArgumentException ako je num izvan dopuštenog raspona
	 */
	public Point startForXNumber(int num) {
		if (num < chart.getMinX() || num > chart.getMaxX()) {
			throw new IllegalArgumentException("Number do not exist in this chart");
		}

		int width = fmNum.stringWidth(Integer.toString(num));

		int y = origin.y + mark + verticalPadding + fmNum.getAscent() / 2;

		int cellWidth = xAxisDots[num - chart.getMinX() + 1] - xAxisDots[num - chart.getMinX()];

		int x = origin.x + xAxisDots[num - chart.getMinX()] + (cellWidth - width) / 2;

		return new Point(x, y);
	}

	/**
	 * Getter za origin, origin je promjenjiv
	 * 
	 * @return origin
	 */
	public Point getOrigin() {
		return origin;
	}

	/**
	 * Getter za originalno polje xAxisDots
	 * 
	 * @return xAxisDots
	 */
	public int[] getXAxisDots() {
		return xAxisDots;
	}

	/**
	 * Getter za originalno polje yAxisDots
	 * 
	 * @return
	 */
	public int[] getYAxisDots() {
		return yAxisDots;
	}

	/**
	 * Getter za originalnu točku xDescStart
	 * 
	 * @return xDescStart
	 */
	public Point getXDescStart() {
		return xDescStart;
	}

	/**
	 * Getter za originalnu točku yDescStart
	 * 
	 * @return yDescStart
	 */
	public Point getYDescStart() {
		return yDescStart;
	}

	/**
	 * Metoda za izračun točaka na koordinatnim osima
	 * 
	 * @param x true ako se radi izračun za x os, false ako za y
	 * 
	 * @return polje koje sadrži udaljenosti od ishodišta za svaku jediničnu
	 *         udaljenost na grafu
	 */
	private int[] axisDots(boolean x) {
		int lineLength = x ? dim.width - origin.x - rightInset : origin.y - topInset;

		int numberOfUnits = x ? chart.getMaxX() - chart.getMinX() + 1
				: (chart.getMaxY() - chart.getMinY()) / chart.getDelta();
		int[] result = new int[numberOfUnits + 1];

		result[0] = 0;

		for (int i = numberOfUnits; i > 0; i--) {
			int length = lineLength / i;

			result[numberOfUnits + 1 - i] = result[numberOfUnits - i] + length;

			lineLength -= length;
		}

		return result;
	}

	/**
	 * Metoda za izračun yDescStart
	 */
	private void calculateYDescStart() {
		int yOffset = dim.height - insets.bottom - 4 * verticalPadding - textHeight - numHeight;
		origin.y = yOffset - mark;

		int y = yOffset - (yOffset - fmText.stringWidth(chart.getYDesc())) / 2;
		int x = insets.left + verticalPadding + fmText.getHeight() / 2;

		yDescStart = new Point(x, y);
	}

	/**
	 * Metoda za izračun xDescStart
	 */
	private void calculateXDescStart() {
		int xOffset = insets.left + verticalPadding * 2 + textHeight + 2 * horizontalPadding + yAxisNumWidth;
		origin.x = xOffset + mark;

		int x = xOffset + (dim.width - xOffset - fmText.stringWidth(chart.getXDesc())) / 2;
		int y = dim.height - insets.bottom - verticalPadding - fmText.getHeight() / 2;

		xDescStart = new Point(x, y);
	}

	/**
	 * Metoda za izračun minimalnih dimenzija da bi se sve na grafu
	 * moglo prikazati
	 * 
	 * @return minimalne dimenzije
	 */
	public Dimension getMinimalSize() {
		int yDescMin = 2 * horizontalPadding + fmText.stringWidth(chart.getYDesc());
		int yNumMin = (chart.getMaxY() - chart.getMinY()) / chart.getDelta()
				* (2 * verticalPadding + fmNum.getHeight());

		int yAxisDataMin = yDescMin < yNumMin ? yNumMin : yDescMin;

		int xDescMin = 2 * horizontalPadding + fmText.stringWidth(chart.getXDesc());
		int xNumMin = (chart.getMaxX() - chart.getMinX())
				* (2 * horizontalPadding + fmNum.stringWidth(Integer.toString(chart.getMaxX())));

		int xAxisDataMin = xDescMin < xNumMin ? xNumMin : xDescMin;

		yAxisDataMin += fmNum.getHeight() + 4 * verticalPadding + fmText.getHeight();
		xAxisDataMin += yAxisNumWidth + 2 * horizontalPadding + 2 * verticalPadding + fmText.getHeight();
		return new Dimension(xAxisDataMin, yAxisDataMin);
	}
}
