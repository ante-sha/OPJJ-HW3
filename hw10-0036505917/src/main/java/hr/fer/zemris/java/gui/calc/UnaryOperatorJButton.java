package hr.fer.zemris.java.gui.calc;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

/**
 * Razred koji implementira promjenjivi unarni operator nad decimalnim
 * vrijednostima.
 * 
 * @author Ante Miličević
 *
 */
public class UnaryOperatorJButton extends MultableJButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Prvi operator
	 */
	private DoubleUnaryOperator op1;
	/**
	 * Drugi operator
	 */
	private DoubleUnaryOperator op2;
	/**
	 * Stanje kalkulatora
	 */
	private CalcModel calcModel;

	/**
	 * Konstruktor koji definira promjenjivi kalkulator sa svim njegovim parametrima
	 * 
	 * @param name1     naziv prvog operatora
	 * @param name2     naziv drugog operatora
	 * @param op1       prvi operator
	 * @param op2       drugi operator
	 * @param calcModel stanje kalkulatora
	 * 
	 * @throws NullPointerException ako su nazivi ili calcModel null
	 */
	public UnaryOperatorJButton(String name1, String name2, DoubleUnaryOperator op1, DoubleUnaryOperator op2, CalcModel calcModel) {
		super(name1, name2);
		this.op1 = Objects.requireNonNull(op1);
		this.op2 = Objects.requireNonNull(op2);
		this.calcModel = Objects.requireNonNull(calcModel);

		initBtn();
	}

	/**
	 * Konstruktor koji definira operator koji nije promjenjiv, tj. ako se nad njim
	 * izvrši promjena ista nema utjecaja
	 * 
	 * @param name naziv operacije
	 * @param op operacija
	 * @param calcModel stanje kalkulatora
	 * 
	 * @throws NullPointerException ako je name ili calcModel null
	 */
	public UnaryOperatorJButton(String name, DoubleUnaryOperator op, CalcModel calcModel) {
		this(name, name, op, op, calcModel);
	}

	/**
	 * Metoda za inicijalizaciju
	 */
	private void initBtn() {
		addActionListener((e) -> {
			DoubleUnaryOperator op = isOneActive() ? op1 : op2;
			calcModel.setValue(op.applyAsDouble(calcModel.getValue()));
		});
	}
}
