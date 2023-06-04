package protocols;

import java.io.IOException;
import java.io.InputStream;

public class utils {
    /**
     * Converting the length to a 4-byte array (big-endian)
     * @param length length of the data
     * @return 4-byte array
     */
    protected static byte[] getLengthBytes(int length) {
        byte[] lengthBytes = new byte[4];
        lengthBytes[0] = (byte) (length >> 24);
        lengthBytes[1] = (byte) (length >> 16);
        lengthBytes[2] = (byte) (length >> 8);
        lengthBytes[3] = (byte) length;
        return lengthBytes;
    }

    /**
     * Reading the length from an input stream
     * @param inputStream input stream
     * @return length of the data
     * @throws IOException if an I/O error occurs
     */
    protected static int readLength(InputStream inputStream) throws IOException {
        byte[] lengthBytes = new byte[4];
        inputStream.read(lengthBytes);
        int length = ((lengthBytes[0] & 0xFF) << 24) |
                ((lengthBytes[1] & 0xFF) << 16) |
                ((lengthBytes[2] & 0xFF) << 8) |
                (lengthBytes[3] & 0xFF);
        return length;
    }
}