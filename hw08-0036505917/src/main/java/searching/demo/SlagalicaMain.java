package searching.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import searching.algorithms.Node;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.SearchUtil;
import searching.slagalica.Slagalica;

/**
 * Demonstracijski program algoritama pretraživanja prostora kod jednostavne
 * slagalice dimenzija 3x3 gdje se nalaze brojevi od 1-8 i jedna praznina. Cilj
 * slagalice je složiti ju tako da se u prvom retku nalaze 1, 2 i 3; u drugom 4,
 * 5 i 6; u trećem 7, 8 i praznina.
 * 
 * @author Ante Miličević
 *
 */
public class SlagalicaMain {

	/**
	 * Ulazna točka programa
	 * 
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		Slagalica slagalica = new Slagalica(new KonfiguracijaSlagalice(new int[] { 2, 3, 0, 1, 4, 6, 7, 5, 8 }));
		Node<KonfiguracijaSlagalice> rješenje = SearchUtil.bfs(slagalica, slagalica, slagalica);
		if (rješenje == null) {
			System.out.println("Nisam uspio pronaći rješenje.");
		} else {
			System.out.println("Imam rješenje. Broj poteza je: " + rješenje.getCost());
			List<KonfiguracijaSlagalice> lista = new ArrayList<>();
			Node<KonfiguracijaSlagalice> trenutni = rješenje;
			while (trenutni != null) {
				lista.add(trenutni.getState());
				trenutni = trenutni.getParent();
			}
			Collections.reverse(lista);
			lista.stream().forEach(k -> {
				System.out.println(k);
				System.out.println();
			});
		}

//		Slagalica slagalica = new Slagalica(new KonfiguracijaSlagalice(new int[] { 1,6,4,5,0,2,8,7,3}));
//		Node<KonfiguracijaSlagalice> rješenje = SearchUtil.bfsv(slagalica, slagalica, slagalica);
//		if (rješenje == null) {
//			System.out.println("Nisam uspio pronaći rješenje.");
//		} else {
//			System.out.println("Imam rješenje. Broj poteza je: " + rješenje.getCost());
//			List<KonfiguracijaSlagalice> lista = new ArrayList<>();
//			Node<KonfiguracijaSlagalice> trenutni = rješenje;
//			while (trenutni != null) {
//				lista.add(trenutni.getState());
//				trenutni = trenutni.getParent();
//			}
//			Collections.reverse(lista);
//			lista.stream().forEach(k -> {
//				System.out.println(k);
//				System.out.println();
//			});
//		}
	}
}
