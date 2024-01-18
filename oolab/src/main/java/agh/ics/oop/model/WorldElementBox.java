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
        }
        else if (worldElement instanceof Animal) {
            imageView = new ImageView(MAP_IMAGE.getImage(worldElement.toString()));
        }
        else
            throw new IllegalArgumentException("Invalid worldElement");


        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        this.vBox = new VBox(imageView);

        this.vBox.setAlignment(Pos.CENTER);

    }

    public VBox getVBox() {
        return vBox;
    }
}
