package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Razred koji modelira {@link IFilter} koji u svom konstruktoru prima listu
 * {@link ConditionalExpression}-a na temelju kojih filtrira
 * {@link StudentRecord} zapise
 * 
 * @author Ante Miličević
 *
 */
public class QueryFilter implements IFilter {
	/**
	 * Lista usporednih uvjeta
	 */
	private List<ConditionalExpression> expressions;

	/**
	 * Konstruktor kroz koji se prima lista usporednih uvjeta koji se zatim kopiraju
	 * u člansku varijablu tako da prilikom vanjske izmjene se članska varijabla se
	 * mijenja
	 * 
	 * @param expressions lista usporednih uvjeta
	 * 
	 * @throws NullPointerException     ako je expressions null
	 * @throws IllegalArgumentException ako lista sadrži null elemente
	 */
	public QueryFilter(List<ConditionalExpression> expressions) {
		if (Objects.requireNonNull(expressions).indexOf(null) != -1) {
			throw new IllegalArgumentException("Expression can not be null");
		}
		this.expressions = new ArrayList<>(expressions);
	}

	@Override
	public boolean accepts(StudentRecord record) {
		for (ConditionalExpression expression : expressions) {
			if (!expression.testRecord(record)) {
				return false;
			}
		}
		return true;
	}

}
