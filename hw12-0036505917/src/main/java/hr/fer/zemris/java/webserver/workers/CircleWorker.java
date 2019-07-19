package hr.fer.zemris.java.webserver.workers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred koji implementira radnika koji iscrtava kružnicu polumjera 50px na pozadini
 * veličine 100pxx100px.
 * 
 * @author Ante Miličević
 *
 */
public class CircleWorker implements IWebWorker {
	/**
	 * Boja pozadine slike
	 */
	private static final Color bgCOLOR = Color.gray;
	/**
	 * Boja kruga
	 */
	private static final Color fgCOLOR = Color.black;
	
	@Override
	public void processRequest(RequestContext context) throws Exception {
		context.setMimeType("image/png");
		BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);
		
		Graphics2D g2d = bim.createGraphics();
		g2d.setColor(bgCOLOR);
		g2d.fillRect(0, 0, 200, 200);
		g2d.setColor(fgCOLOR);
		g2d.fillOval(50,50,100,100);
		g2d.dispose();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bim, "png", bos);
			context.write(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
