package protocols;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static protocols.DataType.*;
import static protocols.utils.getLengthBytes;


public class Writer {
    private static final int BUFFER_SIZE = 4096;

    /**
     * Write the String data to the socket
     * @param socket The socket it will write to
     * @param data the data to write
     * @throws IOException if an I/O error occurs
     */
    public static void writeString(Socket socket, String data) throws IOException {
        OutputStream outputStream = socket.getOutputStream();

        byte[] stringBytes = data.getBytes(StandardCharsets.UTF_8);

        outputStream.write(STRING_TYPE.getValue());
        outputStream.write(getLengthBytes(stringBytes.length));

        outputStream.write(stringBytes);
        outputStream.flush();
    }

    /**
     * Write the file to the socket
     * @param socket The socket it will write to
     * @param file the file to write
     * @throws IOException if an I/O error occurs
     */
    public static void writeFile(Socket socket, File file) throws IOException {
        OutputStream outputStream = socket.getOutputStream();

        if (file.length() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("File size exceeds the maximum supported value.");
        }

        outputStream.write(BINARY_TYPE.getValue());
        outputStream.write(getLengthBytes((int) file.length()));

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.flush();

        fileInputStream.close();
    }

    /**
     * Write the int data to the socket
     * @param socket The socket it will write to
     * @param data the data to write
     * @throws IOException if an I/O error occurs
     */
    public static void writeInteger(Socket socket, int data) throws IOException {
        OutputStream outputStream = socket.getOutputStream();

        // Conversion de l'entier en un tableau de 4 octets (big-endian)
        byte[] intBytes = new byte[4];
        intBytes[0] = (byte) (data >> 24);
        intBytes[1] = (byte) (data >> 16);
        intBytes[2] = (byte) (data >> 8);
        intBytes[3] = (byte) data;

        outputStream.write(INTEGER_TYPE.getValue());
        outputStream.write(getLengthBytes(intBytes.length));

        outputStream.write(intBytes);
        outputStream.flush();
    }

    /**
     * Write the boolean data to the socket
     * @param socket The socket it will write to
     * @param data the data to write
     * @throws IOException if an I/O error occurs
     */
    public static void writeBoolean(Socket socket, boolean data) throws IOException {
        OutputStream outputStream = socket.getOutputStream();

        outputStream.write(BOOLEAN_TYPE.getValue());
        outputStream.write(getLengthBytes(1));

        outputStream.write(data ? 0x01 : 0x00);

        outputStream.flush();
    }
}