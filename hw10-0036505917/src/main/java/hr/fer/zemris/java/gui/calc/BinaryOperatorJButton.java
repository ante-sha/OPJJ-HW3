package hr.fer.zemris.java.gui.calc;

import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

/**
 * Razred koji implementira promjenjivi binarni operator nad decimalnim
 * vrijednostima.
 * 
 * @author Ante Miličević
 *
 */
public class BinaryOperatorJButton extends MultableJButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Prvi operator
	 */
	private DoubleBinaryOperator op1;
	/**
	 * Drugi operator
	 */
	private DoubleBinaryOperator op2;

	/**
	 * Status kalkulatora
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
	 * @throws NullPointerException ako su imena ili calcModel null
	 */
	public BinaryOperatorJButton(String name1, String name2, DoubleBinaryOperator op1, DoubleBinaryOperator op2,
			CalcModel calcModel) {
		super(name1, name2);
		this.op1 = op1;
		this.op2 = op2;

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
	public BinaryOperatorJButton(String name, DoubleBinaryOperator op, CalcModel calcModel) {
		this(name, name, op, op, calcModel);
	}

	/**
	 * Inicijalizacija gumba
	 */
	private void initBtn() {
		addActionListener((e) -> {
			DoubleBinaryOperator operator = isOneActive() ? op1 : op2;
			if (operator == null) {
				if (calcModel.getPendingBinaryOperation() == null) {
					calcModel.setValue(calcModel.getValue());
				} else if (calcModel.isActiveOperandSet()) {
					calcModel.setValue(calcModel.getPendingBinaryOperation().applyAsDouble(calcModel.getActiveOperand(),
							calcModel.getValue()));
				}
				calcModel.clearActiveOperand();
			} else if (calcModel.isActiveOperandSet()) {
				calcModel.setActiveOperand(calcModel.getPendingBinaryOperation()
						.applyAsDouble(calcModel.getActiveOperand(), calcModel.getValue()));
				calcModel.clear();
			} else {
				calcModel.setActiveOperand(calcModel.getValue());
				calcModel.clear();
			}
			calcModel.setPendingBinaryOperation(operator);
		});

	}
}