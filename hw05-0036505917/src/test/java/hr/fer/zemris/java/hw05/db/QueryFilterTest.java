package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import static hr.fer.zemris.java.hw05.db.ComparisonOperators.*;
import static hr.fer.zemris.java.hw05.db.FieldValueGetters.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class QueryFilterTest {
	static List<ConditionalExpression> listOfExpressions;
	static List<StudentRecord> database;
	
	@BeforeAll
	public static void listInit() {
		listOfExpressions = new ArrayList<ConditionalExpression>();
		
		listOfExpressions.add(new ConditionalExpression(JMBAG,"0000000010",GREATER));
		listOfExpressions.add(new ConditionalExpression(LAST_NAME,"G",LESS));
		listOfExpressions.add(new ConditionalExpression(FIRST_NAME,"Dario",NOT_EQUALS));
		
		database = new ArrayList<StudentRecord>();
		
		database.add(new StudentRecord("0000000011","Horvat","Ivan",5));
		database.add(new StudentRecord("0000000009","Benić","Filip",2));
		database.add(new StudentRecord("0000000015","Anić","Dario",5));
		database.add(new StudentRecord("0000000016","Anić","Ivana",5));
	}
	
	@Test
	public void constructorWithNullList() {
		assertThrows(NullPointerException.class,()->new QueryFilter(null));
	}
	
	@Test
	public void constructorWithNullInList() {
		List<ConditionalExpression> list = new ArrayList<ConditionalExpression>();
		list.add(null);
		
		assertThrows(IllegalArgumentException.class,()->new QueryFilter(list));
	}
	
	@Test
	public void acceptsTest() {
		QueryFilter filter = new QueryFilter(listOfExpressions);
		assertFalse(filter.accepts(database.get(0)));
		assertFalse(filter.accepts(database.get(1)));
		assertFalse(filter.accepts(database.get(2)));
		assertTrue(filter.accepts(database.get(3)));
	}
	
	@Test
	public void effectOfOutsideChangingListOfExpressions() {
		List<ConditionalExpression> clone = new ArrayList<>(listOfExpressions);
		QueryFilter filter = new QueryFilter(clone);
		assertFalse(filter.accepts(database.get(0)));
		assertFalse(filter.accepts(database.get(1)));
		assertFalse(filter.accepts(database.get(2)));
		assertTrue(filter.accepts(database.get(3)));
		
		clone.clear();
		assertFalse(filter.accepts(database.get(0)));
		assertFalse(filter.accepts(database.get(1)));
		assertFalse(filter.accepts(database.get(2)));
		assertTrue(filter.accepts(database.get(3)));
	}

}
