package hr.fer.zemris.java.custom.scripting.demo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * Demonstracijski program idejnog rada INodeVisitora na primjeru visitora koji
 * iz korijenskog čvora rekonstruira dokument i ispisuje ga.
 * 
 * @author Ante Miličević
 *
 */
public class TreeWriter {
	/**
	 * Ulazna točka programa
	 * 
	 * @param args prihvaća se samo jedan parametar, a to je staza do skripte
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("You must enter path to smart script file");
			System.exit(1);
		}
		
		
		try {
			Path path = Paths.get(args[0]);
			String docBody = Files.readString(path);
			
			SmartScriptParser parser = new SmartScriptParser(docBody);
			
			WriterVisitor visitor = new WriterVisitor();
			
			parser.getDocumentNode().accept(visitor);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Demonstracijski visitor koji rekonstruira i ispisuje sadržaj skripte
	 * iz njenog korijenskog čvora.
	 * 
	 * @author Ante Miličević
	 *
	 */
	private static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node);
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			System.out.print(node);
			for(int i = 0,n = node.numberOfChildren();i<n;i++) {
				node.getChild(i).accept(this);
			}
			System.out.print("{$END$}");
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.print(node);
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for(int i = 0,n = node.numberOfChildren();i<n;i++) {
				node.getChild(i).accept(this);
			}
		}
	}
}
