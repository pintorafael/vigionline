package vigionline.vce.stream.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import vigionline.vce.exception.EndOfStreamException;

public class JpegParser implements IFrameParser {

    private InputStream _bis;
    private boolean _isEndOfStream;

    public JpegParser(InputStream inputStream) {
        _bis = inputStream;
        _isEndOfStream = false;
    }

    @Override
    public byte[] getNextFrame() {
        return _bis != null ? parseJpeg() : null;
    }

    @Override
    public boolean isEndOfStream() {
        return _isEndOfStream;
    }

    private byte[] parseJpeg() {
        try {
            return getFrame();
        } catch (IOException ex) {
            return null;
        } catch (EndOfStreamException ex) {
            _isEndOfStream = true;
            return null;
        }
    }

    private byte[] findFrameLimit(int prevExpected, int currExpected) throws IOException, EndOfStreamException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            int prev = 0, curr;
            while ((curr = _bis.read()) != -1) {
                outputStream.write(curr);
                if (prev == prevExpected && curr == currExpected) // found Limit
                {
                    return outputStream.toByteArray();
                }
                prev = curr;
            }
            throw new EndOfStreamException();
        }
    }

    private byte[] getFrame() throws IOException, EndOfStreamException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(new byte[]{(byte) 0xff, (byte) 0xd8});
            findFrameLimit(0xff, 0xd8);
            outputStream.write(findFrameLimit(0xff, 0xd9));
            return outputStream.toByteArray();
        }
    }
}
