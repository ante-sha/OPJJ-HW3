package hr.fer.zemris.java.custom.collections.demo;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import hr.fer.zemris.java.custom.collections.SimpleHashtable;

public class SimpleHashtableIteratorDemo {
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
		
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
		while(iter.hasNext()) {
			SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
			if(pair.getKey().equals("Ivana")) {
				iter.remove(); // sam iterator kontrolirano uklanja trenutni element
			}
		}
		
		examMarks.forEach(System.out::println);
		
		//dodavanje Ivane za novu demonstraciju gdje je cilj vidjeti ponašanje iteratora 
		//pri drugom pozivu remove metode
		examMarks.put("Ivana", 5);
		try {
			Iterator<SimpleHashtable.TableEntry<String,Integer>> iter2 = examMarks.iterator();
			while(iter2.hasNext()) {
				SimpleHashtable.TableEntry<String,Integer> pair = iter2.next();
				if(pair.getKey().equals("Ivana")) {
					iter2.remove();
					iter2.remove();
				}
			}
		} catch (IllegalStateException e) {
			System.out.println("1. slučaj dobar");
		}
		
		//dodavanje Ivane za demonstraciju reakcije iteratora na promjenu kolekcije
		//"izvana"
		examMarks.put("Ivana", 5);
		try {
			Iterator<SimpleHashtable.TableEntry<String,Integer>> iter3 = examMarks.iterator();
			while(iter3.hasNext()) {
				SimpleHashtable.TableEntry<String,Integer> pair = iter3.next();
				if(pair.getKey().equals("Ivana")) {
					examMarks.remove("Ivana");
				}
			}
		} catch (ConcurrentModificationException e) {
			System.out.println("2. slučaj dobar");
		}
		
		//velicina kolekcije nakon svih uklanjanja
		examMarks.put("Ivana",5);
		System.out.println(examMarks.size());
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter4 = examMarks.iterator();
		while(iter4.hasNext()) {
			SimpleHashtable.TableEntry<String,Integer> pair = iter4.next();
			System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
			iter4.remove();
		}
		System.out.printf("Veličina: %d%n", examMarks.size());
	}
}
