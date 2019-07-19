/**
 * Paket u kojem se nalaze svi mogući čvorovi koje generira {@link SmartScriptParser}
 * grupiranjem elemenata {@link Element}.
 */
package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Razred koji modelira vršni čvor dokumenta kojeg generira {@link SmartScriptParser}
 * @author Ante Miličević
 *
 */
public class DocumentNode extends Node {
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}
}
