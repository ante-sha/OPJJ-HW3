package hr.fer.zemris.java.hw17.jvdraw.drawing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectListener;

/**
 * Implementacija modela koji sadrži sve geometrijske objekte.
 * 
 * @author Ante Miličević
 *
 */
public class DrawingModelImpl implements DrawingModel {
	/**
	 * Svi geometrijski objekti
	 */
	private List<GeometricalObject> objects = new ArrayList<>();
	/**
	 * Promatrači
	 */
	private List<DrawingModelListener> listeners = new CopyOnWriteArrayList<>();
	/**
	 * Zastavica modificiranosti modela
	 */
	private boolean modified = false;
	/**
	 * Promatrač na geometrijske objekte
	 */
	private GeometricalObjectListener listener = (o)->{
		int index = objects.indexOf(o);
		fireChanged(index,index);
	};

	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		objects.add(object);
		object.addGeometricalObjectListener(listener);
		fireAdded(objects.size() - 1, objects.size() - 1);
	}
 
	@Override
	public void remove(GeometricalObject object) {
		int index = objects.indexOf(object);
		if(index < 0) {
			return;
		}
		objects.remove(object);
		object.removeGeometricalObjectListener(listener);
		
		fireRemoved(index, index);
	}

	@Override
	public void changeOrder(GeometricalObject object, int offset) {
		int index = objects.indexOf(object);
		if (index < 0) {
			throw new RuntimeException("Object do not exist in model");
		}
		if(index + offset < 0 || index + offset > objects.size()) {
			throw new IndexOutOfBoundsException();
		}
		
		objects.remove(object);
		fireRemoved(index,index);
		objects.add(index + offset, object);
		fireAdded(index + offset, index + offset);
	}

	@Override
	public int indexOf(GeometricalObject object) {
		return objects.indexOf(object);
	}

	@Override
	public void clear() {
		int lastIndex = objects.size() - 1;
		objects.clear();
		fireRemoved(0,lastIndex == -1 ? 0 : lastIndex);
	}

	/**
	 * Metoda koja se poziva kada dođe do uklanjanja geometrijskih objekata
	 * 
	 * @param index0 početni indeks
	 * @param index1 završni indeks
	 */
	private void fireRemoved(int index0, int index1) {
		modified = true;
		for(DrawingModelListener listener : listeners) {
			listener.objectsRemoved(this, index0, index1);
		}
	}

	/**
	 * Metoda koja se poziva kada dođe do dodavanja geometrijskih objekata
	 * 
	 * @param index0 početni indeks
	 * @param index1 završni indeks
	 */
	private void fireAdded(int index0, int index1) {
		modified = true;
		for(DrawingModelListener listener : listeners) {
			listener.objectsAdded(this, index0, index1);
		}
	}

	/**
	 * Metoda koja se poziva kada dođe do izmjene geometrijskih objekata
	 * 
	 * @param index0 početni indeks
	 * @param index1 završni indeks
	 */
	private void fireChanged(int index0, int index1) {
		modified = true;
		for(DrawingModelListener listener : listeners) {
			listener.objectsChanged(this, index0, index1);
		}
	}

	@Override
	public void clearModifiedFlag() {
		modified = false;
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		if(l != null && !listeners.contains(l)) {
			listeners.add(l);
		}
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}

}
