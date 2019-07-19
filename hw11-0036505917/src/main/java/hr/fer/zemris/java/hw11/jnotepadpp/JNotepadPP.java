package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.Collator;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;

import hr.fer.zemris.java.hw11.jnotepadpp.documentModels.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.documentModels.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.documentModels.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.documentModels.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.localization.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.localization.LJLabel;
import hr.fer.zemris.java.hw11.jnotepadpp.localization.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.localization.LocalizationProvider;

/**
 * Program za editiranje tekstnih dokumenata.
 * 
 * @author Ante Miličević
 *
 */
public class JNotepadPP extends JFrame {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Lokalizator za ovaj prozor
	 */
	private FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
	/**
	 * Prozor za odabir staza
	 */
	private JFileChooser jfc;
	/**
	 * Otvoreni dokumenti
	 */
	private MultipleDocumentModel documents;
	/**
	 * Lista akcija koje se onemogućavaju kada nema dokumenata
	 */
	private List<Action> disabledWhenNoDocuments = new ArrayList<>();
	/**
	 * Lista akcija koje manipuliraju označenim znakovima
	 */
	private List<Action> caseManipulators = new ArrayList<>();

	/**
	 * Labela koja prikazuje broj znakova u dokumentu
	 */
	private JLabel length;
	/**
	 * Labela koja prikazuje broj trenutnog retka u kojem se nalazi znak za umetanje
	 */
	private JLabel ln;
	/**
	 * Labela koja prikazuje broj trenutnog stupca u kojem se nalazi znak za
	 * umetanje
	 */
	private JLabel col;
	/**
	 * Labela koja prikazuje broj označenih znakova
	 */
	private JLabel sel;

	/**
	 * Zastavica kojom se regulira zaustavljanje sata
	 */
	private volatile boolean stopWatch;
	/**
	 * Naziv programa
	 */
	private static final String JNOTEPADPP = "JNotepad++";

	/**
	 * Konstruktor
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle(JNOTEPADPP);
		initGUI();
		// set size to full screen
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
	}

	/**
	 * Metoda za inicijalizaciju sučelja
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		JPanel center = new JPanel();
		cp.add(center, BorderLayout.CENTER);

		center.setLayout(new BorderLayout());
		documents = new DefaultMultipleDocumentModel();

		center.add((JTabbedPane) documents, BorderLayout.CENTER);
		center.add(createStatusBar(), BorderLayout.PAGE_END);

		createMenus();

		configureActions();

		// there are no documents at the beggining of the program
		disabledWhenNoDocuments.forEach((act) -> act.setEnabled(false));
		caseManipulators.forEach((act) -> act.setEnabled(false));

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit.actionPerformed(null);
			}
		});

		cp.add(createToolBar(), BorderLayout.LINE_END);
	}

	/**
	 * Metoda za stvaranje i inicijalizacija izbornika
	 */
	private void createMenus() {
		JMenuBar mb = new JMenuBar();

		// file-----------------------------------------
		JMenu file = new JMenu(new LocalizableAction("file", null, flp) {
			private static final long serialVersionUID = 1L;
		});
		mb.add(file);
		file.setMnemonic(KeyEvent.VK_F);

		file.add(new JMenuItem(createDocument));
		file.add(new JMenuItem(openDocument));
		file.addSeparator();
		file.add(new JMenuItem(statistics));
		file.addSeparator();
		file.add(new JMenuItem(saveDocument));
		file.add(new JMenuItem(saveDocumentAs));
		file.add(new JMenuItem(closeDocument));
		file.addSeparator();
		file.add(new JMenuItem(exit));

		// clip---------------------------------------------
		JMenu clip = new JMenu(new LocalizableAction("clip", "tt_clip", flp) {
			private static final long serialVersionUID = 1L;
		});
		clip.setMnemonic(KeyEvent.VK_I);
		mb.add(clip);

		clip.add(new JMenuItem(copy));
		clip.add(new JMenuItem(paste));
		clip.add(new JMenuItem(cut));

		// lang----------------------------------------------
		JMenu lang = new JMenu(new LocalizableAction("languages", "tt_lang", flp) {
			private static final long serialVersionUID = 1L;
		});
		lang.setMnemonic(KeyEvent.VK_E);
		mb.add(lang);

		lang.add(new JMenuItem(langEn));
		lang.add(new JMenuItem(langHr));
		lang.add(new JMenuItem(langDe));

		// caseChange-------------------------------------------
		JMenu caseChange = new JMenu(new LocalizableAction("caseChange", null, flp) {
			private static final long serialVersionUID = 1L;
		});
		caseChange.setMnemonic(KeyEvent.VK_C);
		mb.add(caseChange);

		caseChange.add(new JMenuItem(toUpper));
		caseChange.add(new JMenuItem(toLower));
		caseChange.add(new JMenuItem(invert));

		// sort-------------------------------------------------
		JMenu sort = new JMenu(new LocalizableAction("sort", "tt_sort", flp) {
			private static final long serialVersionUID = 1L;
		});
		sort.setMnemonic(KeyEvent.VK_S);
		mb.add(sort);

		sort.add(new JMenuItem(ascending));
		sort.add(new JMenuItem(descending));

		// line--------------------------------------------------
		JMenu line = new JMenu(new LocalizableAction("line", "tt_line", flp) {
			private static final long serialVersionUID = 1L;
		});
		line.setMnemonic(KeyEvent.VK_N);
		mb.add(line);

		line.add(new JMenuItem(unique));

		setJMenuBar(mb);
	}

	private void configureActions() {
		// document manipulation actions-----------------------------
		configureAction(createDocument, KeyStroke.getKeyStroke("control N"), KeyEvent.VK_C, readIcon("create"));

		configureAction(openDocument, KeyStroke.getKeyStroke("control O"), KeyEvent.VK_O, readIcon("open"));

		configureAction(statistics, KeyStroke.getKeyStroke("control T"), KeyEvent.VK_T, null);
		disabledWhenNoDocuments.add(statistics);

		configureAction(saveDocument, KeyStroke.getKeyStroke("control S"), KeyEvent.VK_S, readIcon("save"));
		disabledWhenNoDocuments.add(saveDocument);

		configureAction(saveDocumentAs, KeyStroke.getKeyStroke("control shift S"), KeyEvent.VK_A, readIcon("saveAs"));
		disabledWhenNoDocuments.add(saveDocumentAs);

		configureAction(closeDocument, KeyStroke.getKeyStroke("control W"), KeyEvent.VK_C, readIcon("close"));
		disabledWhenNoDocuments.add(closeDocument);

		configureAction(exit, KeyStroke.getKeyStroke("control Q"), KeyEvent.VK_E, readIcon("exit"));

		// clipboard actions--------------------------------------------
		configureAction(copy, KeyStroke.getKeyStroke("control C"), KeyEvent.VK_C, null);
		disabledWhenNoDocuments.add(copy);

		configureAction(paste, KeyStroke.getKeyStroke("control V"), KeyEvent.VK_P, null);
		disabledWhenNoDocuments.add(paste);

		configureAction(cut, KeyStroke.getKeyStroke("control X"), KeyEvent.VK_U, null);
		disabledWhenNoDocuments.add(cut);

		// language actions-----------------------------------------------
		configureAction(langEn, KeyStroke.getKeyStroke("F1"), KeyEvent.VK_E, readIcon("enFlag"));

		configureAction(langHr, KeyStroke.getKeyStroke("F2"), KeyEvent.VK_H, readIcon("hrFlag"));

		configureAction(langDe, KeyStroke.getKeyStroke("F3"), KeyEvent.VK_D, readIcon("deFlag"));

		// case change actions----------------------------------------------
		configureAction(toUpper, KeyStroke.getKeyStroke("control U"), KeyEvent.VK_U, readIcon("up"));
		caseManipulators.add(toUpper);

		configureAction(toLower, KeyStroke.getKeyStroke("control L"), KeyEvent.VK_L, readIcon("down"));
		caseManipulators.add(toLower);

		configureAction(invert, KeyStroke.getKeyStroke("control I"), KeyEvent.VK_I, readIcon("invert"));
		caseManipulators.add(invert);

		// line manipulation actions-------------------------------------------
		configureAction(unique, KeyStroke.getKeyStroke("control alt U"), KeyEvent.VK_U, readIcon("unique"));
		disabledWhenNoDocuments.add(unique);

		configureAction(ascending, KeyStroke.getKeyStroke("control shift A"), KeyEvent.VK_A, readIcon("ascending"));
		disabledWhenNoDocuments.add(ascending);

		configureAction(descending, KeyStroke.getKeyStroke("control shift D"), KeyEvent.VK_D, readIcon("descending"));
		disabledWhenNoDocuments.add(descending);

		// add listener on documents for activation of actions in
		// disabledWhenNoDocuments
		documents.addMultipleDocumentListener(new MultipleDocumentListener() {
			@Override
			public void documentRemoved(SingleDocumentModel model) {
				if (documents.getNumberOfDocuments() == 0) {
					disabledWhenNoDocuments.forEach((action) -> action.setEnabled(false));
				}
			}

			@Override
			public void documentAdded(SingleDocumentModel model) {
				if (documents.getNumberOfDocuments() == 1) {
					disabledWhenNoDocuments.forEach((action) -> action.setEnabled(true));
				}
			}

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
			}
		});
	}

	/**
	 * Metoda za konfiguraciju akcije, ako argument jednak null zanemaruje se
	 * 
	 * @param action      akcija koja je konfigurira
	 * @param accelerator globalni prečac
	 * @param mnemonic    lokalni prečac
	 * @param icon        ikona
	 * @param desc        opis
	 */
	private void configureAction(Action action, KeyStroke accelerator, Integer mnemonic, ImageIcon icon) {
		if (accelerator != null) {
			action.putValue(Action.ACCELERATOR_KEY, accelerator);
		}
		if (mnemonic != null) {
			action.putValue(Action.MNEMONIC_KEY, mnemonic);
		}
		if (icon != null) {
			action.putValue(Action.SMALL_ICON, icon);
		}
	}

	/**
	 * Metoda za čitanje ikona iz "hr.fer.zemris.java.hw11.jnotepadpp.actionsIcons
	 * koje su u png formatu, ako čitanje ne uspije metoda vraća null
	 * 
	 * @param name ime datoteke
	 * @return pročitana ikona
	 */
	private ImageIcon readIcon(String name) {
		try (InputStream is = getClass().getResourceAsStream("actionIcons/" + name + ".png")) {

			byte[] buff = is.readAllBytes();

			return new ImageIcon(buff);

		} catch (IOException ignored) {
		}
		return null;
	}

	/**
	 * Metoda za stvaranje alatne trake
	 * 
	 * @return inicijalizirana alatna traka
	 */
	private JToolBar createToolBar() {
		JToolBar tb = new JToolBar();
		tb.setFloatable(true);

		// file-------------------------------
		tb.add(createButton(createDocument));
		tb.add(createButton(openDocument));
		tb.add(createButton(saveDocument));
		tb.add(createButton(saveDocumentAs));
		tb.add(createButton(closeDocument));
		tb.addSeparator();

		// case change------------------------
		tb.add(createButton(toUpper));
		tb.add(createButton(toLower));
		tb.add(createButton(invert));
		tb.addSeparator();

		// line manipulation------------------
		tb.add(createButton(ascending));
		tb.add(createButton(descending));
		tb.add(createButton(unique));
		tb.addSeparator();

		// exit-------------------------------
		tb.add(createButton(exit));

		tb.setOrientation(SwingUtilities.VERTICAL);

		return tb;
	}

	/**
	 * Metoda za stvaranje gumba bez teksta
	 * 
	 * @param action akcija gumba
	 * 
	 * @return gumb
	 */
	private JButton createButton(Action action) {
		JButton button = new JButton(action);
		button.setHideActionText(true);
		return button;
	}

	/**
	 * Metoda za stvaranje statusne trake
	 * 
	 * @return inicijalizirana statusna traka
	 */
	private Component createStatusBar() {
		JPanel leftPanel = createLeftStatusBar();
		JPanel rightPanel = createRightPanel();

		JPanel statusBar = new JPanel(new GridLayout(1, 0, 2, 0));
		statusBar.setBackground(Color.GRAY);
		statusBar.add(leftPanel);
		statusBar.add(rightPanel);
		statusBar.add(createWatch());

		documents.addMultipleDocumentListener(new MultipleDocumentListener() {

			@Override
			public void documentRemoved(SingleDocumentModel model) {
				model.getTextComponent().removeCaretListener(caretListener);
			}

			@Override
			public void documentAdded(SingleDocumentModel model) {
				model.getTextComponent().addCaretListener(caretListener);
			}

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				setLeftStatusBar(currentModel == null ? null : currentModel.getTextComponent());
				setRightStatusBar(currentModel == null ? null : currentModel.getTextComponent());

				if(previousModel != null) {
					previousModel.getTextComponent().removeCaretListener(caretListener);
				}
				
				if (currentModel != null) {
					String name = currentModel.getFilePath() == null ? "unnamed"
							: currentModel.getFilePath().toAbsolutePath().normalize().toString();
					setTitle(JNOTEPADPP + " - " + name);
					
					currentModel.getTextComponent().addCaretListener(caretListener);
				} else {
					setTitle(JNOTEPADPP);
				}
			}

		});
		return statusBar;
	}

	/**
	 * Metoda za stvaranje lijevog dijela statusne trake
	 * 
	 * @return inicijalizirani lijevi dio statusne trake
	 */
	private JPanel createLeftStatusBar() {
		JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));

		left.add(new LJLabel("length", flp));
		JLabel delim = new JLabel(":");
		left.add(delim);
		length = new JLabel("");
		left.add(length);

		return left;
	}

	/**
	 * Metoda za stvaranje desnog dijela statusne trake
	 * 
	 * @return inicijalizirani desni dio statusne trake
	 */
	private JPanel createRightPanel() {
		JPanel right = new JPanel(new FlowLayout(FlowLayout.LEFT));

		right.add(new LJLabel("ln", flp));
		JLabel delim = new JLabel(":");
		right.add(delim);
		ln = new JLabel();
		right.add(ln);

		right.add(new LJLabel("col", flp));
		delim = new JLabel(":");
		right.add(delim);
		col = new JLabel();
		right.add(col);

		right.add(new LJLabel("sel", flp));
		delim = new JLabel(":");
		right.add(delim);
		sel = new JLabel();
		right.add(sel);

		return right;
	}

	/**
	 * Stvaranje i pokretanje sata
	 * 
	 * @return kontejner koji sadrži sat koji se povremeno ažurira na trenutno
	 *         vrijeme
	 */
	private JPanel createWatch() {
		JLabel watch = new JLabel();
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(watch);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

		Thread clock = new Thread(() -> {
			int sleep = 500;
			while (!stopWatch) {
				String time = formatter.format(ZonedDateTime.now());
				SwingUtilities.invokeLater(() -> watch.setText(time));

				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
				}
			}
		});
		clock.setDaemon(true);
		clock.start();

		return panel;
	}

	/**
	 * Metoda koja postavlja desni dio statusne trake
	 * 
	 * @param currentArea dokument u fokusu
	 */
	private void setRightStatusBar(JTextArea currentArea) {
		// if there is no document
		if (currentArea == null) {
			ln.setText("");
			col.setText("");
			sel.setText("");
			return;
		}

		Caret caret = currentArea.getCaret();
		int position = caret.getDot();

		Element element = currentArea.getDocument().getDefaultRootElement();

		int line = element.getElementIndex(position) + 1;

		ln.setText(Integer.toString(element.getElementIndex(position) + 1));
		col.setText(Integer.toString(position - element.getElement(line - 1).getStartOffset() + 1));
		sel.setText(Integer.toString(getLength(caret)));
	}

	/**
	 * Metoda za postavljanje lijevog dijela statusne trake
	 * 
	 * @param currentArea dokument u fokusu
	 */
	private void setLeftStatusBar(JTextArea currentArea) {
		// if there is no document
		if (currentArea == null) {
			length.setText("");
		} else {
			length.setText(Integer.toString(currentArea.getDocument().getLength()));
		}
	}

	/**
	 * Akcija uzlaznog sortiranja
	 */
	private Action ascending = new LocalizableAction("sort_ascending", null, flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			sortWordsInLines(true);
		}
	};

	/**
	 * Akcija silaznog sortiranja
	 */
	private Action descending = new LocalizableAction("sort_descending", null, flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			sortWordsInLines(false);
		}
	};

	/**
	 * Metoda koja sortira označene retke dokumenta
	 * 
	 * @param ascending true ako uzlazno, false ako je sortiranje silazno
	 */
	private void sortWordsInLines(boolean ascending) {
		Locale locale = new Locale(flp.getLanguage());
		Collator collator = Collator.getInstance(locale);

		Comparator<? super String> comp = ascending ? collator : collator.reversed();

		SingleDocumentModel doc = documents.getCurrentDocument();
		Document document = doc.getTextComponent().getDocument();

		Caret caret = doc.getTextComponent().getCaret();

		Element element = document.getDefaultRootElement();

		int startLine = element.getElementIndex(Math.min(caret.getDot(), caret.getMark()));
		int endLine = element.getElementIndex(Math.max(caret.getDot(), caret.getMark()));

		List<String> lines = getLines(startLine, endLine, element, document);

		lines.sort(comp);

		insertLines(lines, element.getElement(startLine), element.getElement(endLine), document);

	}

	/**
	 * Metoda koja čita retke dokumenta od retka start do retka end (uključivo).
	 * 
	 * @param start    indeks početnog retka
	 * @param end      indeks zadnjeg retka
	 * @param root     vršni element dokumenta
	 * @param document dokument
	 * 
	 * @return pročitane linije
	 */
	private List<String> getLines(int start, int end, Element root, Document document) {
		return IntStream.rangeClosed(start, end).mapToObj((i) -> root.getElement(i)).map((elem) -> {
			try {
				int length = elem.getEndOffset() >= document.getLength() + 1
						? elem.getEndOffset() - 1 - elem.getStartOffset()
						: elem.getEndOffset() - elem.getStartOffset();
				return document.getText(elem.getStartOffset(), length).stripTrailing();
			} catch (BadLocationException ignore) {
			}
			return null;
		}).collect(Collectors.toList());
	}

	/**
	 * Metoda za zamjenu trenutnih redaka s novim.
	 * 
	 * @param lines     novi retci
	 * @param startElem element starog početnog retka
	 * @param endElem   element starog završnog retka
	 * @param doc       dokument
	 */
	private void insertLines(List<String> lines, Element startElem, Element endElem, Document doc) {
		try {
			int start = startElem.getStartOffset();
			int length = endElem.getEndOffset() - 1 - startElem.getStartOffset();

			doc.remove(start, length);

			int position = start;
			for (String line : lines) {
				line = lines.indexOf(line) != lines.size() - 1 ? line + '\n' : line;

				doc.insertString(position, line, null);

				position += line.length();
			}
		} catch (BadLocationException e) {
		}

	}

	/**
	 * Akcija uklananja nejedinstvenih linija koje se nalaze u selekciji
	 */
	private Action unique = new LocalizableAction("unique", "tt_unique", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Document document = documents.getCurrentDocument().getTextComponent().getDocument();
			Element root = document.getDefaultRootElement();

			Caret caret = documents.getCurrentDocument().getTextComponent().getCaret();

			int startLine = root.getElementIndex(Math.min(caret.getDot(), caret.getMark()));
			int endLine = root.getElementIndex(Math.max(caret.getDot(), caret.getMark()));

			List<String> newLines = getLines(startLine, endLine, root, document).stream().distinct()
					.collect(Collectors.toList());

			insertLines(newLines, root.getElement(startLine), root.getElement(endLine), document);

		}

	};

	/**
	 * Akcija promjene velikih slova u mala, a malih u velika. Promjena se odvija na
	 * selektiranim slovima.
	 */
	private Action invert = new LocalizableAction("invertCase", null, flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			changeStringInDocument(documents.getCurrentDocument(), Character::toUpperCase, Character::toLowerCase);
		}

	};

	/**
	 * Akcija promjene svih selektiranih slova u mala slova.
	 */
	private Action toLower = new LocalizableAction("toLower", null, flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			changeStringInDocument(documents.getCurrentDocument(), Character::toLowerCase, Character::toLowerCase);
		}
	};

	/**
	 * Akcija promjene svih selektiranih slova u velika.
	 */
	private Action toUpper = new LocalizableAction("toUpper", null, flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			changeStringInDocument(documents.getCurrentDocument(), Character::toUpperCase, Character::toUpperCase);
		}
	};

	/**
	 * Metoda koja vrši promjenu slova na selektiranom dijelu dokumenta ovisno o
	 * predanim unarnim operatorima.
	 * 
	 * @param doc   dokument nad kojim se vrši operacija
	 * @param lower operator koji opisuje promjenu kada se naiđe na malo slovo
	 * @param upper operator koji opisuje promjenu kada se naiđe na veliko slovo
	 */
	private void changeStringInDocument(SingleDocumentModel doc, UnaryOperator<Character> lower,
			UnaryOperator<Character> upper) {
		Caret caret = doc.getTextComponent().getCaret();

		int start = getStart(caret);
		int len = getLength(caret);

		String text;
		try {
			text = doc.getTextComponent().getText(start, len);
			text = changeString(text, lower, upper);
			doc.getTextComponent().getDocument().remove(start, len);
			doc.getTextComponent().getDocument().insertString(start, text, null);
		} catch (BadLocationException e) {
		}
	}

	/**
	 * Metoda koja mijenja predani string na način da sprema rezultate operacija
	 * operatora primjenjenih u odgovarajućem sločaju.
	 * 
	 * @param text  string koji se mijenja
	 * @param lower zadana promjena za mala slova
	 * @param upper zadana promjena za velika slova
	 * 
	 * @return promijenjen string
	 */
	private String changeString(String text, UnaryOperator<Character> lower, UnaryOperator<Character> upper) {
		char[] data = text.toCharArray();
		for (int i = 0; i < data.length; i++) {
			if (Character.isUpperCase(data[i])) {
				data[i] = upper.apply(data[i]);
			} else if (Character.isLowerCase(data[i])) {
				data[i] = lower.apply(data[i]);
			}
		}
		return new String(data);
	}

	/**
	 * Akcija koja mijenja trenutni jezik u hrvatski
	 */
	private Action langHr = new LocalizableAction("langHr", null, flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
		}
	};

	/**
	 * Akcija koja mijenja trenutni jezik u njemački
	 */
	private Action langDe = new LocalizableAction("langDe", null, flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("de");
		}
	};

	/**
	 * Akcija koja mijenja trenutni jezik u engleski
	 */
	private Action langEn = new LocalizableAction("langEn", null, flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
		}
	};

	/**
	 * Akcija za prikazivanje statistike dokumenta. Očekivani format poruke u
	 * datoteci za lokalizaciju je ".*'X'.*'Y'.*'Z'.*", gdje 'X', 'Y' i 'Z' ne
	 * moraju nužno ići tim slijedom.
	 */
	private Action statistics = new LocalizableAction("statistics", "tt_stats", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel doc = documents.getCurrentDocument();

			String text = doc.getTextComponent().getText();

			int x = text.length();

			int y = text.replaceAll("\\s+", "").length();

			int z = x == 0 ? 0 : text.replaceAll("[^\n]+", "").length() + 1;

			String message = flp.getString("m_statistics").replace("'X'", Integer.toString(x))
					.replace("'Y'", Integer.toString(y)).replace("'Z'", Integer.toString(z));

			JOptionPane.showMessageDialog(JNotepadPP.this, message, flp.getString("t_statistics"),
					JOptionPane.INFORMATION_MESSAGE);
		}
	};

	/**
	 * Akcija za izlazak iz programa. Prilikom zahtjeva za gašenje programa prvo se
	 * provjerava status svih otvorenih dokumenata i ako postoje modificirani
	 * dokumenti korisniku se nudi dijalog za spremanje dokumenata.
	 */
	private Action exit = new LocalizableAction("exit", "tt_exit", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean modified = false;

			for (SingleDocumentModel doc : documents) {
				if (doc.isModified()) {
					modified = true;
					break;
				}
			}

			if (!modified) {
				closeDocuments();
			} else {
				int option = askForPermission(flp.getString("m_exit"), JOptionPane.YES_NO_CANCEL_OPTION,
						new String[] { flp.getString("p_yes"), flp.getString("p_no"), flp.getString("p_cancel") });

				if (option == JOptionPane.NO_OPTION) {
					closeDocuments();
				} else if (option == JOptionPane.YES_OPTION) {

					for (SingleDocumentModel doc : documents) {
						if (doc.isModified()) {
							if (!saveDocument(doc)) {
								return;
							}
						}
					}
				} else {
					return;
				}
			}

			dispose();
			stopWatch = true;
		}

	};

	/**
	 * Metoda koja zatvara sve dokumente
	 */
	private void closeDocuments() {
		for (SingleDocumentModel doc : documents) {
			documents.closeDocument(doc);
		}
	}

	/**
	 * Akcija koja trenutnu selekciju sprema u međuspremnik sustava i briše ju iz
	 * dokumenta.
	 */
	private Action cut = new LocalizableAction("cut", null, flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel doc = documents.getCurrentDocument();

			Caret caret = doc.getTextComponent().getCaret();

			int start = getStart(caret);
			int len = getLength(caret);

			try {
				StringSelection sel = new StringSelection(doc.getTextComponent().getText(start, len));
				doc.getTextComponent().getDocument().remove(start, len);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
			} catch (BadLocationException ignorable) {
			}

		}
	};

	/**
	 * Akcija koja vrijednost iz međuspremnika sustava umeće u dokument ako se tekst
	 * može pretvoriti u String inače se zahtjec ignorira.
	 */
	private Action paste = new LocalizableAction("paste", null, flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel doc = documents.getCurrentDocument();

			Caret caret = doc.getTextComponent().getCaret();
			int start = getStart(caret);

			Transferable sel = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
			if (!sel.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				return;
			}

			try {
				String clipboard = (String) sel.getTransferData(DataFlavor.stringFlavor);
				doc.getTextComponent().insert(clipboard, start);
			} catch (UnsupportedFlavorException | IOException e1) {
			}

		}
	};

	/**
	 * Akcija koja kopira trenutno označeni tekst u međuspremnik sustava.
	 */
	private Action copy = new LocalizableAction("copy", null, flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel doc = documents.getCurrentDocument();

			Caret caret = doc.getTextComponent().getCaret();

			int start = getStart(caret);
			int len = getLength(caret);

			try {
				StringSelection sel = new StringSelection(doc.getTextComponent().getText(start, len));
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
			} catch (BadLocationException ignorable) {
			}
		}
	};

	/**
	 * Metoda za dohvaćanje duljine selekcije
	 * 
	 * @param caret znak za umetanje
	 * @return duljina selekcije
	 */
	private int getLength(Caret caret) {
		return Math.abs(caret.getDot() - caret.getMark());
	}

	/**
	 * Metoda za dohvaćanje početne pozicije selekcije
	 * 
	 * @param caret znak za umetanje
	 * @return početna pozicija selekcije
	 */
	private int getStart(Caret caret) {
		return Math.min(caret.getDot(), caret.getMark());
	}

	/**
	 * Akcija za zatvaranje dokumenta. Ako je dokument modificiran korisniku se nudi
	 * mogućnost spremanja dokumenta.
	 */
	private Action closeDocument = new LocalizableAction("close", "tt_close", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel doc = documents.getCurrentDocument();
			if (doc.isModified()) {
				int option = askForPermission(flp.getString("m_close"), JOptionPane.YES_NO_CANCEL_OPTION,
						new String[] { flp.getString("p_yes"), flp.getString("p_no"), flp.getString("p_cancel") });
				if (option == JOptionPane.YES_OPTION) {
					if(!saveDocument(doc)) {
						return;
					}
				} else if (option == JOptionPane.NO_OPTION) {
					documents.closeDocument(doc);
				} else {
					return;
				}
			}
			
			documents.closeDocument(doc);
		}

	};

	/**
	 * Akcija za spremanje dokumenta. Ako je dokument nema svoju stazu korisnika se
	 * ispituje na koju stazu želi spremiti ne imenovani dokument.
	 */
	private Action saveDocument = new LocalizableAction("save", "tt_save", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			saveDocument(documents.getCurrentDocument());
		}
	};

	/**
	 * Metoda za spremanje dokumenta
	 * 
	 * @param doc dokument kojeg se sprema
	 * @return true ako je dokument spremljen, false inače
	 */
	private boolean saveDocument(SingleDocumentModel doc) {
		Path path = doc.getFilePath();

		if (path != null) {
			documents.saveDocument(doc, path);
			return true;
		} else {
			return saveDocumentAs(doc, "unnamed");
		}
	}

	/**
	 * Metoda koja sprema dokument na stazu koju korisnik odabire. Ako se na toj
	 * stazi nalazi neka druga datoteka koja je otvorena u programu javlja se greška
	 * i datoteka se ne sprema.
	 * 
	 * @param doc  dokument koji se sprema
	 * @param name naziv datoteke koja se sprema
	 * 
	 * @return true ako se datoteka spremila, false inače
	 */
	private boolean saveDocumentAs(SingleDocumentModel doc, String name) {
		if (askForPath(flp.getString("m_saveAs_part1") + " " + name + " "
				+ flp.getString("m_saveAs_part2")) != JFileChooser.APPROVE_OPTION) {
			return false;
		}
		Path path = jfc.getSelectedFile().toPath();

		if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
			if (askForPermission(flp.getString("m_saveAs_overwrite"), JOptionPane.YES_NO_OPTION,
					new String[] { flp.getString("p_yes"), flp.getString("p_no") }) != JOptionPane.YES_OPTION) {
				return false;
			}
		}

		try {
			documents.saveDocument(documents.getCurrentDocument(), path);
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, flp.getString("m_saveAs_error"), flp.getString("m_error"),
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	/**
	 * Akcija spremanja dokumenta na željenu stazu
	 */
	private Action saveDocumentAs = new LocalizableAction("saveAs", "tt_saveAs", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel doc = documents.getCurrentDocument();
			saveDocumentAs(doc, doc.getFilePath() == null ? "unnamed" : doc.getFilePath().getFileName().toString());
		}
	};

	/**
	 * Metoda za traženje dozvole korisnika za iduću operaciju
	 * 
	 * @param text    poruka koja se ispisuje u dijalogu
	 * @param option  ponuđenje opcije
	 * @param options tekst opcija
	 * 
	 * @return zastavica koja označava korisnikov odgovor
	 */
	private int askForPermission(String text, int option, Object[] options) {
		return JOptionPane.showOptionDialog(this, text, flp.getString("m_warning"), option, JOptionPane.WARNING_MESSAGE,
				null, options, null);
	}

	/**
	 * Akcija za stvaranje novog dokumenta
	 */
	private Action createDocument = new LocalizableAction("create", "tt_create", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel doc = documents.createNewDocument();

			doc.getTextComponent().addCaretListener(caretListener);
		}
	};

	/**
	 * Promatrač koji prati akcije miša
	 */
	private CaretListener caretListener = (e) -> {
		caseManipulators.forEach((act) -> act.setEnabled(e.getDot() != e.getMark()));
		setLeftStatusBar((JTextArea)e.getSource());
		setRightStatusBar((JTextArea)e.getSource());
	};

	/**
	 * Akcija za otvaranje postojećeg dokumenta.
	 */
	private Action openDocument = new LocalizableAction("open", "tt_open", flp) {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			if (askForPath(flp.getString("t_open_document")) == JFileChooser.APPROVE_OPTION) {
				documents.loadDocument(jfc.getSelectedFile().toPath());
			}
		}
	};

	/**
	 * Metoda za traženje staze od korisnika
	 * 
	 * @param title naslov JFileChoosera
	 * @return opcija odgovora
	 */
	private int askForPath(String title) {
		jfc = new JFileChooser();
		jfc.setSelectedFile(null);
		jfc.setDialogTitle(title);
		return jfc.showOpenDialog(this);
	}

	/**
	 * Ulazna točka programa
	 * 
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JNotepadPP().setVisible(true);
		});
	}
}
