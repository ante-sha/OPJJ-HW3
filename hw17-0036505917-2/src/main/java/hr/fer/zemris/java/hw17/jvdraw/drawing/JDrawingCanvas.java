package hr.fer.zemris.java.hw17.jvdraw.drawing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Objects;
import java.util.function.Supplier;

import javax.swing.JComponent;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.toggle.Tool;

/**
 * Komponenta za crtanje geometrijskih objekata definiranih pomoću
 * {@link GeometricalObject} i koji su sadržani u {@link DrawingModel}. Uz već
 * spremljene geometrijske objekte crta se i objekt u nastajanju ako takav
 * postoji i za to je odgovoran trenutno aktivni alat {@link Tool}.
 * 
 * @author Ante Miličević
 *
 */
public class JDrawingCanvas extends JComponent {
	private static final long serialVersionUID = 1L;

	/**
	 * Objekt za dohvat trenutno aktivnog alata
	 */
	private Supplier<Tool> toolSup;
	/**
	 * Model koji sadrži sve geometrijske objekte
	 */
	private DrawingModel dModel;

	/**
	 * Konstruktor
	 * 
	 * @param toolSup objekt za dohvat trenutno aktivnog alata
	 * @param dModel  model koji sadrži sve geometrijske objekte
	 */
	public JDrawingCanvas(Supplier<Tool> toolSup, DrawingModel dModel) {
		setDrawingModelListener(Objects.requireNonNull(dModel));
		this.dModel = dModel;
		this.toolSup = Objects.requireNonNull(toolSup);
		setMouseListeners();
	}

	/**
	 * Metoda za postavljanje promatrača na miš koji ovisno o akcijama miša šalje
	 * trenutno aktivnom alatu informacije o akcijama.
	 */
	private void setMouseListeners() {
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				toolSup.get().mouseClicked(e);
			}
		});
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				toolSup.get().mouseMoved(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
	}

	/**
	 * Metoda koja postavlja promatrača na model tako da se prilikom izmjena u
	 * modelu slika ponovo iscrtava.
	 * 
	 * @param dModel model koji sadrži sve geometrijske objekte
	 */
	private void setDrawingModelListener(DrawingModel dModel) {
		dModel.addDrawingModelListener(new DrawingModelListener() {

			@Override
			public void objectsRemoved(DrawingModel source, int index0, int index1) {
				repaint();
			}

			@Override
			public void objectsChanged(DrawingModel source, int index0, int index1) {
				repaint();
			}

			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				repaint();
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		GeometricalObjectPainter painter = new GeometricalObjectPainter((Graphics2D) g);
		for (int i = 0, n = dModel.getSize(); i < n; i++) {
			dModel.getObject(i).accept(painter);
		}
		toolSup.get().paint((Graphics2D) g);
		g.dispose();
	}
}
