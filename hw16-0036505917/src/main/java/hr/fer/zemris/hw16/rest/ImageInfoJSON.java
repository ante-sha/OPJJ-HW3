package hr.fer.zemris.hw16.rest;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

import hr.fer.zemris.hw16.data.DataStorage;
import hr.fer.zemris.hw16.data.ImageItem;

/**
 * Razred koji je odgovoran za sve zahtjeve s /imageInfo staze. Ideja ovog servisa
 * je dohvat svih informacija o slikama.
 * 
 * @author Ante Miličević
 *
 */
@Path("/imageInfo")
public class ImageInfoJSON {
	/**
	 * Dohvat popisa svih oznaka
	 * 
	 * @return json odgovor popisa svih oznaka
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTagList() {
		Set<String> tags = DataStorage.getTags();
		
		JSONObject result = new JSONObject();
		result.put("tags", tags);
		
		return Response.status(Status.OK).entity(result.toString()).build();
	}
	
	/**
	 * Dohvat popisa svih imena slika koje su povezane s predanim tagom
	 * 
	 * @param tag tag
	 * @return popis svih imena slika koje su povezane s predanim tagom
	 */
	@Path("{tag}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getImageNamesForTag(@PathParam("tag") String tag) {
		Set<String> imageNames = DataStorage.getImageNamesThatContainsTag(tag);
		
		JSONObject result = new JSONObject();
		result.put("imageNames", imageNames);
		
		return Response.status(Status.OK).entity(result.toString()).build();
	}
	
	/**
	 * Dohvat informacija o slici nazima name
	 * 
	 * @param name naziv slike
	 * @return opis slike i oznake pridružene slici
	 */
	@Path("image/{name}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllInformationsForImage(@PathParam("name") String name) {
		ImageItem item = DataStorage.getImageItem(name);
		
		if(item == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		JSONObject result = new JSONObject();
		result.put("desc", item.getDescription());
		result.put("tags", item.getTags());
		
		return Response.status(Status.OK).entity(result.toString()).build();
	}
}
