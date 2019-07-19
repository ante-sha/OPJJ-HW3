package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;
import static hr.fer.zemris.java.hw05.db.ComparisonOperators.*;
import static hr.fer.zemris.java.hw05.db.FieldValueGetters.*;

import org.junit.jupiter.api.Test;

public class ConditionalExpressionTest {

	@Test
	public void constructorTestOneArgumentNull() {
		assertThrows(NullPointerException.class,()->new ConditionalExpression(null,"string",EQUALS));
		assertThrows(NullPointerException.class,()->new ConditionalExpression(JMBAG,"string",null));
		assertThrows(NullPointerException.class,()->new ConditionalExpression(JMBAG,null,EQUALS));
	}
	
	@Test
	public void regularArgumentsInConstructor() {
		new ConditionalExpression(JMBAG,"string",EQUALS);
	}
	
	@Test
	public void twoWildcardsInLiteralAndOperationLIKE() {
		assertThrows(IllegalArgumentException.class,()->new ConditionalExpression(JMBAG, "1*d*", LIKE));
	}
	
	@Test
	public void testRecordTest() {
		StudentRecord student = new StudentRecord("0000000005","Perić","Ivo",4);
		
		assertTrue(new ConditionalExpression(JMBAG,"0000000005",EQUALS).testRecord(student));
		assertFalse(new ConditionalExpression(JMBAG,"0000000006",EQUALS).testRecord(student));
		assertFalse(new ConditionalExpression(JMBAG,"000000*005",EQUALS).testRecord(student));
		
		assertTrue(new ConditionalExpression(LAST_NAME,"Zasić",LESS).testRecord(student));
		assertTrue(new ConditionalExpression(LAST_NAME,"Per*",LIKE).testRecord(student));
		
		assertTrue(new ConditionalExpression(FIRST_NAME,"I*vo",LIKE).testRecord(student));
		assertTrue(new ConditionalExpression(FIRST_NAME,"Ivo*",LIKE).testRecord(student));
		
		StudentRecord student2 = new StudentRecord("0000000001","Glavnić Pecotić","Petar",2);
		
		assertTrue(new ConditionalExpression(LAST_NAME,"GlavnićPecotić",NOT_EQUALS).testRecord(student2));
		assertTrue(new ConditionalExpression(LAST_NAME,"Glavnić* Pecotić",LIKE).testRecord(student2));
		assertTrue(new ConditionalExpression(LAST_NAME,"Glavn*ić",LIKE).testRecord(student2));
		assertTrue(new ConditionalExpression(LAST_NAME,"Gla*ić",LIKE).testRecord(student2));
	}
}
