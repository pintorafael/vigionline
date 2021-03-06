package vigionline.vwc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import vigionline.common.model.Camera;
import vigionline.common.model.Location;
import vigionline.vri.CamerasResource;
import vigionline.vri.LocationsResource;

import com.sun.jersey.api.view.Viewable;

@RolesAllowed("admin")
@Path("/recordings")
public class RecordingsController {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable getHTML()
	{
		List<Camera> cameras = new CamerasResource().getCameras(null, -1);
		List<Location> locations = new LocationsResource().getLocations();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("cameras", cameras);
		data.put("locations", locations);
		return new Viewable("/recordings", data);
	}
}
