package service;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageFileService {
    BufferedImage upload(ImageView imageView, Label label) throws IOException;

    void save(BufferedImage bufferedImage) throws IOException;
}
