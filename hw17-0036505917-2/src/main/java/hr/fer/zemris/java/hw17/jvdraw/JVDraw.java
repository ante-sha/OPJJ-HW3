package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.function.Supplier;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw17.jvdraw.color.ColorInfoLabel;
import hr.fer.zemris.java.hw17.jvdraw.color.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingModelImpl;
import hr.fer.zemris.java.hw17.jvdraw.drawing.DrawingObjectListModel;
import hr.fer.zemris.java.hw17.jvdraw.drawing.JDrawingCanvas;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.GeometricalObjectSaver;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.Line;
import hr.fer.zemris.java.hw17.jvdraw.toggle.CircleToolToggleButton;
import hr.fer.zemris.java.hw17.jvdraw.toggle.FilledCircleToggleButton;
import hr.fer.zemris.java.hw17.jvdraw.toggle.LineToolToggleButton;
import hr.fer.zemris.java.hw17.jvdraw.toggle.Tool;

/**
 * Aplikacija koja omogućava vektorsko crtanje geometrijskih objekata uz osnovne
 * alate i mogućnosti spremanja, učitavanja i exportanja projekta.
 * 
 * @author Ante Miličević
 *
 */
public class JVDraw extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Model koji sadrži sve geometrijske objekte
	 */
	private DrawingModel dModel;
	/**
	 * Grupa alata
	 */
	private ButtonGroup toolGroup;
	/**
	 * Lista geometrijskih objekata
	 */
	private JList<String> list;
	/**
	 * Objekt za dohvaćanje alata
	 */
	private Supplier<Tool> toolSup = () -> getSelectedTool();
	/**
	 * Staza do otvorenog projekta
	 */
	private Path openedFile;

	/**
	 * Konstruktor
	 */
	public JVDraw() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("JVDraw");
		initGUI();
		pack();
		setSize(new Dimension(800, 500));
		setVisible(true);
	}

	/**
	 * Metoda za inicijalizaciju GUI-ja
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout(5, 5));

		JToolBar toolBar = new JToolBar();
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
		toolBar.setFloatable(false);
		cp.add(toolBar, BorderLayout.NORTH);

		JColorArea fgColor = new JColorArea(Color.red);
		toolBar.add(fgColor);
		JColorArea bgColor = new JColorArea(Color.blue);
		toolBar.add(bgColor);

		dModel = new DrawingModelImpl();
		JDrawingCanvas canvas = new JDrawingCanvas(toolSup, dModel);
		cp.add(canvas, BorderLayout.CENTER);

		initToggles(toolBar, dModel, canvas, fgColor, bgColor);

		initObjectList(cp);

		setInfoLabel(cp, fgColor, bgColor);

		initMenus(cp);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit.actionPerformed(null);
			}
		});
	}

	/**
	 * Metoda za incijalizaciju liste objekata
	 * 
	 * @param cp content pane
	 */
	private void initObjectList(Container cp) {
		ListModel<String> model = new DrawingObjectListModel(dModel);
		list = new JList<String>(model);
		list.setFixedCellWidth(300);

		JPanel objectList = new JPanel(new BorderLayout());
		objectList.add(list, BorderLayout.CENTER);
		JLabel title = new JLabel("List of objects");
		title.setBackground(Color.BLACK);
		title.setForeground(Color.WHITE);
		title.setOpaque(true);
		title.setPreferredSize(new Dimension(300, getPreferredSize().height));
		objectList.add(title, BorderLayout.NORTH);
		cp.add(objectList, BorderLayout.EAST);

		list.addMouseListener(createEditorListener());
		list.addKeyListener(createOrderListener());
	}

	/**
	 * Metoda koja stvara promatrača za manipulaciju nad geometrijskim objektima.
	 * <ul>
	 * Akcije
	 * <li>'+' pomak elementa za jedno mjesto gore</li>
	 * <li>'-' pomak elementa za jedno mjesto dole</li>
	 * <li>'DEL' brisanje elementa iz liste</li>
	 * </ul>
	 * 
	 * @return promatrač na tipkovnicu
	 */
	private KeyListener createOrderListener() {
		return new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				int index = list.getSelectedIndex();
				if (index < 0) {
					return;
				}
				if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_ADD) {
					if (index == 0) {
						return;
					}
					dModel.changeOrder(dModel.getObject(index), -1);
					list.setSelectedIndex(index - 1);
				} else if (e.getKeyCode() == KeyEvent.VK_MINUS || e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
					if (index == dModel.getSize() - 1) {
						return;
					}
					dModel.changeOrder(dModel.getObject(index), 1);
					list.setSelectedIndex(index + 1);
				} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					dModel.remove(dModel.getObject(index));
				}
			}

		};
	}

	/**
	 * Metoda koja stvara promatrača za pozivanje uređivača geometrijskog objekta
	 * koji reagira na dvoklik miša.
	 * 
	 * @return promatrač na miš
	 */
	private MouseListener createEditorListener() {
		return new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() < 2) {
					return;
				}
				int index = list.getSelectedIndex();
				if (index < 0) {
					return;
				}
				GeometricalObjectEditor editor = dModel.getObject(index).createGeometricalObjectEditor();

				if (JOptionPane.showConfirmDialog(JVDraw.this, editor, "Edit object", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.DEFAULT_OPTION) == JOptionPane.OK_OPTION) {
					try {
						editor.checkEditing();
						editor.acceptEditing();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(JVDraw.this, "Error", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		};
	}

	/**
	 * Metoda za incijalizaciju toggle gumbova
	 * 
	 * @param toolBar tool bar
	 * @param dModel  model koji sadrži sve geometrijske objekte
	 * @param canvas  komponenta u kojoj se crtaju svi geometrijski objekti
	 * @param fgColor objekt koji sadrži prednju boju
	 * @param bgColor objekt koji sadrži stražnju boju
	 */
	private void initToggles(JToolBar toolBar, DrawingModel dModel, JDrawingCanvas canvas, JColorArea fgColor,
			JColorArea bgColor) {
		LineToolToggleButton line = new LineToolToggleButton(dModel, fgColor, canvas);
		toolBar.add(line);

		CircleToolToggleButton circle = new CircleToolToggleButton(dModel, fgColor, canvas);
		toolBar.add(circle);

		FilledCircleToggleButton filled = new FilledCircleToggleButton(dModel, fgColor, bgColor, canvas);
		toolBar.add(filled);

		toolGroup = new ButtonGroup();
		toolGroup.add(line);
		toolGroup.add(circle);
		toolGroup.add(filled);

		line.setSelected(true);
	}

	/**
	 * Metoda za postavljanje labele za prikaz informacija o trenutno odabranim
	 * bojama.
	 * 
	 * @param cp      content pane
	 * @param fgColor objekt koji sadrži prednju boju
	 * @param bgColor objekt koji sadrži stražnju boju
	 */
	private void setInfoLabel(Container cp, JColorArea fgColor, JColorArea bgColor) {
		JPanel pan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		ColorInfoLabel infoLabel = new ColorInfoLabel(fgColor, bgColor);

		pan.add(infoLabel);
		cp.add(pan, BorderLayout.SOUTH);
	}

	/**
	 * Metoda za dohvat aktivnog alata
	 * 
	 * @return alat ili null ako ni jedan nije odabran
	 */
	private Tool getSelectedTool() {
		Enumeration<AbstractButton> buttons = toolGroup.getElements();
		while (buttons.hasMoreElements()) {
			AbstractButton button = buttons.nextElement();
			if (button.isSelected()) {
				return (Tool) button;
			}
		}
		return null;
	}

	/**
	 * Metoda za incijalizaciju menu bara
	 * 
	 * @param cp content pane
	 */
	private void initMenus(Container cp) {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu file = new JMenu("File");
		menuBar.add(file);

		JMenuItem open = new JMenuItem(fileOpen);
		file.add(open);
		JMenuItem saveFileAs = new JMenuItem(saveAs);
		file.add(saveFileAs);
		JMenuItem saveFile = new JMenuItem(save);
		file.add(saveFile);

		JMenu export = new JMenu("Export");
		menuBar.add(export);

		JMenuItem jpg = new JMenuItem(jpgExport);
		export.add(jpg);
		JMenuItem gif = new JMenuItem(gifExport);
		export.add(gif);
		JMenuItem png = new JMenuItem(pngExport);
		export.add(png);

		JButton exitBtn = new JButton(exit);
		menuBar.add(exitBtn);
	}

	/**
	 * Akcija otvaranja dokumenta
	 */
	private Action fileOpen = new AbstractAction("Open") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (dModel.isModified()) {
				if (JOptionPane.showConfirmDialog(JVDraw.this, "File not saved do you want to continue?", "Warning",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
					return;
				}
			}
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Choose file to open");
			if (chooser.showOpenDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			Path path = chooser.getSelectedFile().toPath();
			if (!path.getFileName().toString().endsWith(".jvd")) {
				JOptionPane.showMessageDialog(JVDraw.this, "Wrong file format", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				DrawingModel newModel = readFile(path);
				dModel.clear();
				for (int i = 0, n = newModel.getSize(); i < n; i++) {
					dModel.add(newModel.getObject(i));
				}
				dModel.clearModifiedFlag();
				openedFile = path;
				setNewTitle();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(JVDraw.this, "File corrupted", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	};

	/**
	 * Akcija spremanja dokumenta s novom stazom
	 */
	private Action saveAs = new AbstractAction("Save as...") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Choose file location");
			chooser.setFileFilter(new FileNameExtensionFilter("*.jvd", "jvd"));
			chooser.setSelectedFile(new File("unnamed.jvd"));
			if (chooser.showOpenDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			Path path = chooser.getSelectedFile().toPath();
			if (!path.getFileName().toString().endsWith(".jvd")) {
				JOptionPane.showMessageDialog(JVDraw.this, "Wrong extension", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
				if (JOptionPane.showConfirmDialog(JVDraw.this, "Overwrite file?", "Warning", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
					return;
				}
			}

			try {
				saveFileTo(path);
				openedFile = path;
				setNewTitle();
				dModel.clearModifiedFlag();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(JVDraw.this, "Save failed", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	/**
	 * Akcija za spremanje dokumenta
	 */
	private Action save = new AbstractAction("Save") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (openedFile == null) {
				saveAs.actionPerformed(e);
				return;
			}
			try {
				saveFileTo(openedFile);
				dModel.clearModifiedFlag();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(JVDraw.this, "Save failed", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	/**
	 * Metoda za spremanje dokumenta na lokaciju path
	 * 
	 * @param path staza
	 * @throws IOException ako pisanje u datoteku ne uspije
	 */
	private void saveFileTo(Path path) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			GeometricalObjectSaver saver = new GeometricalObjectSaver(writer);
			for (int i = 0, n = dModel.getSize(); i < n; i++) {
				dModel.getObject(i).accept(saver);
			}
		}
	}

	/**
	 * Metoda za čitanje i formatiranje podataka s lokacije path
	 * 
	 * @param path staza do datoteke
	 * @return model koji sadrži sve geometrijske objekte spremljene u datoteci
	 * @throws IOException ako čitanje ne uspije
	 */
	private DrawingModel readFile(Path path) throws IOException {
		BufferedReader reader = Files.newBufferedReader(path);
		DrawingModel result = new DrawingModelImpl();
		while (true) {
			String string = reader.readLine();
			if (string == null) {
				break;
			}
			String[] data = string.split(" ");
			GeometricalObject object = null;
			if (data[0].equals("LINE")) {
				object = parseLine(data);
			} else if (data[0].equals("CIRCLE")) {
				object = parseCircle(data);
			} else if (data[0].equals("FCIRCLE")) {
				object = parseFCircle(data);
			} else {
				throw new RuntimeException("Unexpected name of object " + data[0]);
			}
			result.add(object);
		}
		return result;
	}

	/**
	 * Metoda za parsiranje podataka iz datoteke o krugu
	 * 
	 * @param data podaci odvojeni razmakom
	 * @return krug
	 */
	private GeometricalObject parseFCircle(String[] data) {
		return new FilledCircle(new Point(Integer.parseInt(data[1]), Integer.parseInt(data[2])),
				Integer.parseInt(data[3]),
				new Color(Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6])),
				new Color(Integer.parseInt(data[7]), Integer.parseInt(data[8]), Integer.parseInt(data[9])));
	}

	/**
	 * Metoda za parsiranje podataka iz datoteke o kružnici
	 * 
	 * @param data podaci odvojeni razmakom
	 * @return kružnica
	 */
	private GeometricalObject parseCircle(String[] data) {
		return new Circle(new Point(Integer.parseInt(data[1]), Integer.parseInt(data[2])), Integer.parseInt(data[3]),
				new Color(Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6])));
	}

	/**
	 * Metoda za parsiranje podataka iz datoteke o dužini
	 * 
	 * @param data podaci odvojeni razmakom
	 * @return dužina
	 */
	private Line parseLine(String[] data) {
		return new Line(new Point(Integer.parseInt(data[1]), Integer.parseInt(data[2])),
				new Point(Integer.parseInt(data[3]), Integer.parseInt(data[4])),
				new Color(Integer.parseInt(data[5]), Integer.parseInt(data[6]), Integer.parseInt(data[7])));
	}

	/**
	 * Akcija za izvoz projekta kao png slika
	 */
	private Action pngExport = new AbstractAction("Png") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				exportAs("png");
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(JVDraw.this, "Error occurred while exporting file", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	/**
	 * Akcija za izvoz projekta kao gif slika
	 */
	private Action gifExport = new AbstractAction("Gif") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				exportAs("gif");
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(JVDraw.this, "Error occurred while exporting file", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	/**
	 * Akcija za izvoz projekta kao jpg slika
	 */
	private Action jpgExport = new AbstractAction("Jpg") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				exportAs("jpg");
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(JVDraw.this, "Error occurred while exporting file", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	/**
	 * Metoda za izvoz dokumenta kao slike s ekstenzijom ext
	 * 
	 * @param ext ekstenzija
	 * @throws IOException ako pisanje ne uspije
	 */
	private void exportAs(String ext) throws IOException {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choose file location");
		chooser.setFileFilter(new FileNameExtensionFilter("*." + ext, ext));
		chooser.setSelectedFile(new File("unnamed." + ext));

		if (chooser.showOpenDialog(JVDraw.this) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		Path path = chooser.getSelectedFile().toPath();
		if (!path.getFileName().toString().endsWith("." + ext)) {
			JOptionPane.showMessageDialog(JVDraw.this, "Wrong extension", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
			if (JOptionPane.showConfirmDialog(JVDraw.this, "Overwrite file?", "Warning", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
				return;
			}
		}

		Rectangle box = calculateBoundingBox();
		BufferedImage image = new BufferedImage(box.width, box.height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();
		AffineTransform t = AffineTransform.getTranslateInstance(-box.x, -box.y);
		g.transform(t);
		// set white background
		g.setColor(Color.WHITE);
		g.fillRect(box.x, box.y, box.width, box.height);
		paintObjects(g);
		g.dispose();

		ImageIO.write(image, ext, path.toFile());
		JOptionPane.showMessageDialog(JVDraw.this, "Image successfully exported", "Info",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Metoda za crtanje objekata 
	 * 
	 * @param g objekt za crtanje
	 */
	private void paintObjects(Graphics2D g) {
		GeometricalObjectPainter painter = new GeometricalObjectPainter(g);
		for (int i = 0, n = dModel.getSize(); i < n; i++) {
			dModel.getObject(i).accept(painter);
		}
	}

	/**
	 * Metoda za izračun bounding boxa
	 * 
	 * @return bounding box
	 */
	private Rectangle calculateBoundingBox() {
		GeometricalObjectBBCalculator bbcalc = new GeometricalObjectBBCalculator();
		for (int i = 0, n = dModel.getSize(); i < n; i++) {
			dModel.getObject(i).accept(bbcalc);
		}
		return bbcalc.getBoundingBox();
	}

	/**
	 * Metoda za postavljanje naslova aplikacije
	 */
	private void setNewTitle() {
		setTitle("JVDraw - " + openedFile.normalize().toAbsolutePath());
	}

	/**
	 * Akcija koja se poziva prilikom pokužaja izlaska iz aplikacije
	 */
	private Action exit = new AbstractAction("Exit") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			while (dModel.isModified()) {
				int answer = JOptionPane.showConfirmDialog(JVDraw.this, "Do you want to save file?", "Warning",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (answer == JOptionPane.NO_OPTION) {
					break;
				} else if (answer == JOptionPane.CLOSED_OPTION) {
					return;
				}
				save.actionPerformed(e);
			}
			JVDraw.this.dispose();
		}
	};

	/**
	 * Ulazna točka programa
	 * 
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JVDraw();
		});
	}
}
