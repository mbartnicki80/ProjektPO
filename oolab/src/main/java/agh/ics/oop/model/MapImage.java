package agh.ics.oop.model;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapImage {
    private final Map<String, Image> mapImages = new HashMap<>();

    public MapImage() {
        try (InputStream imageStream = WorldElement.class.getResourceAsStream("plant.png")) {
            Image image = new Image(Objects.requireNonNull(imageStream));
            mapImages.put("#", image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (MapDirection value : MapDirection.values()) {
            try (InputStream imageStream = WorldElement.class.getResourceAsStream(value.toString() + ".png")) {
                Image image = new Image(Objects.requireNonNull(imageStream));
                mapImages.put(value.toString(), image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Image getImage(String key) {
        return mapImages.get(key);
    }
}