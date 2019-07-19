package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Sučelje koje definira metode za obrade svih vrsta čvorova dokumenta izvedenih
 * iz razreda {@link Node}
 * 
 * @author Ante Miličević
 *
 */
public interface INodeVisitor {

	/**
	 * Metoda koja se poziva pri obradi tekstnog čvora
	 * 
	 * @param node tekstni čvor
	 */
	public void visitTextNode(TextNode node);

	/**
	 * Metoda koja se poziva pri obradi for petlje
	 * 
	 * @param node for petlja
	 */
	public void visitForLoopNode(ForLoopNode node);

	/**
	 * Metoda koja se poziva pri obradi čvora za dinamičko
	 * generiranje teksta
	 * 
	 * @param node čvor
	 */
	public void visitEchoNode(EchoNode node);

	/**
	 * Metoda koja se poziva pri obradi vršnog čvora
	 * dokumenta
	 * 
	 * @param node vršni čvor dokumenta
	 */
	public void visitDocumentNode(DocumentNode node);
}
