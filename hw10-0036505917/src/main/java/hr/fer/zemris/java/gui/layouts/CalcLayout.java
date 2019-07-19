package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Razred definira upravljač rasporedom koji ima fiksan raspored komponenti.
 * Raspored je sljedeći:<br>
 * -5 redaka i 7 stupaca gdje su ćelije od pozicije (1,1) do (1,5) spojene<br>
 * -svi retci su iste visine<br>
 * -svi stupci su iste dužine<br>
 * -razmak između ćelija se definira u konstruktoru<br>
 * 
 * @author Ante Miličević
 *
 */
public class CalcLayout implements LayoutManager2 {

	/**
	 * Mapa pozicija -> komponenta
	 */
	private Map<RCPosition, Component> components = new HashMap<>();

	/**
	 * Razmak između ćelija
	 */
	private int padding;

	/**
	 * Konstruktor koji definira ramak između ćelija
	 * 
	 * @param padding
	 * 
	 * @throws CalcLayoutException ako je zadani razmak negativan
	 */
	public CalcLayout(int padding) throws CalcLayoutException {
		if (padding < 0) {
			throw new CalcLayoutException("Padding can not be less than 0");
		}
		this.padding = padding;
	}

	/**
	 * Defaultni konstruktor koji inicijalizira zadani razmak između ćelija na 0.
	 */
	public CalcLayout() {
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		components.values().removeIf(tmp -> tmp == comp);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return calculateSize(parent, Integer::compare, (component) -> component.getPreferredSize());
	}

	/**
	 * Metoda koja računa željenu veličinu tablice preko dimenzija koju se dobiva
	 * primjenjivanjem funkcije {@code extracter}, te se veličine uspoređuju
	 * temeljem komparatora {@code comp}
	 * 
	 * @param parent    komponenta nad kojom je pozvan izračun veličine
	 * @param comp      komparator za pronalaženje željene veličine
	 * @param extracter funkcija izdvajanja dimenzija tablice
	 * 
	 * @return željena veličina
	 */
	private Dimension calculateSize(Container parent, Comparator<Integer> comp,
			Function<Component, Dimension> extracter) {
		int width = comp.compare(0, Integer.MAX_VALUE) <= 0 ? 0 : Integer.MAX_VALUE;
		int height = comp.compare(0, Integer.MAX_VALUE) <= 0 ? 0 : Integer.MAX_VALUE;

		for (int j = 1; j <= 5; j++) {
			for (int i = 1; i <= 7; i++) {
				Component component = components.get(new RCPosition(j, i));
				if (component == null) {
					continue;
				}
				Dimension dim = extracter.apply(component);

				if (dim == null)
					continue;

				height = comp.compare(height, dim.height) >= 0 ? height : dim.height;

				if (i == 1 && j == 1) {
					int oneWidth = (int) Math.ceil((1.0 * dim.width - 4.0 * padding) / 5);
					width = comp.compare(width, oneWidth) >= 0 ? width : oneWidth;
				} else {
					width = comp.compare(width, dim.width) >= 0 ? width : dim.width;
				}
			}
		}
		return new Dimension(7 * width + 6 * padding, height * 5 + padding * 4);
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return calculateSize(parent, Integer::compare, (component) -> component.getMinimumSize());
	}

	@Override
	public void layoutContainer(Container parent) {
		synchronized (parent) {
			Insets in = parent.getInsets();
			Rectangle bounds = parent.getBounds();
			Rectangle r = new Rectangle(in.left, in.top, bounds.width - in.left - in.right,
					bounds.height - in.top - in.bottom);

			int[] yStarts = computeStarts(r, false);
			int[] xStarts = computeStarts(r, true);

			for (int j = 1; j <= 5; j++) {
				for (int i = 1; i <= 7; i++) {
					Component comp = components.get(new RCPosition(j, i));
					if (comp == null) {
						continue;
					}
					if (i == 1 && j == 1) {
						comp.setLocation(in.left + xStarts[0],in.top + yStarts[0]);
						comp.setSize(xStarts[5] - padding - xStarts[0], yStarts[1] - yStarts[0] - padding);
					} else {
						comp.setLocation(in.left + xStarts[i - 1], in.top + yStarts[j - 1]);
						comp.setSize(i == 7 ? xStarts[7] - xStarts[6] : xStarts[i] - xStarts[i - 1] - padding,
								j == 5 ? yStarts[5] - yStarts[4] : yStarts[j] - yStarts[j - 1] - padding);
					}
				}
			}
		}

	}

	/**
	 * Metoda koja izračunava početne dužine ovisno o željenoj osi, dobivene
	 * vrijednosti su podjeljene u jednolikoj distribuciji razmaka.
	 * 
	 * @param r zadani okvir unutar kojega se komponente moraju prikazati
	 * @param x true ako se račun provodi za x os, false ako za y
	 * 
	 * @return početne dužine
	 */
	private int[] computeStarts(Rectangle r, boolean x) {
		int dimLeft = x ? r.width : r.height;
		int n = x ? 7 : 5;
		int[] position = new int[n + 1];
		position[0] = 0;
		position[n] = dimLeft;
		int j = 1;
		for (int i = n; i > 1; i -= 2, j++) {
			double d = (dimLeft * 1.0 - (i - 1) * padding) / (i);
			dimLeft = (int) (dimLeft - 2 * (Math.round(d) + padding));

			position[j] = (int) (Math.round(d) + padding + position[j - 1]);
			position[n - j] = (int) (position[n - j + 1] - Math.round(d) - padding);
		}
		position[j] = position[j - 1] + dimLeft;

		return position;
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) throws UnsupportedOperationException {
		if (constraints instanceof String) {
			constraints = convertConstraintsToRC((String) constraints);
		}
		if (!(constraints instanceof RCPosition)) {
			throw new UnsupportedOperationException("Unsupported constraint");
		}

		RCPosition position = validatePosition(comp, (RCPosition) constraints);

		components.put(position, comp);
	}

	/**
	 * Metoda za pretvorbu teksta oblika "r,s" (redak,stupac) u {@link RCPosition} s
	 * istim parametrima.
	 * 
	 * @param constraints ograničenja u tekstualnom obliku
	 * @return ograničenja prikazana u RCPosition obliku
	 * 
	 * @throws UnsupportedOperationException ako se unos ne može pretvoriti u
	 *                                       RCPosition
	 */
	private RCPosition convertConstraintsToRC(String constraints) throws UnsupportedOperationException {
		try {
			String[] data = constraints.split(",");

			return new RCPosition(Integer.parseInt(data[0]), Integer.parseInt(data[1]));

		} catch (Exception ex) {
			throw new UnsupportedOperationException(constraints + " can not be interpreted into 'row, column'");
		}
	}

	/**
	 * Metoda koja provjerava da li je predana pozicija validna, tj. da je: u
	 * zadanom području; ta pozicija slobodna (ako se pokušava drugi puta dodati
	 * ista komponenta na istu poziciju drugi poziv se ignorira); indeks stupca
	 * izvan intervala [2,5] ako je traženi prvi redak.
	 * 
	 * @param comp        komponenta koju se ubacuje
	 * @param constraints pozicija
	 * 
	 * @return pozicija predana u argumentu metode
	 * 
	 * @throws UnsupportedOperationException ako nisu zadovoljeni svi uvjeti iz
	 *                                       opisa metode
	 */
	private RCPosition validatePosition(Component comp, RCPosition constraints) throws UnsupportedOperationException {

		RCPosition position = (RCPosition) constraints;
		if (position.getRow() < 1 || position.getRow() > 5) {
			throw new UnsupportedOperationException("Invalid row number " + position.getRow());
		}
		if (position.getColumn() < 1 || position.getColumn() > 7) {
			throw new UnsupportedOperationException("Invalid column number " + position.getColumn());
		}
		if (position.getRow() == 1 && (position.getColumn() >= 2 && position.getColumn() <= 5)) {
			throw new UnsupportedOperationException(
					"Invalid combination of row and column (" + position.getRow() + ", " + position.getColumn() + ")");
		}
		if (components.containsKey(position) && comp != components.get(position)) {
			throw new UnsupportedOperationException("Position already taken!");
		}
		return position;
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return calculateSize(target, Integer::compare, (component) -> component.getMaximumSize());
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
	}

}
