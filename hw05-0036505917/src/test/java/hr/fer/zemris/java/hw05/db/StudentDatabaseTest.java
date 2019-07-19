package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class StudentDatabaseTest {
	static StudentDatabase db;
	
	@BeforeAll
	public static void initDB() {
		List<String> list = 
				Arrays.asList("0000000001	Perić	Pero	2",
						"0000000002	Anić	Ana	3",
						"0000000003	Taler	Kruno	4");
		db = new StudentDatabase(list);
	}
	
	@Test
	public void filterTestWithAllAcceptingFilter() {
		List<StudentRecord> resultList = db.filter(rec->true);
		
		assertTrue(resultList.contains(new StudentRecord("0000000001","Perić","Pero",2)));
		assertTrue(resultList.contains(new StudentRecord("0000000002","Anić","Ana",3)));
		assertTrue(resultList.contains(new StudentRecord("0000000003","Taler","Kruno",4)));
	}
	
	@Test
	public void filterTestWithAcceptingNoneFilter() {
		List<StudentRecord> resultList = db.filter(rec->false);
		
		assertFalse(resultList.contains(new StudentRecord("0000000001","Perić","Pero",2)));
		assertFalse(resultList.contains(new StudentRecord("0000000002","Anić","Ana",3)));
		assertFalse(resultList.contains(new StudentRecord("0000000003","Taler","Kruno",4)));
	}
	
	@Test
	public void forJMBAGTest() {
		assertEquals(new StudentRecord("0000000001","Perić","Pero",2),db.forJMBAG("0000000001"));
		assertEquals(new StudentRecord("0000000002","Anić","Ana",3),db.forJMBAG("0000000002"));
		assertEquals(new StudentRecord("0000000003","Taler","Kruno",4),db.forJMBAG("0000000003"));
	}
	
	@Test
	public void forJMBAGWithNullJmbag() {
		assertEquals(null,db.forJMBAG(null));
	}
	
	@Test
	public void forJMBAGWithUnexistingJmbag() {
		assertEquals(null,db.forJMBAG("0000000000"));
	}
}
