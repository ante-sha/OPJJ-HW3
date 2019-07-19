package hr.fer.zemris.java.gui.prim;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Consumer;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrimListModelTest {

	public static boolean enter;
	
	private PrimListModel getNewList() {
		return new PrimListModel();
	}
	
	private ListDataListener getListener(Consumer<ListDataEvent> cons) {
		return new ListDataListener() {
			
			@Override
			public void intervalAdded(ListDataEvent e) {
				cons.accept(e);
			}
			
			@Override
			public void intervalRemoved(ListDataEvent e) {
				Assertions.fail();
			}

			@Override
			public void contentsChanged(ListDataEvent e) {
				Assertions.fail();
			}
		};
	}

	@Test
	public void addNullListener() {
		assertThrows(NullPointerException.class,()->getNewList().addListDataListener(null));
	}
	
	@Test
	public void emptyList() {
		assertEquals(1,getNewList().getSize());
	}
	
	@Test
	public void listenerCallOnUpdate() {
		PrimListModel prim = getNewList();
		
		enter = false;
		
		ListDataListener listener = getListener((e)->{
			enter = true;
		});
		
		prim.addListDataListener(listener);
		
		prim.next();
		
		if(!enter) {
			fail();
		}
	}
	
	@Test
	public void listenerCallAfterRemove() {
		PrimListModel prim = getNewList();
		
		ListDataListener listener = getListener((e)->{
			enter = true;
		});
		
		prim.next();
		enter = false;
		prim.removeListDataListener(listener);
		
		prim.next();
		if(enter) {
			fail();
		}
	}
	
	@Test
	public void primCheck() {
		PrimListModel prim = getNewList();
		
		assertEquals(1, prim.getElementAt(0));
		
		prim.next();
		assertEquals(2,prim.getElementAt(1));
		assertEquals(2,prim.getSize());
		prim.next();
		assertEquals(3,prim.getElementAt(2));
		assertEquals(3,prim.getSize());
		prim.next();
		prim.next();
		prim.next();
		prim.next();
		
		assertEquals(5,prim.getElementAt(3));
		assertEquals(7,prim.getElementAt(4));
		assertEquals(11,prim.getElementAt(5));
		assertEquals(13,prim.getElementAt(6));
	}
}
