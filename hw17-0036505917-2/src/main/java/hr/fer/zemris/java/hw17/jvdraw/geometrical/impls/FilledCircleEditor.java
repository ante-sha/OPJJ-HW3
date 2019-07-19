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
 * Implementacija za uređivač kruga.
 * 
 * @author Ante Miličević
 *
 */
public class FilledCircleEditor extends GeometricalObjectEditor {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Krug koji se uređuje
	 */
	private FilledCircle fCircle;
	/**
	 * Polje za unos x koordinate centra kruga
	 */
	private JTextField xCenter;
	/**
	 * Polje za unos y koordinate centra kruga
	 */
	private JTextField yCenter;
	/**
	 * Polje za unos polumjera kruga
	 */
	private JTextField radius;
	/**
	 * Polje za promjenu boje ruba kruga
	 */
	private JColorArea fgColor;
	/**
	 * Polje za promjenu boje kruga
	 */
	private JColorArea bgColor;
	
	/**
	 * Konstruktor
	 * 
	 * @param fCircle objekt koji se uređuje
	 */
	public FilledCircleEditor(FilledCircle fCircle) {
		this.fCircle = Objects.requireNonNull(fCircle);
		initEditor();
	}

	/**
	 * Metoda za inicijalizaciju komponente.
	 */
	private void initEditor() {
		setLayout(new GridLayout(0, 1));
		
		xCenter = new JTextField(String.format("%.2f",fCircle.getCenter().getX()));
		xCenter.setHorizontalAlignment(JTextField.RIGHT);
		yCenter = new JTextField(String.format("%.2f",fCircle.getCenter().getY()));
		yCenter.setHorizontalAlignment(JTextField.RIGHT);
		
		xCenter.setColumns(10);
		yCenter.setColumns(10);
		
		JPanel center = new JPanel(new FlowLayout());
		center.add(new JLabel("Center (x,y): "));
		center.add(xCenter);
		center.add(yCenter);
		add(center);
		
		JPanel radiusPanel = new JPanel(new FlowLayout());
		radius = new JTextField(String.format("%.2f",fCircle.getRadius()));
		radius.setColumns(10);
		radius.setHorizontalAlignment(JTextField.RIGHT);
		radiusPanel.add(new JLabel("Radius: "));
		radiusPanel.add(radius);
		add(radiusPanel);
		
		JPanel colors = new JPanel(new FlowLayout(FlowLayout.CENTER));
		fgColor = new JColorArea(fCircle.getFgColor());
		colors.add(new JLabel("FG"));
		colors.add(fgColor);
		bgColor = new JColorArea(fCircle.getBgColor());
		colors.add(bgColor);
		colors.add(new JLabel("BG"));
		
		add(colors);
	}

	@Override
	public void checkEditing() {
		try {
			Double.parseDouble(xCenter.getText());
			Double.parseDouble(yCenter.getText());
			Double.parseDouble(radius.getText());
		} catch (NumberFormatException e) {
			throw new RuntimeException("Invalid data");
		}
	}

	@Override
	public void acceptEditing() {
		Point center = new Point((int)Double.parseDouble(xCenter.getText()), (int)Double.parseDouble(yCenter.getText()));
		
		fCircle.setCenter(center);
		fCircle.setRadius(Double.parseDouble(radius.getText()));
		fCircle.setFgColor(fgColor.getCurrentColor());
		fCircle.setBgColor(bgColor.getCurrentColor());
	}

}
