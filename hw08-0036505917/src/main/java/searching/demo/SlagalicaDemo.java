package searching.demo;

import searching.algorithms.Node;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.SearchUtil;
import searching.slagalica.Slagalica;
import searching.slagalica.gui.SlagalicaViewer;

/**
 * Demonstracijski program algoritama pretraživanja prostora kod jednostavne
 * slagalice dimenzija 3x3 gdje se nalaze brojevi od 1-8 i jedna praznina. Cilj
 * slagalice je složiti ju tako da se u prvom retku nalaze 1, 2 i 3; u drugom 4,
 * 5 i 6; u trećem 7, 8 i praznina. Rezultat rješavanja slagalice se priazuje s
 * {@link SlagalicaViewer}-om.
 * 
 * @author Ante Miličević
 *
 */
public class SlagalicaDemo {

	/**
	 * Ulazna točka programa
	 * 
	 * @param args brojkama se zadaje konfiguracija slagalice, 0 označava prazninu
	 */
	public static void main(String[] args) {

		if(args.length != 1) {
			System.err.println("You must enter one argument");
			System.exit(1);
		}
		args[0] = args[0].trim();
		if(!args[0].matches("[0-9]{9}")) {
			System.err.println("Argument must be string with 9 numbers");
			System.exit(1);
		}
		int[] array = convert(args[0]);
		Slagalica slagalica = new Slagalica(new KonfiguracijaSlagalice(array));
		
		Node<KonfiguracijaSlagalice> rješenje = SearchUtil.bfsv(slagalica, slagalica, slagalica);
		SlagalicaViewer.display(rješenje);
		
		if(rješenje == null) {
			System.out.println("Puzzle can not be solved!");
		}
	}

	/**
	 * Metoda prevodi niz brojki zapisan u stringu u polje brojeva
	 * 
	 * @param string String koji sadži samo znamenke
	 * @return znamenke zapisane u polju
	 */
	private static int[] convert(String string) {
		int[] array = new int[9];
		int i = 0;
		for(char c : string.toCharArray()) {
			array[i] = c - '0';
			i++;
		}
		return array;
	}
}
