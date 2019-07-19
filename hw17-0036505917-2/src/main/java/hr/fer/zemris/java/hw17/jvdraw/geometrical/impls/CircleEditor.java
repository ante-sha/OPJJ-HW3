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
 * Implementacija za uređivač kružnicu.
 * 
 * @author Ante Miličević
 *
 */
public class CircleEditor extends GeometricalObjectEditor {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Kružnica koju se uređuje
	 */
	private Circle circle;
	/**
	 * Polje za unos x koordinate točke središta
	 */
	private JTextField xCenter;
	/**
	 * Polje za unos y koordinata točke središta
	 */
	private JTextField yCenter;
	/**
	 * Polje za unos polumjera
	 */
	private JTextField radius;
	/**
	 * Polje za odabir boje kružnice
	 */
	private JColorArea color;
	
	/**
	 * Konstruktor
	 * 
	 * @param circle kružnica koja se uređuje
	 */
	public CircleEditor(Circle circle) {
		this.circle = Objects.requireNonNull(circle);
		initEditor();
	}

	/**
	 * Metoda za inicijalizaciju komponente.
	 */
	private void initEditor() {
		setLayout(new GridLayout(0, 1));
		xCenter = new JTextField(String.format("%.2f",circle.getCenter().getX()));
		xCenter.setHorizontalAlignment(JTextField.RIGHT);
		yCenter = new JTextField(String.format("%.2f",circle.getCenter().getY()));
		yCenter.setHorizontalAlignment(JTextField.RIGHT);
		
		xCenter.setColumns(10);
		yCenter.setColumns(10);
		
		JPanel center = new JPanel(new FlowLayout());
		center.add(new JLabel("Center (x,y): "));
		center.add(xCenter);
		center.add(yCenter);
		add(center);
		
		JPanel radiusPanel = new JPanel(new FlowLayout());
		radius = new JTextField(String.format("%.2f",circle.getRadius()));
		radius.setColumns(10);
		radius.setHorizontalAlignment(JTextField.RIGHT);
		radiusPanel.add(new JLabel("Radius: "));
		radiusPanel.add(radius);
		add(radiusPanel);
		
		JPanel colorPanel = new JPanel(new FlowLayout());
		color = new JColorArea(circle.getColor());
		colorPanel.add(new JLabel("FG"));
		colorPanel.add(color);
		add(colorPanel);
	}

	@Override
	public void checkEditing() {
		try {
			Integer.parseInt(xCenter.getText());
			Integer.parseInt(yCenter.getText());
			Double.parseDouble(radius.getText());
		} catch (NumberFormatException e) {
			throw new RuntimeException("Invalid data");
		}
	}

	@Override
	public void acceptEditing() {
		Point center = new Point((int)Double.parseDouble(xCenter.getText()), (int)Double.parseDouble(yCenter.getText()));
		
		circle.setCenter(center);
		circle.setRadius(Double.parseDouble(radius.getText()));
		circle.setColor(color.getCurrentColor());
		
//		circle.fireChanged();
	}

}
