package hr.fer.zemris.java.gui.layouts.demo;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

public class Demo {
	public static void main(String[] args) {
	
		class MojProzor extends JFrame{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public MojProzor() {
				Container p = getContentPane();
				p.setLayout(new CalcLayout(2));
				JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
				JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
				p.add(l1, new RCPosition(1,1));
				p.add(l2, new RCPosition(3,3));
				Dimension dim = p.getPreferredSize();
				System.out.println(dim.width);
				System.out.println(dim.height);
				
				pack();
				setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			}
		}
		
		SwingUtilities.invokeLater(()->{
			new MojProzor().setVisible(true);;
			
		});
	}
}
