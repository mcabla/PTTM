package tools;

public enum Direction {
    NORTH ((byte) 1), EAST((byte) 2), SOUTH((byte) 4), WEST((byte) 8);

    private byte value;

    Direction(byte value){
        this.value = value;
    }

    public byte getValue(){
        return this.value;
    }

}
