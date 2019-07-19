package hr.fer.zemris.java.hw11.jnotepadpp.documentModels;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 * Razred koji je adapter za JTextArea. Razred se ponaša kao dokument koji je
 * prikazan u tekstualnoj komponenti te za svaku svoju promjenu dojavljuje
 * svojim promatračima o kojoj je promijeni riječ.
 * 
 * @author Ante Miličević
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
	/**
	 * Komponenta u kojoj se nalazi tekst dokumenta
	 */
	private JTextArea textComponent;
	/**
	 * Status modificiranosti
	 */
	private boolean modified;
	/**
	 * Staza do dokumenta na disku
	 */
	private Path filePath;
	/**
	 * Skup promatrača
	 */
	private List<SingleDocumentListener> listeners;

	/**
	 * Konstruktor
	 * 
	 * @param path    staza do dokumenta
	 * @param content sadržaj dokumenta
	 */
	public DefaultSingleDocumentModel(Path path, String content) {
		textComponent = new JTextArea(content);

		listeners = new CopyOnWriteArrayList<>();

		filePath = path;

		setDocumentListener();
	}

	/**
	 * Metoda za postavljanje promatrača na tekstualnu komponentu radi praćenja
	 * promjena dokumenta.
	 */
	private void setDocumentListener() {
		Document doc = textComponent.getDocument();

		doc.addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				modified = true;
				notifyAllListenersOnUpdate();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				modified = true;
				notifyAllListenersOnUpdate();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				modified = true;
				notifyAllListenersOnUpdate();
			}
		});

	}

	/**
	 * Metoda za dojavljivanje promjene teksta dokumenta
	 */
	private void notifyAllListenersOnUpdate() {
		listeners.forEach((listener) -> {
			listener.documentModifyStatusUpdated(this);
		});
	}

	@Override
	public JTextArea getTextComponent() {
		return textComponent;
	}

	@Override
	public Path getFilePath() {
		return filePath;
	}

	@Override
	public void setFilePath(Path path) {
		filePath = Objects.requireNonNull(path);

		listeners.forEach((l) -> {
			l.documentFilePathUpdated(this);
		});
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
		notifyAllListenersOnUpdate();
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener listener) {
		if (listeners.contains(listener) || listener == null) {
			return;
		}
		listeners.add(listener);
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener listener) {
		listeners.remove(listener);
	}

}
