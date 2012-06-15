package vigionline.vce.stream.virtual;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import vigionline.vce.stream.iterator.Messages;
import vigionline.vce.stream.iterator.StreamIterator;

public final class StreamConsumer implements StreamingOutput {

	private StreamIterator<Messages.Message> _iterator;

	public StreamConsumer(StreamIterator<Messages.Message> iterator) {
		this._iterator = iterator;
	}

	@Override
	public void write(OutputStream outputStream) throws IOException,
			WebApplicationException {

		try {
			while (_iterator.hasNext()) {
				Messages.Message msg = _iterator.next();
				if (msg instanceof Messages.PoisonMessage) {
					break;
				}
				outputStream.write("--myboundary\r\n".getBytes());
				outputStream.write("Content-Type: image/jpeg\r\n\r\n"
						.getBytes());
				outputStream.write(((Messages.ImageMessage) msg).image);
				outputStream.flush();
			}
		} finally {
			outputStream.close();
			_iterator.shutdown();
		}
	}
}
