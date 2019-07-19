package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program koji prima jedan argument kroz naredbeni redak u formatu:<br>
 * [1] Opis x osi<br>
 * [2] Opis y osi<br>
 * [3] x1,y1 x2,y2 x3,y3 ... <br>
 * [4] yMin<br>
 * [5] yMax<br>
 * [6] delta<br>
 * ...<br>
 * 
 * @author Ante Miličević
 *
 */
public class BarChartDemo extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor
	 * 
	 * @param model
	 * @param path
	 */
	public BarChartDemo(BarChart model, String path) {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("BarChart v1.0");

		Container cp = getContentPane();

		cp.setLayout(new BorderLayout());
		cp.add(new BarChartComponent(model), BorderLayout.CENTER);

		JLabel label = new JLabel(Paths.get(path).toAbsolutePath().normalize().toString());
		label.setHorizontalAlignment(SwingConstants.CENTER);
		cp.add(label, BorderLayout.NORTH);

		pack();
		setMinimumSize(getPreferredSize());

		//centriranje prozora
		Dimension window = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int) (window.width - getWidth()) / 2, (int) (window.getHeight() - getHeight()) / 2);

	}

	/**
	 * Ulazna točka programa
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("You must enter only one argument!");
		}

		try (BufferedReader reader = Files.newBufferedReader(Paths.get(args[0]))) {
			String xDesc = reader.readLine();
			String yDesc = reader.readLine();

			String chartData = reader.readLine().trim();

			List<XYValue> list = new ArrayList<>();
			for (String pair : chartData.split(" ")) {
				String[] number = pair.split(",");

				list.add(new XYValue(Integer.parseInt(number[0]), Integer.parseInt(number[1])));
			}

			if (list.isEmpty()) {
				throw new IllegalArgumentException("Data set is empty");
			}

			int yMin = Integer.parseInt(reader.readLine());
			int yMax = Integer.parseInt(reader.readLine());

			int delta = Integer.parseInt(reader.readLine());

			BarChart model = new BarChart(list, xDesc, yDesc, yMin, yMax, delta);

			SwingUtilities.invokeLater(() -> {
				new BarChartDemo(model, args[0]).setVisible(true);
				;
			});
		} catch (Exception e) {
			System.out.format("Something went wrong%nError message: %s", e.getMessage());
		}

	}
}
