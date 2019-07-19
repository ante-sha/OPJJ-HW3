package hr.fer.zemris.java.custom.scripting.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;

public class SmartScriptParserTest {
	
	@Test
	public void testiranjeKonstruktoraSNull() {
		assertThrows(SmartScriptParserException.class, ()->new SmartScriptParser(null));
	}
	
	@Test
	public void tagSKrivimImenom() {
		assertThrows(SmartScriptParserException.class, 
				()->new SmartScriptParser("{$NEMA n @sin p$}"));
	}
	
	@Test
	public void echoTag() {
		SmartScriptParser parser = new SmartScriptParser("{$=  123  i   \" \\\" os \"  @sin  $}");
		EchoNode echo = (EchoNode)parser.getDocumentNode().getChild(0);
		assertEquals("{$= 123 i \" \\\" os \" @sin$}",echo.toString());
		Element[] elementi = echo.getElements();
		assertEquals(new ElementConstantInteger(123),elementi[0]);
		assertEquals(new ElementVariable("i"),elementi[1]);
		assertEquals(new ElementString(" \" os "),elementi[2]);
		assertEquals(new ElementFunction("sin"),elementi[3]);
		assertEquals(4,echo.getElements().length);
	}
	
	@Test
	public void echoTagSNedozvoljenomEscapeSekvencom() {
		assertThrows(SmartScriptParserException.class,()->new SmartScriptParser("{$= 123 i \\a$}"));
		
	}
	
	@Test
	public void forTagSDobrimArgumentima() {
		SmartScriptParser parser = new SmartScriptParser("{$FOR i   \" os \"  10  $}{$END$}");
		ForLoopNode forLoop = (ForLoopNode)parser.getDocumentNode().getChild(0);
		ForLoopNode test = new ForLoopNode(new ElementVariable("i"),new ElementString(" os "),new ElementConstantInteger(10),null);
		assertEquals(test,forLoop);
		assertEquals("{$FOR i \" os \" 10$}",forLoop.toString());
		
		SmartScriptParser parser2 = new SmartScriptParser("{$FOR i  -1 tekst  $}{$END$}");
		ForLoopNode forLoop2 = (ForLoopNode)parser2.getDocumentNode().getChild(0);
		test = new ForLoopNode(new ElementVariable("i"),new ElementConstantInteger(-1),new ElementVariable("tekst"),null);
		assertEquals(test,forLoop2);
		assertEquals("{$FOR i -1 tekst$}",forLoop2.toString());
		
		SmartScriptParser parser3 = new SmartScriptParser("{$ FOR it_2er3-1.35bbb\"1\" $}{$END$}");
		ForLoopNode forLoop3 = (ForLoopNode)parser3.getDocumentNode().getChild(0);
		test = new ForLoopNode(new ElementVariable("it_2er3"),new ElementConstantDouble(-1.35),new ElementVariable("bbb"),new ElementString("1"));
		assertEquals(test,forLoop3);
		assertEquals("{$FOR it_2er3 -1.35 bbb \"1\"$}",forLoop3.toString());
		
		SmartScriptParser parser4 = new SmartScriptParser("{$FOR var  -1 \" \\n \\t \" end  $}{$END$}");
		ForLoopNode forLoop4 = (ForLoopNode)parser4.getDocumentNode().getChild(0);
		test = new ForLoopNode(new ElementVariable("var"),new ElementConstantInteger(-1),new ElementString(" \n \t "),new ElementVariable("end"));
		assertEquals(test,forLoop4);
		assertEquals("{$FOR var -1 \" \n \t \" end$}",forLoop4.toString());
	}
	
	@Test
	public void forTagSKrivimArgumentima() {
		assertThrows(SmartScriptParserException.class,()->new SmartScriptParser("{$ FOR  3 1 10 1 $}{$END$}"),"3 ne smije biti ime varijable");
		assertThrows(SmartScriptParserException.class,()->new SmartScriptParser("{$ FOR  * \"1\" -10 \"1\" $}{$END$}"),"* ne smije biti ime varijable");
		assertThrows(SmartScriptParserException.class,()->new SmartScriptParser("{$ FOR  year @sin 10$}{$END$}"),"@sin ne može biti argument for petlje");
		assertThrows(SmartScriptParserException.class,()->new SmartScriptParser("{$ FOR  year 3 1 10 1 $}{$END$}"),"Previše argumenata");
		assertThrows(SmartScriptParserException.class,()->new SmartScriptParser("{$ FOR  year$}{$END$}"),"Premalo argumenata");
		assertThrows(SmartScriptParserException.class,()->new SmartScriptParser("{$ FOR  3 1 10 \"1\" \"23\" $}{$END$}"),"Previše argumenata");
	}
	
	@Test
	public void nedostatakEndTagova() {
		assertThrows(SmartScriptParserException.class,()->new SmartScriptParser("{$ FOR i 1 10 $}"),"For nema svoj end tag");
		assertThrows(SmartScriptParserException.class,()->new SmartScriptParser("{$FOR var 5 8$} some text"
				+"{$ FOR index \"pero\" 10 $}{$END$} sjlk jlk \n"),"For nema svoj end tag");
	}
	
	@Test
	public void ocuvanjeRazmakaUTekst() {
		SmartScriptParser parser = new SmartScriptParser("Here is some text \r \t \n  ");
		TextNode text = (TextNode)parser.getDocumentNode().getChild(0);
		assertEquals("Here is some text \r \t \n  ",text.getText());
	}
	
	@Test
	public void odrzavanjeStrukturePoslijeParsiranjaDoc1() {
		String tijeloDokumenta = loader("doc1.txt");
		
		SmartScriptParser parser = new SmartScriptParser(tijeloDokumenta);
		String parsirano = SmartScriptParser.createOriginalDocumentBody(parser.getDocumentNode());
		SmartScriptParser parser2 = new SmartScriptParser(parsirano);
		
		assertEquals(parser.getDocumentNode(),parser2.getDocumentNode(),"Uspoređivanje strukture i istoznačnosti elemenata");
		assertEquals(parsirano,SmartScriptParser.createOriginalDocumentBody(parser2.getDocumentNode()),"Uspoređivanje teksta kreiranog dokumena");
	}
	
	@Test
	public void odrzavanjeStrukturePoslijeParsiranjaDoc2() {
		String tijeloDokumenta = loader("doc2.txt");
		
		SmartScriptParser parser = new SmartScriptParser(tijeloDokumenta);
		String parsirano = SmartScriptParser.createOriginalDocumentBody(parser.getDocumentNode());
		SmartScriptParser parser2 = new SmartScriptParser(parsirano);
		assertEquals(parser.getDocumentNode(),parser2.getDocumentNode(),"Uspoređivanje strukture i istoznačnosti elemenata");
		assertEquals(parsirano, SmartScriptParser.createOriginalDocumentBody(parser2.getDocumentNode()),"Uspoređivanje teksta kreiranog dokumena");
	}
	
	@Test
	public void odrzavanjeStrukturePoslijeParsiranjaDoc3() {
		String tijeloDokumenta = loader("doc3.txt");
		
		SmartScriptParser parser = new SmartScriptParser(tijeloDokumenta);
		String parsirano = SmartScriptParser.createOriginalDocumentBody(parser.getDocumentNode());
		SmartScriptParser parser2 = new SmartScriptParser(parsirano);
		assertEquals(parser.getDocumentNode(),parser2.getDocumentNode(),"Uspoređivanje strukture i istoznačnosti elemenata");
		assertEquals(parsirano, SmartScriptParser.createOriginalDocumentBody(parser2.getDocumentNode()),"Uspoređivanje teksta kreiranog dokumena");
	}
	
	//Metoda za učitavanje datoteke
	private String loader(String filename) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try(	
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename)) {
			byte[] buffer = new byte[1024];
		while(true) {
			int read = is.read(buffer);
			if(read<1) break;
			bos.write(buffer, 0, read);
		}
			return new String(bos.toByteArray(), StandardCharsets.UTF_8);
		} catch(IOException ex) {
			return null;
		}
	}
}
