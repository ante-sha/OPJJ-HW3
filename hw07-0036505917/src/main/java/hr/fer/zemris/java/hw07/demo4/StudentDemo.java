package hr.fer.zemris.java.hw07.demo4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentDemo {

	public static void main(String[] args) {
		try {
			List<String> lines = Files.readAllLines(Paths.get("./studenti.txt"));
			List<StudentRecord> records = convert(lines);

			// Zadatak 1 -----------------------------------

			printHeader(1);
			System.out.println(vratiBodovaViseOd25(records));

			// Zadatak 2 --------------------------------------

			printHeader(2);
			System.out.println(vratiBrojOdlikasa(records));

			// Zadatak 3 -------------------------------------------

			printHeader(3);
			vratiListuOdlikasa(records).forEach(System.out::println);

			// Zadatak 4 ---------------------------------------

			printHeader(4);
			vratiSortiranuListuOdlikasa(records).forEach(System.out::println);

			// Zadatak 5 -----------------------------------------

			printHeader(5);
			vratiPopisNepolozenih(records).forEach(System.out::println);

			// Zadatak 6 -----------------------------------------

			printHeader(6);
			razvrstajStudentePoOcjenama(records).forEach((ocjena, studenti) -> {
				System.out.println("xxxxxxxx" + ocjena + "xxxxxxxx");
				studenti.forEach(System.out::println);
			});

			// Zadatak 7 ------------------------------------------

			printHeader(7);
			vratiBrojStudenataPoOcjenama(records).forEach((ocjena,broj)->{
				System.out.println(ocjena + "->" + broj);
			});

			// Zadatak 8 -------------------------------------------

			printHeader(8);
			razvrstajProlazPad(records).forEach((zastavica,lista)->{
				System.out.println("xxxxxxxx" + zastavica + "xxxxxxxx");
				lista.forEach(System.out::println);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void printHeader(int i) {
		System.out.printf("Zadatak %d%n", i);
		System.out.println("=========");
	}

	private static List<StudentRecord> convert(List<String> lines) {
		List<StudentRecord> result = new ArrayList<>();
		for (String line : lines) {
			if(line.isBlank())
				continue;
			String[] studentData = line.split("\t");

			double meduispit = Double.parseDouble(studentData[3]);
			double zavrsni = Double.parseDouble(studentData[4]);
			double labos = Double.parseDouble(studentData[5]);
			int ocjena = Integer.parseInt(studentData[6]);

			result.add(new StudentRecord(studentData[0], studentData[1], studentData[2], meduispit, zavrsni, labos,
					ocjena));
		}

		return result;
	}
	
	// Zadatak 1 -----------------------------------
	private static long vratiBodovaViseOd25(List<StudentRecord> records) {
		return records.stream()
				.filter((student) -> student.getMeduispit() + student.getZavrsni() + student.getLabos() > 25).count();
	}

	// Zadatak 2 -----------------------------------
	private static long vratiBrojOdlikasa(List<StudentRecord> records) {
		return records.stream().filter((student) -> student.getOcjena() == 5).count();
	}

	// Zadatak 3 -----------------------------------
	private static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> records) {
		return records.stream().filter((student) -> student.getOcjena() == 5).collect(Collectors.toList());
	}

	// Zadatak 4 -----------------------------------
	private static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> records) {
		return records.stream().filter((student) -> student.getOcjena() == 5)
				.sorted((s1, s2) -> Double.compare(s2.getLabos() + s2.getMeduispit() + s2.getZavrsni(),
						s1.getLabos() + s1.getMeduispit() + s1.getZavrsni()))
				.collect(Collectors.toList());
	}

	// Zadatak 5 ------------------------------------
	private static List<String> vratiPopisNepolozenih(List<StudentRecord> records) {
		return records.stream().filter((student) -> student.getOcjena() == 1).map(student -> student.getJmbag())
				.sorted().collect(Collectors.toList());
	}

	// Zadatak 6 ------------------------------------
	private static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> records) {
		return records.stream().collect(Collectors.groupingBy(student -> student.getOcjena()));
	}

	// Zadatak 7 ------------------------------------
	private static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> records) {
		return records.stream()
				.collect(Collectors.toMap(s -> s.getOcjena(), s -> Integer.valueOf(1), (old, init) -> old+1));
	}
	
	// Zadatak 8 ------------------------------------
	private static Map<Boolean,List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> records){
		return records.stream()
				.collect(Collectors.partitioningBy(student -> student.getOcjena() > 1));
	}
}
