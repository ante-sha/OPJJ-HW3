package hr.fer.zemris.java.hw17.jvdraw.geometrical.impls;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.color.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectEditor;

/**
 * Implementacija za uređivač dužine.
 * 
 * @author Ante Miličević
 *
 */
public class LineEditor extends GeometricalObjectEditor {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Dužina koja se uređuje
	 */
	private Line line;
	/**
	 * Polje za unos x koordinate početne točke
	 */
	private JTextField startX;
	/**
	 * Polje za unos y koordinate početne točke
	 */
	private JTextField startY;
	/**
	 * Polje za unos x koordinate završne točke
	 */
	private JTextField endX;
	/**
	 * Polje za unos y koordinate završne točke
	 */
	private JTextField endY;
	/**
	 * Boja dužine
	 */
	private JColorArea color;
	
	/**
	 * Konstruktor
	 * 
	 * @param line dužina koja se uređuje
	 */
	public LineEditor(Line line) {
		this.line = Objects.requireNonNull(line);
		initEditor();
	}

	/**
	 * Metoda za inicijalizaciju komponente.
	 */
	private void initEditor() {
		setLayout(new GridLayout(0, 1));
		
		startX = new JTextField(String.format("%.2f",line.getStartPoint().getX()));
		startX.setHorizontalAlignment(JTextField.RIGHT);
		startY = new JTextField(String.format("%.2f",line.getStartPoint().getY()));
		startY.setHorizontalAlignment(JTextField.RIGHT);
		
		startX.setColumns(10);
		startY.setColumns(10);
		
		JPanel startPan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		startPan.add(new JLabel("Start"));
		startPan.add(startX);
		startPan.add(startY);
		add(startPan);
		
		endX = new JTextField(String.format("%.2f",line.getEndPoint().getX()));
		endX.setHorizontalAlignment(JTextField.RIGHT);
		endY = new JTextField(String.format("%.2f",line.getEndPoint().getY()));
		endY.setHorizontalAlignment(JTextField.RIGHT);
		
		endX.setColumns(10);
		endY.setColumns(10);
		
		JPanel endPan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		endPan.add(new JLabel("End"));
		endPan.add(endX);
		endPan.add(endY);
		add(endPan);
		
		JPanel lineColor = new JPanel(new FlowLayout(FlowLayout.CENTER));
		color = new JColorArea(line.getColor());
		lineColor.add(new JLabel("FG"));
		lineColor.add(color);
		add(lineColor);
	}

	@Override
	public void checkEditing() {
		try {
			Double.parseDouble(startX.getText());
			Double.parseDouble(startY.getText());
			Double.parseDouble(endX.getText());
			Double.parseDouble(endY.getText());
		} catch (NumberFormatException e) {
			throw new RuntimeException("Invalid data");
		}
	}

	@Override
	public void acceptEditing() {
		Point start = new Point((int)Double.parseDouble(startX.getText()), (int)Double.parseDouble(startY.getText()));
		Point end = new Point((int)Double.parseDouble(endX.getText()), (int)Double.parseDouble(endY.getText()));
		
		line.setStartPoint(start);
		line.setEndPoint(end);
		line.setColor(color.getCurrentColor());
	}

}
