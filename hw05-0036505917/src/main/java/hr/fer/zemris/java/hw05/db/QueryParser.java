package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static hr.fer.zemris.java.hw05.db.Token.TokenType.*;

/**
 * Razred koji modelira parser za naredbe filtriranja zapisa
 * {@link StudentRecord} koji koji su spremljeni unutar razreda
 * {@link StudentDatabase}. Naredbe se sastaju od jedne ili više uvjetnih
 * usporedbi povezanih AND operatorom. Ako se u naredbi nalazi samo jedan uvjet
 * koji ispituje jednakost literala s jmbagom tada je naredba direktna, tj.
 * moguće je dobiti rezultat u složenosti O(1).
 * 
 * @author Ante Miličević
 *
 */
public class QueryParser {
	/**
	 * Lekser koji tokenizira tekst naredbe
	 */
	private QueryLexer lexer;
	/**
	 * Lista usporednih uvjeta
	 */
	private List<ConditionalExpression> expressions = new ArrayList<>();
	/**
	 * Zastavica direktnosti
	 */
	private boolean direct = false;

	private static final String AND = "and";

	/**
	 * Konstruktor koji kao argument prima tekst naredbe
	 * 
	 * @param query tekst naredbe
	 * 
	 * @throws NullPointerException     ako je {@code query == null}
	 * @throws IllegalArgumentException ako naredba nema dobar format
	 */
	public QueryParser(String query) {
		lexer = new QueryLexer(Objects.requireNonNull(query));

		parse();
	}

	/**
	 * Metoda koja parsira tekst u usporedne uvjete povezane and operatorom. Naredba
	 * se mora sastojati od minimalno jednog usporednog uvjeta.
	 * 
	 * @throws IllegalArgumentException ako je naredba u krivom formatu ili ako
	 *                                  naredba ne sadrži izraz
	 */
	private void parse() {
		try {
			do {
				readExpression();
			} while (isAndToken(lexer.nextToken()));
		} catch (IllegalArgumentException e) {
			throw expressions.size() == 0 ? new IllegalArgumentException("There is no valid expression in query") : e;
		}

		if (!lexer.getEnd())
			throw new IllegalArgumentException("Query is in wrong format");

		checkIfDirect();
	}

	/**
	 * Pomoćna metoda koja provjerava je li {@code token} token koji predstavlja
	 * ključnu riječ and, usporedba zanemaruje da li su slova velika ili mala
	 * 
	 * @param token
	 * @return true ako je, false inače
	 */
	private boolean isAndToken(Token token) {
		if (token.getType() != TEXT) {
			return false;
		}
		if (AND.compareToIgnoreCase(token.getValue()) != 0) {
			return false;
		}
		return true;
	}

	/**
	 * Metoda koja provjerava da li naredba direktna, ako je u zastavicu
	 * {@code direct} upisuje true
	 */
	private void checkIfDirect() {
		if (expressions.size() != 1) {
			return;
		}
		ConditionalExpression expression = expressions.get(0);

		if (expression.getFieldGetter() != FieldValueGetters.JMBAG) {
			return;
		}
		if (expression.getOperator() != ComparisonOperators.EQUALS) {
			return;
		}

		direct = true;
	}

	/**
	 * Metoda koja čita atribut,operator i literal usporednog izraza te pomoću njih
	 * inicijalizira novi {@link ConditionalExpression} i sprema ga u svoju
	 * kolekciju usporednih izraza
	 */
	private void readExpression() {
		IFieldValueGetter attribute = readAttribute();
		IComparisonOperator operator = readOperator();
		String literal = readLiteral();
		expressions.add(new ConditionalExpression(attribute, literal, operator));

	}

	/**
	 * Metoda koja čita atribut izraza
	 * 
	 * @return {@link IFieldValueGetter} za navedeni atribut
	 * 
	 * @throws IllegalArgumentException ako atribut ne postoji ili ako se tokeni ne
	 *                                  mogu interpretirati kao atribut
	 */
	private IFieldValueGetter readAttribute() {
		Token atribute = lexer.nextToken();
		if (atribute.getType() == TEXT) {
			return FieldValueGetters.interpretStringAsFieldGetter(atribute.getValue());
		}
		throw new IllegalArgumentException("Atribute is missing");
	}

	/**
	 * Metoda koja čita operator izraza
	 * 
	 * @return {@link ComparisonOperator} za navedeni operator
	 * 
	 * @throws IllegalArgumentException ako operator nije podržan
	 */
	private IComparisonOperator readOperator() {
		Token operator = lexer.nextToken();
		IComparisonOperator comparisonOperator = null;

		if (operator.getType() == OPERATOR) {
			comparisonOperator = ComparisonOperators.interpretStringAsOperator((operator.getValue()));
		} else if (operator.getType() == TEXT && operator.getValue().equals("LIKE")) {
			comparisonOperator = ComparisonOperators.interpretStringAsOperator(operator.getValue());
		} else {
			throw new IllegalArgumentException(
					"Operator " + (operator.getType() == EOF ? "" : operator.getValue()) + " is not defined!");
		}
		return comparisonOperator;
	}

	/**
	 * Metoda koja čita literal izraza
	 * 
	 * @return litaral
	 * 
	 * @throws IllegalArgumentException ako sljedeći niz znakova nije literal
	 */
	private String readLiteral() {
		Token literal = lexer.nextToken();
		if (literal.getType() != LITERAL)
			throw new IllegalArgumentException(
					"Query definition is wrong near " + (literal.getType() == EOF ? "end" : literal.getValue()));
		return literal.getValue();
	}

	/**
	 * Metoda koja provjerava je li naredba direktna, tj. da li je rezultat
	 * dohvatljiv u složenosti O(1)
	 * 
	 * @return true ako je, false inače
	 */
	public boolean isDirectQuery() {
		return direct;
	}

	/**
	 * Metoda koja vraća listu {@link ConditionalExpression} koji predstavljaju
	 * uvjetne usporedbe koje su navedene u naredbi predanoj u konstruktoru
	 * 
	 * @return lista uvjetnih usporedbi
	 */
	public List<ConditionalExpression> getQuery() {
		return expressions;
	}

	/**
	 * Metoda koja, ako je naredba direktna, dohvaća traženi jmbag
	 * inače javlja grešku
	 * 
	 * @return traženi jmbag
	 */
	public String getQueriedJMBAG() {
		if (!isDirectQuery())
			throw new IllegalStateException("Query is not direct!");

		return expressions.get(0).getLiteral();
	}
}
