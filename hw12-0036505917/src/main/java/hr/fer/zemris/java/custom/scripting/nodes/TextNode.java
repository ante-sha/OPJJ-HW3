/**
 * Paket u kojem se nalaze svi mogući čvorovi koje generira {@link SmartScriptParser}
 * grupiranjem elemenata {@link Element}.
 */
package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

/**
 * Razred koji modelira vršni čvor dokumenta kojeg generira
 * {@link SmartScriptParser}
 * 
 * @author Ante Miličević
 *
 */
public class TextNode extends Node {
	/** Tekst koji čvor čuva */
	private String text;

	/**
	 * Konstruktor koji inicijalizira varijablu text
	 * 
	 * @param text
	 */
	public TextNode(String text) {
		this.text = text;
	}

	/**
	 * Getter za text
	 * 
	 * @return text
	 */
	public String getText() {
		return text;
	}

	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}

	/**
	 * <p>
	 * Zbog pravila {@link SmartScriptLexer}-a i {@link SmartScriptParser}-a te
	 * rekreiranja istoznačnog dokumenta pri prebacivanju stringa nazad u text
	 * vraćaju se nazad znakovi '\' na nužno potrebna mjesta da bi ponovnim
	 * parsiranjem dokument imao isto značenje.
	 * </p>
	 * <br>
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String rezultat = text.replaceAll("(\\\\)", "\\\\$1");
		rezultat = rezultat.replaceAll("(\\{\\$)", "\\\\$1");
		return rezultat;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(text);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof TextNode))
			return false;
		TextNode other = (TextNode) obj;
		return Objects.equals(text, other.text);
	}

}
