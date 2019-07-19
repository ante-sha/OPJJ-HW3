package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program za demonstraciju rada {@link PrimListModel} pomoću dvije
 * {@link JList} komponente
 * @author Ante Miličević
 *
 */
public class PrimDemo extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor
	 */
	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("PrimDemo");
		initGUI();
		pack();
		
		Dimension window = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int) (window.width - getWidth()) / 2, (int) (window.getHeight() - getHeight()) / 2);
	}
	
	/**
	 * Inicijalizacija sučelja
	 */
	private void initGUI() {
		Container cp = getContentPane();
		
		cp.setLayout(new BorderLayout());
		
		PrimListModel model = new PrimListModel();
		
		JList<Integer> list1 = new JList<>(model);
		JList<Integer> list2 = new JList<>(model);
		
		cp.add(new JScrollPane(list1), BorderLayout.WEST);
		cp.add(new JScrollPane(list2), BorderLayout.EAST);
		
		JButton next = new JButton("Sljedeći");
		next.addActionListener((e)->{
			model.next();
		});
		
		cp.add(next, BorderLayout.SOUTH);
	}

	/**
	 * Ulazna točka programa
	 * 
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new PrimDemo().setVisible(true);
		});
	}
}
