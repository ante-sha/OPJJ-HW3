package hr.fer.zemris.java.hw17.jvdraw.drawing;

/**
 * Promatrač na {@link DrawingModel}
 * 
 * @author Ante Miličević
 *
 */
public interface DrawingModelListener {
	/**
	 * Metoda koja se poziva kada se dodaju geometrijski objekti
	 * 
	 * @param source model koji se promijenio
	 * @param index0 početni indeks na kojeg se dodaju geometrijski objekti
	 * @param index1 zavšni indeks na kojeg se dodaju geometrijski objekti
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);
	
	/**
	 * Metoda koja se poziva kada se uklanjaju geometrijski objekti
	 * 
	 * @param source model koji se promijenio
	 * @param index0 početni indeks s kojeg se uklanjaju geometrijski objekti
	 * @param index1 završni indeks s kojeg se uklanjaju geometrijski objekti
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);
	
	/**
	 * Metoda koja se poziva kada se mijenjaju geometrijski objekti
	 * 
	 * @param source model koji se promijenio
	 * @param index0 početni indeks na kojem je geometrijski objekt promijenjen
	 * @param index1 završni indeks na kojem je geometrijski objekt promijenjen
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
}
