package agh.ics.oop.model;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WorldElementBox {

    private final VBox vBox;
    private static final Image plantImage;
    private static final Map<String, Image> animalImages = new HashMap<>();

    static {
        try {
            plantImage = new Image(new FileInputStream("java/agh/ics/oop/resources/plant.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Arrays.stream(MapDirection.values()).forEach(value -> {
            try {
                Image image = new Image(new FileInputStream("/" + value.toString() + ".png"));
                animalImages.put(value.toString(), image);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public WorldElementBox(WorldElement worldElement) {


        ImageView imageView;
        if (worldElement instanceof Plant) {
            imageView = new ImageView(plantImage);
        }
        else if (worldElement instanceof Animal) {
            imageView = new ImageView(animalImages.get(worldElement.toString()));
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
