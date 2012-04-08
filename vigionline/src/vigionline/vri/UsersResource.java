package vigionline.vri;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import vigionline.common.database.DatabaseLocator;
import vigionline.common.database.IDatabase;
import vigionline.common.model.User;

@Path("/api/users")
public class UsersResource {

	private final IDatabase _database = DatabaseLocator.Get();

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<User> getUsers() {
		List<User> users = null;
		try {
			users = _database.getUsers();
		} catch (SQLException e) {
			throw new WebApplicationException(500);
		}
		if (users == null)
			throw new WebApplicationException(404);
		return users;
	}

	@GET
	@Path("{idUser}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getUser(@PathParam("idUser") int idUser) {
		User user = null;
		try {
			user = _database.getUser(idUser);
		} catch (SQLException e) {
			throw new WebApplicationException(500);
		}
		if (user == null)
			throw new WebApplicationException(404);
		return user;
	}

	@POST
	@Path("create")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response createUser(@FormParam("name") String name,
			@FormParam("username") String username,
			@FormParam("password") String password) {
		User user = new User();
		user.setName(name);
		user.setUsername(username);
		user.setPassword(password);
		try {
			_database.createUser(user);
			return Response.status(200).build();
		} catch (SQLException e) {
			return Response.status(500).build();
		}
	}

	@POST
	@Path("{idUser}/edit")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateUser(@FormParam("idUser") int idUser,
			@FormParam("name") String name,
			@FormParam("username") String username,
			@FormParam("password") String password) {
		User user = new User();
		user.setIdUser(idUser);
		user.setName(name);
		user.setUsername(username);
		user.setPassword(password);
		try {
			_database.updateUser(user);
			return Response.status(200).build();
		} catch (SQLException e) {
			return Response.status(500).build();
		}
	}

	@Path("{idUser}/delete")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response deleteUser(@FormParam("idUser") int idUser) {
		try {
			_database.deleteUser(idUser);
			return Response.status(200).build();
		} catch (SQLException e) {
			return Response.status(500).build();
		}
	}
}