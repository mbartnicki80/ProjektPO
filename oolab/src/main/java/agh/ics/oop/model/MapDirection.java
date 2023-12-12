package agh.ics.oop.model;
public enum MapDirection {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;

    private static final Vector2d NORTH_VECTOR = new Vector2d(0, 1);
    private static final Vector2d NORTH_EAST_VECTOR = new Vector2d(1, 1);
    private static final Vector2d EAST_VECTOR = new Vector2d(1, 0);
    private static final Vector2d SOUTH_EAST_VECTOR = new Vector2d(1, -1);
    private static final Vector2d SOUTH_VECTOR = new Vector2d(0, -1);
    private static final Vector2d SOUTH_WEST_VECTOR = new Vector2d(-1, -1);
    private static final Vector2d WEST_VECTOR = new Vector2d(-1, 0);
    private static final Vector2d NORTH_WEST_VECTOR = new Vector2d(-1, 1);

    public String toString() {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
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

    public MapDirection rotate(int rotation) {
        MapDirection[] directions = MapDirection.values();
        return directions[(this.ordinal()+rotation)%(directions.length)];
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> NORTH_VECTOR;
            case SOUTH -> SOUTH_VECTOR;
            case WEST -> WEST_VECTOR;
            case NORTH_EAST -> NORTH_EAST_VECTOR;
            case EAST -> EAST_VECTOR;
            case SOUTH_EAST -> SOUTH_EAST_VECTOR;
            case SOUTH_WEST -> SOUTH_WEST_VECTOR;
            case NORTH_WEST -> NORTH_WEST_VECTOR;
        };
    }
}
