package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.SimpleHashtable;

public class SimpleHashtableDemo {
	public static void main(String[] args) {
		// create collection:
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		// query collection:
		Integer kristinaGrade = examMarks.get("Kristina");
		System.out.println("Kristina's exam grade is: " + kristinaGrade); // writes: 5
		// What is collection's size? Must be four!
		System.out.println("Number of stored pairs: " + examMarks.size()); // writes: 4
		
		
		SimpleHashtable<String, Integer> examMarks2 = new SimpleHashtable<>(5);
		examMarks2.put("Ante", 2);
		examMarks2.put("Jasna", 2);
		examMarks2.put("Kristina", 5);
		examMarks2.put("Ivana", 5);
		System.out.println(examMarks2);
		
		for(SimpleHashtable.TableEntry<String, Integer> p : examMarks2)
			System.out.println(p);
		
		//kartezijev produkt
		for(SimpleHashtable.TableEntry<String,Integer> pair1 : examMarks) {
			for(SimpleHashtable.TableEntry<String,Integer> pair2 : examMarks) {
				System.out.printf(
					"(%s => %d) - (%s => %d)%n",
				pair1.getKey(), pair1.getValue(),
				pair2.getKey(), pair2.getValue()
					);
			}
		}
	}
}
