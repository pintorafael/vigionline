package vigionline.common.database.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import vigionline.common.model.Camera;

public class CameraMapper extends Mapper<Camera> {

	@Override
	protected Camera getObject(ResultSet result) throws SQLException {
		Camera camera = new Camera();
		camera.setIdCamera(result.getInt(1));
		camera.setIdLocation(result.getInt(2));
		camera.setIdModel(result.getInt(3));
		camera.setName(result.getString(4));
		camera.setUrl(result.getString(5));
		camera.setPort(result.getInt(6));
		camera.setUsername(result.getString(7));
		camera.setPassword(result.getString(8)); // TODO : Decrypt Password
		return camera;
	}

	@Override
	protected String getAllQuery() {
		return "SELECT idCamera, idLocation, idModel, name, url, port, username, password FROM Camera";
	}

	@Override
	protected String getByIdQuery() {
		return "SELECT idCamera, idLocation, idModel, name, url, port, username, password FROM Camera WHERE idCamera = ?";
	}

	@Override
	protected PreparedStatement getInsertStatement(Camera camera, Connection con)
			throws SQLException {
		PreparedStatement prep = con.prepareStatement("INSERT INTO Camera (idLocation, idModel, name, url, port, username, password) VALUES(?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		prep.setInt(1, camera.getIdLocation());
		prep.setInt(2, camera.getIdModel());
		prep.setString(3, camera.getName());
		prep.setString(4, camera.getUrl());
		prep.setInt(5, camera.getPort());
		prep.setString(6, camera.getUsername());
		prep.setString(7, camera.getPassword());
		return prep;
	}

	@Override
	protected PreparedStatement getUpdateStatement(Camera camera, Connection con)
			throws SQLException {
		PreparedStatement prep = con.prepareStatement("UPDATE Camera SET idLocation = ?, idModel = ?, name = ?, url = ?, port = ?, username = ?, password = ? WHERE idCamera = ?");
		prep.setInt(1, camera.getIdLocation());
		prep.setInt(2, camera.getIdModel());
		prep.setString(3, camera.getName());
		prep.setString(4, camera.getUrl());
		prep.setInt(5, camera.getPort());
		prep.setString(6, camera.getUsername());
		prep.setString(7, camera.getPassword());
		prep.setInt(8, camera.getIdCamera());
		return prep;
	}

	@Override
	protected PreparedStatement getDeleteStatement(int id, Connection con)
			throws SQLException {
		PreparedStatement prep = con.prepareStatement("DELETE FROM Camera WHERE idCamera = ?");
		prep.setInt(1, id);
		return prep;
	}
}