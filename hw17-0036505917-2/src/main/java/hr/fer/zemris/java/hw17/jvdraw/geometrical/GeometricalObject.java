package hr.fer.zemris.java.hw17.jvdraw.geometrical;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Razred koji modelira funkcionalnost OO Observer i Visitor. GeometricalObject
 * je model geometrijskog objekta kojeg se sprema u DrawingModel, a iscrtava
 * u JDrawingCanvas.
 * 
 * @author Ante Miličević
 *
 */
public abstract class GeometricalObject {
	/**
	 * Lista promatrača
	 */
	private List<GeometricalObjectListener> listeners = new CopyOnWriteArrayList<>();
	
	/**
	 * Metoda za prihvaćanje visitora
	 * 
	 * @param v visitor
	 */
	public abstract void accept(GeometricalObjectVisitor v);
	
	/**
	 * Metoda za stvaranje komponente za uređivanje geometrijskog objekta.
	 * 
	 * @return komponenta za uređivanje geometrijskog objekta
	 */
	public abstract GeometricalObjectEditor createGeometricalObjectEditor();
	
	/**
	 * Metoda za dodavanje promatrača na geometrijski objekt
	 * 
	 * @param l promatrač
	 */
	public void addGeometricalObjectListener(GeometricalObjectListener l) {
		if(l != null && !listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	/**
	 * Metoda za uklanjanje promatrača na geometrijski objekt
	 * 
	 * @param l promatrač
	 */
	public void removeGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Metoda za dojavu promjene geometrijskog objekta
	 */
	protected void fireChanged() {
		for(GeometricalObjectListener listener : listeners)  {
			listener.geometricalObjectChanged(this);
		}
	}
}
