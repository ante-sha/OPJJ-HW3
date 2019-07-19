package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;
import static hr.fer.zemris.java.hw05.db.ComparisonOperators.*;
import static hr.fer.zemris.java.hw05.db.FieldValueGetters.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class QueryParserTest {

	@Test
	public void constructorWithNull() {
		assertThrows(NullPointerException.class,()->new QueryParser(null));
	}
	
	@Test
	public void emptyBodyQuery() {
		assertThrows(IllegalArgumentException.class,()->new QueryParser(""));
	}
	
	@Test
	public void regularQueryWithoutAnd() {
		QueryParser parser = new QueryParser("lastName = \"LIKE\"");
		
		List<ConditionalExpression> list = parser.getQuery();
		
		assertEquals(1,list.size());
		ConditionalExpression expression = list.get(0);
		assertEquals(LAST_NAME,expression.getFieldGetter());
		assertEquals(EQUALS,expression.getOperator());
		assertEquals("LIKE",expression.getLiteral());
		
		QueryParser parser2 = new QueryParser("jmbag LIKE \"lit*\"");
		
		list = parser2.getQuery();
		assertEquals(1,list.size());
		ConditionalExpression expr = list.get(0);
		assertEquals(JMBAG,expr.getFieldGetter());
		assertEquals(LIKE,expr.getOperator());
		assertEquals("lit*",expr.getLiteral());
	}
	
	@Test
	public void regularQueryWithAnd() {
		QueryParser parser = new QueryParser(" lastName	=\"lit1\" aNd jmbag>=\"lit2\" AND firstName!=\"Pero\"");
		
		List<ConditionalExpression> list = parser.getQuery();
		assertEquals(3,list.size());
		ConditionalExpression ex1 = list.get(0);
		assertEquals(LAST_NAME,ex1.getFieldGetter());
		assertEquals(EQUALS,ex1.getOperator());
		assertEquals("lit1",ex1.getLiteral());
		
		ConditionalExpression ex2 = list.get(1);
		assertEquals(JMBAG,ex2.getFieldGetter());
		assertEquals(GREATER_OR_EQUALS,ex2.getOperator());
		assertEquals("lit2",ex2.getLiteral());
		
		ConditionalExpression ex3 = list.get(2);
		assertEquals(FIRST_NAME,ex3.getFieldGetter());
		assertEquals(NOT_EQUALS,ex3.getOperator());
		assertEquals("Pero",ex3.getLiteral());
	}
	
	@Test
	public void queryWithAditionalAnd() {
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastName = \"lit\" and and"));
	}
	
	@Test
	public void queryWithTwoExpressionsWithoutAnd() {
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastName = \"lit\" firstName > \"lit\""));
	}
	
	@Test
	public void unsupportedSymbolsInQueryOutsideLiteral() {
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastName $= \"lit\""));
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastName = \"lit\"%"));
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastName = \"lit\" and firstName ! \"lit\""));
		assertThrows(IllegalArgumentException.class,()->new QueryParser("?lastName = \"lit\""));
	}
	
	@Test
	public void comparingAtributeWithRawText() {
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastName = text"));
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastName = lastName"));
	}
	
	@Test
	public void wrongAtributeNames() {
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastNames = \"lit\""));
		assertThrows(IllegalArgumentException.class,()->new QueryParser("attr = \"lit\""));
		assertThrows(IllegalArgumentException.class,()->new QueryParser("jmBag = \"lit\""));
	}
	
	@Test
	public void likeWithTwoWildcards() {
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastName LIKE \"*li*t\""));
	}
	
	@Test
	public void textInsteadOfOperator() {
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastName text \"lit\""));
	}
	
	@Test
	public void unfinishedExpression() {
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastName LIKE "));
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastName >="));
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastName"));
	}
	
	@Test
	public void unclosedLiteral() {
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastName > \"lit"));
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastName < lit\""));
		assertThrows(IllegalArgumentException.class,()->new QueryParser("lastName \">= \"lit\""));
	}
}
