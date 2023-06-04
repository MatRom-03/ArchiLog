package protocols;

public enum DataType {
    STRING_TYPE((byte) 0x01),
    BINARY_TYPE((byte) 0x02),
    INTEGER_TYPE((byte) 0x03),
    BOOLEAN_TYPE((byte) 0x04);

    private final byte value;

    DataType(byte value) {
        this.value = value;
    }

    /**
     * Get the value of DataType
     * @return value
     */
    public byte getValue() {
        return value;
    }
}