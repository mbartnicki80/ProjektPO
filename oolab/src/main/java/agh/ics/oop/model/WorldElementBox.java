package agh.ics.oop.model;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class WorldElementBox {

    private final VBox vBox;
    private final static MapImage MAP_IMAGE = new MapImage();

    public WorldElementBox(WorldElement worldElement) {

        ImageView imageView;
        if (worldElement instanceof Plant) {
            imageView = new ImageView(MAP_IMAGE.getImage("#"));
            imageView.setFitHeight(39);
            imageView.setFitWidth(39);
        }
        else if (worldElement instanceof Animal) {
            imageView = new ImageView(MAP_IMAGE.getImage(worldElement.toString()));
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
        }
        else
            throw new IllegalArgumentException("Invalid worldElement");

        this.vBox = new VBox(imageView);

        this.vBox.setAlignment(Pos.CENTER);

    }

    public VBox getVBox() {
        return vBox;
    }
}
