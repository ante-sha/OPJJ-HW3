package hr.fer.zemris.java.gui.calc.model;

import java.util.HashSet;
import java.util.Set;
import java.util.function.DoubleBinaryOperator;

/**
 * Implementacija modela kalkulatora s jednim registrom za decimalni broj
 * 
 * @author Ante Miličević
 *
 */
public class CalcModelImpl implements CalcModel {

	/**
	 * Zastavica koja označava da je moguće mijenjati vrijednost broja
	 */
	private boolean editable = true;
	/**
	 * Zastavica pozitivnosti broja
	 */
	private boolean positive = true;
	/**
	 * Tekstualna reprezentacija broja
	 */
	private String strNumber = new String("");
	/**
	 * Trenutni broj
	 */
	private double decNumber = 0.0;

	/**
	 * Vrijednost registra sačuvanog broja
	 */
	private double activeOperand;
	/**
	 * Zastavica koja označava da li je registar spreman na korištenje
	 */
	private boolean activeSet = false;

	/**
	 * Operator koji izvodi iduću operaciju
	 */
	private DoubleBinaryOperator operator;
	/**
	 * Skup promatrača
	 */
	private Set<CalcValueListener> listeners = new HashSet<>();

	@Override
	public void addCalcValueListener(CalcValueListener l) {
		listeners.add(l);
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(l);
	}

	/**
	 * Metoda koja obavještava promatrače da se vrijednost u stanju kalkulatora
	 * promijenila
	 */
	private void notifyListeners() {
		listeners.forEach((l) -> l.valueChanged(this));
	}

	@Override
	public double getValue() {
		return decNumber;
	}

	@Override
	public void setValue(double value) {
		if (Double.isNaN(value)) {
			strNumber = "NaN";
			positive = true;
		} else if (Double.isInfinite(value)) {
			positive = value > 0;
			strNumber = "Infinity";
		} else {
			strNumber = Double.toString(value < 0 ? -value : value);
			positive = value >= 0;
		}
		decNumber = value;
		editable = false;
		notifyListeners();
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void clear() {
		decNumber = 0.0;
		strNumber = "";
		positive = true;
		editable = true;
		notifyListeners();
	}

	@Override
	public void clearAll() {
		clear();
		clearActiveOperand();
		operator = null;
		notifyListeners();
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if (!editable) {
			throw new CalculatorInputException("Can not swap sign");
		}
		decNumber = -decNumber;
		positive = !positive;
		notifyListeners();
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if (!editable || strNumber.isEmpty()) {
			throw new CalculatorInputException("Can not insert decimal point");
		}
		if (strNumber.contains(".")) {
			throw new CalculatorInputException("Decimal point already exists!");
		}

		strNumber = strNumber + ".";
		notifyListeners();

	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if (!editable) {
			throw new CalculatorInputException("Calculator is not editable!");
		}
		String newNumber = strNumber.startsWith("0") && !strNumber.contains(".") ? Integer.toString(digit)
				: strNumber + digit;

		decNumber = Double.parseDouble(newNumber);
		if (Double.isInfinite(decNumber)) {
			throw new CalculatorInputException("Not enough memory for new decimal");
		}
		decNumber = positive ? decNumber : -decNumber;

		strNumber = newNumber;
		notifyListeners();
	}

	@Override
	public boolean isActiveOperandSet() {
		return activeSet;
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if (!activeSet) {
			throw new IllegalStateException("Operand is not set");
		}
		return activeOperand;
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
		activeSet = true;
	}

	@Override
	public void clearActiveOperand() {
		activeSet = false;
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return operator;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		this.operator = op;
	}

	@Override
	public String toString() {
		if (strNumber.isEmpty()) {
			return String.format("%s", positive ? "0" : "-0");
		} else {
			return String.format("%s", positive ? strNumber : "-" + strNumber);
		}
	}

}
