package hr.fer.zemris.java.gui.layouts;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

public class CalcLayoutTest {

	@Test
	public void RCindexOutOfBounds() {
		CalcLayout cl = new CalcLayout();
		JPanel p = new JPanel(cl);
		assertThrows(UnsupportedOperationException.class, () -> {
			p.add(new JLabel(""), new RCPosition(0, 4));
		});

		assertThrows(UnsupportedOperationException.class, () -> {
			p.add(new JLabel(""), new RCPosition(1, 8));
		});

		assertThrows(UnsupportedOperationException.class, () -> {
			p.add(new JLabel(""), new RCPosition(-1, -2));
		});

		assertThrows(UnsupportedOperationException.class, () -> {
			p.add(new JLabel(""), new RCPosition(6, 9));
		});
	}
	
	@Test
	public void RCUnsupportedIndexes() {
		CalcLayout cl = new CalcLayout();
		JPanel p = new JPanel(cl);
		
		assertThrows(UnsupportedOperationException.class,()->{
			p.add(new JLabel(""), new RCPosition(1,2));
		});
		
		assertThrows(UnsupportedOperationException.class,()->{
			p.add(new JLabel(""), new RCPosition(1,3));
		});
		
		assertThrows(UnsupportedOperationException.class,()->{
			p.add(new JLabel(""), new RCPosition(1,4));
		});
		
		assertThrows(UnsupportedOperationException.class,()->{
			p.add(new JLabel(""), new RCPosition(1,5));
		});
	}
	
	@Test
	public void stringAdd() {
		CalcLayout cl = new CalcLayout();
		JPanel p = new JPanel(cl);
		
		p.add(new JLabel("1"),"1,1");
		p.add(new JLabel("2"),"2,7");
		p.add(new JLabel("1"),"4,1");
	}
	
	@Test
	public void unsupportedIndexesStringAdd() {
		CalcLayout cl = new CalcLayout();
		JPanel p = new JPanel(cl);
		
		assertThrows(UnsupportedOperationException.class,()->{
			p.add(new JLabel(""),"1,2");
		});
		
		assertThrows(UnsupportedOperationException.class,()->{
			p.add(new JLabel(""),"1,3");
		});
		
		assertThrows(UnsupportedOperationException.class,()->{
			p.add(new JLabel(""),"1,4");
		});
		
		assertThrows(UnsupportedOperationException.class,()->{
			p.add(new JLabel(""),"1,5");
		});
	}
	
	@Test
	public void multipleComponentsOnSamePlace() {
		CalcLayout cl = new CalcLayout();
		JPanel p = new JPanel(cl);
		
		p.add(new JLabel(""), new RCPosition(1,1));
		p.add(new JLabel("p"), new RCPosition(2,3));
		
		assertThrows(UnsupportedOperationException.class,()->p.add(new JLabel(""),new RCPosition(1,1)));
		
		p.add(new JLabel("g"), new RCPosition(3,2));
		
		assertThrows(UnsupportedOperationException.class,()->p.add(new JLabel(""), new RCPosition(2,3)));
		
	}
	
	@Test
	public void prefferedSize1() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
		p.add(l1, new RCPosition(2,2));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		
		assertEquals(152,dim.width);
		assertEquals(158,dim.height);
	}
	
	@Test
	public void prefferedSize2() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
		p.add(l1, new RCPosition(1,1));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		
		assertEquals(152,dim.width);
		assertEquals(158,dim.height);
	}
}
