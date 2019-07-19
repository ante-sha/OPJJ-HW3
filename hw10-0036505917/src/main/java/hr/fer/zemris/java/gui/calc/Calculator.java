package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcModelImpl;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * Razred koji modelira jednostavano sučelje kalkulatora, stanje kalkulatora je
 * definirano sučeljem {@link CalcModel}.
 * 
 * @author Ante Miličević
 *
 */
public class Calculator extends JFrame {

	/**
	 * Labela koja prikazuje rezultat
	 */
	private JLabel result;

	/**
	 * Oznaka za decimalu
	 */
	private static final String DECIMAL = ".";
	/**
	 * Oznaka za mijenjanje predznaka
	 */
	private static final String SWAP = "+/-";
	/**
	 * Oznaka za sinus
	 */
	private static final String SIN = "sin";
	/**
	 * Oznaka za arkus sinus
	 */
	private static final String ARCSIN = "arcsin";
	/**
	 * Oznaka za kosinus
	 */
	private static final String COS = "cos";
	/**
	 * Oznaka za arkus kosinus
	 */
	private static final String ARCCOS = "arccos";
	/**
	 * Oznaka za tangens
	 */
	private static final String TAN = "tan";
	/**
	 * Oznaka za arkus tangens
	 */
	private static final String ARCTAN = "arctan";
	/**
	 * Oznaka za kontangens
	 */
	private static final String CTG = "ctg";
	/**
	 * Oznaka za arkus kotangens
	 */
	private static final String ARCCTG = "arcctg";
	/**
	 * Oznaka za recipročnu vrijednost
	 */
	private static final String REC = "1/x";
	/**
	 * Oznaka za logaritam
	 */
	private static final String LOG = "log";
	/**
	 * Oznaka za eksponencijalnu funkciju s bazom 10
	 */
	private static final String EXP10 = "10^x";
	/**
	 * Oznaka za prirodni logaritam
	 */
	private static final String LN = "ln";
	/**
	 * Oznaka za eksponencijalu
	 */
	private static final String EXP = "e^x";
	/**
	 * Oznaka za eksponencijalnu funkciju
	 */
	private static final String POW = "x^n";
	/**
	 * Oznaka za korijensku funkciju
	 */
	private static final String ROOT = "x^(1/n)";
	/**
	 * Oznaka za množenje
	 */
	private static final String MUL = "*";
	/**
	 * Oznaka za zbrajanje
	 */
	private static final String ADD = "+";
	/**
	 * Oznaka za oduzimanje
	 */
	private static final String SUB = "-";
	/**
	 * Oznaka za dijeljenje
	 */
	private static final String DIV = "/";
	/**
	 * Oznaka za jednako
	 */
	private static final String EQ = "=";
	/**
	 * Oznaka za čišćenje
	 */
	private static final String CLR = "clr";
	/**
	 * Oznaka za restartiranje stanja kalkulatora
	 */
	private static final String RESET = "reset";
	/**
	 * Oznaka za stavljanje vrijednosti na stog
	 */
	private static final String PUSH = "push";
	/**
	 * Oznaka za uzimanje vrijednosti s vrha stoga
	 */
	private static final String POP = "pop";
	/**
	 * Oznaka za invertiranje funkcija
	 */
	private static final String INVERT = "Inv";

	/**
	 * Defaultni razmak između ćelija
	 */
	private static final int defaultPadding = 3;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Stanje kalkulatora
	 */
	private CalcModel calcModel;

	/**
	 * Stog vrijednosti
	 */
	private LinkedList<Double> stack = new LinkedList<>();

	/**
	 * Promjenjivi gumbi
	 */
	private List<MultableJButton> watched = new ArrayList<>();

	/**
	 * Defaultni konstruktor
	 */
	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("JCalc");
		initGUI();
		pack();

		// place frame in the center of the screen
		Dimension window = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int) (window.width - getWidth()) / 2, (int) (window.getHeight() - getHeight()) / 2);

		// set minimum size to be preffered size
		setMinimumSize(getSize());
	}

	/**
	 * Metoda za inicijalizaciju GUI-a
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(defaultPadding));

		setLabel(cp);

		calcModel = new CalcModelImpl();

		// adding listener that sets text on every change of value
		calcModel.addCalcValueListener((calc) -> {
			result.setText(calc.toString());
		});

		addDigitBtns(cp);

		addModifierBtns(cp);

		addBinaryOperators(cp);

		addUnaryOperators(cp);

		addControlers(cp);
	}

	/**
	 * Metoda dodaje unarne operatore u prozor
	 * 
	 * @param cp ContentPane
	 */
	private void addUnaryOperators(Container cp) {

		cp.add(addOnList(new UnaryOperatorJButton(SIN, ARCSIN, Math::sin, Math::asin, calcModel)),
				new RCPosition(2, 2));

		cp.add(addOnList(new UnaryOperatorJButton(COS, ARCCOS, Math::cos, Math::acos, calcModel)),
				new RCPosition(3, 2));

		cp.add(addOnList(new UnaryOperatorJButton(TAN, ARCTAN, Math::tan, Math::atan, calcModel)),
				new RCPosition(4, 2));

		cp.add(addOnList(new UnaryOperatorJButton(CTG, ARCCTG, (d) -> (1. / Math.tan(d)), (d) -> (1. / Math.atan(d)),
				calcModel)), new RCPosition(5, 2));

		cp.add(new UnaryOperatorJButton(REC, (d) -> 1 / d, calcModel), new RCPosition(2, 1));

		cp.add(addOnList(new UnaryOperatorJButton(LOG, EXP10, Math::log10, (d) -> Math.pow(10, d), calcModel)),
				new RCPosition(3, 1));

		cp.add(addOnList(new UnaryOperatorJButton(LN, EXP, Math::log, Math::exp, calcModel)), new RCPosition(4, 1));
	}

	/**
	 * Metoda dodaje promjenjive gumbe listu promjenjivih gumbi {@code watched}
	 * 
	 * @param multableOperatorJButton gumb promjenjivog operatora
	 * @return mutableOperatorJButton
	 */
	private Component addOnList(MultableJButton multableOperatorJButton) {
		watched.add(multableOperatorJButton);
		return multableOperatorJButton;
	}

	/**
	 * Metoda dodaje kontrolne funkcije kalkulatora u prozor
	 * 
	 * @param cp ContentPadne
	 */
	private void addControlers(Container cp) {
		cp.add(generateButton(CLR, (e) -> calcModel.clear()), new RCPosition(1, 7));

		cp.add(generateButton(RESET, (e) -> calcModel.clearAll()), new RCPosition(2, 7));

		cp.add(generateButton(PUSH, (e) -> stack.push(calcModel.getValue())), new RCPosition(3, 7));

		cp.add(generateButton(POP, (e) -> {
			try {
				calcModel.setValue(stack.pop());
			} catch (NoSuchElementException ex) {
				JOptionPane.showMessageDialog(Calculator.this, "Stack is empty", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}), new RCPosition(4, 7));

		cp.add(generateInvertCheckBox(), new RCPosition(5, 7));
	}

	/**
	 * Metoda generira check box za invertiranje operacija u
	 * {@link MutableOperatorJButton}
	 * 
	 * @return check box koji invertira operacije
	 */
	private Component generateInvertCheckBox() {
		/**
		 * Check box za invertiranje operacija u {@link MutableOperatorJButton}
		 * 
		 * @author Ante Miličević
		 *
		 */
		class InvertCheck extends JCheckBox {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Defaultni konstruktor koji postavlja promatrača koji prilikom klika invertira
			 * sve operatore iz liste {@code watched}
			 */
			public InvertCheck() {
				super(INVERT);
				initBox();
			}

			/**
			 * Metoda za inicijalizaciju
			 */
			private void initBox() {
				addActionListener((e) -> {
					watched.forEach((btn) -> btn.changeOperator());
				});
			}
		}
		return new InvertCheck();
	}

	/**
	 * Metoda za dodavanje binarnih operatora u prozor
	 * 
	 * @param cp ContentPane
	 */
	private void addBinaryOperators(Container cp) {
		cp.add(new BinaryOperatorJButton(EQ, null, calcModel), new RCPosition(1, 6));

		cp.add(new BinaryOperatorJButton(DIV, (d1, d2) -> d1 / d2, calcModel), new RCPosition(2, 6));

		cp.add(new BinaryOperatorJButton(MUL, (d1, d2) -> d1 * d2, calcModel), new RCPosition(3, 6));

		cp.add(new BinaryOperatorJButton(SUB, (d1, d2) -> d1 - d2, calcModel), new RCPosition(4, 6));

		cp.add(new BinaryOperatorJButton(ADD, (d1, d2) -> d1 + d2, calcModel), new RCPosition(5, 6));

		cp.add(addOnList(new BinaryOperatorJButton(POW, ROOT, (d1, d2) -> Math.pow(d1, d2),
				(d1, d2) -> Math.pow(d1, 1.0 / d2), calcModel)), new RCPosition(5, 1));
	}

	/**
	 * Metoda za dodavanje modifikatora broja u prozor
	 * 
	 * @param cp ContentPane
	 */
	private void addModifierBtns(Container cp) {
		ButtonModifierListener ml = new ButtonModifierListener();

		cp.add(generateButton(DECIMAL, ml), new RCPosition(5, 5));
		cp.add(generateButton(SWAP, ml), new RCPosition(5, 4));
	}

	/**
	 * Metoda koja postavlja labelu rezultat
	 * 
	 * @param cp ContentPane kontejner
	 */
	private void setLabel(Container cp) {
		result = new JLabel("");
		result.setBackground(Color.YELLOW);
		result.setFont(result.getFont().deriveFont(30f));

		result.setOpaque(true);
		result.setHorizontalAlignment(SwingConstants.RIGHT);
		result.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		cp.add(result, new RCPosition(1, 1));
	}

	/**
	 * Metoda za dodavanje brojeva u prozor
	 * 
	 * @param cp ContentPane
	 */
	private void addDigitBtns(Container cp) {
		cp.add(new NumberJButton(0), new RCPosition(5, 3));
		cp.add(new NumberJButton(1), new RCPosition(4, 3));
		cp.add(new NumberJButton(2), new RCPosition(4, 4));
		cp.add(new NumberJButton(3), new RCPosition(4, 5));
		cp.add(new NumberJButton(4), new RCPosition(3, 3));
		cp.add(new NumberJButton(5), new RCPosition(3, 4));
		cp.add(new NumberJButton(6), new RCPosition(3, 5));
		cp.add(new NumberJButton(7), new RCPosition(2, 3));
		cp.add(new NumberJButton(8), new RCPosition(2, 4));
		cp.add(new NumberJButton(9), new RCPosition(2, 5));

	}

	/**
	 * Metoda za generiranje gumba čiji je naziv jednak naredbi
	 * 
	 * @param command naredba
	 * @param al      objekt koji obrađuje događaj nad gumbom
	 * 
	 * @return generirani gumb
	 */
	private Component generateButton(String command, ActionListener al) {
		JButton btn = new JButton(command);
		btn.setActionCommand(command);
		btn.addActionListener(al);

		return btn;
	}

	/**
	 * Razred koji modelira gumb jedne numeričke znamenke u kalkulatoru
	 * 
	 * @author Ante Miličević
	 *
	 */
	private class NumberJButton extends JButton {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Znamenka
		 */
		private int digit;

		/**
		 * Konstruktor koji inicijalizira znamenku
		 * 
		 * @param digit znamenka
		 */
		public NumberJButton(int digit) {
			super(Integer.toString(digit));

			this.digit = digit;
			initBtn();
		}

		/**
		 * Inicijalizacijska metoda
		 */
		private void initBtn() {
			// set larger font
			setFont(getFont().deriveFont(30f));
			addActionListener((e) -> {
				try {
					if (calcModel.isEditable()) {
						calcModel.insertDigit(digit);
					} else {
						calcModel.clear();
						calcModel.insertDigit(digit);
					}
				} catch (CalculatorInputException ex) {
					JOptionPane.showMessageDialog(Calculator.this, "Decimal number is too large", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			});
		}

		@Override
		public Dimension getPreferredSize() {
			FontMetrics fm = this.getFontMetrics(getFont());
			Insets in = getInsets();
			return new Dimension(fm.stringWidth(Integer.toString(digit)) + in.left + in.right, fm.getHeight());
		}
	}

	/**
	 * Promatrač za modifikatore kalkulatora
	 * 
	 * @author Ante Miličević
	 */
	private class ButtonModifierListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals(DECIMAL)) {
				try {
					if (calcModel.isEditable()) {
						calcModel.insertDecimalPoint();
					} else {
						calcModel.clear();
					}
				} catch (CalculatorInputException ex) {
				}
			} else if (command.equals(SWAP)) {
				try {
					if (calcModel.isEditable()) {
						calcModel.swapSign();
					} else {
						calcModel.clear();
					}
				} catch (CalculatorInputException ex) {
				}
			} else {
				throw new IllegalArgumentException("Unsupported action");
			}
		}

	}

	/**
	 * Ulazna točka programa
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new Calculator().setVisible(true);
		});
	}

}
