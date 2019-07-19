package hr.fer.zemris.java.hw17.jvdraw.drawing;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.Line;

/**
 * Model liste koji služi kao adapter prema {@link DrawingModel}-u.
 * 
 * @author Ante Miličević
 *
 */
public class DrawingObjectListModel extends AbstractListModel<String> {
	private static final long serialVersionUID = 1L;
	/**
	 * Model koji sadrži sve geometrijske objekte
	 */
	private DrawingModel dModel;

	/**
	 * Konstruktor
	 * 
	 * @param dModel model koji sadrži sve geometrijske objekte
	 */
	public DrawingObjectListModel(DrawingModel dModel) {
		this.dModel = dModel;
		dModel.addDrawingModelListener(new DrawingModelListener() {
			@Override
			public void objectsRemoved(DrawingModel source, int index0, int index1) {
				DrawingObjectListModel.this.fireIntervalRemoved(source, index0, index1);
			}

			@Override
			public void objectsChanged(DrawingModel source, int index0, int index1) {
				DrawingObjectListModel.this.fireContentsChanged(source, index0, index1);
			}

			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				DrawingObjectListModel.this.fireIntervalAdded(source, index0, index1);
			}
		});
	}

	@Override
	public int getSize() {
		return dModel.getSize();
	}

	@Override
	public String getElementAt(int index) {
		return generateTextForObject(dModel.getObject(index));
	}

	private String generateTextForObject(GeometricalObject object) {
		if (object instanceof Line) {
			return lineText((Line) object);
		} else if (object instanceof Circle) {
			return circleText((Circle) object);
		} else if (object instanceof FilledCircle) {
			return filledCircleText((FilledCircle) object);
		}
		return null;
	}

	/**
	 * Formatiranje naziva za dužinu
	 * 
	 * @param line dužina
	 * @return formatirani naziv
	 */
	private String lineText(Line line) {
		return String.format("Line (%d,%d)-(%d,%d)", line.getStartPoint().x, line.getStartPoint().y,
				line.getEndPoint().x, line.getEndPoint().y);
	}

	/**
	 * Formatiranje naziva za kružnicu
	 * 
	 * @param circle kružnica
	 * @return formatirani naziv
	 */
	private String circleText(Circle circle) {
		return String.format("Circle (%d,%d), %d", circle.getCenter().x, circle.getCenter().y,
				(int) circle.getRadius());
	}

	/**
	 * Formatiranje naziva za krug
	 * 
	 * @param fCircle krug
	 * @return formatirani naziv
	 */
	private String filledCircleText(FilledCircle fCircle) {
		return String.format("Filled circle (%d,%d), %d, #%06X", fCircle.getCenter().x, fCircle.getCenter().y,
				(int) fCircle.getRadius(), fCircle.getBgColor().getRGB());
	}

}
