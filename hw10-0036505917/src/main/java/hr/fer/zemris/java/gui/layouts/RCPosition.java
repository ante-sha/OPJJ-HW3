package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

/**
 * Razred koji označava ograničenja postavljanja komponenti u
 * {@link CalcLayout}.
 * 
 * @author Ante Miličević
 *
 */
public class RCPosition {

	/**
	 * Redak
	 */
	private int row;
	/**
	 * Stupac
	 */
	private int column;

	/**
	 * Konstruktor koji definira redak i stupac
	 * @param row
	 * @param column
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Getter za redak
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Getter za stupac
	 * @return stupac
	 */
	public int getColumn() {
		return column;
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RCPosition))
			return false;
		RCPosition other = (RCPosition) obj;
		return column == other.column && row == other.row;
	}
}
