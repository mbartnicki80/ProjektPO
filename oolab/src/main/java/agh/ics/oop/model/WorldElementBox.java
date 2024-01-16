package agh.ics.oop.model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WorldElementBox {


    private final VBox vBox;


    public WorldElementBox(WorldElement worldElement) throws FileNotFoundException {

/*
        Image image = Toolkit.getDefaultToolkit()
                .getImage(worldElement.getImageName());
*/

        Image image = new Image(
                new FileInputStream(worldElement.getImageName()));

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
