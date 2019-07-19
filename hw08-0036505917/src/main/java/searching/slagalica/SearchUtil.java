package searching.slagalica;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import searching.algorithms.Node;
import searching.algorithms.Transition;

/**
 * Razred koji implementira algoritme za pretraživanje prostora, parametri koji
 * su nužni za provođenje algoritma su:<br>
 * Početno stanje (Supplier)<br>
 * Funkcija sljedbenika (Function)<br>
 * Ciljani rezultat (Predicate)
 * 
 * @author Ante Miličević
 *
 */
public class SearchUtil {

	/**
	 * Implementacija bfs (breadth first search) algoritma koji pretražuje prostor
	 * razinu po razinu stabla gdje je korijen stabla stanje s0, a svaku novu razinu
	 * čine sljedbenici stanja prethode razine.
	 * 
	 * @param s0   početno stanje
	 * @param succ funkcija sljedbenika
	 * @param goal ciljani rezultat
	 * @param      <S> tip podatka rješenja
	 * @return {@link Node} koji sadrži podatke o putu rješavanja problema
	 * 
	 * @throws OutOfMemoryError ako rješenje nepostoji
	 */
	public static <S> Node<S> bfs(Supplier<S> s0, Function<S, List<Transition<S>>> succ, Predicate<S> goal) {
		List<Node<S>> toProcess = new LinkedList<>();
		Node<S> tmpNode = new Node<S>(s0.get(), 0, null);
		toProcess.add(tmpNode);

		while (!toProcess.isEmpty()) {
			Node<S> ni = toProcess.remove(0);

			if (goal.test(ni.getState()))
				return ni;

			for (Transition<S> transition : succ.apply(ni.getState())) {
				toProcess.add(new Node<S>(transition.getState(), ni.getCost() + transition.getCost(), ni));
			}
		}

		return null;
	}

	/**
	 * Implementacija bfs (breadth first search) algoritma koji pretražuje prostor
	 * razinu po razinu stabla gdje je korijen stabla stanje s0, a svaku novu razinu
	 * čine sljedbenici stanja prethode razine. Dodatna funkcionalnost ove
	 * implementacije algoritma bfs je ta da se ista stanja ne posjećuju više puta
	 * 
	 * @param s0   početno stanje
	 * @param succ funkcija sljedbenika
	 * @param goal ciljani rezultat
	 * @param      <S> tip podatka rješenja
	 * @return {@link Node} koji sadrži podatke o putu rješavanja problema ili null
	 *         ako rješenje ne postoji
	 */
	public static <S> Node<S> bfsv(Supplier<S> s0, Function<S, List<Transition<S>>> succ, Predicate<S> goal) {
		List<Node<S>> toProcess = new LinkedList<>();
		Set<S> visited = new HashSet<>();
		Node<S> tmpNode = new Node<S>(s0.get(), 0, null);
		toProcess.add(tmpNode);
		visited.add(s0.get());

		while (!toProcess.isEmpty()) {
			Node<S> ni = toProcess.remove(0);

			if (goal.test(ni.getState()))
				return ni;

			for (Transition<S> transition : succ.apply(ni.getState())) {
				if (visited.contains(transition.getState())) {
					continue;
				}
				toProcess.add(new Node<S>(transition.getState(), ni.getCost() + transition.getCost(), ni));
				visited.add(transition.getState());
			}
		}

		return null;
	}
}
