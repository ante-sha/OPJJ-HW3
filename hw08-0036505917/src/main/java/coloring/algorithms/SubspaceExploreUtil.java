package coloring.algorithms;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Razred koji u svojim statičkim metodama nudi simulacije bfs, dfs i bfsv
 * algoritama pretraživanja prostora.
 * 
 * @author Ante Miličević
 *
 */
public class SubspaceExploreUtil {

	/**
	 * Metoda koja simulira rad bfs-a u popunjavanju ograničenog podprostora
	 * 
	 * @param s0         početno stanje
	 * @param process    sučelje koje obrađuje stanja
	 * @param succ       sučelje koje vraća djecu čvora predanog u funkciji
	 * @param acceptable metoda koja ispituje da li je stanje prihvatljivo
	 */
	public static <S> void bfs(Supplier<S> s0, Consumer<S> process, Function<S, List<S>> succ,
			Predicate<S> acceptable) {
		List<S> toProcess = new LinkedList<>();
		toProcess.add(s0.get());

		while (!toProcess.isEmpty()) {
			S si = toProcess.remove(0);
			if (!acceptable.test(si)) {
				continue;
			}

			toProcess.addAll(succ.apply(si));
			process.accept(si);
		}
	}

	/**
	 * Metoda koja simulira rad dfs-a u popunjavanju ograničenog podprostora
	 * 
	 * @param s0         početno stanje
	 * @param process    sučelje koje obrađuje stanja
	 * @param succ       sučelje koje vraća djecu čvora predanog u funkciji
	 * @param acceptable metoda koja ispituje da li je stanje prihvatljivo
	 */
	public static <S> void dfs(Supplier<S> s0, Consumer<S> process, Function<S, List<S>> succ,
			Predicate<S> acceptable) {
		List<S> toProcess = new LinkedList<>();
		toProcess.add(s0.get());

		while (!toProcess.isEmpty()) {
			S si = toProcess.remove(0);
			if (!acceptable.test(si)) {
				continue;
			}

			toProcess.addAll(0, succ.apply(si));
			process.accept(si);
		}
	}

	/**
	 * Metoda koja simulira rad bfsv-a u popunjavanju ograničenog podprostora,
	 * razlika u odnosu na bfs je ta da se isto stanje ne provjerava više od jedanput
	 * 
	 * @param s0         početno stanje
	 * @param process    sučelje koje obrađuje stanja
	 * @param succ       sučelje koje vraća djecu čvora predanog u funkciji
	 * @param acceptable metoda koja ispituje da li je stanje prihvatljivo
	 */
	public static <S> void bfsv(Supplier<S> s0, Consumer<S> process, Function<S, List<S>> succ,
			Predicate<S> acceptable) {

		List<S> toProcess = new LinkedList<>();
		toProcess.add(s0.get());
		Set<S> visited = new HashSet<S>();
		visited.add(s0.get());

		while (!toProcess.isEmpty()) {
			S si = toProcess.remove(0);
			if (!acceptable.test(si)) {
				continue;
			}
			process.accept(si);

			List<S> siblings = succ.apply(si);
			siblings.removeIf(visited::contains);

			toProcess.addAll(siblings);
			visited.addAll(siblings);
		}
	}
}
