package hr.fer.zemris.java.gui.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.Objects;

import javax.swing.JComponent;

/**
 * Komponenta koja na svojoj površini iscrtava histogram modeliran s podacima
 * {@link BarChart}. Sve mjere grafa su zadane u razredu
 * {@link BarChartMeasures}.
 * 
 * @author Ante Miličević
 *
 */
public class BarChartComponent extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Podaci grafa
	 */
	private BarChart chart;

	/**
	 * Boja mreže
	 */
	private static final Color gridColor = Color.decode("#FAD96C");
	/**
	 * Boja stupaca
	 */
	private static final Color barColor = Color.decode("#EE5E0B");
	/**
	 * Boja sjene stupaca
	 */
	private static final Color shadowColor = new Color(0.6f, 0.6f, 0.6f, 0.5f);
	/**
	 * Defaultna boja
	 */
	private static final Color defaultColor = Color.BLACK;

	/**
	 * Font za ispis teksta
	 */
	private static Font textFont;
	/**
	 * Font za ispis brojeva
	 */
	private static Font numFont;

	private static final int defaultStrokeWidth = 2;

	private static final int gridStrokeWidth = 3;

	/**
	 * Mjere grafa
	 */
	private BarChartMeasures chartMeasures;

	/**
	 * Konstruktor
	 * 
	 * @param chart podaci za graf koji se iscrtava
	 */
	public BarChartComponent(BarChart chart) {
		this.chart = Objects.requireNonNull(chart);

		textFont = new Font("text", Font.PLAIN, 15);
		numFont = textFont.deriveFont(20f);

		// initialize measures
		chartMeasures = new BarChartMeasures(chart, getInsets(), getFontMetrics(textFont), getFontMetrics(numFont));
	}

	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;

		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		Stroke oldStroke = g.getStroke();

		g.setColor(defaultColor);

		g.setStroke(new BasicStroke(defaultStrokeWidth));

		Dimension dim = getSize();

		chartMeasures.updateDimension(dim);

		drawYDescription(g);
		drawXDescription(g);

		drawYAxis(g);
		drawXAxis(g);

		drawGrid(g);
		drawBars(g);

		g.setColor(oldColor);
		g.setFont(oldFont);
		g.setStroke(oldStroke);
	}

	@Override
	public Dimension getPreferredSize() {
		return chartMeasures.getMinimalSize();
	}

	/**
	 * Metoda za iscrtavanje stupaca
	 * 
	 * @param g objekt za crtanje
	 */
	private void drawBars(Graphics2D g) {
		Color old = g.getColor();
		Stroke oldStroke = g.getStroke();
		g.setColor(barColor);

		BasicStroke white = new BasicStroke(2);
		int halfWhiteWidth = (int) white.getLineWidth() / 2;
		BasicStroke shadow = new BasicStroke(getWidth() / chart.getValues().size() / 20);
		int shaddowPadding = (int) (shadow.getLineWidth() / 2 + white.getLineWidth());

		int padding = gridStrokeWidth / 2;
		int[] yAxisDots = chartMeasures.getYAxisDots();
		int[] xAxisDots = chartMeasures.getXAxisDots();
		Point origin = chartMeasures.getOrigin();

		chart.getValues().forEach((value) -> {
			g.setColor(barColor);
			int x1 = origin.x + xAxisDots[value.getX() - chart.getMinX()] + padding;
			int y1 = origin.y - padding;

			int x2 = origin.x + xAxisDots[value.getX() - chart.getMinX() + 1] - padding;

			double k = 1;
			int r = (value.getY() - chart.getMinY()) % chart.getDelta();
			// if y is value that can not be represented by delta precision
			if (r != 0) {
				// percentage of size that is first greater y that can be represented in delta
				// precision
				k = (value.getY() + chart.getDelta() - r - chart.getMinY()) * 1.0 / (value.getY() - chart.getMinY());
			}
			int y2 = (int) (origin.y
					- k * yAxisDots[(int) Math.ceil((value.getY() - chart.getMinY()) / chart.getDelta())] + padding);

			Rectangle rect = new Rectangle(x1, y2, x2 - x1, y1 - y2);
			g.fill(rect);

			g.setColor(Color.WHITE);
			g.setStroke(white);
			g.drawLine(x1, y2 + halfWhiteWidth, x2, y2 + halfWhiteWidth);
			g.drawLine(x2 + halfWhiteWidth, y2, x2 + halfWhiteWidth, y1);

			g.setPaint(shadowColor);
			g.setStroke(shadow);
			g.drawLine(x2 + shaddowPadding, y2 + 20 > y1 ? y1 : y2 + 20, x2 + shaddowPadding,
					y1 - (shaddowPadding + 1) / 2);
		});

		g.setStroke(oldStroke);
		g.setColor(old);
	}

	/**
	 * Metoda za iscrtavanje mreže
	 * 
	 * @param g objekt za iscrtavanje
	 */
	private void drawGrid(Graphics2D g) {
		Color old = g.getColor();
		Stroke oldStroke = g.getStroke();
		g.setColor(gridColor);
		g.setStroke(new BasicStroke(3));

		Point origin = chartMeasures.getOrigin();

		Arrays.stream(chartMeasures.getYAxisDots()).forEach((y) -> {
			if (y == 0) {
				return;
			}
			g.drawLine(origin.x + gridStrokeWidth, origin.y - y,
					getWidth() - BarChartMeasures.rightInset + BarChartMeasures.mark, origin.y - y);
		});
		;

		Arrays.stream(chartMeasures.getXAxisDots()).forEach((x) -> {
			if (x == 0) {
				return;
			}
			g.drawLine(origin.x + x, origin.y - gridStrokeWidth, origin.x + x,
					BarChartMeasures.topInset - BarChartMeasures.mark);
		});
		;

		g.setColor(old);
		g.setStroke(oldStroke);
	}

	/**
	 * Metoda za ispis opisa y osi
	 * 
	 * @param g objekt za iscrtavanje
	 */
	private void drawYDescription(Graphics2D g) {
		AffineTransform saveAT = g.getTransform();

		g.transform(AffineTransform.getQuadrantRotateInstance(3));

		Point start = chartMeasures.getYDescStart();

		writeString(g, false, new Point(-start.y, start.x), chart.getYDesc());

		g.setTransform(saveAT);
	}

	/**
	 * Metoda za ispis texta na poziciji start
	 * 
	 * @param g      objekt za iscrtavanje
	 * @param number true ako se ispisuje fontom za brojeve, false ako za tekst
	 * @param start  početna točka za ispis u koordinatnom sustavu objekta za
	 *               iscrtavanje
	 * @param text   tekst za ispis
	 */
	private void writeString(Graphics2D g, boolean number, Point start, String text) {
		Font old = g.getFont();

		Font font = number ? numFont : textFont;
		g.setFont(font);
		g.drawString(text, start.x, start.y);

		g.setFont(old);
	}

	/**
	 * Metoda za iscrtavanje y osi
	 * 
	 * @param g objekt za iscrtavanje
	 */
	private void drawYAxis(Graphics2D g) {
		int[] yAxisDots = chartMeasures.getYAxisDots();
		Point origin = chartMeasures.getOrigin();

		for (int i = chart.getMinY(), n = chart.getMaxY(), d = chart.getDelta(), j = 0; i <= n; i += d, j++) {
			Point start = chartMeasures.startForYNumber(i);
			writeString(g, true, start, Integer.toString(i));

			g.drawLine(j == 0 ? origin.x - BarChartMeasures.mark : origin.x, origin.y - yAxisDots[j],
					j == 0 ? getWidth() - BarChartMeasures.horizontalPadding - 5 : origin.x - BarChartMeasures.mark,
					origin.y - yAxisDots[j]);
		}

		int triangleEnd = getWidth() - BarChartMeasures.horizontalPadding;

		Polygon triangle = new Polygon(new int[] { triangleEnd, triangleEnd - 10, triangleEnd - 10 },
				new int[] { origin.y, origin.y - 5, origin.y + 5 }, 3);

		g.fillPolygon(triangle);

	}

	/**
	 * Metoda za ispis opisa x osi
	 * 
	 * @param g objekt za iscrtavanje
	 */
	private void drawXDescription(Graphics2D g) {
		Point start = chartMeasures.getXDescStart();
		writeString(g, false, start, chart.getXDesc());
	}

	/**
	 * Metoda za iscrtavanje x osi
	 * 
	 * @param g objekt za iscrtavanje
	 */
	private void drawXAxis(Graphics2D g) {
		int maxX = chart.getMaxX();
		int minX = chart.getMinX();
		
		for (int i = minX; i <= maxX; i++) {
			Point start = chartMeasures.startForXNumber(i);
			writeString(g, true, start, Integer.toString(i));
		}

		Point origin = chartMeasures.getOrigin();
		int[] xAxisDots = chartMeasures.getXAxisDots();

		for (int i = 1, n = maxX - minX + 1; i <= n; i++) {
			g.drawLine(origin.x + xAxisDots[i], origin.y, origin.x + xAxisDots[i], origin.y + BarChartMeasures.mark);
		}

		g.drawLine(origin.x, origin.y + BarChartMeasures.mark, origin.x, BarChartMeasures.topInset - 5);

		Polygon triangle = new Polygon(new int[] { origin.x, origin.x - 5, origin.x + 5 }, new int[] { 5, 15, 15 }, 3);

		g.fillPolygon(triangle);
	}

}
