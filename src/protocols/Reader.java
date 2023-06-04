package protocols;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static protocols.DataType.*;
import static protocols.utils.readLength;

public class Reader {

    private static final int BUFFER_SIZE = 4096;

    /**
     * Read the String data from the socket
     * @param socket The socket it will read to
     * @return the string data
     * @throws IOException if an I/O error occurs
     */
    public static String readString(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();

        byte dataType = (byte) inputStream.read();
        int dataLength = readLength(inputStream);

        byte[] dataBytes = new byte[dataLength];
        inputStream.read(dataBytes);

        if (dataType == STRING_TYPE.getValue()) {
            return new String(dataBytes, "UTF-8");
        } else {
            throw new IllegalArgumentException("Received data is not of type STRING.");
        }
    }

    /**
     * Read the String data from the socket
     * @param socket The socket it will read to
     * @param destinationPath The path where the file will be saved
     * @throws IOException if an I/O error occurs
     */
    public static void readFile(Socket socket, String destinationPath) throws IOException {
        InputStream inputStream = socket.getInputStream();

        byte dataType = (byte) inputStream.read();
        int dataLength = readLength(inputStream);

        FileOutputStream fileOutputStream = new FileOutputStream(destinationPath);
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        int totalBytesRead = 0;

        while (totalBytesRead < (dataLength - BUFFER_SIZE) && (bytesRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
            totalBytesRead += bytesRead;
        }

        buffer = new byte[dataLength - totalBytesRead];
        inputStream.read(buffer);
        fileOutputStream.write(buffer);
        fileOutputStream.flush();

        fileOutputStream.close();

        if (dataType != BINARY_TYPE.getValue()) {
            throw new IllegalArgumentException("Received data is not of type BINARY.");
        }
    }

    /**
     * Read the int data from the socket
     * @param socket The socket it will read to
     * @return the int data
     * @throws IOException if an I/O error occurs
     */
    public static int readInt(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();

        byte dataType = (byte) inputStream.read();
        int dataLength = readLength(inputStream);

        byte[] dataBytes = new byte[dataLength];
        inputStream.read(dataBytes);

        if (dataType == INTEGER_TYPE.getValue()) {
            int data = ((dataBytes[0] & 0xFF) << 24) |
                    ((dataBytes[1] & 0xFF) << 16) |
                    ((dataBytes[2] & 0xFF) << 8) |
                    (dataBytes[3] & 0xFF);
            return data;
        } else {
            throw new IllegalArgumentException("Received data is not of type INTEGER.");
        }
    }

    /**
     * Read the boolean data from the socket
     * @param socket The socket it will read to
     * @return the boolean data
     * @throws IOException if an I/O error occurs
     */
    public static boolean readBoolean(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();

        byte dataType = (byte) inputStream.read();
        int dataLength = readLength(inputStream);

        if (dataLength != 1) {
            throw new IllegalArgumentException("Received data length is invalid.");
        }

        byte[] dataBytes = new byte[dataLength];
        inputStream.read(dataBytes);

        if (dataType == BOOLEAN_TYPE.getValue()) {
            return dataBytes[0] != 0x00;
        } else {
            throw new IllegalArgumentException("Received data is not of type BOOLEAN.");
        }
    }
}