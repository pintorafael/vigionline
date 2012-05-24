package vigionline.vce.record;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.sql.Date;

import vigionline.common.database.mapper.ImageMapper;
import vigionline.common.model.Camera;
import vigionline.common.model.Image;
import vigionline.vce.stream.iterator.StreamIterator;

public class Recorder implements Runnable {

	public Boolean STOP_RECORDING = Boolean.FALSE;
	private Camera _camera;
	private ImageMapper _imageMapper;
	private StreamIterator<byte[]> _iterator;
	private String _directory;
	private long _timestamp;
	private final long MAXIMUM_LAST_RECORDING_TIME = 1000 * 30;

	public Recorder(StreamIterator<byte[]> iterator, Camera camera,
			String directory) {
		this._iterator = iterator;
		this._camera = camera;
		this._directory = directory;
	}

	@Override
	public void run() {
		while (!STOP_RECORDING) {
			while (_iterator.hasNext()) {
				byte[] image = _iterator.next();
				try {
					Date date = new Date(System.currentTimeMillis());
					writeToDB(image, date);
					writeToDisk(image, date);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public boolean stillRecording() {
		return _timestamp > (System.currentTimeMillis() - MAXIMUM_LAST_RECORDING_TIME);
	}

	private String writeToDisk(byte[] img, Date date) throws IOException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		_timestamp = date.getTime();

		String dirname = _directory + "/" + cal.get(Calendar.YEAR) + "/"
				+ (cal.get(Calendar.MONTH) + 1) + "/"
				+ cal.get(Calendar.DAY_OF_MONTH) + "/"
				+ cal.get(Calendar.HOUR_OF_DAY) + "/camera"
				+ _camera.getIdCamera() + "/";

		String filename = dirname + _timestamp + ".jpg";

		File dir = new File(dirname);
		if (!dir.exists())
			dir.mkdirs();

		File file = new File(filename);
		BufferedOutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(file));
			os.write(img);
		} finally {
			if (os != null) {
				os.flush();
				os.close();
			}
			os = null;
			file = null;
			dir = null;
			cal = null;
		}
		return filename;
	}

	private void writeToDB(byte[] img, Date date) throws SQLException {
		Image image = new Image();
		try {
			String filename = writeToDisk(img, date);
			image.setIdCamera(_camera.getIdCamera());
			image.setFilename(filename);
			image.setSize(img.length);
			image.setDate(date);
			_imageMapper.insert(image);
		} catch (IOException e) {
			// Could not write to disk
		} finally {
			img = null;
			image = null;
		}
	}
}