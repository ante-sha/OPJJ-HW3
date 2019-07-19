package hr.fer.zemris.java.hw05.db.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import hr.fer.zemris.java.hw05.db.ComparisonOperators;
import hr.fer.zemris.java.hw05.db.IComparisonOperator;
import hr.fer.zemris.java.hw05.db.StudentDatabase;
import hr.fer.zemris.java.hw05.db.StudentRecord;

public class DBDemo {
	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("./database.txt"),StandardCharsets.UTF_8);
		
		StudentDatabase db = new StudentDatabase(lines);
		
		List<StudentRecord> none = db.filter((e)->false);
		
		none.forEach(System.out::println);
		
		List<StudentRecord> all = db.filter((e)->true);
		
		all.subList(0, 10).forEach(System.out::println);
		
		IComparisonOperator oper = ComparisonOperators.LIKE;
		System.out.println(oper.satisfied("Zagreb", "Aba*"));
		System.out.println(oper.satisfied("AAA", "AA*AA"));
		System.out.println(oper.satisfied("AAAA", "AA*AA"));
		// false
		// false
		// true
	}
}
