package hr.fer.zemris.java.gui.prim;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Model liste prostih brojeva.
 * 
 * @author Ante Miličević
 *
 */
public class PrimListModel implements ListModel<Integer> {

	/**
	 * Kolekcija izgeneriranih prostih brojeva
	 */
	private List<Integer> primCol = new LinkedList<>();
	/**
	 * Skup promatrača
	 */
	private Set<ListDataListener> listeners = new HashSet<>();
	/**
	 * Zadnji predani prosti broj
	 */
	private int lastPrim = 1;
	
	public PrimListModel() {
		primCol.add(lastPrim);
	}
	
	/**
	 * Metoda koja generira prosti broj
	 */
	public void next() {
		List<Integer> listForIteration = primCol.subList(1, primCol.size());
		
		for(lastPrim++;;lastPrim++) {
			boolean prim = true;
			
			for(Integer i : listForIteration) {
				if(i > Math.sqrt(lastPrim)) {
					break;
				} else if(lastPrim % i == 0) {
					prim = false;
					break;
				}
			}
			
			if(prim) {
				break;
			}
		}
		
		primCol.add(lastPrim);
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, primCol.size() - 1, primCol.size() -1);
		notifyAllListeners(event);
	}
	
	/**
	 * Metoda koja javlja svim primatračima da se dogodio {@code event}
	 * 
	 * @param event događaj
	 */
	private void notifyAllListeners(ListDataEvent event) {
		listeners.forEach((l)->l.intervalAdded(event));
	}

	@Override
	public int getSize() {
		return primCol.size();
	}

	@Override
	public Integer getElementAt(int index) {
		return primCol.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(Objects.requireNonNull(l));
		
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);		
	}
}
