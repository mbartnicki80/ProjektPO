package agh.ics.oop.model;

public class RectangularMap extends AbstractWorldMap {
    private final Boundary bounds;

    public RectangularMap(int width, int height) {
        super();
        this.bounds = new Boundary(new Vector2d(0, 0), new Vector2d(width-1, height-1));
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return (bounds.lowerLeft().precedes(position) && bounds.upperRight().follows(position)) && super.canMoveTo(position);
    }

    @Override
    public Boundary getCurrentBounds() {
        return bounds;
    }

}
