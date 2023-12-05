package agh.ics.oop.model;
public enum MapDirection {

    NORTH, EAST, SOUTH, WEST;

    private static final Vector2d NORTH_VECTOR = new Vector2d(0, 1);
    private static final Vector2d EAST_VECTOR = new Vector2d(1, 0);
    private static final Vector2d SOUTH_VECTOR = new Vector2d(0, -1);
    private static final Vector2d WEST_VECTOR = new Vector2d(-1, 0);

    public String toString() {
        String[] directions = {"N", "E", "S", "W"};
        return directions[(this.ordinal())%(directions.length)];
    }

    public MapDirection next() {
        MapDirection[] directions = MapDirection.values();
        return directions[(this.ordinal()+1)%(directions.length)];
    }

    public MapDirection previous() {
        MapDirection[] directions = MapDirection.values();
        return directions[(this.ordinal()+directions.length-1)%(directions.length)];
    }
    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> NORTH_VECTOR;
            case SOUTH -> SOUTH_VECTOR;
            case WEST -> WEST_VECTOR;
            case EAST -> EAST_VECTOR;
        };
    }
}
