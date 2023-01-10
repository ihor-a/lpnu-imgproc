package com.example.imgproc.service;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageFileServiceImpl extends BaseImageService implements ImageFileService {
    @Override
    public BufferedImage upload(ImageView imageView, Label label) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File. Available formats: png, jpg, bmp, tiff");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.tiff")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile == null) {
            return null;
        }

        BufferedImage bufferedImage = ImageIO.read(selectedFile);
        setImage(bufferedImage, imageView);
        label.setText("");
        return bufferedImage;
    }

    @Override
    public void save(BufferedImage bufferedImage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File in png format");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png")
        );
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            ImageIO.write(bufferedImage, "png", selectedFile);
        }
    }
}
