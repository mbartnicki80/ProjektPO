package agh.ics.oop.model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.util.Objects;

public class WorldElementBox {

    private final VBox vBox;


    public WorldElementBox(WorldElement worldElement) {

        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(worldElement.getImageName())));

        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        this.vBox = new VBox(imageView);

        this.vBox.setAlignment(Pos.CENTER);
    }

    public VBox getVBox() {
        return vBox;
    }
}
