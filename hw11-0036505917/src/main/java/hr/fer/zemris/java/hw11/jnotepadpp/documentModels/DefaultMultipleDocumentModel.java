package hr.fer.zemris.java.hw11.jnotepadpp.documentModels;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * Razred koji je adapter oko JTabbedPane. U svojoj implementaciji nudi
 * mogučnost manipulacije dokumentima. Svaki stvoren (ili učitan) primjerak
 * dokumenta sprema se u {@link SingleDocumentModel}.
 * 
 * @author Ante Miličević
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Mapa komponenti i dokumenata
	 */
	private LinkedHashMap<JScrollPane, SingleDocumentModel> documents = new LinkedHashMap<>();
	/**
	 * Skup promatrača na komponentu
	 */
	private List<MultipleDocumentListener> listeners = new CopyOnWriteArrayList<>();
	/**
	 * Trenutno prikazani dokument
	 */
	private SingleDocumentModel current;
	/**
	 * Ikona koja označava da u dokumentu nije bilo promjena
	 */
	private static ImageIcon greenIcon;
	/**
	 * Ikona koja označava da je dokument izmjenjen
	 */
	private static ImageIcon redIcon;

	/**
	 * Konstruktor
	 */
	public DefaultMultipleDocumentModel() {
		// adding listener on parent class
		addChangeListener((e) -> {
			SingleDocumentModel previous = current;
			current = getSelectedComponent() == null ? null : documents.get(getSelectedComponent());
			fireCurrentDocumentChanged(previous, current);
		});

		readIcon(true);
		readIcon(false);
	}

	/**
	 * Metoda za učitavanje ikona
	 * 
	 * @param green true ako se učitava greenIcon, false ako redIcon
	 */
	private void readIcon(boolean green) {
		try (InputStream is = green ? getClass().getResourceAsStream("icons/save-green.png")
				: getClass().getResourceAsStream("icons/save-red.png")) {

			byte[] buff = is.readAllBytes();

			if (green) {
				greenIcon = new ImageIcon(buff);
			} else {
				redIcon = new ImageIcon(buff);
			}
		} catch (IOException ignored) {
		}
	}

	/**
	 * Metoda za obavještavanje promatrača o promjeni fokusiranog dokumenta. Jedan
	 * od predanih argumenata može biti null.
	 * 
	 * @param previous prethodni
	 * @param current trenutni
	 */
	private void fireCurrentDocumentChanged(SingleDocumentModel previous, SingleDocumentModel current) {
		listeners.forEach((l) -> l.currentDocumentChanged(previous, current));
	}

	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return new ArrayList<>(documents.values()).iterator();
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel doc = new DefaultSingleDocumentModel(null, "");

		JScrollPane comp = new JScrollPane(doc.getTextComponent());

		documents.put(comp, doc);

		addDocumentListener(doc);

		addTab("unnamed", comp);
		setToolTipTextAt(indexOfComponent(comp), "unnamed");
		setSelectedComponent(comp);

		doc.setModified(false);

		repaint();

		fireDocumentAdded(doc);

		return doc;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return current;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		SingleDocumentModel doc = null;
		try {
			//look if document is already opened
			for(var document : documents.entrySet()) {
				if(path.equals(document.getValue().getFilePath())) {
					setSelectedComponent(document.getKey());
					fireCurrentDocumentChanged(current, document.getValue());
					current = document.getValue();
					return document.getValue();
				}
			}

			String content = Files.readString(path, StandardCharsets.UTF_8);
			doc = new DefaultSingleDocumentModel(path, content);

			addDocumentListener(doc);

			JScrollPane comp = new JScrollPane(doc.getTextComponent());

			documents.put(comp, doc);

			addTab(path.getFileName().toString(), comp);
			setToolTipTextAt(indexOfComponent(comp), doc.getFilePath().toAbsolutePath().toString());
			setSelectedComponent(comp);

			doc.setModified(false);

			repaint();

			fireDocumentAdded(doc);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error in reading file", "Error", JOptionPane.ERROR_MESSAGE);
		}

		return doc;
	}

	/**
	 * Metoda koja obavještava promatrače o dodavanju novog dokumenta
	 * 
	 * @param model dodani dokument
	 */
	private void fireDocumentAdded(SingleDocumentModel model) {
		listeners.forEach((l) -> l.documentAdded(model));
	}

	/**
	 * Metoda koja dodaje promatrača na dokument
	 * 
	 * @param doc dokument
	 */
	private void addDocumentListener(SingleDocumentModel doc) {
		doc.addSingleDocumentListener(new SingleDocumentListener() {

			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				JScrollPane comp = findComponentForModel(model);

				setIconAt(indexOfComponent(comp), model.isModified() ? redIcon : greenIcon);

			}

			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				JScrollPane comp = findComponentForModel(model);

				setTitleAt(indexOfComponent(comp), model.getFilePath().getFileName().toString());
				setToolTipTextAt(indexOfComponent(comp), model.getFilePath().toAbsolutePath().toString());
			}
		});

	}

	/**
	 * Metoda koja vraća komponentu u kojoj se nalazi predani dokument
	 * 
	 * @param model model dokumenta
	 * 
	 * @return komponenta u kojoj se nalazi dokument model
	 */
	private JScrollPane findComponentForModel(SingleDocumentModel model) {
		JScrollPane component = null;
		for (var entry : documents.entrySet()) {
			if (entry.getValue() == model) {
				component = entry.getKey();
				break;
			}
		}
		return component;
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		if (newPath == null) {
			newPath = model.getFilePath();
		}

		for (SingleDocumentModel otherModel : documents.values()) {
			if (otherModel != model && newPath.equals(otherModel.getFilePath())) {
				throw new IllegalArgumentException();
			}
		}

		try {
			Files.writeString(newPath, model.getTextComponent().getText(), StandardCharsets.UTF_8);
			model.setModified(false);

		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "Path " + newPath + " is not valid", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		model.setFilePath(newPath);
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		remove(findComponentForModel(model));

		documents.values().remove(model);

		fireDocumentRemoved(model);

		repaint();
	}

	/**
	 * Metoda za obavještavanje promatrača o uklanjanju dokumenta model
	 * 
	 * @param model dokument
	 */
	public void fireDocumentRemoved(SingleDocumentModel model) {
		listeners.forEach((l) -> l.documentRemoved(model));
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		if (listeners.contains(l) || l == null) {
			return;
		}
		listeners.add(l);
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return documents.get(getComponentAt(index));
	}

}
